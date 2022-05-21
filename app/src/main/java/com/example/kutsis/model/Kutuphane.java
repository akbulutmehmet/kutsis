package com.example.kutsis.model;

import java.io.Serializable;
import java.util.List;

public class Kutuphane implements Serializable {
    private Long id;
    private String kutuphaneName;
    private List<Masa> masaList;
    public Kutuphane () {

    }
    public  Kutuphane (Long id,List<Masa> masaList,String kutuphaneName) {
        this.id = id;
        this.masaList = masaList;
        this.kutuphaneName = kutuphaneName;
    }

    public String getKutuphaneName() {
        return kutuphaneName;
    }

    public void setKutuphaneName(String kutuphaneName) {
        this.kutuphaneName = kutuphaneName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<Masa> getMasaList() {
        return masaList;
    }

    public void setMasaList(List<Masa> masaList) {
        this.masaList = masaList;
    }

}
