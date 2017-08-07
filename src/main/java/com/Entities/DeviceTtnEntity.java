package com.Entities;

/**
 * Created by michal on 20.04.17.
 */

public class DeviceTtnEntity {

    private Integer id;

    private String accesskey;

    private String devid;

    private Integer idfly;

    private Boolean active;

    public DeviceTtnEntity(Integer id, String accesskey, String devid, Integer idfly, Boolean active) {
        this.id = id;
        this.accesskey = accesskey;
        this.devid = devid;
        this.idfly = idfly;
        this.active = active;
    }

    public DeviceTtnEntity() { }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public Integer getIdfly() {
        return idfly;
    }

    public void setIdfly(Integer idfly) {
        this.idfly = idfly;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "DeviceTtnEntity{" +
                "id=" + id +
                ", accesskey='" + accesskey + '\'' +
                ", devid='" + devid + '\'' +
                ", idfly=" + idfly +
                ", active=" + active +
                '}';
    }
}
