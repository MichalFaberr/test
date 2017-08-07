package com.Entities;

import java.util.Date;

/**
 * Created by michal on 04.04.17.
 */

public class GpsEntity {

    private int idgps;

    private int idfly;

    private float longitude;

    private float latitude;

    private int altitude;

    private int way;

    private Date date;

    public GpsEntity(int idfly, float longitude, float latitude, int altitude, int way, Date date) {
        this.idfly = idfly;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.way = way;
        this.date = date;
    }

    public GpsEntity(){
    }

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdgps() {
        return idgps;
    }

    public void setIdgps(int idgps) {
        this.idgps = idgps;
    }

    public int getIdfly() {
        return idfly;
    }

    public void setIdfly(int idfly) {
        this.idfly = idfly;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }


    @Override
    public String toString() {
        return "GpsEntity{" +
                "idgps=" + idgps +
                ", idfly=" + idfly +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", way=" + way +
                ", date=" + date +
                '}';
    }

    public String toStringLatLonAlt(){
        return latitude + " " + longitude + " " + altitude;
    }
}
