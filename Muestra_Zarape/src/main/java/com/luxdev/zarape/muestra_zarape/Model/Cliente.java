package com.luxdev.zarape.muestra_zarape.Model;

public class Cliente {
    private int idCliente;
    private Persona persona;
    private int activoCliente;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public int getActivoCliente() {
        return activoCliente;
    }

    public void setActivoCliente(int activoCliente) {
        this.activoCliente = activoCliente;
    }
}
