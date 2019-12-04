package com.redhat.cajun.navy.finding.service;

import com.redhat.cajun.navy.finding.model.Shelter;
import com.redhat.cajun.navy.finding.model.Victim;
import io.vertx.core.json.JsonObject;

public class JsonCodec {

    public static JsonObject toJsonObject(Victim victim) {
        return new JsonObject().put("id", victim.getId())
                .put("lat", victim.getLat())
                .put("lon", victim.getLon())
                .put("medicalNeeded", victim.isMedicalNeeded())
                .put("numberOfPeople", victim.getNumberOfPeople())
                .put("victimName", victim.getVictimName())
                .put("victimPhoneNumber", victim.getVictimPhoneNumber())
                .put("timeStamp", victim.getTimestamp())
                .put("status", victim.getStatus());
    }

    public static JsonObject toShelterJsonObject(Shelter shelter) {
        return new JsonObject().put("name", shelter.getName())
                .put("lat", shelter.getLat())
                .put("lon", shelter.getLon());
    }

}
