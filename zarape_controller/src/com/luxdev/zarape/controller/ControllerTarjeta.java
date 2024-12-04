/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luxdev.zarape.controller;

/**
 *
 * @author Uriel Zavala
 */
/*
import java.util.List;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import com.luxdev.zarape.db.ConexionMySQL;
import com.luxdev.zarape.model.Tarjeta;

public class ControllerTarjeta
{
    public int insert(Tarjeta a) throws Exception
    {
        // Se define la consulta SQL:
        String sql = "{CALL insertarAlimento(?, ?, ?, ?, ?, ?, ?)}";
        
        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        // Generamos un CallableStatement para invocar al Stored Procedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        
        // Colocamos los valores de los parametros de entrada que requiere
        // el Stored Procedure:
        cstmt.setString(1, a.getTitular());
        cstmt.setString(2, a.getNumero());
        cstmt.setString(3, a.getYy());
        cstmt.setString(4, a.getMm());
        cstmt.setString(5, a.getCvv());
        cstmt.setString(6, a.getCalle());
        cstmt.setString(7, a.getNumCalle());
        cstmt.setString(8, a.getColonia());
        cstmt.setString(9, a.getCp());
        // Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
        //Cerramos los objetos de conexion:
        cstmt.close();
        connMySQL.close();
        
        // Devolvemos el ID de Alimento que se genero:
        return a.getIdTarjeta();
    }
    
    public void update(Tarjeta a) throws Exception
    {
        // Se define la consulta SQL:
        String sql = "{CALL actualizarAlimento(?, ?, ?, ?, ?, ?)}";
        
        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        // Generamos un CallableStatement para invocar al Stored Procedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        
        // Colocamos los valores de los parametros de entrada que requiere
        // el Stored Procedure:
        cstmt.setString(1, a.getTitular());
        cstmt.setString(2, a.getNumero());
        cstmt.setString(3, a.getYy());
        cstmt.setString(4, a.getMm());
        cstmt.setString(5, a.getCvv());
        cstmt.setString(6, a.getCalle());
        cstmt.setString(7, a.getNumCalle());
        cstmt.setString(8, a.getColonia());
        cstmt.setString(9, a.getCp());
        // Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
                
        //Cerramos los objetos de conexion:
        cstmt.close();
        connMySQL.close();
    }
    
    public void delete(int id) throws Exception
    {
        // Se define la consulta SQL:
        String sql = "UPDATE producto SET activo=0 WHERE idProducto=?";
        
        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        // Llenamos los datos del PreparedStatement:
        pstmt.setInt(1, id);
        
        // Ejecutamos la consulta:
        pstmt.executeUpdate();
        
        // Cerramos los objetos de conexion:
        pstmt.close();
        connMySQL.close();
    }
    
    public List<Tarjeta> getAll(String filtro) throws Exception
    {
        List<Tarjeta> tarjeta = new ArrayList<>();
        // Se define la consulta SQL:
        String sql = "SELECT * FROM alimento";
        
        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        
        Tarjeta alim = null;
        
        // Recorremos cada registro devuelto por la consulta:
        while(rs.next())
        {
            alim = fill(rs);
            tarjeta.add(alim);
        }
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return tarjeta;
    }
    
    private Tarjeta fill(ResultSet rs) throws Exception
    {
        Tarjeta c = new Tarjeta();
        
        // Establecemos los valores de cada atributo de
        // los objetos relacionados, extraidos de cada
        // campo del ResultSet:
        
        c.setIdTarjeta(rs.getInt("idTarjeta"));
        
        c.setTitular(rs.getInt("productoActivo"));
        c.setNumero(rs.getString("descripcionProducto"));
        c.setYy(rs.getString("foto"));
        c.setMm(rs.get("idProducto"));
        c.setNombreProducto(rs.getString("nombre"));
        c.setPrecioProducto(rs.getDouble("precio"));
        
        c.setActivoCategoria(rs.getInt("categoriaActiva"));
        c.setDescripcionCategoria(rs.getString("descripcionCategoria"));
        c.setIdCategoria(rs.getInt("idCategoria"));
        c.setTipoCategoria(rs.getString("tipo"));
        
        return a;
    }
}
¨*/