package com.armi.service;

import com.armi.model.AppUser;
import com.armi.model.Shift;
import com.armi.model.Store;
import com.armi.model.TimeLog;
import com.armi.repository.AppUserRepository;
import com.armi.repository.ShiftRepository;
import com.armi.repository.StoreRepository;
import com.armi.repository.TimeLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftService {
    
    private final ShiftRepository shiftRepository;
    private final StoreRepository storeRepository;
    private final AppUserRepository userRepository;
    private final TimeLogRepository timeLogRepository;

    public ShiftService(ShiftRepository shiftRepository, StoreRepository storeRepository, AppUserRepository userRepository, TimeLogRepository timeLogRepository) {
        this.shiftRepository = shiftRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.timeLogRepository = timeLogRepository;
    }

    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    private Store ensureStoreSaved(Store store) {
        if (store == null) return null;
        if (store.getId() != null) {
            return storeRepository.findById(store.getId()).orElseGet(() -> storeRepository.save(store));
        }
        return storeRepository.save(store);
    }

    @Transactional
    public Shift createShift(Shift shift) {
        shift.setStore(ensureStoreSaved(shift.getStore()));
        if (shift.getStatus() == null) {
            shift.setStatus("available");
        }
        return shiftRepository.save(shift);
    }

    @Transactional
    public List<Shift> createRecurringShift(Shift baseShift, Integer repeatDays, Long driverId) {
        Store savedStore = ensureStoreSaved(baseShift.getStore());
        int count = (repeatDays != null && repeatDays > 0) ? repeatDays : 1;
        AppUser driver = (driverId != null) ? userRepository.findById(driverId).orElse(null) : null;
        List<Shift> createdShifts = new ArrayList<>();

        Shift shift = new Shift();
        shift.setStore(savedStore);
        shift.setStartTime(baseShift.getStartTime());
        shift.setEndTime(baseShift.getEndTime());
        shift.setHourlyRate(baseShift.getHourlyRate());
        shift.setAvailableSpots(baseShift.getAvailableSpots());
        shift.setDurationDays(count);
        shift.setPayType(baseShift.getPayType() != null ? baseShift.getPayType() : "HOURLY");
        
        if (driver != null) {
            shift.setAssignedTo(driver);
            shift.setStatus("assigned");
        } else {
            shift.setStatus("available");
        }
        
        createdShifts.add(shiftRepository.save(shift));

        return createdShifts;
    }

    @Transactional
    public List<Shift> createShiftsBulk(List<Shift> shifts) {
        List<Shift> savedList = new ArrayList<>();
        for (Shift s : shifts) {
            s.setStore(ensureStoreSaved(s.getStore()));
            if (s.getStatus() == null) s.setStatus("available");
            if (s.getPayType() == null) s.setPayType("HOURLY");
            if (s.getAvailableSpots() == null) s.setAvailableSpots(1);
            if (s.getDurationDays() == null) s.setDurationDays(1);
            savedList.add(shiftRepository.save(s));
        }
        return savedList;
    }

    @Transactional
    public Shift updateShift(Long shiftId, Shift updated) {
        Shift existing = shiftRepository.findById(shiftId).orElseThrow(() -> new RuntimeException("Shift not found"));
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

        if (updated.getAssignedTo() != null && updated.getAssignedTo().getId() != null) {
            AppUser driver = userRepository.findById(updated.getAssignedTo().getId()).orElse(null);
            existing.setAssignedTo(driver);

            // Auto-calculate earnings if shift transitions to completed
            if ("completed".equalsIgnoreCase(updated.getStatus()) && !"completed".equalsIgnoreCase(prevStatus) && driver != null) {
                double earned;
                if ("FIXED".equalsIgnoreCase(existing.getPayType())) {
                    earned = (existing.getHourlyRate() != null && existing.getHourlyRate() > 0) ? existing.getHourlyRate() : 60000.0;
                } else {
                    long mins = java.time.Duration.between(existing.getStartTime(), existing.getEndTime()).toMinutes();
                    double hours = mins > 0 ? (mins / 60.0) : 8.0;
                    earned = hours * (existing.getHourlyRate() != null ? existing.getHourlyRate() : 10000.0);
                }
                driver.setAccumulatedEarnings((driver.getAccumulatedEarnings() != null ? driver.getAccumulatedEarnings() : 0.0) + earned);
                userRepository.save(driver);
            }
        }
        return shiftRepository.save(existing);
    }

    @Transactional
    public void deleteShift(Long shiftId) {
        timeLogRepository.findByShiftId(shiftId).forEach(timeLogRepository::delete);
        shiftRepository.deleteById(shiftId);
    }

    @Transactional
    public void deleteAllShifts() {
        timeLogRepository.deleteAll();
        shiftRepository.deleteAll();
    }

    @Transactional
    public void resetAllData() {
        timeLogRepository.deleteAll();
        shiftRepository.deleteAll();
        storeRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.save(new AppUser("ARMI Admin", "admin@armi.com", "123456", "ADMIN"));
    }

    @Transactional
    public Shift assignShift(Long shiftId, Long driverId) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        AppUser driver = userRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));
        
        LocalDateTime start = shift.getStartTime();
        int days = (shift.getDurationDays() != null && shift.getDurationDays() > 0) ? shift.getDurationDays() : 1;
        LocalDateTime end = shift.getEndTime().plusDays(days - 1);
        List<Shift> overlapping = shiftRepository.findOverlappingShifts(driver, start, end);
        if (!overlapping.isEmpty()) {
            throw new RuntimeException("El repartidor ya tiene un turno asignado en ese horario (cruce de horarios).");
        }

        shift.setAssignedTo(driver);
        shift.setStatus("assigned");
        return shiftRepository.save(shift);
    }

    @Transactional
    public Shift startShift(Long shiftId, Long driverId) {
        return startShift(shiftId, driverId, null, null);
    }

    @Transactional
    public Shift startShift(Long shiftId, Long driverId, Double lat, Double lng) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        if (shift.getAssignedTo() == null || !shift.getAssignedTo().getId().equals(driverId)) {
            throw new RuntimeException("Turno no asignado a este repartidor");
        }
        if (!"assigned".equals(shift.getStatus())) {
            throw new RuntimeException("El turno no está en estado asignado");
        }

        // Enforce rule: Cannot start a new shift if another shift is currently in progress
        List<Shift> activeShifts = shiftRepository.findAll().stream()
                .filter(s -> s.getAssignedTo() != null && s.getAssignedTo().getId().equals(driverId) && "in_progress".equals(s.getStatus()))
                .toList();
        if (!activeShifts.isEmpty()) {
            throw new RuntimeException("No puedes iniciar este turno porque ya tienes un turno en curso. Debes finalizar el turno activo antes de iniciar uno nuevo.");
        }

        shift.setStatus("in_progress");
        LocalDateTime now = LocalDateTime.now();

        TimeLog log = new TimeLog();
        log.setShift(shift);
        log.setDriver(shift.getAssignedTo());
        log.setActualStart(now);

        // Strict Geolocation Enforcement: Block starting shift if GPS is off or missing
        if (lat == null || lng == null || (lat == 0.0 && lng == 0.0)) {
            throw new RuntimeException("No se detectó la ubicación GPS de tu dispositivo. Por favor activa la ubicación (GPS) en tu teléfono y otorga permisos para poder iniciar el turno.");
        }

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

        timeLogRepository.save(log);
        return shiftRepository.save(shift);
    }

    @Transactional
    public Shift endShift(Long shiftId, Long driverId) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        if (shift.getAssignedTo() == null || !shift.getAssignedTo().getId().equals(driverId) || !"in_progress".equals(shift.getStatus())) {
            throw new RuntimeException("Operación inválida o el turno no está en curso");
        }

        // Calculate single daily payout for 1 completed session (FIXED vs HOURLY)
        double dailyEarned;
        if ("FIXED".equalsIgnoreCase(shift.getPayType())) {
            dailyEarned = (shift.getHourlyRate() != null && shift.getHourlyRate() > 0) ? shift.getHourlyRate() : 60000.0;
        } else {
            long scheduledMinutes = Duration.between(shift.getStartTime(), shift.getEndTime()).toMinutes();
            double scheduledHours = scheduledMinutes > 0 ? (scheduledMinutes / 60.0) : 8.0;
            dailyEarned = scheduledHours * (shift.getHourlyRate() != null ? shift.getHourlyRate() : 10000.0);
        }

        TimeLog log = timeLogRepository.findFirstByShiftIdOrderByIdDesc(shiftId)
                .orElseThrow(() -> new RuntimeException("No se encontró el registro de inicio de este turno"));
        log.setActualEnd(LocalDateTime.now());
        log.setTotalEarned(dailyEarned);
        timeLogRepository.save(log);

        AppUser driver = shift.getAssignedTo();
        driver.setAccumulatedEarnings(driver.getAccumulatedEarnings() + dailyEarned);
        userRepository.save(driver);

        // Decrement remaining days in shift block
        int currentDuration = (shift.getDurationDays() != null && shift.getDurationDays() > 0) ? shift.getDurationDays() : 1;
        int remainingDays = currentDuration - 1;
        shift.setDurationDays(remainingDays);

        if (remainingDays > 0) {
            // Move session dates forward 1 day for tomorrow and set back to assigned (Próximos)
            shift.setStartTime(shift.getStartTime().plusDays(1));
            shift.setEndTime(shift.getEndTime().plusDays(1));
            shift.setStatus("assigned");
        } else {
            shift.setStatus("completed");
        }

        return shiftRepository.save(shift);
    }

    public List<TimeLog> getAllTimeLogs() {
        return timeLogRepository.findAll();
    }
}
