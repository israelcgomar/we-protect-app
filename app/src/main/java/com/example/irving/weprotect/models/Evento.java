package com.example.irving.weprotect.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import java.io.Serializable;

public class Evento implements Serializable{
    private String nombreEvento;
    private String lugarEvento;
    private String fechaHora;
    private String descripcion;
    private Bitmap imagen;
    private String nombre;
    public Evento(String nombreEvento, String lugarEvento, String fechaHora, String descripcion, @Nullable Bitmap imagen, String nombre) {
        this.nombreEvento = nombreEvento;
        this.lugarEvento = lugarEvento;
        this.fechaHora = fechaHora;
        this.descripcion = descripcion;
        if (imagen != null){
            this.imagen = imagen;
        }
        this.nombre = nombre;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public String getLugarEvento() {
        return lugarEvento;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getNombre(){
        return nombre;
    }
}
