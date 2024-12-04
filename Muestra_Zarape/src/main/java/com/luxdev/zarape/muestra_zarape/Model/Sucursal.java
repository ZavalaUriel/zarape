package com.luxdev.zarape.muestra_zarape.Model;

public class Sucursal {
    private int idSucursal;
    private String nombreSucursal;
    private String latitud;
    private String longitud;
    private String fotoSucursal;
    private String url;
    private String horarios;
    private String calle;
    private String numCalle;
    private String colonia;
    private Ciudad ciudad;
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

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFotoSucursal() {
        return fotoSucursal;
    }

    public void setFotoSucursal(String fotoSucursal) {
        this.fotoSucursal = fotoSucursal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumCalle() {
        return numCalle;
    }

    public void setNumCalle(String numCalle) {
        this.numCalle = numCalle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public int getActivoSucursal() {
        return activoSucursal;
    }

    public void setActivoSucursal(int activoSucursal) {
        this.activoSucursal = activoSucursal;
    }
}
