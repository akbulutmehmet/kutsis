package com.example.kutsis.model;

import java.util.List;

public class Oda {
    private Long odaId;
    private List<Masa> masaList;

    public List<Masa> getMasaList() {
        return masaList;
    }

    public void setMasaList(List<Masa> masaList) {
        this.masaList = masaList;
    }

    public Long getOdaId() {
        return odaId;
    }

    public void setOdaId(Long odaId) {
        this.odaId = odaId;
    }
}
