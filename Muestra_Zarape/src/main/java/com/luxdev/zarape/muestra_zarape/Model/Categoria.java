package com.luxdev.zarape.muestra_zarape.Model;

public class Categoria {
    private int idCategoria;
    private String descripcionCategoria;
    private String tipoCategoria;
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

    public String getTipoCategoria() {
        return tipoCategoria;
    }

    public void setTipoCategoria(String tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    public int getActivoCategoria() {
        return activoCategoria;
    }

    public void setActivoCategoria(int activoCategoria) {
        this.activoCategoria = activoCategoria;
    }
}
