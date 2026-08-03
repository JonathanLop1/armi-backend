package com.armi.domain.port.out;

import com.armi.dto.ArmiStoreOrderRequest;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface ArmiApiPort {
    ResponseEntity<Map<String, Object>> queryOrdersByStore(ArmiStoreOrderRequest request);
}
