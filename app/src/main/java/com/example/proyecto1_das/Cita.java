package com.example.proyecto1_das;

public class Cita implements java.io.Serializable{

    private String email;
    private String tipo;
    private String fecha;



    public Cita(String email, String tipo, String fecha) {
        this.email = email;
        this.tipo = tipo;
        this.fecha = fecha;

    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}

