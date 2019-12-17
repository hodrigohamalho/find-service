package com.redhat.cajun.navy.finding.client;

import io.quarkus.test.Mock;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import java.util.LinkedList;
import java.util.List;

@Mock
@ApplicationScoped
@RestClient
public class MockMissionRestClient implements MissionRestClient {

    @Override
    public List<String> getMissions() {
        List<String> missionList = new LinkedList<>();
        missionList.add("test-incident-id-1-rid");
        missionList.add("test-incident-id-2-rid");
        return missionList;
    }

    @Override
    public String getMissionById(String missionId) {
        if (missionId.equals("test-incident-id-1-rid"))
            return "{\n" + "\t\"destinationLat\": \"34.1706\",\n" + "\t\"destinationLong\": \"-77.949\"\n" + "}";
        else if (missionId.equals("test-incident-id-2-rid"))
            return "{\n" + "\t\"destinationLat\": \"00\",\n" + "\t\"destinationLong\": \"00\"\n" + "}";
        return null;
    }
}
