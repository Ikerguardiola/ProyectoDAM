package com.example.proyectodam;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    //Declaramos las variables de los botones de Login y Registro
    Button Btn_Login, Btn_Registrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

    //Conectamos los botones

        Btn_Login = findViewById(R.id.Btn_Login);
        Btn_Registrar = findViewById(R.id.Btn_Registrar);

        //Ubicación de nuestra fuente

        String ubicacion = "fuentes/ChillPixels-Maximal.otf";
        Typeface tf = Typeface.createFromAsset(MainActivity.this.getAssets(),ubicacion);

        Btn_Login.setTypeface(tf);
        Btn_Registrar.setTypeface(tf);

        //Cuando hagamos click en el boton, nos llevará a la vista "Login"

        Btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Hacemos un intent para que cuando hagamos click en LOGIN nos lleve a la vista de "Login"

                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });

        //Cuando hagamos click nos llevará a la actividad "Registro"

        Btn_Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Hacemos un intent para que cuando hagamos click en REGISTRAR nos lleve a la vista de Registro

                Intent intent = new Intent(MainActivity.this,Registro.class);
                startActivity(intent);
            }
        });
    }
}