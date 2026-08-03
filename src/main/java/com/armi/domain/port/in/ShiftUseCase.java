package com.armi.domain.port.in;

import com.armi.model.Shift;
import com.armi.model.TimeLog;
import java.util.List;

public interface ShiftUseCase {
    List<Shift> getAllShifts();
    Shift createShift(Shift shift);
    List<Shift> createRecurringShift(Shift shift, Integer days, Long driverId);
    List<Shift> createShiftsBulk(List<Shift> shifts);
    Shift updateShift(Long shiftId, Shift updated);
    void deleteShift(Long shiftId);
    void deleteAllShifts();
    void resetAllData();
    Shift assignShift(Long shiftId, Long driverId);
    Shift startShift(Long shiftId, Long driverId, Double lat, Double lng);
    Shift endShift(Long shiftId, Long driverId);
    List<TimeLog> getAllTimeLogs();
}
