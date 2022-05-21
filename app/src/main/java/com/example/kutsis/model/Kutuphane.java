package com.example.kutsis.model;

import java.io.Serializable;
import java.util.List;

public class Kutuphane implements Serializable {
    private Long id;
    private List<Masa> masaList;

    public void setMasaList(List<Masa> masaList) {
        this.masaList = masaList;
    }

    public List<Masa> getMasaList() {
        return masaList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
