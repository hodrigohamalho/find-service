package com.redhat.cajun.navy.finding.rest;

import com.redhat.cajun.navy.finding.service.FindService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/find")
public class Resource {

    @Inject
    FindService findService;

    @GET
    @Path("/victim/byName/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVictims(@PathParam("name") String victimName) {

        return findService.getVictimByName(victimName);
    }

    @GET
    @Path("/shelter/{incidentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShelter(@PathParam("incidentId") String incidentId) {

        return findService.getVictimShelter(incidentId);
    }

}
