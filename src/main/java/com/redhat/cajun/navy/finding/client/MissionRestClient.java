package com.redhat.cajun.navy.finding.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/api/missions")
@RegisterRestClient
public interface MissionRestClient {

    @GET
    @Path("/{incidentId}")
    @Produces("application/json")
    String getMissionByIncidentId(@PathParam String incidentId);

    @GET
    @Path("/keys")
    List<String> getMissions();

}
