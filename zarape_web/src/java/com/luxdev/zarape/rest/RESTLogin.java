
package com.luxdev.zarape.rest;

import com.google.gson.Gson;
import com.luxdev.zarape.controller.ControllerLogin;
import com.luxdev.zarape.model.Usuario;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("principal")
public class RESTLogin {
//    @GET
//    @Path("saludar")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response saludar()
//    {
//        String out = """
//                     {"result":"Hola desde la API-REST de El Zarape"}
//                     """;
//        return Response.ok(out).build();
//    }

    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    
    public Response Resolver(@QueryParam("user") String user,
            @QueryParam("password") String password) throws Exception {
            
            String out = null;
            
            ControllerLogin userLog = new ControllerLogin();
            Usuario us = new Usuario();
            
            us.setNombreUsuario(user);
            us.setContrasenia(password);
            
            userLog.ValidarDatos(us);
                   
        out = new Gson().toJson(us);
        return Response.ok(out).build();

    }
}

    
   

    
   

    



