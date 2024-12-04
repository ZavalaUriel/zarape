package com.luxdev.zarape.controller;

import java.util.List;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import com.luxdev.zarape.db.ConexionMySQL;
import com.luxdev.zarape.model.Ciudad;
import com.luxdev.zarape.model.Cliente;
import com.luxdev.zarape.model.Estado;
import com.luxdev.zarape.model.Persona;

public class ControllerCliente {

    public void insertCliente(Cliente c) throws Exception {
        // Definir la consulta SQL o procedimiento almacenado para insertar un empleado
        String sql = "{CALL insertarCliente(?,?,?,?,?,?)}"; // Procedimiento almacenado de mysql

        // Abrir la conexión con la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        // Preparar el CallableStatement para llamar al procedimiento almacenado
        CallableStatement cstmt = conn.prepareCall(sql);

        // Establecer los parámetros del procedimiento almacenado

        cstmt.setString(1, c.getPersona().getNombrePersona());
        cstmt.setString(2, c.getPersona().getApellidos());
        cstmt.setString(3, c.getPersona().getTelefono());
        cstmt.setInt   (4, c.getPersona().getCiudad().getIdCiudad());
        /*
        System.out.println(sql);
        // Ejecutar el procedimiento almacenado
        cstmt.executeUpdate();s
        */
        cstmt.executeUpdate();
         // Recuperamos el ID de Producto y de cliente generados:
        c.getPersona().setIdPersona(cstmt.getInt(5));
        c.setIdCliente(cstmt.getInt(6));

        // Cerrar la conexión
        cstmt.close();
        connMySQL.close();
    }

    public void update(Cliente c) throws Exception {
        // Definir la consulta SQL o procedimiento almacenado para actualizar un empleado
        String sql = "{CALL actualizarCliente(?, ?, ?, ?,?)}"; // Procedimiento almacenado hipotético

        // Abrir la conexión con la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        // Preparar el CallableStatement para llamar al procedimiento almacenado
        CallableStatement cstmt = conn.prepareCall(sql);

        
        
        // Establecer los parámetros del procedimiento almacenado 
        cstmt.setInt   (1, c.getPersona().getIdPersona());
        cstmt.setString(2, c.getPersona().getNombrePersona());
        cstmt.setString(3, c.getPersona().getApellidos());
        cstmt.setString(4, c.getPersona().getTelefono());
        cstmt.setInt   (5, c.getPersona().getCiudad().getIdCiudad());
        
        
        // Ejecutar el procedimiento almacenado
        cstmt.executeUpdate();

        // Cerrar la conexión
        cstmt.close();
        connMySQL.close();
    }

    public void delete(int idCliente) throws Exception {
        // Definir la consulta SQL para desactivar un empleado (eliminación lógica)
        String sql = "UPDATE cliente SET activo = 0 WHERE idCliente = ?";

        // Abrir la conexión con la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        // Preparar el PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Establecer el parámetro
        pstmt.setInt(1, idCliente);

        // Ejecutar la actualización
        pstmt.executeUpdate();

        // Cerrar la conexión
        pstmt.close();
        connMySQL.close();
    }

    public List<Cliente> getAll() throws Exception {
        List<Cliente> clientes = new ArrayList<>();

        // Definir la consulta SQL para obtener todos los empleados
        String sql = "SELECT * FROM v_cliente";  // Cambia esto por tu vista o tabla de empleados

        // Abrir la conexión con la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        // Preparar la sentencia SQL
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Ejecutar la consulta
        ResultSet rs = pstmt.executeQuery();

        Cliente cli = null;

        // Recorrer los resultados y llenar la lista de empleados
        while (rs.next()) {
            cli = fill(rs);  // Método que rellena el objeto Empleado
            clientes.add(cli);
        }

        // Cerrar la conexión
        rs.close();
        pstmt.close();
        connMySQL.close();

        return clientes;
    }

    private Cliente fill(ResultSet rs) throws Exception {
        Ciudad ci = new Ciudad();
        Cliente c = new Cliente();
        Persona persona = new Persona();
        
        // Asignar los objetos relacionados a Empleados
        c.setPersona(persona);

        // Asignar los valores de los atributos de Empleados desde el ResultSet
        c.setIdCliente(rs.getInt("idCliente"));
        c.setActivoCliente(rs.getInt("activo"));

        // Asignar los valores de los atributos de Persona desde el ResultSet
        persona.setIdPersona(rs.getInt("idPersona"));
        persona.setNombrePersona(rs.getString("nombrePersona"));
        persona.setApellidos(rs.getString("apellidos"));
        persona.setTelefono(rs.getString("telefono"));
        ci.setIdCiudad(rs.getInt("idCiudad"));
        ci.setNombreCiudad(rs.getString("nombreCiudad"));
        persona.setCiudad(ci);
        
        return c;
    }


    public List<Ciudad> getAllCiudades(String filtro) throws Exception
    {
        List<Ciudad> ciudades = new ArrayList<>();
        // Se define la consulta SQL:
        String sql = "SELECT * FROM vista_ciudades";
        
        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        
        Ciudad ciud = null;
        
        // Recorremos cada registro devuelto por la consulta:
        while(rs.next())
        {
            ciud = fillCiudad(rs);
            ciudades.add(ciud);
        }
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return ciudades;
    }
    
    private Ciudad fillCiudad(ResultSet rs) throws Exception{
        Estado e = new Estado();
        Ciudad c = new Ciudad();
        Persona p = new Persona();
        c.setIdCiudad(rs.getInt("idCiudad"));
        c.setNombreCiudad(rs.getString("nombre"));
        e.setIdEstado(rs.getInt("idEstado"));
        c.setEstado(e);
        p.setCiudad(c);
        c.setEstado(e);
        return c;
    }
}