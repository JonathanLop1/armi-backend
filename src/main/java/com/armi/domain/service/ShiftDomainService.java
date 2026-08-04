package com.armi.domain.service;

import com.armi.domain.port.in.ShiftUseCase;
import com.armi.domain.port.out.ShiftPersistencePort;
import com.armi.domain.port.out.UserPersistencePort;
import com.armi.model.AppUser;
import com.armi.model.Shift;
import com.armi.model.Store;
import com.armi.model.TimeLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftDomainService implements ShiftUseCase {

    private final ShiftPersistencePort shiftPersistencePort;
    private final UserPersistencePort userPersistencePort;

    public ShiftDomainService(ShiftPersistencePort shiftPersistencePort, UserPersistencePort userPersistencePort) {
        this.shiftPersistencePort = shiftPersistencePort;
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public List<Shift> getAllShifts() {
        return shiftPersistencePort.findAllShifts();
    }

    @Override
    @Transactional
    public Shift createShift(Shift shift) {
        if (shift.getStore() != null) {
            shift.setStore(ensureStoreSaved(shift.getStore()));
        }
        if (shift.getStatus() == null || shift.getStatus().isEmpty()) {
            shift.setStatus("available");
        }
        return shiftPersistencePort.saveShift(shift);
    }

    @Override
    @Transactional
    public List<Shift> createRecurringShift(Shift shift, Integer days, Long driverId) {
        int duration = (days != null && days > 0) ? days : 1;
        shift.setDurationDays(duration);
        
        AppUser driver = null;
        if (driverId != null) {
            driver = userPersistencePort.findUserById(driverId).orElse(null);
        } else if (shift.getAssignedTo() != null && shift.getAssignedTo().getId() != null) {
            driver = userPersistencePort.findUserById(shift.getAssignedTo().getId()).orElse(null);
        }

        if (driver != null) {
            shift.setAssignedTo(driver);
            shift.setStatus("assigned");
        } else {
            shift.setStatus("available");
        }

        Shift savedMaster = createShift(shift);
        List<Shift> result = new ArrayList<>();
        result.add(savedMaster);
        return result;
    }

    @Override
    @Transactional
    public List<Shift> createShiftsBulk(List<Shift> shifts) {
        List<Shift> created = new ArrayList<>();
        for (Shift s : shifts) {
            created.add(createShift(s));
        }
        return created;
    }

    @Override
    @Transactional
    public Shift updateShift(Long shiftId, Shift updated) {
        Shift existing = shiftPersistencePort.findShiftById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));
        if (updated.getStore() != null) {
            existing.setStore(ensureStoreSaved(updated.getStore()));
        }
        if (updated.getStartTime() != null) existing.setStartTime(updated.getStartTime());
        if (updated.getEndTime() != null) existing.setEndTime(updated.getEndTime());
        if (updated.getHourlyRate() != null) existing.setHourlyRate(updated.getHourlyRate());
        if (updated.getAvailableSpots() != null) existing.setAvailableSpots(updated.getAvailableSpots());
        if (updated.getDurationDays() != null) existing.setDurationDays(updated.getDurationDays());
        String prevStatus = existing.getStatus();
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getPayType() != null) existing.setPayType(updated.getPayType());
        if (updated.getOrdersCount() != null) existing.setOrdersCount(updated.getOrdersCount());

        if ("available".equalsIgnoreCase(updated.getStatus()) || updated.getAssignedTo() == null) {
            existing.setAssignedTo(null);
        } else if (updated.getAssignedTo() != null && updated.getAssignedTo().getId() != null) {
            AppUser driver = userPersistencePort.findUserById(updated.getAssignedTo().getId()).orElse(null);
            existing.setAssignedTo(driver);

            if ("completed".equalsIgnoreCase(updated.getStatus()) && !"completed".equalsIgnoreCase(prevStatus) && driver != null) {
                double earned;
                if ("FIXED".equalsIgnoreCase(existing.getPayType())) {
                    earned = (existing.getHourlyRate() != null && existing.getHourlyRate() > 0) ? existing.getHourlyRate() : 60000.0;
                } else {
                    long mins = Duration.between(existing.getStartTime(), existing.getEndTime()).toMinutes();
                    double hours = mins > 0 ? (mins / 60.0) : 8.0;
                    earned = hours * (existing.getHourlyRate() != null ? existing.getHourlyRate() : 10000.0);
                }
                driver.setAccumulatedEarnings((driver.getAccumulatedEarnings() != null ? driver.getAccumulatedEarnings() : 0.0) + earned);
                userPersistencePort.saveUser(driver);
            }
        }
        return shiftPersistencePort.saveShift(existing);
    }

    @Override
    @Transactional
    public void deleteShift(Long shiftId) {
        shiftPersistencePort.deleteShiftById(shiftId);
    }

    @Override
    @Transactional
    public void deleteAllShifts() {
        shiftPersistencePort.deleteAllTimeLogs();
        shiftPersistencePort.deleteAllShifts();
    }

    @Override
    @Transactional
    public void resetAllData() {
        shiftPersistencePort.deleteAllTimeLogs();
        shiftPersistencePort.deleteAllShifts();
        shiftPersistencePort.deleteAllStores();
        userPersistencePort.deleteAllUsers();
        userPersistencePort.saveUser(new AppUser("ARMI Admin", "admin@armi.com", "123456", "ADMIN"));
    }

    @Override
    @Transactional
    public Shift assignShift(Long shiftId, Long driverId) {
        Shift shift = shiftPersistencePort.findShiftById(shiftId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        AppUser driver = userPersistencePort.findUserById(driverId)
                .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));

        LocalDateTime start = shift.getStartTime();
        int days = (shift.getDurationDays() != null && shift.getDurationDays() > 0) ? shift.getDurationDays() : 1;
        LocalDateTime end = shift.getEndTime().plusDays(days - 1);
        List<Shift> overlapping = shiftPersistencePort.findOverlappingShifts(driver, start, end);
        if (!overlapping.isEmpty()) {
            throw new RuntimeException("El repartidor ya tiene un turno asignado en ese horario (cruce de horarios).");
        }

        shift.setAssignedTo(driver);
        shift.setStatus("assigned");
        return shiftPersistencePort.saveShift(shift);
    }

    @Override
    @Transactional
    public Shift startShift(Long shiftId, Long driverId, Double lat, Double lng) {
        Shift shift = shiftPersistencePort.findShiftById(shiftId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        AppUser driver = userPersistencePort.findUserById(driverId)
                .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));

        if ("in_progress".equals(shift.getStatus()) && shift.getAssignedTo() != null && shift.getAssignedTo().getId().equals(driverId)) {
            return shift;
        }

        if (shift.getAssignedTo() == null || "available".equals(shift.getStatus())) {
            shift.setAssignedTo(driver);
            shift.setStatus("assigned");
        }

        if (!shift.getAssignedTo().getId().equals(driverId)) {
            throw new RuntimeException("Este turno está asignado a otro repartidor.");
        }

        shift.setStatus("in_progress");
        LocalDateTime now = LocalDateTime.now();

        TimeLog log = new TimeLog();
        log.setShift(shift);
        log.setDriver(shift.getAssignedTo());
        log.setActualStart(now);
        log.setStartLatitude(lat);
        log.setStartLongitude(lng);
        log.setStartCoordinates(String.format(java.util.Locale.US, "%.6f, %.6f", lat, lng));

        long delayMinutes = Duration.between(shift.getStartTime(), now).toMinutes();
        log.setStartDelayMinutes(delayMinutes);
        if (delayMinutes > 5) {
            log.setPunctualityStatus("LATE");
        } else if (delayMinutes < -5) {
            log.setPunctualityStatus("EARLY");
        } else {
            log.setPunctualityStatus("ON_TIME");
        }

        shiftPersistencePort.saveTimeLog(log);
        return shiftPersistencePort.saveShift(shift);
    }

    @Override
    @Transactional
    public Shift endShift(Long shiftId, Long driverId) {
        Shift shift = shiftPersistencePort.findShiftById(shiftId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        AppUser driver = userPersistencePort.findUserById(driverId)
                .orElseGet(() -> (shift.getAssignedTo() != null) ? shift.getAssignedTo() : null);

        if (driver == null) {
            throw new RuntimeException("Repartidor no encontrado para este turno");
        }

        double dailyEarned;
        if ("FIXED".equalsIgnoreCase(shift.getPayType())) {
            dailyEarned = (shift.getHourlyRate() != null && shift.getHourlyRate() > 0) ? shift.getHourlyRate() : 60000.0;
        } else {
            long scheduledMinutes = Duration.between(shift.getStartTime(), shift.getEndTime()).toMinutes();
            double scheduledHours = scheduledMinutes > 0 ? (scheduledMinutes / 60.0) : 8.0;
            dailyEarned = scheduledHours * (shift.getHourlyRate() != null ? shift.getHourlyRate() : 10000.0);
        }

        TimeLog log = shiftPersistencePort.findFirstTimeLogByShiftId(shiftId).orElseGet(() -> {
            TimeLog newLog = new TimeLog();
            newLog.setShift(shift);
            newLog.setDriver(driver);
            newLog.setActualStart(shift.getStartTime() != null ? shift.getStartTime() : LocalDateTime.now());
            newLog.setPunctualityStatus("ON_TIME");
            return newLog;
        });

        log.setActualEnd(LocalDateTime.now());
        log.setTotalEarned(dailyEarned);
        shiftPersistencePort.saveTimeLog(log);

        double currentEarnings = (driver.getAccumulatedEarnings() != null) ? driver.getAccumulatedEarnings() : 0.0;
        driver.setAccumulatedEarnings(currentEarnings + dailyEarned);
        userPersistencePort.saveUser(driver);

        int currentDuration = (shift.getDurationDays() != null && shift.getDurationDays() > 0) ? shift.getDurationDays() : 1;
        int remainingDays = currentDuration - 1;
        shift.setDurationDays(remainingDays);

        if (remainingDays > 0) {
            shift.setStartTime(shift.getStartTime().plusDays(1));
            shift.setEndTime(shift.getEndTime().plusDays(1));
            shift.setStatus("assigned");
        } else {
            shift.setStatus("completed");
        }

        return shiftPersistencePort.saveShift(shift);
    }

    @Override
    public List<TimeLog> getAllTimeLogs() {
        return shiftPersistencePort.findAllTimeLogs();
    }

    private Store ensureStoreSaved(Store store) {
        if (store.getId() != null) {
            return shiftPersistencePort.findAllStores().stream()
                    .filter(s -> s.getId().equals(store.getId()))
                    .findFirst()
                    .orElseGet(() -> shiftPersistencePort.saveStore(store));
        }
        return shiftPersistencePort.saveStore(store);
    }
}
