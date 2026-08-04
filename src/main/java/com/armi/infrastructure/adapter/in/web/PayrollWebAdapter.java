package com.armi.infrastructure.adapter.in.web;

import com.armi.model.PayrollLog;
import com.armi.repository.PayrollLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payroll")
@CrossOrigin(origins = "*")
public class PayrollWebAdapter {

    private final PayrollLogRepository payrollLogRepository;

    public PayrollWebAdapter(PayrollLogRepository payrollLogRepository) {
        this.payrollLogRepository = payrollLogRepository;
    }

    @GetMapping
    public ResponseEntity<List<PayrollLog>> getAllPayrollLogs() {
        return ResponseEntity.ok(payrollLogRepository.findAll());
    }

    @GetMapping("/summary")
    public ResponseEntity<List<Map<String, Object>>> getWeeklyPayrollSummary() {
        List<PayrollLog> logs = payrollLogRepository.findAll();
        Map<String, List<PayrollLog>> grouped = logs.stream()
                .collect(Collectors.groupingBy(l -> (l.getDriverEmail() != null ? l.getDriverEmail() : l.getDriverName())));

        List<Map<String, Object>> summaryList = new ArrayList<>();
        for (Map.Entry<String, List<PayrollLog>> entry : grouped.entrySet()) {
            List<PayrollLog> driverLogs = entry.getValue();
            if (driverLogs.isEmpty()) continue;

            PayrollLog first = driverLogs.get(0);
            double totalPay = driverLogs.stream().mapToDouble(l -> l.getPayValue() != null ? l.getPayValue() : 0.0).sum();
            int totalOrders = driverLogs.stream().mapToInt(l -> l.getCompletedOrdersCount() != null ? l.getCompletedOrdersCount() : 0).sum();
            Set<String> stores = driverLogs.stream().map(PayrollLog::getStoreName).filter(Objects::nonNull).collect(Collectors.toSet());

            Map<String, Object> map = new HashMap<>();
            map.put("driverName", first.getDriverName());
            map.put("driverEmail", first.getDriverEmail());
            map.put("weekRange", first.getWeekRange());
            map.put("totalShifts", driverLogs.size());
            map.put("totalOrders", totalOrders);
            map.put("totalPay", totalPay);
            map.put("stores", String.join(", ", stores));
            map.put("logs", driverLogs);

            summaryList.add(map);
        }

        return ResponseEntity.ok(summaryList);
    }

    @PostMapping
    public ResponseEntity<PayrollLog> createPayrollLog(@RequestBody PayrollLog payrollLog) {
        return ResponseEntity.ok(payrollLogRepository.save(payrollLog));
    }

    @PostMapping("/seed")
    public ResponseEntity<List<PayrollLog>> seedInitialExcelPayroll() {
        List<PayrollLog> seeds = Arrays.asList(
                new PayrollLog("27 al 02 de Agosto", "Jean Carlos zapata pulgarin", "551004735386", "KFC Centro Pereira", "11:00 - 15:00", 4, 48000.0),
                new PayrollLog("27 al 02 de Agosto", "Oscar Gallo", "5593370096", "KFC Galerias", "17:00 - 21:00", 4, 48000.0),
                new PayrollLog("27 al 02 de Agosto", "Jesús Alberto Martinez Carrasco", "551127927088", "KFC Pedro Heredia", "18:00 - 22:00", 4, 48000.0),
                new PayrollLog("27 al 02 de Agosto", "José Carlos Cogollo González", "551047445372", "KFC Turbaco", "11:00 - 15:00", 4, 48000.0),
                new PayrollLog("27 al 02 de Agosto", "Carlos palacio torres", "5572018940", "KFC La Cordialidad", "12:00 - 16:00", 5, 60000.0),
                new PayrollLog("27 al 02 de Agosto", "Andrés Felipe Ladino Bartolo", "551004720234", "KFC Centro", "12:00 - 16:00", 4, 48000.0),
                new PayrollLog("27 al 02 de Agosto", "Huberney Flórez castaño", "559874376", "KFC Centro Pereira", "11:00 - 15:00", 4, 48000.0),
                new PayrollLog("27 al 02 de Agosto", "Allan David polo", "551044216654", "KFC Villa de Mar", "12:00 - 16:00", 4, 48000.0)
        );
        return ResponseEntity.ok(payrollLogRepository.saveAll(seeds));
    }
}
