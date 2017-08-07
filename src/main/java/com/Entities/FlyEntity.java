package com.Entities;

/**
 * Created by michal on 04.04.17.
 */

import java.util.Date;


public class FlyEntity{

    private int idfly;

    private String name;

    private Date date;

    public FlyEntity(int idfly, String name, Date date) {
        this.idfly = idfly;
        this.name = name;
        this.date = date;
    }

    public FlyEntity() {

    }

    public int getIdfly() {
        return idfly;
    }

    public void setIdfly(int idfly) {
        this.idfly = idfly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "FlyEntity{" +
                "idfly=" + idfly +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
