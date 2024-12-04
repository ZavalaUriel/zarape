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
import com.luxdev.zarape.controller.ControllerEmpleado;
import com.luxdev.zarape.model.Empleado;
import com.luxdev.zarape.model.Usuario;

@Path("empleado")
public class RESTEmpleado {

    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosEmpleado") @DefaultValue("") String datosEmpleado) {
        String out;
        ControllerEmpleado ce = new ControllerEmpleado();
        Empleado e;
        
        
        Gson gson = new Gson();
        
        // Verificar los datos recibidos
        System.out.println("Datos de Empleado: " + datosEmpleado);

        try {
            // Convertir la cadena JSON en un objeto de tipo Empleados
            e = gson.fromJson(datosEmpleado, Empleado.class);

            // Determinar si se realizará un INSERT o un UPDATE
            if (e.getIdEmpleado() < 1) {
                ce.insertEmpleados(e); // Cambiado a insertEmpleados
            } else {
                ce.update(e);
            }

            // Convertir el objeto Empleados a JSON para devolverlo con los IDs generados
            out = gson.toJson(e);
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
    public Response delete(@FormParam("idEmpleado") @DefaultValue("0") int idEmpleado) {
        String out;
        ControllerEmpleado ce = new ControllerEmpleado();
        
        try {
            ce.delete(idEmpleado);
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
        ControllerEmpleado ce = new ControllerEmpleado();
        List<Empleado> empleados;

        try {
            empleados = ce.getAll();
            out = new Gson().toJson(empleados);
        } catch (Exception ex) {
            ex.printStackTrace();
            out = """
                  {"error":"Error interno del servidor, comunícate al área de sistemas de El Zarape."}
                  """;
        }
        
        return Response.ok(out).build();
    }
}
