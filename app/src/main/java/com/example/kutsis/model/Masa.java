package com.example.kutsis.model;

public class Masa {
    private Long masaId;
    private Boolean isReserve;

    public Boolean getReserve() {
        return isReserve;
    }

    public void setReserve(Boolean reserve) {
        isReserve = reserve;
    }

    public Long getMasaId() {
        return masaId;
    }

    public void setMasaId(Long masaId) {
        this.masaId = masaId;
    }
}
