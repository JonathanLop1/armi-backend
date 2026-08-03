package com.armi.service;

import com.armi.dto.ArmiStoreOrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ArmiOrderService {

    @Value("${armi.api.baseUrl:https://armi-services-support-rakdtiqnya-uc.a.run.app/support}")
    private String baseUrl;

    @Value("${armi.api.token:6IiKv72bT9}")
    private String apiToken;

    private final RestTemplate restTemplate;

    public ArmiOrderService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Consulta órdenes por tienda en ARMI API: POST /delivery/v1/Consulta_Ordenes_x_Tienda
     * 
     * @param request Parámetros de consulta (storeId, cityId, fechaInicio, fechaFin)
     * @return Respuesta estructurada con el listado de órdenes y estado HTTP
     */
    public ResponseEntity<Map<String, Object>> fetchOrdersByStore(ArmiStoreOrderRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        String endpointUrl = baseUrl + "/delivery/v1/Consulta_Ordenes_x_Tienda";

        // Preparar headers con Content-Type y Authorization Bearer Token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiToken);

        // Estructura del cuerpo de la petición (Payload JSON)
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("tokenNum", apiToken);
        requestBody.put("storeId", request.getStoreId() != null ? request.getStoreId() : "1");
        requestBody.put("cityId", request.getCityId() != null ? request.getCityId() : "BARRANQUILLA");
        if (request.getFechaInicio() != null) requestBody.put("fechaInicio", request.getFechaInicio());
        if (request.getFechaFin() != null) requestBody.put("fechaFin", request.getFechaFin());

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> rawResponse = restTemplate.postForEntity(endpointUrl, httpEntity, String.class);
            String bodyText = rawResponse.getBody();

            if (bodyText == null || bodyText.trim().isEmpty()) {
                responseMap.put("success", true);
                responseMap.put("message", "No se encontraron órdenes registradas para esta tienda en el rango de tiempo seleccionado.");
                responseMap.put("orders", new Object[]{});
                return ResponseEntity.ok(responseMap);
            }

            responseMap.put("success", true);
            responseMap.put("data", bodyText);
            responseMap.put("status", rawResponse.getStatusCode().value());
            return ResponseEntity.ok(responseMap);

        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.Forbidden e) {
            responseMap.put("success", false);
            responseMap.put("error", "Error de Autenticación (401/403): Token inválido o permisos insuficientes.");
            responseMap.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            responseMap.put("success", false);
            responseMap.put("error", "Error HTTP en el servicio de ARMI: " + e.getStatusCode());
            responseMap.put("details", e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(responseMap);

        } catch (Exception e) {
            responseMap.put("success", false);
            responseMap.put("error", "Falla de conexión con el servicio ARMI.");
            responseMap.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }
}
