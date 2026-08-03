package com.armi.domain.port.in;

import com.armi.dto.ArmiStoreOrderRequest;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface ArmiOrderUseCase {
    ResponseEntity<Map<String, Object>> fetchOrdersByStore(ArmiStoreOrderRequest request);
}
