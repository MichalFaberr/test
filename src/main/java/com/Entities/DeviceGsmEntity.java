package com.Entities;

/**
 * Created by michal on 20.04.17.
 */


public class DeviceGsmEntity {


    private String idgsm;

    private Integer idfly;

    public DeviceGsmEntity(String idgsm, Integer idfly) {
        this.idgsm = idgsm;
        this.idfly = idfly;
    }

    public DeviceGsmEntity() { }

    public String getIdgsm() {
        return idgsm;
    }

    public void setIdgsm(String idgsm) {
        this.idgsm = idgsm;
    }

    public Integer getIdfly() {
        return idfly;
    }

    public void setIdfly(Integer idfly) {
        this.idfly = idfly;
    }

    @Override
    public String toString() {
        return "DeviceGsmEntity{" +
                "idgsm='" + idgsm + '\'' +
                ", idfly=" + idfly +
                '}';
    }
}
