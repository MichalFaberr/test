package com.Entities;


import javax.validation.constraints.NotNull;

/**
 * Created by michal on 20.04.17.
 */

public class AppTtnEntity {

    private String accesskey;

    private String appid;

    private String region;

    private Boolean active;

    public AppTtnEntity(String accesskey, String appid, String region, Boolean active) {
        this.accesskey = accesskey;
        this.appid = appid;
        this.region = region;
        this.active = active;
    }

    public AppTtnEntity() { }

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "AppTtnEntity{" +
                "accesskey='" + accesskey + '\'' +
                ", appid='" + appid + '\'' +
                ", region='" + region + '\'' +
                ", active=" + active +
                '}';
    }
}
