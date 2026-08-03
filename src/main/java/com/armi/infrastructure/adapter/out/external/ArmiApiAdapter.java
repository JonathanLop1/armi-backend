package com.armi.infrastructure.adapter.out.external;

import com.armi.domain.port.out.ArmiApiPort;
import com.armi.dto.ArmiStoreOrderRequest;
import com.armi.service.ArmiOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ArmiApiAdapter implements ArmiApiPort {

    private final ArmiOrderService armiOrderService;

    public ArmiApiAdapter(ArmiOrderService armiOrderService) {
        this.armiOrderService = armiOrderService;
    }

    @Override
    public ResponseEntity<Map<String, Object>> queryOrdersByStore(ArmiStoreOrderRequest request) {
        return armiOrderService.fetchOrdersByStore(request);
    }
}
