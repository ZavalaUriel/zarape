/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luxdev.zarape.model;

public class Alimentos {

    private int idAlimento;
    private Productos producto;

    public int getId(){
        return idAlimento;
    }

    public void setId(int idAlimento) {
        this.idAlimento = idAlimento;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }
}
