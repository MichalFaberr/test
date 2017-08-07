package com.Tools;

import com.Entities.FlyEntity;
import com.Entities.GpsEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * Created by michal on 22.04.17.
 */
public class CommunicationTools extends Rest {

    public static GpsEntity[] getGpsAll() {
        return getArray(_baseUrl + "/gps/all", GpsEntity[].class);
    }

    public static GpsEntity[] getGpsAllIdFly(int idfly) {
        return getArray(_baseUrl + "/gps/all/" + idfly, GpsEntity[].class);
    }

    public static GpsEntity getGpsLastIdFly(int idfly) {

        return getOne(_baseUrl + "/gps/last/" + idfly, GpsEntity.class);
        /*
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<GpsEntity[]> responseEntity = restTemplate.getForEntity(_baseUrl + "/gps/last/" + idfly, GpsEntity[].class);
            GpsEntity[] gpsObjects = responseEntity.getBody();
            HttpStatus statusCode = responseEntity.getStatusCode();
            if (statusCode != HttpStatus.OK)
                return null;

            return gpsObjects[0];

        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }*/
    }

    public static FlyEntity[] getFlyAll() {

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<FlyEntity[]> responseEntity = restTemplate.getForEntity(_baseUrl + "/fly/all", FlyEntity[].class);
            FlyEntity[] flyObjects = responseEntity.getBody();
            HttpStatus statusCode = responseEntity.getStatusCode();
            if (statusCode != HttpStatus.OK)
                return null;

            return flyObjects;

        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public static FlyEntity getFlyById(int idfly) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<FlyEntity[]> responseEntity = restTemplate.getForEntity(_baseUrl + "/fly/idfly/" + idfly, FlyEntity[].class);
            FlyEntity[] flyObjects = responseEntity.getBody();
            HttpStatus statusCode = responseEntity.getStatusCode();
            if (statusCode != HttpStatus.OK)
                return null;

            return flyObjects[0];

        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public static GpsEntity[] getGpsAllIdFly(int idfly, Date fromDate) {
        return postOneGetArray(_baseUrl + "/gps/date/" + idfly, fromDate, GpsEntity[].class);
    }

    public static boolean updateTtn(String appKey, String appId, String appRegion, String devId) {
        return putOne(_baseUrl + "/ttn/update", new String[]{appKey, appId, appRegion, devId});
    }
}
