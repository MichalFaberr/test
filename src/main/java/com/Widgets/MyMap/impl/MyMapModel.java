package com.Widgets.MyMap.impl;

import com.Entities.GpsEntity;
import com.Tools.CommunicationTools;
import sun.awt.Mutex;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by michal on 22.04.17.
 */
public class MyMapModel {

    enum GPS_SELECT {
        LAST,
        ALL
    }
    private GPS_SELECT _gpsSelect = GPS_SELECT.LAST;

    private AtomicBoolean _autoCenter = new AtomicBoolean(true);

    private double _zoomValue = 20.0;

    private Mutex _mutexZoomValue;
    private AtomicInteger _timeToRefreshMs = new AtomicInteger(5000);

    private Date _fromDate;

    private Mutex _mutexFromDate = new Mutex();
    private AtomicBoolean _fromDateEnabled = new AtomicBoolean(false);

    private AtomicBoolean _enableTtnSettings = new AtomicBoolean(false);

    private int _idfly;

    public MyMapModel(int idfly) {
        _idfly = idfly;
        _mutexZoomValue = new Mutex();

    }

    GpsEntity[] getGpsAll() {

        if (_fromDateEnabled.get()) {
            _mutexFromDate.lock();
            Date fromDateCurrentValue = _fromDate;
            _mutexFromDate.unlock();
            return CommunicationTools.getGpsAllIdFly(_idfly, fromDateCurrentValue);
        } else {
            return CommunicationTools.getGpsAllIdFly(_idfly);
        }
    }

    GpsEntity getGpsLast() {
        return CommunicationTools.getGpsLastIdFly(_idfly);
    }

    void setFromDate(LocalDateTime fromDate) {
        _mutexFromDate.lock();
        _fromDate = Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant());
        _mutexFromDate.unlock();
    }

    public Boolean get_enableTtnSettings() {
        return _enableTtnSettings.get();
    }

    public void set_enableTtnSettings(Boolean enableTtnSettings) {
        this._enableTtnSettings.set(enableTtnSettings);
    }

    Date get_fromDate() {
        _mutexFromDate.lock();
        Date fromDate = _fromDate;
        _mutexFromDate.unlock();
        return fromDate;
    }

    Boolean get_fromDateEnabled() {
        return _fromDateEnabled.get();
    }

    void set_fromDateEnabled(Boolean fromDateEnabled) {
        this._fromDateEnabled.set(fromDateEnabled);
    }

    GPS_SELECT get_gpsSelect() {
        return _gpsSelect;
    }

    void set_gpsSelect(GPS_SELECT _gpsSelect) {
        this._gpsSelect = _gpsSelect;
    }

    boolean is_autoCenter() {
        return _autoCenter.get();
    }

    void set_autoCenter(boolean autoCenter) {
        _autoCenter.set(autoCenter);
    }

    double get_zoomValue() {
        _mutexZoomValue.lock();
        double zoomValue = _zoomValue;
        _mutexZoomValue.unlock();
        return zoomValue;
    }

    void set_zoomValue(double _zoomValue) {
        _mutexZoomValue.lock();
        this._zoomValue = _zoomValue;
        _mutexZoomValue.unlock();
    }

    int get_timeToRefreshMs() {
        return _timeToRefreshMs.get();
    }

    void set_timeToRefreshMs(int timeToRefreshMs) {
        this._timeToRefreshMs.set(timeToRefreshMs);
    }

    boolean updateTtn(String appKey, String appId, String appRegion, String deviceId) {
        return CommunicationTools.updateTtn(
                appKey,
                appId,
                appRegion,
                deviceId
        );
    }
}
