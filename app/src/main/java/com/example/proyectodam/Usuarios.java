package com.example.proyectodam;

public class Usuarios {

    String Correo, Usuario;
    int Score;

    public Usuarios() {
    }

    public Usuarios(String correo, String usuario, int score) {
        this.Correo = correo;
        this.Usuario = usuario;
        this.Score = score;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        this.Correo = correo;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        this.Usuario = usuario;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        this.Score = score;
    }
}
