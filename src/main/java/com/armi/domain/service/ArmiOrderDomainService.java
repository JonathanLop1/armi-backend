package com.armi.domain.service;

import com.armi.domain.port.in.ArmiOrderUseCase;
import com.armi.domain.port.out.ArmiApiPort;
import com.armi.dto.ArmiStoreOrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ArmiOrderDomainService implements ArmiOrderUseCase {

    private final ArmiApiPort armiApiPort;

    public ArmiOrderDomainService(ArmiApiPort armiApiPort) {
        this.armiApiPort = armiApiPort;
    }

    @Override
    public ResponseEntity<Map<String, Object>> fetchOrdersByStore(ArmiStoreOrderRequest request) {
        return armiApiPort.queryOrdersByStore(request);
    }
}
