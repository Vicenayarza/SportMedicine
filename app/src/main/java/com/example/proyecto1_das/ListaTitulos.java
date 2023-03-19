package com.example.proyecto1_das;

import java.io.Serializable;

public class ListaTitulos implements Serializable {
//Clase Lista Titulos para el Recycler View + CardView
    public String titulo;
    public int año;

    public ListaTitulos( String titulo,int año){
        this.titulo = titulo;
        this.año = año;

    }
    public String getTitulo() {
        return titulo;
    }


    public void setName(String titulo) {
        this.titulo = titulo;
    }
    public int getAño() {
        return año;
    }


    public void setAño(int año) {
        this.año = año;
    }



}
