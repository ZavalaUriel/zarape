package com.luxdev.zarape.controller;

import java.util.List;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.luxdev.zarape.db.ConexionMySQL;
import com.luxdev.zarape.model.Ciudad;
import com.luxdev.zarape.model.Empleado;
import com.luxdev.zarape.model.Estado;
import com.luxdev.zarape.model.Persona;
import com.luxdev.zarape.model.Sucursal;
import com.luxdev.zarape.model.Usuario;

public class ControllerEmpleado {

    public int insertEmpleados(Empleado e) throws Exception {
        // Definir la consulta SQL o procedimiento almacenado para insertar un empleado
        String sql = "{CALL insertarEmpleado(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"; // Procedimiento con parámetros de salida

        // Abrir la conexión con la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        // Preparar el CallableStatement para llamar al procedimiento almacenado
        CallableStatement cstmt = conn.prepareCall(sql);

        // Establecer los parámetros del procedimiento almacenado
        cstmt.setString(1, e.getPersona().getNombrePersona());
        cstmt.setString(2, e.getPersona().getApellidos());
        cstmt.setString(3, e.getPersona().getTelefono());
        cstmt.setInt(4, e.getPersona().getCiudad().getIdCiudad());
        cstmt.setString(5, e.getUsuario().getNombreUsuario());
        cstmt.setString(6, e.getUsuario().getContrasenia());
        cstmt.setInt(7, e.getSucursal().getIdSucursal());
        cstmt.registerOutParameter(8, java.sql.Types.INTEGER);
        cstmt.registerOutParameter(9, java.sql.Types.INTEGER);
        cstmt.registerOutParameter(10, java.sql.Types.INTEGER);
        // System.out.println(sql);

        // Ejecutar el procedimiento almacenado
        cstmt.executeUpdate();

        // OBTENER VALORES DE OUT
        e.getPersona().setIdPersona(cstmt.getInt(8));
        e.getUsuario().setIdUsuario(cstmt.getInt(9));
        e.setIdEmpleado(cstmt.getInt(10));
        
        // Cerrar la conexión
        cstmt.close();
        connMySQL.close();
        
        return e.getIdEmpleado();
    }

    public void update(Empleado e) throws Exception {
        // Definir la consulta SQL o procedimiento almacenado para actualizar un empleado
        String sql = "{CALL actualizarEmpleado(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        // Abrir la conexión con la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        // Preparar el CallableStatement para llamar al procedimiento almacenado
        CallableStatement cstmt = conn.prepareCall(sql);

        // Establecer los parámetros del procedimiento almacenado
        cstmt.setInt(1, e.getIdEmpleado());
        cstmt.setInt(2, e.getPersona().getIdPersona());
        cstmt.setInt(3, e.getUsuario().getIdUsuario());
        cstmt.setString(4, e.getPersona().getNombrePersona());
        cstmt.setString(5, e.getPersona().getApellidos());
        cstmt.setString(6, e.getPersona().getTelefono());
        cstmt.setInt(7, e.getPersona().getCiudad().getIdCiudad());
        cstmt.setString(8, e.getUsuario().getNombreUsuario());
        cstmt.setString(9, e.getUsuario().getContrasenia());
        cstmt.setInt(10, e.getSucursal().getIdSucursal());

        // Ejecutar el procedimiento almacenado
        cstmt.executeUpdate();

        // Cerrar la conexión
        cstmt.close();
        connMySQL.close();
    }

    public void delete(int idEmpleado) throws Exception {
        // Definir la consulta SQL para desactivar un empleado (eliminación lógica)
        String sql = "UPDATE empleado SET activo = 0 WHERE idEmpleado = ?";

        // Abrir la conexión con la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        // Preparar el PreparedStatement para consultar los parámetros 
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Establecer el parámetro
        pstmt.setInt(1, idEmpleado);

        // Ejecutar la actualización
        pstmt.executeUpdate();

        // Cerrar la conexión
        pstmt.close();
        connMySQL.close();
    }

    public List<Empleado> getAll() throws Exception {
        List<Empleado> empleados = new ArrayList<>();

        // Definir la consulta SQL para obtener todos los empleados
        String sql = "SELECT * FROM v_empleado";

        // Abrir la conexión con la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        // Preparar la sentencia SQL
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Ejecutar la consulta
        ResultSet rs = pstmt.executeQuery();

        Empleado emp = null;

        // Recorrer los resultados y llenar la lista de empleados
        while (rs.next()) {
            emp = fill(rs);  // Método que rellena el objeto Empleado
            empleados.add(emp);
        }

        // Cerrar la conexión
        rs.close();
        pstmt.close();
        connMySQL.close();

        return empleados;
    }

    private Empleado fill(ResultSet rs) throws Exception {
        Empleado e = new Empleado();
        Persona persona = new Persona();
        Sucursal sucursal = new Sucursal();
        Ciudad ciudad = new Ciudad();
        Usuario usuario = new Usuario();

        // Asignar los objetos relacionados a Empleados
        

        // Asignar los valores de los atributos de Empleados desde el ResultSet
        //e.setIdEmpleado(rs.getInt("idEmpleado"));
        //e.setActivo(rs.getInt("activo"));

        // Asignar los valores de los atributos de Persona desde el ResultSet
        e.setIdEmpleado(rs.getInt("idEmpleado"));
        persona.setIdPersona(rs.getInt("idPersona"));
        persona.setNombrePersona(rs.getString("nombrePersona"));
        persona.setApellidos(rs.getString("apellidosPersona"));
        persona.setTelefono(rs.getString("telefonoPersona"));

        // Asignar los valores de los atributos de Ciudad desde el ResultSet
        ciudad.setNombreCiudad(rs.getString("ciudad"));
        persona.setCiudad(ciudad);
        e.setPersona(persona);
        
        usuario.setIdUsuario(rs.getInt("idUsuario"));
        usuario.setNombreUsuario(rs.getString("usuario"));
        usuario.setContrasenia(rs.getString("contrasenia"));
        e.setUsuario(usuario);

        // Asignar los valores de los atributos de Sucursal desde el ResultSet
        sucursal.setIdSucursal(rs.getInt("idSucursal"));
        sucursal.setNombreSucursal(rs.getString("nombreSucursal"));
        e.setSucursal(sucursal);
        
        e.setActivoEmpleado(rs.getInt("activo"));

        return e;
    }

}