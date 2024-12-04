package com.luxdev.zarape.muestra_zarape.Model;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasenia;
    private int activoUsuario;
    private boolean ingreso;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public int getActivoUsuario() {
        return activoUsuario;
    }

    public void setActivoUsuario(int activoUsuario) {
        this.activoUsuario = activoUsuario;
    }

    public boolean isIngreso() {
        return ingreso;
    }

    public void setIngreso(boolean ingreso) {
        this.ingreso = ingreso;
    }
}
