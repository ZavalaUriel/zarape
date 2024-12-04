package com.luxdev.zarape.controller;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import com.luxdev.zarape.db.ConexionMySQL;
import com.luxdev.zarape.model.Usuario;
import java.sql.Connection;

public class ControllerLogin {

    public void ValidarDatos(Usuario us) throws Exception {

        String sql = "select nombre, contrasenia from usuario where nombre = ? and contrasenia = ?";

        System.out.println("punto de prueba 1");

        try {
            ConexionMySQL connMySQL = new ConexionMySQL();
            Connection conn = connMySQL.open();

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, us.getNombreUsuario());
            ps.setString(2, us.getContrasenia());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                                        
                System.out.println("Ingreso exitoso meloso");
                String nombre = rs.getString("nombre");
                String contrasena = rs.getString("contrasenia");
                System.out.println("nombre: " + nombre + "\n contrasena: " + contrasena);
                us.setIngreso(true);
                System.out.println(us.isIngreso());
                
                rs.close();
                conn.close();
            }
            if (!rs.next()) {
                System.out.println("Contraseña incorrecta");
                us.setIngreso(false);   
                System.out.println(us.isIngreso());
            }

        } catch (SQLException e) {

            e.getStackTrace();
        }
}
}
