package com.redhat.cajun.navy.finding.client;

import com.redhat.cajun.navy.finding.model.Victim;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/incidents")
@RegisterRestClient
public interface IncidentRestClient {

    @GET
    @Path("/byname/{name}")
    @Produces("application/json")
    List<Victim> getByName(@PathParam String name);

}
