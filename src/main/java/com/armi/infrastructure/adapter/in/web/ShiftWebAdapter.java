package com.armi.infrastructure.adapter.in.web;

import com.armi.domain.port.in.ShiftUseCase;
import com.armi.model.Shift;
import com.armi.model.TimeLog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@CrossOrigin(origins = "*")
public class ShiftWebAdapter {

    private final ShiftUseCase shiftUseCase;

    public ShiftWebAdapter(ShiftUseCase shiftUseCase) {
        this.shiftUseCase = shiftUseCase;
    }

    @GetMapping
    public ResponseEntity<List<Shift>> getAllShifts() {
        return ResponseEntity.ok(shiftUseCase.getAllShifts());
    }

    @PostMapping
    public ResponseEntity<Shift> createShift(@RequestBody Shift shift) {
        return ResponseEntity.ok(shiftUseCase.createShift(shift));
    }

    @PostMapping("/recurring")
    public ResponseEntity<List<Shift>> createRecurringShift(
            @RequestBody Shift shift,
            @RequestParam(required = false, defaultValue = "1") Integer days,
            @RequestParam(required = false) Long driverId
    ) {
        return ResponseEntity.ok(shiftUseCase.createRecurringShift(shift, days, driverId));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Shift>> createShiftsBulk(@RequestBody List<Shift> shifts) {
        return ResponseEntity.ok(shiftUseCase.createShiftsBulk(shifts));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shift> updateShift(@PathVariable Long id, @RequestBody Shift shift) {
        return ResponseEntity.ok(shiftUseCase.updateShift(id, shift));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        shiftUseCase.deleteShift(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllShifts() {
        shiftUseCase.deleteAllShifts();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{shiftId}/assign/{driverId}")
    public ResponseEntity<?> assignShift(@PathVariable Long shiftId, @PathVariable Long driverId) {
        try {
            return ResponseEntity.ok(shiftUseCase.assignShift(shiftId, driverId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{shiftId}/start/{driverId}")
    public ResponseEntity<?> startShift(
            @PathVariable Long shiftId,
            @PathVariable Long driverId,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng
    ) {
        try {
            return ResponseEntity.ok(shiftUseCase.startShift(shiftId, driverId, lat, lng));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{shiftId}/end/{driverId}")
    public ResponseEntity<?> endShift(@PathVariable Long shiftId, @PathVariable Long driverId) {
        try {
            return ResponseEntity.ok(shiftUseCase.endShift(shiftId, driverId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
