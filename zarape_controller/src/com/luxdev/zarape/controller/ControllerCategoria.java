/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luxdev.zarape.controller;

import com.luxdev.zarape.db.ConexionMySQL;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.luxdev.zarape.model.Categoria;
import java.util.List;
public class ControllerCategoria {
    public int insert(Categoria c)throws Exception
    {
         // Se define la consulta SQL:
        String sql = "{CALL insertarCategoria(?, ?, ?)}";
        
        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        // Generamos un CallableStatement para invocar al Stored Procedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        
        // Colocamos los valores de los parametros de entrada que requiere
        // el Stored Procedure:
        cstmt.setString(1, c.getDescripcionCategoria());
        cstmt.setString(2, c.getTipo());
        
        // Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
        
        // Recuperamos el ID de Producto y de Alimento generados:
        c.setIdCategoria(cstmt.getInt(3));
        
        //Cerramos los objetos de conexion:
        cstmt.close();
        connMySQL.close();
        
        // Devolvemos el ID de Alimento que se genero:
        return c.getIdCategoria();
        
    }
    public void update(Categoria c) throws Exception
    {
         // Se define la consulta SQL:
        String sql = "{CALL ActualizarCategoria(?, ?, ?)}";
        
        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        // Generamos un CallableStatement para invocar al Stored Procedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        
        // Colocamos los valores de los parametros de entrada que requiere
        // el Stored Procedure:
        cstmt.setInt(1, c.getIdCategoria());
        cstmt.setString(2, c.getDescripcionCategoria());
        cstmt.setString(3, c.getTipo());
        
        // Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
                
        //Cerramos los objetos de conexion:
        cstmt.close();
        connMySQL.close();
    }
    public void delete(int idCategoria) throws Exception
    {
         // Se define la consulta SQL:
        String sql = "UPDATE categoria SET activo=0 WHERE idCategoria=?";
        
        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        // Llenamos los datos del PreparedStatement:
        pstmt.setInt(1, idCategoria);
        
        // Ejecutamos la consulta:
        pstmt.executeUpdate();
        
        // Cerramos los objetos de conexion:
        pstmt.close();
        connMySQL.close();
    }
    public List<Categoria> getAllByTipo(String tipo) throws Exception
    {
        String sql = "SELECT * FROM categoria WHERE tipo=? ORDER BY tipo, descripcion";
        List<Categoria> categorias = new ArrayList<>();
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = null;
        
        pstmt.setString(1, tipo);
        rs = pstmt.executeQuery();
        
        while(rs.next()){
            categorias.add(fill(rs));
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return categorias;
    }
    public List<Categoria> getAll(String filtro) throws Exception
    {
         List<Categoria> categorias = new ArrayList<>();

        // Definir la consulta SQL para obtener todos los empleados
        String sql = "SELECT * FROM v_categoria"; 

        // Abrir la conexión con la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        // Preparar la sentencia SQL
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Ejecutar la consulta
        ResultSet rs = pstmt.executeQuery();

        Categoria cat = null;

        // Recorrer los resultados y llenar la lista de empleados
        while (rs.next()) {
            cat = fill(rs);  // Método que rellena el objeto Empleado
            categorias.add(cat);
        }

        // Cerrar la conexión
        rs.close();
        pstmt.close();
        connMySQL.close();

        return categorias;
    }
     private Categoria fill(ResultSet rs) throws Exception
    {
        Categoria c = new Categoria();
        
        // Establecemos los valores de cada atributo de
        // los objetos relacionados, extraidos de cada
        // campo del ResultSet:
        c.setIdCategoria(rs.getInt("idCategoria"));
        c.setDescripcionCategoria(rs.getString("descripcion"));
        c.setTipo(rs.getString("tipo"));
        c.setActivoCategoria(rs.getInt("activo"));
        
        return c;
    }
     
     
}