package com.armi.dto;

public class ArmiStoreOrderRequest {
    private String storeId;
    private String cityId;
    private String fechaInicio;
    private String fechaFin;

    public ArmiStoreOrderRequest() {}

    public ArmiStoreOrderRequest(String storeId, String cityId, String fechaInicio, String fechaFin) {
        this.storeId = storeId;
        this.cityId = cityId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }
    public String getCityId() { return cityId; }
    public void setCityId(String cityId) { this.cityId = cityId; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
}
