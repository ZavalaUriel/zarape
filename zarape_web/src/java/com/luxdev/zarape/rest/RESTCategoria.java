package com.luxdev.zarape.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.luxdev.zarape.controller.ControllerCategoria;
import com.luxdev.zarape.model.Categoria;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("categoria")
public class RESTCategoria 
{

    /**
     *
     * @param datosCategoria
     * @return
     */
    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosCategoria") @DefaultValue("") String datosCategoria)
    {
        String out = null;
        ControllerCategoria cc = new ControllerCategoria();
        Categoria c = null;
        Gson gson = new Gson();
        System.out.println("Datos de Alimento: " + datosCategoria);
        try
        {
            // Convertimos la cadena JSON que llega por HTTP en un objeto 
            // de tipo Alimento:
            c = gson.fromJson(datosCategoria, Categoria.class);
           
            // Si todo va bien hasta aqui, revisamos si se hara un
            // INSERT o un UPDATE:
            if (c.getIdCategoria()< 1)
                cc.insert(c);
            else
                cc.update(c);
            
            // Convertimos a JSON nuestro objeto de tipo Alimento
            // para devolverlo con los IDs que se pudieron haber generado:
            out = gson.toJson(c);
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
    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idCategoria") @DefaultValue("0") int idCategoria)throws Exception{
        String out = null;
        ControllerCategoria cc = new ControllerCategoria();        
        try
        {
            cc.delete(idCategoria);
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
    @Path("getAllByTipo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllByTipo(@QueryParam("tipo") @DefaultValue("") String tipo)
    {
        String out = null;
        List<Categoria> categorias = null;
        ControllerCategoria cc = new ControllerCategoria();
        try {
            if (tipo==null || tipo.trim().isEmpty())
                categorias = cc.getAllByTipo(null);
            else
                categorias = cc.getAllByTipo(tipo);
            out =  new Gson().toJson(categorias);
        } catch (Exception e) {
            e.printStackTrace();
            out = """
                  {"error":"Error interno del servidor, comunicate al area de sistemas"}
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
        ControllerCategoria cc = new ControllerCategoria();
        List<Categoria> categorias = null;
        try
        {
            categorias = cc.getAll(null);
            out = new Gson().toJson(categorias);
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