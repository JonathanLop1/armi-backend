package com.armi.infrastructure.adapter.out.persistence;

import com.armi.domain.port.out.ShiftPersistencePort;
import com.armi.model.AppUser;
import com.armi.model.Shift;
import com.armi.model.Store;
import com.armi.model.TimeLog;
import com.armi.repository.ShiftRepository;
import com.armi.repository.StoreRepository;
import com.armi.repository.TimeLogRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ShiftPersistenceAdapter implements ShiftPersistencePort {

    private final ShiftRepository shiftRepository;
    private final StoreRepository storeRepository;
    private final TimeLogRepository timeLogRepository;

    public ShiftPersistenceAdapter(ShiftRepository shiftRepository, StoreRepository storeRepository, TimeLogRepository timeLogRepository) {
        this.shiftRepository = shiftRepository;
        this.storeRepository = storeRepository;
        this.timeLogRepository = timeLogRepository;
    }

    @Override
    public List<Shift> findAllShifts() {
        return shiftRepository.findAll();
    }

    @Override
    public Optional<Shift> findShiftById(Long id) {
        return shiftRepository.findById(id);
    }

    @Override
    public Shift saveShift(Shift shift) {
        return shiftRepository.save(shift);
    }

    @Override
    public void deleteShiftById(Long id) {
        shiftRepository.deleteById(id);
    }

    @Override
    public void deleteAllShifts() {
        shiftRepository.deleteAll();
    }

    @Override
    public List<Shift> findOverlappingShifts(AppUser driver, LocalDateTime start, LocalDateTime end) {
        return shiftRepository.findOverlappingShifts(driver, start, end);
    }

    @Override
    public Store saveStore(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public List<Store> findAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public void deleteAllStores() {
        storeRepository.deleteAll();
    }

    @Override
    public TimeLog saveTimeLog(TimeLog log) {
        return timeLogRepository.save(log);
    }

    @Override
    public List<TimeLog> findAllTimeLogs() {
        return timeLogRepository.findAll();
    }

    @Override
    public Optional<TimeLog> findFirstTimeLogByShiftId(Long shiftId) {
        return timeLogRepository.findFirstByShiftIdOrderByIdDesc(shiftId);
    }

    @Override
    public void deleteAllTimeLogs() {
        timeLogRepository.deleteAll();
    }
}
