package com.redhat.cajun.navy.finding.client;

import com.redhat.cajun.navy.finding.model.Victim;
import io.quarkus.test.Mock;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import java.util.LinkedList;
import java.util.List;

@Mock
@ApplicationScoped
@RestClient
public class MockIncidentRestClient implements IncidentRestClient {

    @Override
    public List<Victim> getByName(String name) {
        List<Victim> victimList = new LinkedList<>();
        if (name.equals("test-victim-1"))
            victimList.add(getTestVictim());
        return victimList;
    }

    private Victim getTestVictim() {
        Victim victim = new Victim();
        victim.setVictimName("test-victim-1");
        victim.setId("test-incident-id");
        victim.setStatus("REPORTED");
        victim.setTimestamp(null);
        victim.setNumberOfPeople(123);
        victim.setMedicalNeeded(true);
        victim.setLat("00.00");
        victim.setLon("00.00");
        return victim;
    }
}
