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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 *
 */
@Path("account")
@RequestScoped
public class AccountManagement {
    Random rand;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of register
     */
    public AccountManagement() {
        rand = new Random();
    }

    /**
     * Retrieves representation of an instance of com.espenv.storefront.register
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of register
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
    
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response Register(@Valid Account account)
    {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = emFactory.createEntityManager();
       
        entityManager.getTransaction().begin();
        
        int userID = rand.nextInt(9999);
        account.setId(String.valueOf(userID));
        
        entityManager.merge(account);
        
        entityManager.getTransaction().commit();
        entityManager.close();
        System.out.println(account.getUsername());
        return Response.status(201).entity("OK").build();
    }
    
    @GET
    @Path("/register2")
    @Produces(MediaType.TEXT_PLAIN)
    public Response Register2(@QueryParam("username")String username, 
            @QueryParam("password")String password, 
            @QueryParam("email")String email, 
            @QueryParam("zipcode")String zipcode, 
            @QueryParam("city")String city,
            @QueryParam("address")String address,
            @QueryParam("firstname")String firstname,
            @QueryParam("lastname")String lastname)
    {
         EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = emFactory.createEntityManager();
       
        entityManager.getTransaction().begin();
        
        int userID = rand.nextInt(9999);
        Account account = new Account();
        account.setUsername(username);
        account.setId(String.valueOf(userID));
        account.setPassword(password);
        account.setEmail(email);
        account.setZipcode(zipcode);
        account.setCity(city);
        account.setAddress(address);
        account.setFirstname(firstname);
        account.setLastname(lastname);
        
        entityManager.merge(account);
        
        entityManager.getTransaction().commit();
        entityManager.close();
        System.out.println(account.getUsername());
        return Response.status(Response.Status.OK).entity("OK").build();
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response Login(@Valid Account account)
    {
        int sessionID = rand.nextInt(9999);
        
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        List<Account> accounts = entityManager.createNamedQuery("Account.findByEmail").setParameter("email", account.getUsername())
                .getResultList();
        if (accounts.size() == 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        accounts.get(0).setSession(String.valueOf(sessionID));
        
        entityManager.merge(accounts.get(0));
        
        entityManager.getTransaction().commit();
       
        int foundAccount = entityManager.createNamedQuery("Account.authorize").setParameter("email", account.getUsername()).setParameter("password", account.getPassword())
                .getResultList().size();
        entityManager.close();
        
        if (foundAccount == 1) {
            //Success
            return Response.status(201).entity(sessionID).build();
        }
        else {
            //Failure
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
    }
    
    @POST
    @Path("/session")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response session(@Valid Account account)
    {
       
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = emFactory.createEntityManager();
       
        int foundAccount = entityManager.createNamedQuery("Account.findBySession").setParameter("session", account.getSession())
                .getResultList().size();
        entityManager.close();
        
        System.out.println("Session ID: " +account.getSession());
        if (foundAccount == 1) {
            //Success
            System.out.println("Success");
            return Response.status(201).entity("OK").build();
        }
        else {
            //Failure
            System.out.println("Failure");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
    }
    
    @GET
    @Path("/login2")
    @Produces(MediaType.TEXT_PLAIN)
    public Response Login2(@QueryParam("username")String username, @QueryParam("password")String password)
    {
         int sessionID = rand.nextInt(9999);
        
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        List<Account> accounts = entityManager.createNamedQuery("Account.findByEmail").setParameter("email", username)
                .getResultList();
        if (accounts.size() == 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        accounts.get(0).setSession(String.valueOf(sessionID));
        
        entityManager.merge(accounts.get(0));
        
        entityManager.getTransaction().commit();
       
        int foundAccount = entityManager.createNamedQuery("Account.authorize").setParameter("email", username).setParameter("password", password)
                .getResultList().size();
        entityManager.close();
        
        if (foundAccount == 1) {
            //Success
            return Response.status(Response.Status.OK).entity(sessionID).build();
        }
        else {
            //Failure
            return Response.status(Response.Status.NOT_FOUND).build();
        }     
    }
        
}
