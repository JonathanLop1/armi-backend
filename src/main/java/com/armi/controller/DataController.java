package com.armi.controller;

import com.armi.domain.port.in.ShiftUseCase;
import com.armi.domain.port.in.UserUseCase;
import com.armi.repository.IncidentReportRepository;
import com.armi.repository.OrderLogRepository;
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
    private final OrderLogRepository orderLogRepository;
    private final IncidentReportRepository incidentReportRepository;

    public DataController(
            ShiftUseCase shiftUseCase,
            UserUseCase userUseCase,
            StoreRepository storeRepository,
            OrderLogRepository orderLogRepository,
            IncidentReportRepository incidentReportRepository
    ) {
        this.shiftUseCase = shiftUseCase;
        this.userUseCase = userUseCase;
        this.storeRepository = storeRepository;
        this.orderLogRepository = orderLogRepository;
        this.incidentReportRepository = incidentReportRepository;
    }

    // Dashboard Data (Aggregated Facade for Web Dashboard)
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        data.put("stores", storeRepository.findAll());
        data.put("shifts", shiftUseCase.getAllShifts());
        data.put("users", userUseCase.getAllUsers());
        data.put("timeLogs", shiftUseCase.getAllTimeLogs());
        data.put("orders", orderLogRepository.findAll());
        data.put("incidents", incidentReportRepository.findAll());
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

    @DeleteMapping("/stores/all")
    public ResponseEntity<Void> deleteAllStores() {
        storeRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    // Completely wipe all tables to 0 records (Clean Slate)
    @DeleteMapping("/data/clean-slate")
    public ResponseEntity<String> cleanSlateDatabase() {
        orderLogRepository.deleteAll();
        incidentReportRepository.deleteAll();
        shiftUseCase.deleteAllShifts();
        userUseCase.deleteAllUsers();
        storeRepository.deleteAll();
        return ResponseEntity.ok("Base de datos vaciada totalmente a 0 registros.");
    }

    @DeleteMapping("/data/reset-all")
    public ResponseEntity<String> resetAllData() {
        return cleanSlateDatabase();
    }
}
