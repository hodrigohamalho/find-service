package com.redhat.cajun.navy.finding.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.LinkedList;
import java.util.List;

@RegisterForReflection
public class Shelter {

    private String name;

    private String lat;

    private String lon;

    public Shelter() {

    }

    public Shelter(String name, String lat, String lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public static class Builder {

        private final Shelter shelter;

        public Builder(String name) {
            shelter = new Shelter();
            shelter.name = name;
        }

        public Shelter.Builder lat(String lat) {
            shelter.lat = lat;
            return this;
        }

        public Shelter.Builder lon(String lon) {
            shelter.lon = lon;
            return this;
        }

        public Shelter build() {
            return shelter;
        }

    }

    public static List<Shelter> getShelterList() {
        List<Shelter> shelterList = new LinkedList<>();
        shelterList.add(new Shelter("Port City Marina", "34.2461", "-77.9519"));
        shelterList.add(new Shelter("Wilmington Marine Center", "34.1706", "-77.949"));
        shelterList.add(new Shelter("Carolina Beach Yacht Club", "34.0583", "-77.8885"));
        return shelterList;
    }
}

