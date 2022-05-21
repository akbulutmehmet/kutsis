package com.example.kutsis.model;

import java.io.Serializable;
import java.util.Date;

public class Masa implements Serializable {
    private Long id;
    private Boolean isReserve;
    private Date lastReserveDate;
    public Masa () {

    }

    public Date getLastReserveDate() {
        return lastReserveDate;
    }

    public void setLastReserveDate(Date lastReserveDate) {
        this.lastReserveDate = lastReserveDate;
    }

    public Masa (Long id, Boolean isReserve,Date date) {
        this.id = id;
        this.isReserve = isReserve;
        this.lastReserveDate = date;
    }
    public Boolean getReserve() {
        return isReserve;
    }

    public void setReserve(Boolean reserve) {
        isReserve = reserve;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
