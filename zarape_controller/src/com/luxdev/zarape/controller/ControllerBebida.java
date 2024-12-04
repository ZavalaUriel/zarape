package com.luxdev.zarape.controller;

import com.luxdev.zarape.db.ConexionMySQL;
import com.luxdev.zarape.model.Bebida;
import com.luxdev.zarape.model.Categoria;
import com.luxdev.zarape.model.Producto;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ControllerBebida {


    public int insert(Bebida b) throws Exception
    {
        // Se define la consulta SQL:
 String sql = "{CALL insertarBebida(?, ?, ?, ?, ?, ?, ?)}"; 

        
        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        // Generamos un CallableStatement para invocar al Stored Procedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        
        // Colocamos los valores de los parametros de entrada que requiere
        // el Stored Procedure:
        cstmt.setString(1, b.getProducto().getNombreProducto());
        cstmt.setString(2, b.getProducto().getDescripcionProducto());
        cstmt.setString(3, b.getProducto().getFotoProducto());
        cstmt.setDouble(4, b.getProducto().getPrecioProducto());
        cstmt.setInt   (5, b.getProducto().getCategoria().getIdCategoria());
        
        // para deifinir los parametros de salida
        cstmt.registerOutParameter(6, java.sql.Types.INTEGER); // idProducto
        cstmt.registerOutParameter(7, java.sql.Types.INTEGER); // idBebida

        // Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
        
        // Recuperamos el ID de Producto y de Bebida generados:
        b.getProducto().setIdProducto(cstmt.getInt(6));
        b.setIdBebida(cstmt.getInt(7));
        
        //Cerramos los objetos de conexion:
        cstmt.close();
        connMySQL.close();
        
        // Devolvemos el ID de Bebida que se genero:
        return b.getIdBebida();
    }
    
    public void update(Bebida b) throws Exception
    {
        // Se define la consulta SQL:
        String sql = "{CALL actualizarBebida(?, ?, ?, ?, ?,?)}";

        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        // Generamos un CallableStatement para invocar al Stored Procedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        
        // Colocamos los valores de los parametros de entrada que requiere
        // el Stored Procedure:
        cstmt.setString(1, b.getProducto().getNombreProducto());
        cstmt.setString(2, b.getProducto().getDescripcionProducto());
        cstmt.setString(3, b.getProducto().getFotoProducto());
        cstmt.setDouble(4, b.getProducto().getPrecioProducto());
        cstmt.setInt   (5, b.getProducto().getCategoria().getIdCategoria());
        cstmt.setInt   (6, b.getProducto().getIdProducto());
        
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
    
    public List<Bebida> getAll(String filtro) throws Exception
    {
        List<Bebida> bebida = new ArrayList<>();
        // Se define la consulta SQL:
        String sql = "SELECT * FROM v_bebida";
        
        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        
        Bebida be = null;
        
        // Recorremos cada registro devuelto por la consulta:
        while(rs.next())
        {
            be = fill(rs);
            bebida.add(be);
        }
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return bebida;
    }
    
    private Bebida fill(ResultSet rs) throws Exception
    {
        Categoria c = new Categoria();
        Producto  p = new Producto();
        Bebida  b = new Bebida();
        
        p.setCategoria(c);
        b.setProducto(p);
        
        // Establecemos los valores de cada atributo de
        // los objetos relacionados, extraidos de cada
        // campo del ResultSet:
        
        b.setIdBebida(rs.getInt("idBebida"));
        
        p.setActivoProducto(rs.getInt("productoActivo"));
        p.setDescripcionProducto(rs.getString("descripcionProducto"));
        p.setFotoProducto(rs.getString("foto"));
        p.setIdProducto(rs.getInt("idProducto"));
        p.setNombreProducto(rs.getString("nombre"));
        p.setPrecioProducto(rs.getDouble("precio"));
        
        c.setActivoCategoria(rs.getInt("categoriaActiva"));
        c.setDescripcionCategoria(rs.getString("descripcionCategoria"));
        c.setIdCategoria(rs.getInt("idCategoria"));
        c.setTipo(rs.getString("tipo"));
        
        p.setCategoria(c);
        b.setProducto(p);
        return b;
    }
 }
