package com.Tools;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by michal on 03.06.17.
 */
class Rest {

    static final String _baseUrl = "http://localhost:8080";

    static <T> T getOne(String url, Class<T> typeClassReceive) {
        T obj = null;

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<T> responseEntity = restTemplate.getForEntity(url, typeClassReceive);

            if (responseEntity.getStatusCode() != HttpStatus.OK)
                return null;

            obj = responseEntity.getBody();

        } catch (Exception e) {
            System.err.println(e.toString());

        }

        return obj;
    }

    static <T> T[] getArray(String url, Class<T[]> typeClass) {

        T[] objectsArray = null;

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<T[]> responseEntity = restTemplate.getForEntity(url, typeClass);

            if (responseEntity.getStatusCode() != HttpStatus.OK)
                return null;

            objectsArray = responseEntity.getBody();

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        return objectsArray;
    }

    static <T> boolean postOne(String url, T objSend) {

        try {

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<T> entity = new HttpEntity<>(objSend);
            ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, entity, ResponseEntity.class);

            if (response.getStatusCode() != HttpStatus.OK) return false;

        } catch (Exception e) {
            System.err.println(e.toString());
            return false;
        }

        return true;
    }

    static <T, E> E postOneGetOne(String url, T objSend, Class<E> typeObjReceive) {

        E objReceive = null;

        try {

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<T> entity = new HttpEntity<>(objSend);
            ResponseEntity<E> response = restTemplate.exchange(url, HttpMethod.POST, entity, typeObjReceive);

            objReceive = response.getBody();

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        return objReceive;
    }

    static <T, E> T[] postOneGetArray(String url, E objSend, Class<T[]> typeClassResponse) {

        T[] objectsArray = null;

        try {

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<E> entitySend = new HttpEntity<>(objSend);
            ResponseEntity<T[]> entityResponse = restTemplate.exchange(url, HttpMethod.POST, entitySend, typeClassResponse);

            objectsArray = entityResponse.getBody();

        } catch (Exception e) {
            System.err.println("postOneGetList: " + e.toString());
        }

        return objectsArray;
    }


    static <T> boolean putOne(String url, T objSend) {

        try {

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<T> entitySend = new HttpEntity<>(objSend);
            ResponseEntity response = restTemplate.exchange(url, HttpMethod.PUT, entitySend, ResponseEntity.class);

            if (response.getStatusCode() != HttpStatus.OK) return false;

        } catch (Exception e) {
            System.err.println(e.toString());
            return false;
        }

        return true;
    }

    static <T> boolean deleteOne(String url, T objSend) {

        try {

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<T> entity = new HttpEntity<>(objSend);
            ResponseEntity response = restTemplate.exchange(url, HttpMethod.DELETE, entity, ResponseEntity.class);

            if (response.getStatusCode() != HttpStatus.OK) return false;

        } catch (Exception e) {
            System.err.println(e.toString());
            return false;
        }

        return true;
    }
}