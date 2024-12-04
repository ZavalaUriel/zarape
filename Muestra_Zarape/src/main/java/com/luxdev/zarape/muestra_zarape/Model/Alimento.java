package com.luxdev.zarape.muestra_zarape.Model;

public class Alimento {
    private int idAlimento;

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getIdAlimento() {
        return idAlimento;
    }

    public void setIdAlimento(int idAlimento) {
        this.idAlimento = idAlimento;
    }

    private Producto producto;

}
