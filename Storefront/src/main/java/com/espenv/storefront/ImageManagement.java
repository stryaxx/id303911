/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espenv.storefront;

import com.espenv.storefront.Entities.Account;
import com.espenv.storefront.Entities.Images;
import com.espenv.storefront.Entities.Items;
import java.util.List;
import java.util.Random;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Vaden
 */
@Path("image")
@RequestScoped
public class ImageManagement {
Random rand;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ImageManagement
     */
    public ImageManagement() {
        rand = new Random();
    }

    /**
     * Retrieves representation of an instance of com.espenv.storefront.ImageManagement
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ImageManagement
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
    @GET
    @Path("/retrieve")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Images> retrievie(@QueryParam("id")String id)
    {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        List<Images> images = entityManager.createNamedQuery("Images.findByItemid").setParameter("itemid", id)
                .getResultList();
        return images;
    }
    
    @POST
    @Path("/upload")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response upload(@Valid Images image)
    {
        System.out.println("BLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA: " + image.getImage());
        int int_random = rand.nextInt(9999);
        image.setId(String.valueOf(int_random));
        
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = emFactory.createEntityManager();
        Images img = new Images();
        img.setId(image.getId().toString());
        img.setItemid(image.getItemid().toString());
        img.setImage(image.getImage().toString());
        System.out.println("Item ID: " + image.getItemid());
        System.out.println("ID: " + image.getId());
        entityManager.getTransaction().begin();
        
        entityManager.merge(img);
         entityManager.getTransaction().commit();
        
        entityManager.close();
        return Response.status(Response.Status.OK).entity("OK").build();
    }
    
    
}
