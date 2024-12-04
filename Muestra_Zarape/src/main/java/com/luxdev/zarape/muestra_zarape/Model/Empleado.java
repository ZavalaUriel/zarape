package com.luxdev.zarape.muestra_zarape.Model;

public class Empleado {
    private int idEmpleado;
    private Sucursal sucursal;
    private Persona persona;
    private Usuario usuario;
    private int activoEmpleado;

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getActivoEmpleado() {
        return activoEmpleado;
    }

    public void setActivoEmpleado(int activoEmpleado) {
        this.activoEmpleado = activoEmpleado;
    }
}
