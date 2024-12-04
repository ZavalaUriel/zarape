package com.luxdev.zarape.model;

public class DetallesCombos {
private Combos combo;
private Productos producto;
private double precioCombo;

    public Combos getCombo() {
        return combo;
    }

    public void setCombo(Combos combo) {
        this.combo = combo;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }

    public double getPrecioCombo() {
        return precioCombo;
    }

    public void setPrecioCombo(double precioCombo) {
        this.precioCombo = precioCombo;
    }
}