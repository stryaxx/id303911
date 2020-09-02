/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espenv.storefront;

import com.espenv.storefront.Entities.Account;
import com.espenv.storefront.Entities.Items;
import java.util.List;
import java.util.Random;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Vaden
 */
@Path("store")
@RequestScoped
public class ItemMangement {
Random rand;
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ItemMangement
     */
    public ItemMangement() {
        rand = new Random();
    }

    /**
     * Retrieves representation of an instance of com.espenv.storefront.ItemMangement
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ItemMangement
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
    @POST
    @Path("/publish")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response Publish(@Valid Items items)
    {
        
        int int_random = rand.nextInt(9999);
        items.setId(String.valueOf(int_random));
        
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = emFactory.createEntityManager();
       
       entityManager.getTransaction().begin();
        
        entityManager.merge(items);
        
        entityManager.getTransaction().commit();
        
        entityManager.close();
        return Response.status(201).entity("OK").build();
    }
    
    @GET
    @Path("/retrieve")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Items> Retrieve(@Valid Items items)
    {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = emFactory.createEntityManager();
       
       TypedQuery<Items> query = entityManager.createNamedQuery("Items.findAll", Items.class);

        List<Items> results = query.getResultList();

        entityManager.close();
        
        return results;
    }
}
