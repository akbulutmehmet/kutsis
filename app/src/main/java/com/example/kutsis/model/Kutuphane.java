package com.example.kutsis.model;

import java.util.List;

public class Kutuphane {
    private Long kutuphaneId;
    private List<Oda> odaList;

    public List<Oda> getOdaList() {
        return odaList;
    }

    public void setOdaList(List<Oda> odaList) {
        this.odaList = odaList;
    }

    public Long getKutuphaneId() {
        return kutuphaneId;
    }

    public void setKutuphaneId(Long kutuphaneId) {
        this.kutuphaneId = kutuphaneId;
    }
}
