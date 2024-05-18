package com.example.proyectodam;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class PantallaDeCarga extends AppCompatActivity {

    //declaramos la variable del tiempo que tardará en iniciar la aplicacion en milisegundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_carga);

        //Declaramos la variable del tiempo que tardará en iniciar la aplicacion, en milisegundos

        int tiempoEspera = 3000;
        // Usamos un Handler para ejecutar un Runnable después del retraso

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Este método se ejecutará después de los segundos declarados en la variable tiempoEspera
                // Creamos un Intent para iniciar MainActivity
                Intent intent = new Intent(PantallaDeCarga.this, Menu.class);
                // Iniciamos MainActivity
                startActivity(intent);;
            }
        }, tiempoEspera);

    }
}