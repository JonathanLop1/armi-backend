package com.armi.infrastructure.adapter.in.web;

import com.armi.domain.port.in.ArmiOrderUseCase;
import com.armi.dto.ArmiStoreOrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/armi")
@CrossOrigin(origins = "*")
public class ArmiWebAdapter {

    private final ArmiOrderUseCase armiOrderUseCase;

    public ArmiWebAdapter(ArmiOrderUseCase armiOrderUseCase) {
        this.armiOrderUseCase = armiOrderUseCase;
    }

    @PostMapping("/orders-by-store")
    public ResponseEntity<?> getArmiOrdersByStore(@RequestBody ArmiStoreOrderRequest request) {
        return armiOrderUseCase.fetchOrdersByStore(request);
    }
}
