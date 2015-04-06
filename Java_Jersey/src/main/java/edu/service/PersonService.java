package edu.service;

import edu.model.Bookmark;
import edu.model.Person;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MCQ1 on 4/3/2015.
 */
@Path("/account")
@Singleton
public class PersonService {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    List<Person> allPeople = new ArrayList<Person>();

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Person> getAllPeople() {
        return  allPeople;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Person getAPerson(@PathParam("id")int id) {
        try {
            return allPeople.get(id);
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException("User " + id + " not found!");
       }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Person createPerson(Person newPerson) {
        newPerson.setId(allPeople.size());
        allPeople.add(newPerson);
        return newPerson;
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Person updatePerson(@PathParam("id")int id, Person updatedPerson) {
        try {
            updatedPerson.setId(id);
            allPeople.set(id, updatedPerson);
            return allPeople.get(id);
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException("User " + id + " not found!");
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN) //Returns a regular 200 if successful or 404 if invalid ID passed.
    public Response deletePerson(@PathParam("id")int id) {
        try {
            allPeople.remove(id);
            return Response.ok().entity(id).status(Response.Status.OK).build();
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException("User " + id + " not found!");
        }
    }

    //bookmarks management:
    @GET
    @Path("{id}/bookmarks")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Bookmark> getBookmarksForUser(@PathParam("id")int userId) {
        Person p = getAPerson(userId);
        return p.getBookmarks();
    }

    @POST
    @Path("{id}/bookmarks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Bookmark addBookmark(@PathParam("id")int userId, Bookmark newBookmark) {
        Person p = getAPerson(userId);
        if (p.getBookmarks() == null) {
            p.setBookmarks(new ArrayList<Bookmark>());
        }
        p.getBookmarks().add(newBookmark);
        return newBookmark;
    }

    @PUT
    @Path("{userId}/bookmark/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Bookmark updateBookmark(@PathParam("userId")int userId, @PathParam("id")long bmId, Bookmark updatedBookmark) {
        Person p = getAPerson(userId);
        Bookmark b = getBookmark(p.getBookmarks(), bmId);
        b.setDescription(updatedBookmark.getDescription());
        b.setUrl(updatedBookmark.getUrl());
        return b;
    }

    @DELETE
    @Path("{userId}/bookmark/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBookmark(@PathParam("userId")int userId, @PathParam("id")long bmId) {
        Person p = getAPerson(userId);
        for (Bookmark b: p.getBookmarks()) {
            if (bmId == b.getId()) {
                p.getBookmarks().remove(b);
                return Response.ok().entity("bookmark has been removed!").status(Response.Status.OK).build();
            }
        }
        throw new NotFoundException("Bookmark not found for user  BM id " + bmId);
    }


    private Bookmark getBookmark(List<Bookmark> allBM, long bmId) {
        for (Bookmark b: allBM) {
            if (bmId == b.getId()) {
                return b;
            }
        } //If didn't find bookmark, throw 404
        throw new NotFoundException("Bookmark not found for user  BM id " + bmId);
    }
}