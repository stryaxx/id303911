/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espenv.storefront;

import com.espenv.storefront.Entities.Account;
import com.espenv.storefront.Entities.Items;
import com.espenv.storefront.Entities.Purchases;
import java.util.ArrayList;
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
import javax.ws.rs.QueryParam;
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
        
        List<Account> account = entityManager.createNamedQuery("Account.findBySession").setParameter("session", items.getUserid())
                .getResultList();
        if (account.size() == 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
       
        items.setUserid(account.get(0).getId());
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
    
    @POST
    @Path("/buy")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response Buy(@Valid Items items)
    {
       String userSessionId = items.getUserid();
       
       
        
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = emFactory.createEntityManager();
       
        TypedQuery<Items> query = entityManager.createNamedQuery("Items.findById", Items.class).setParameter("id", items.getId());
        List<Items> results = query.getResultList();
        
        entityManager.getTransaction().begin();
        List<Account> account = entityManager.createNamedQuery("Account.findBySession").setParameter("session", userSessionId)
                .getResultList();
        if (account.size() == 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        System.out.println("Item ID: "+items.getId());
        results.get(0).setSold("1");
        entityManager.merge(results.get(0));
        
        
       
        Purchases purchases = new Purchases();
        purchases.setItemid(results.get(0).getId());
        purchases.setUserid(account.get(0).getId());
        
        int int_random = rand.nextInt(9999);
        purchases.setId(String.valueOf(int_random));
        
        entityManager.merge(purchases);
        entityManager.getTransaction().commit();
        
        
        
        entityManager.close();
        return Response.status(201).entity("OK").build();
    }
    
    
    
    @GET
    @Path("/sold")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Items> SoldHistory(@QueryParam("session")String session)
    {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = emFactory.createEntityManager();
        
        System.out.println("User ID" +session);
        List<Account> account = entityManager.createNamedQuery("Account.findBySession").setParameter("session", session)
                .getResultList();
        TypedQuery<Items> query = entityManager.createNamedQuery("Items.findByUserid", Items.class).setParameter("userid", account.get(0).getId());

        List<Items> results = query.getResultList();
        System.out.println("Item count: " +results.size() );
        entityManager.close();
        
        return results;
    }
    
    @GET
    @Path("/bought")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Items> BoughtHistory(@QueryParam("session")String session)
    {
        List<Items>returnItems = new ArrayList<Items>();
        
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = emFactory.createEntityManager();
        
        List<Account> account = entityManager.createNamedQuery("Account.findBySession").setParameter("session", session)
                .getResultList();
        List<Purchases> purchases = entityManager.createNamedQuery("Purchases.findByUserid").setParameter("userid", account.get(0).getId())
                .getResultList();
        for (Purchases purchase : purchases) {
            List<Items> foundItems = entityManager.createNamedQuery("Items.findById").setParameter("id", purchase.getItemid())
                .getResultList(); 
            returnItems.add(foundItems.get(0));
        }
        //TypedQuery<Items> query = entityManager.createNamedQuery("Items.findByUserid", Items.class).setParameter("userid", account.get(0).getId());

        //List<Items> results = query.getResultList();

        entityManager.close();
        
        return returnItems;
    }
   
}
