package com.luxdev.zarape.model;

public class Sucursales {
private int idSucursal;
private String nombreSucursal;
private String latitudSucursal;
private String longitudSucursal;
private String fotoSucursal;
private String urlWeb;
private String horariosSucursal;
private String calleSucursal;
private String numCalleSucursal;
private String coloniaSucursal;
private Ciudades ciudad;
private int activoSucursal;

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public String getLatitudSucursal() {
        return latitudSucursal;
    }

    public void setLatitudSucursal(String latitudSucursal) {
        this.latitudSucursal = latitudSucursal;
    }

    public String getLongitudSucursal() {
        return longitudSucursal;
    }

    public void setLongitudSucursal(String longitudSucursal) {
        this.longitudSucursal = longitudSucursal;
    }

    public String getFotoSucursal() {
        return fotoSucursal;
    }

    public void setFotoSucursal(String fotoSucursal) {
        this.fotoSucursal = fotoSucursal;
    }

    public String getUrlWeb() {
        return urlWeb;
    }

    public void setUrlWeb(String urlWeb) {
        this.urlWeb = urlWeb;
    }

    public String getHorariosSucursal() {
        return horariosSucursal;
    }

    public void setHorariosSucursal(String horariosSucursal) {
        this.horariosSucursal = horariosSucursal;
    }

    public String getCalleSucursal() {
        return calleSucursal;
    }

    public void setCalleSucursal(String calleSucursal) {
        this.calleSucursal = calleSucursal;
    }

    public String getNumCalleSucursal() {
        return numCalleSucursal;
    }

    public void setNumCalleSucursal(String numCalleSucursal) {
        this.numCalleSucursal = numCalleSucursal;
    }

    public String getColoniaSucursal() {
        return coloniaSucursal;
    }

    public void setColoniaSucursal(String coloniaSucursal) {
        this.coloniaSucursal = coloniaSucursal;
    }

    public Ciudades getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudades ciudad) {
        this.ciudad = ciudad;
    }

    public int getActivoSucursal() {
        return activoSucursal;
    }

    public void setActivoSucursal(int activoSucursal) {
        this.activoSucursal = activoSucursal;
    }
}