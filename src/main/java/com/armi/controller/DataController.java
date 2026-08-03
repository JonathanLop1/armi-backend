package com.armi.controller;

import com.armi.domain.port.in.ShiftUseCase;
import com.armi.domain.port.in.UserUseCase;
import com.armi.repository.StoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DataController {

    private final ShiftUseCase shiftUseCase;
    private final UserUseCase userUseCase;
    private final StoreRepository storeRepository;

    public DataController(ShiftUseCase shiftUseCase, UserUseCase userUseCase, StoreRepository storeRepository) {
        this.shiftUseCase = shiftUseCase;
        this.userUseCase = userUseCase;
        this.storeRepository = storeRepository;
    }

    // Dashboard Data (Aggregated Facade for Web Dashboard)
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        data.put("stores", storeRepository.findAll());
        data.put("shifts", shiftUseCase.getAllShifts());
        data.put("users", userUseCase.getAllUsers());
        data.put("timeLogs", shiftUseCase.getAllTimeLogs());
        return ResponseEntity.ok(data);
    }

    @GetMapping("/stores")
    public ResponseEntity<?> getStores() {
        return ResponseEntity.ok(storeRepository.findAll());
    }

    @GetMapping("/logs")
    public ResponseEntity<?> getTimeLogs() {
        return ResponseEntity.ok(shiftUseCase.getAllTimeLogs());
    }

    @DeleteMapping("/data/reset-all")
    public ResponseEntity<String> resetAllData() {
        shiftUseCase.resetAllData();
        return ResponseEntity.ok("Base de datos reiniciada totalmente desde cero.");
    }
}
