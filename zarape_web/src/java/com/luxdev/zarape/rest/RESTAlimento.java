package com.luxdev.zarape.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.luxdev.zarape.controller.ControllerAlimento;
import com.luxdev.zarape.model.Alimento;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("alimento")
public class RESTAlimento
{
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
    
    /**
     * Este servicio se encarga de insertar o actualizar un registro de
     * Alimento en la Base de Datos.
     * @param datosAlimento Una cadena JSON que representa un alimento.
     * @return 
     */
    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosAlimento") @DefaultValue("") String datosAlimento)
    {
        String out = null;
        ControllerAlimento ca = new ControllerAlimento();
        Alimento a = null;
        Gson gson = new Gson();
        System.out.println("Datos de Alimento: " + datosAlimento);
        try
        {
            // Convertimos la cadena JSON que llega por HTTP en un objeto 
            // de tipo Alimento:
            a = gson.fromJson(datosAlimento, Alimento.class);
           
            // Si todo va bien hasta aqui, revisamos si se hara un
            // INSERT o un UPDATE:
            if (a.getIdAlimento()< 1)
                ca.insert(a);
            else
                ca.update(a);
            
            // Convertimos a JSON nuestro objeto de tipo Alimento
            // para devolverlo con los IDs que se pudieron haber generado:
            out = gson.toJson(a);
        }
        catch(JsonParseException jpe)
        {
            jpe.printStackTrace();
            out = """
                  {"error":"El JSON recibido no es correcto."}
                  """;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            out = """
                  {"error":"Error interno del servidor, comunícate al area de sistemas de El Zarape."}
                  """;
        }
        return Response.ok(out).build();
    }
    
    /**
     * Este servicio elimina de manera logica un registro de Alimento:
     * @return 
     */
    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idProducto") @DefaultValue("0") int idProducto)
    {
        String out = null;
        ControllerAlimento ca = new ControllerAlimento();        
        try
        {
            ca.delete(idProducto);
            out = """
                  {"result":"Registro eliminado de forma correcta."}
                  """;
        }
        catch(JsonParseException jpe)
        {
            jpe.printStackTrace();
            out = """
                  {"error":"El JSON recibido no es correcto."}
                  """;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            out = """
                  {"error":"Error interno del servidor, comunícate al area de sistemas de El Zarape."}
                  """;
        }
        return Response.ok(out).build();
    }
    
    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll()
    {
        String out = null;
        ControllerAlimento ca = new ControllerAlimento();
        List<Alimento> alimentos = null;
        try
        {
            alimentos = ca.getAll(null);
            out = new Gson().toJson(alimentos);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
            out = """
                  {"error" : "Error interno del Servidor, comunicate al area de Sistemas"}
                  """;
        }
        return Response.ok(out).build();
    }
}