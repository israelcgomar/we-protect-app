package com.example.irving.weprotect.models;

public class Comentario {

    private String nombre;
    private String comment;
    private String fecha;

    public Comentario(String nombre, String comment, String fecha) {
        this.nombre = nombre;
        this.comment = comment;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
