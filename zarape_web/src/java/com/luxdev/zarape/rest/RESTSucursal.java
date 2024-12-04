package com.luxdev.zarape.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import com.luxdev.zarape.controller.ControllerSucursal;
import com.luxdev.zarape.model.Ciudad;
import com.luxdev.zarape.model.Sucursal;

@Path("sucursal")
public class RESTSucursal {

    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosSucursal") @DefaultValue("") String datosSucursal) {
        String out;
        ControllerSucursal cs = new ControllerSucursal();
        Sucursal s;
        Gson gson = new Gson();

        // Verificar los datos recibidos
        System.out.println("Datos de Sucursal: " + datosSucursal);

        try {
            // Convertir la cadena JSON en un objeto de tipo Sucursales
            s = gson.fromJson(datosSucursal, Sucursal.class);

            // Determinar si se realizará un INSERT o un UPDATE
            if (s.getIdSucursal() < 1) {
                cs.insert(s); // Insertar nueva sucursal
            } else {
                cs.update(s); // Actualizar sucursal existente
            }

            // Convertir el objeto Sucursales a JSON para devolverlo con los IDs generados
            out = gson.toJson(s);
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = """
                  {"error":"El JSON recibido no es correcto."}
                  """;
        } catch (Exception ex) {
            ex.printStackTrace();
            out = String.format("""
                  {"error":"Error interno del servidor, comunícate al área de sistemas de El Zarape.%s"}
                  """, ex);
        }

        return Response.ok(out).build();
    }

    /**
     * Este servicio elimina de manera lógica un registro de sucursal
     */
    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idSucursal") @DefaultValue("0") int idSucursal) {
        String out;
        ControllerSucursal cs = new ControllerSucursal();

        try {
            cs.delete(idSucursal);
            out = """
                  {"result":"Sucursal eliminada de forma correcta."}
                  """;
        } catch (Exception ex) {
            ex.printStackTrace();
            out = """
                  {"error":"Error interno del servidor, comunícate al área de sistemas de El Zarape."}
                  """;
        }

        return Response.ok(out).build();
    }

    /**
     * Este servicio obtiene todos los registros de sucursales
     */
    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        String out;
        ControllerSucursal cs = new ControllerSucursal();
        List<Sucursal> sucursales;

        try {
            // Llama a getAll con valores vacíos para los filtros (si es necesario modificar el método en ControllerSucursal)
            sucursales = cs.getAll("", ""); // Ajustar si getAll se ha modificado
            out = new Gson().toJson(sucursales);
        } catch (Exception ex) {
            ex.printStackTrace();
            out = """
                  {"error":"Error interno del servidor, comunícate al área de sistemas de El Zarape."}
                  """;
        }

        return Response.ok(out).build();
    }
    @GET
    @Path("getAllCiudades")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCiudades()
    {
        String out = null;
        ControllerSucursal cs = new ControllerSucursal();
        List<Ciudad> ciudades = null;
        try
        {
            ciudades = cs.getAllCiudades(null);
            out = new Gson().toJson(ciudades);
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