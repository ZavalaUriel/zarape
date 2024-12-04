package com.luxdev.zarape.controller;

import java.util.List;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import com.luxdev.zarape.db.*;
import com.luxdev.zarape.model.Sucursal;
import com.luxdev.zarape.model.Ciudad;
import com.luxdev.zarape.model.Estado;

public class ControllerSucursal {

    public int insert(Sucursal s) throws Exception {
    // Definimos la consulta SQL para el stored procedure
    String sql = "{CALL insertarSucursalConEstadoCiudad (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    // Abrimos la conexión con la BD
    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();

    // Preparamos el CallableStatement para llamar al stored procedure
    CallableStatement cstmt = conn.prepareCall(sql);

    // Colocamos los valores de los parámetros de entrada
    cstmt.setString(1, s.getNombreSucursal());
    cstmt.setString(2, s.getLatitud());
    cstmt.setString(3, s.getLongitud());
    cstmt.setString(4, s.getFotoSucursal());
    cstmt.setString(5, s.getUrl());
    cstmt.setString(6, s.getHorarios());
    cstmt.setString(7, s.getCalle());
    cstmt.setString(8, s.getNumCalle());
    cstmt.setString(9, s.getColonia());
    cstmt.setInt(10, s.getCiudad().getIdCiudad());  // Nombre de la ciudad
   
    // Registrar el parámetro de salida
    cstmt.registerOutParameter(11, java.sql.Types.INTEGER);

    // Ejecutar el stored procedure
    cstmt.executeUpdate();

    // Obtener el valor del parámetro de salida
    s.setIdSucursal(cstmt.getInt(11));

    // Cerramos la conexión
    cstmt.close();
    connMySQL.close();

    // Retornamos el ID de la sucursal generada
    return s.getIdSucursal();
}


    public void update(Sucursal s) throws Exception {
        // Definimos la consulta SQL para el stored procedure
        String sql = "{CALL actualizarSucursal (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        // Abrimos la conexión con la BD
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        // Preparamos el CallableStatement
        CallableStatement cstmt = conn.prepareCall(sql);

        // Colocamos los valores de los parámetros
        cstmt.setInt(1, s.getIdSucursal());
        cstmt.setString(2, s.getNombreSucursal());
        cstmt.setString(3, s.getLatitud());
        cstmt.setString(4, s.getLongitud());
        cstmt.setString(5, s.getFotoSucursal());
        cstmt.setString(6, s.getUrl());
        cstmt.setString(7, s.getHorarios());
        cstmt.setString(8, s.getCalle());
        cstmt.setString(9, s.getNumCalle());
        cstmt.setString(10, s.getColonia());
        cstmt.setInt(11, s.getCiudad().getIdCiudad());

        // Ejecutamos el stored procedure
        cstmt.executeUpdate();

        // Cerramos la conexión
        cstmt.close();
        connMySQL.close();
    }

    public void delete(int id) throws Exception {
        // Definimos la consulta SQL
        String sql = "UPDATE sucursal SET activo=0 WHERE idSucursal=?";

        // Abrimos la conexión con la BD
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        // Preparamos el PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Colocamos el ID de la sucursal
        pstmt.setInt(1, id);

        // Ejecutamos la actualización
        pstmt.executeUpdate();

        // Cerramos la conexión
        pstmt.close();
        connMySQL.close();
    }

    public List<Sucursal> getAll(String filtroNombre, String filtroUbicacion) throws Exception {
        List<Sucursal> sucursales = new ArrayList<>();

        // Definimos la consulta SQL
        String sql = "{CALL buscarSucursal(?, ?)}";

        // Abrimos la conexión con la BD
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        // Preparamos el CallableStatement para llamar al stored procedure
        CallableStatement cstmt = conn.prepareCall(sql);

        // Colocamos los filtros de búsqueda
        cstmt.setString(1, filtroNombre);
        cstmt.setString(2, filtroUbicacion);

        // Ejecutamos la consulta y obtenemos los resultados
        ResultSet rs = cstmt.executeQuery();

        // Llenamos la lista de sucursales
        while (rs.next()) {
            sucursales.add(fill(rs));
        }

        // Cerramos la conexión
        rs.close();
        cstmt.close();
        connMySQL.close();

        return sucursales;
    }
    // prueba ciudades getAll
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

    private Sucursal fill(ResultSet rs) throws Exception {
    Sucursal s = new Sucursal();
    Ciudad c = new Ciudad();
    Estado e = new Estado();

    // Asignamos los valores de cada columna del ResultSet al objeto Sucursal
    s.setIdSucursal(rs.getInt("idSucursal"));
    s.setNombreSucursal(rs.getString("nombre"));
    s.setLatitud(rs.getString("latitud"));
    s.setLongitud(rs.getString("longitud"));
    s.setFotoSucursal(rs.getString("foto"));
    s.setUrl(rs.getString("urlWeb"));
    s.setHorarios(rs.getString("horarios"));
    s.setCalle(rs.getString("calle"));
    s.setNumCalle(rs.getString("numCalle"));
    s.setColonia(rs.getString("colonia"));
    s.setActivoSucursal(rs.getInt("activo"));

    // Asignamos los datos de la ciudad
    c.setIdCiudad(rs.getInt("idCiudad"));
    c.setNombreCiudad(rs.getString("ciudadNombre"));

    // Asignamos los datos del estado
    e.setIdEstado(rs.getInt("idEstado"));
    e.setNombreEstado(rs.getString("estadoNombre"));

    // Vinculamos ciudad y estado a la sucursal
    c.setEstado(e);
    s.setCiudad(c);

    return s;
}
   private Ciudad fillCiudad(ResultSet rs) throws Exception{
        Estado e = new Estado();
        Ciudad c = new Ciudad();
        Sucursal s = new Sucursal();
        c.setIdCiudad(rs.getInt("idCiudad"));
        c.setNombreCiudad(rs.getString("nombre"));
        e.setIdEstado(rs.getInt("idEstado"));
        c.setEstado(e);
        s.setCiudad(c);
        c.setEstado(e);
        return c;
    }
}