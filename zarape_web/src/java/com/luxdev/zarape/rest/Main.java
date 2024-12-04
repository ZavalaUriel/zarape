/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luxdev.zarape.rest;

import com.google.gson.Gson;
import com.luxdev.zarape.model.Ciudad;
import com.luxdev.zarape.model.Persona;
import com.luxdev.zarape.model.Cliente;

/**
 *
 * @author Uriel Zavala
 */
public class Main {
    public static void main(String[] args) {
        
        Gson gson = new Gson();
        Ciudad c = new Ciudad();
        Persona p = new Persona();
        Cliente e = new Cliente();
        
        c.setIdCiudad(1);
        p.setNombrePersona("juan");
        p.setApellidos("lópez");
        p.setTelefono("4771234567");
        e.setIdCliente(1);
        e.setActivoCliente(1);
        
       
        
    }
}
