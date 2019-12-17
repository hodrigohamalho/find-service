package com.redhat.cajun.navy.finding.service;

import com.redhat.cajun.navy.finding.client.IncidentRestClient;
import com.redhat.cajun.navy.finding.client.MissionRestClient;
import com.redhat.cajun.navy.finding.model.Shelter;
import com.redhat.cajun.navy.finding.model.Victim;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FindService {

    private static final Logger logger = LoggerFactory.getLogger(FindService.class);

    @Inject
    @RestClient
    IncidentRestClient incidentRestClient;

    @Inject
    @RestClient
    MissionRestClient missionRestClient;

    public Response getVictimByName(String name) {

        logger.info("***Requesting Victim Details for victim name - "+name+"***");

        List<Victim> victimList = null;
        try {
            logger.info("Request sent to Incident service for name - " + name);
            victimList = incidentRestClient.getByName(name);
            logger.info("Response received for name - " + name + " - " + victimList);
            JsonArray victimsArray = new JsonArray(victimList.stream().map(this::toJsonObject).collect(Collectors.toList()));
            JsonObject jsonObject = new JsonObject().put("victims", victimsArray);
            return Response.ok(jsonObject).build();
        } catch (Exception ex) {
            logger.info("Unable to connect to Incident Service. ", ex);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }
    }

    private JsonObject toJsonObject(Victim victim) {
        return JsonCodec.toJsonObject(victim);
    }

    public Response getVictimShelter(String incidentId) {

        logger.info("***Requesting Shelter Details for Incident - "+incidentId+"***");

        boolean isMissionCreated = false;
        String missionId = null;

        try {
            logger.info("Request sent to get Mission List");
            List<String> missionList = missionRestClient.getMissions();
            logger.info("Received Mission List");

            //Mission Id = Incident Id + Responder Id
            for (String mission : missionList) {
                isMissionCreated = mission.contains(incidentId);
                if (isMissionCreated) {
                    missionId = mission;
                    logger.info("Found mission Id - " + mission + " for Incident Id - " + incidentId);
                    break;
                }
            }

            if (!isMissionCreated) {
                logger.info("Mission not found for Incident Id - " + incidentId);
                JsonObject jsonObject = new JsonObject().put("status", false);
                jsonObject.put("Desc", "Mission not found");
                return Response.ok(jsonObject).build();
            }

            logger.info("Request sent for getting mission details of missionId - " + missionId);
            String missionDetails = missionRestClient.getMissionById(missionId);
            logger.info("Received Mission details for mission Id - " + missionId);

            JSONObject jsonResponse = new JSONObject(missionDetails);
            String shelterLat = jsonResponse.getString("destinationLat");
            String shelterLong = jsonResponse.getString("destinationLong");

            Shelter shelter = getShelterName(shelterLat, shelterLong);
            logger.info("Shelter Details for Mission Id - " + missionId + " name - " + shelter.getName() + " lat - " + shelter.getLat() + " Long - " + shelter.getLon());

            JsonObject jsonObject = new JsonObject().put("status", true);
            jsonObject.put("shelter", toShelterJsonObject(shelter));
            return Response.ok(jsonObject).build();

        } catch (Exception ex) {
            logger.info("Unable to connect to Mission Service. ", ex);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }
    }

    private Shelter getShelterName(String lat, String lon) {
        List<Shelter> shelterList = Shelter.getShelterList();
        for (Shelter shelter : shelterList) {
            if (lat.equals(shelter.getLat()) && lon.equals(shelter.getLon())) {
                return shelter;
            }
        }
        return new Shelter("Shelter Name not available", lat, lon);
    }

    private JsonObject toShelterJsonObject(Shelter shelter) {
        return JsonCodec.toShelterJsonObject(shelter);
    }

}
