package com.armi.infrastructure.adapter.in.web;

import com.armi.model.OrderLog;
import com.armi.repository.OrderLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderWebAdapter {

    private final OrderLogRepository orderLogRepository;

    public OrderWebAdapter(OrderLogRepository orderLogRepository) {
        this.orderLogRepository = orderLogRepository;
    }

    @GetMapping
    public ResponseEntity<List<OrderLog>> getAllOrders() {
        return ResponseEntity.ok(orderLogRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<OrderLog> createOrder(@RequestBody OrderLog orderLog) {
        return ResponseEntity.ok(orderLogRepository.save(orderLog));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<OrderLog>> createOrdersBulk(@RequestBody List<OrderLog> orders) {
        return ResponseEntity.ok(orderLogRepository.saveAll(orders));
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllOrders() {
        orderLogRepository.deleteAll();
        return ResponseEntity.ok().build();
    }
}
