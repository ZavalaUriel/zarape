/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luxdev.zarape.rest;

/**
 *
 * @author Uriel Zavala
 */
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
import com.luxdev.zarape.controller.ControllerCliente;
import com.luxdev.zarape.model.Ciudad;
import com.luxdev.zarape.model.Cliente;


@Path("cliente")
public class RESTClientes {

    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)

    public Response save(@FormParam("datosCliente") @DefaultValue("") String datosCliente) {
        String out;
        ControllerCliente cc = new ControllerCliente();
        Cliente c;
        Gson gson = new Gson();
        
        // Verificar los datos recibidos
        System.out.println("Datos de Cliente: " + datosCliente);

        try {
            
            c = gson.fromJson(datosCliente, Cliente.class);

            // Determinar si se realizará un INSERT o un UPDATE
            if (c.getIdCliente()< 1) {
                cc.insertCliente(c); // Cambiado a insertEmpleados
            } else {
                cc.update(c);
            }

            // Convertir el objeto Empleados a JSON para devolverlo con los IDs generados
            out = gson.toJson(c);
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = """
                  {"error":"El JSON recibido no es correcto."}
                  """;
        } catch (Exception ex) {
            ex.printStackTrace();
            out = """
                  {"error":"Error interno del servidor, comunícate al área de sistemas de El Zarape.%s"}
                  """;
            out = String.format(out, ex);
        }
        
        return Response.ok(out).build();
    }

    /**
     * Este servicio elimina de manera lógica un registro de empleado
     */
    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idCliente") @DefaultValue("0") int idCliente) {
        String out;
        ControllerCliente cc = new ControllerCliente();
        
        try {
            cc.delete(idCliente);
            out = """
                  {"result":"Registro eliminado de forma correcta."}
                  """;
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = """
                  {"error":"El JSON recibido no es correcto."}
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
     * Este servicio obtiene todos los registros de empleados
     */
    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        String out;
        ControllerCliente cc = new ControllerCliente();
        List<Cliente> cliente;

        try {
            cliente = cc.getAll();
            out = new Gson().toJson(cliente);
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
        ControllerCliente cs = new ControllerCliente();
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