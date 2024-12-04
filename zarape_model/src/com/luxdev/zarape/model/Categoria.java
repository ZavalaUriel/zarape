package com.luxdev.zarape.model;

/**
 *
 * @author karla
 */
public class Categoria {
    private int idCategoria;
    private String descripcionCategoria;
    private String tipo;
    private int activoCategoria;

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getActivoCategoria() {
        return activoCategoria;
    }

    public void setActivoCategoria(int activoCategoria) {
        this.activoCategoria = activoCategoria;
    }
}