package com.luxdev.zarape.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.luxdev.zarape.controller.ControllerBebida;
import com.luxdev.zarape.model.Bebida;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("bebida")
public class RESTBebida {

    /*     * Este servicio se encarga de insertar o actualizar un registro de
     * Alimento en la Base de Datos.
     * @param datosBebida Una cadena JSON que representa un alimento.
     * @return 
     */
 /*el metodo post se usa para insertar, eliminar, modificar, etc*/
    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosBebida") @DefaultValue("") String datosBebida) {
        String out = null;
        ControllerBebida cb = new ControllerBebida();
        Bebida b = null;
        Gson gson = new Gson();
        System.out.println("Datos de Bebida: " + datosBebida);
        try {
            // Convertimos la cadena JSON que llega por HTTP en un objeto 
            // de tipo Bebida:
            b = gson.fromJson(datosBebida, Bebida.class);

            // Si todo va bien hasta aqui, revisamos si se hara un
            // INSERT o un UPDATE:
            if (b.getIdBebida() < 1) {
                cb.insert(b);
            } else {
                cb.update(b);
            }

            // Convertimos a JSON nuestro objeto de tipo Bebida
            // para devolverlo con los IDs que se pudieron haber generado:
            out = gson.toJson(b);
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = """
                  {"error":"El JSON recibido no es correcto."}
                  """;
        } catch (Exception e) {
            e.printStackTrace();
            out = """
                  {"error":"Error interno del servidor, comunícate al area de sistemas de El Zarape."}
                  """;
        }
        return Response.ok(out).build();
    }

    /**
     * Este servicio elimina de manera logica un registro de Alimento:
     *
     * @return
     */
    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idProducto") @DefaultValue("0") int idProducto) {
        String out = null;
        ControllerBebida cb = new ControllerBebida();
        try {
            cb.delete(idProducto);
            out = """
                  {"result":"Registro eliminado de forma correcta."}
                  """;
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = """
                  {"error":"El JSON recibido no es correcto."}
                  """;
        } catch (Exception e) {
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
    public Response getAll() {
        String out = null;
        ControllerBebida cb = new ControllerBebida();
        List<Bebida> bebidas = null;
        try {
            bebidas = cb.getAll(null);
            out = new Gson().toJson(bebidas);
        } catch (Exception e) {
            e.printStackTrace();
            out = """
                  {"error" : "Error interno del Servidor, comunicate al area de Sistemas"}
                  """;
        }
        return Response.ok(out).build();
    }
}
