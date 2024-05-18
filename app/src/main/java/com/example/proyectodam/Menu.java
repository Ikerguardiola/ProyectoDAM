package com.example.proyectodam;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference usuarios;
    Button CerrarSesion, Jugar, Puntuacion, Acercade;

    TextView MiPuntuacion, Score, User, Mail, IdU;

    //Dialogo
    Dialog miDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        //Inicializar auth y database

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //Ubicación de nuestra fuente

        String ubicacion = "fuentes/ChillPixels-Maximal.otf";
        Typeface tf = Typeface.createFromAsset(Menu.this.getAssets(),ubicacion);

        //Cogemos los datos de la base de datos creada en Firebase, en nuestro caso con el nombre "USUARIOS"

        firebaseDatabase = FirebaseDatabase.getInstance();
        usuarios = firebaseDatabase.getReference("USUARIOS");


        //Enlazar variables con el xml

        //Botones

        CerrarSesion = findViewById(R.id.CerrarSesion);
        Jugar = findViewById(R.id.JugarBtn);
        Puntuacion = findViewById(R.id.PuntuacionBtn);
        Acercade = findViewById(R.id.AcercadeBtn);

        //Diálogo
        miDialog = new Dialog(Menu.this);

        //Texto

        MiPuntuacion = findViewById(R.id.MiPuntuacion);
        IdU = findViewById(R.id.IdU);
        Score = findViewById(R.id.Score);
        User = findViewById(R.id.User);
        Mail = findViewById(R.id.Mail);

        //Cambiar la fuente

        MiPuntuacion.setTypeface(tf);
        IdU.setTypeface(tf);
        Score.setTypeface(tf);
        User.setTypeface(tf);
        Mail.setTypeface(tf);

        CerrarSesion.setTypeface(tf);
        Jugar.setTypeface(tf);
        Puntuacion.setTypeface(tf);
        Acercade.setTypeface(tf);


        Jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Menu.this,Juego.class);

                //Mandamos los parámetros a la escena del juego

                String Idusuario = IdU.getText().toString();
                String nombre = User.getText().toString();
                String verduras = Score.getText().toString();

                intent.putExtra("IdUsuario",Idusuario);
                intent.putExtra("Usuario",nombre);
                intent.putExtra("Score",verduras);

                startActivity(intent);

            }
        });

        Puntuacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Puntuaciones.class);
                startActivity(intent);
            }
        });

        Acercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu.this,"Acerca de ",Toast.LENGTH_SHORT).show();
            }
        });


        //Al hacer click en el botón  nos cerrará la sesión

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cerrarSesion();
            }
        });

    }

    //Este método se ejecuta al abrir el juego

    protected void onStart(){
        jugadorLinea();
        super.onStart();
    }

    //El método comprueba si el usuario ha iniciado sesión
    private void jugadorLinea(){
        if(user != null){

            //Ejecutamos el método para mostrar la información del usuario
            consulta();

            Toast.makeText(Menu.this,"Jugador en línea",Toast.LENGTH_SHORT).show();
        }
        else{
            startActivity(new Intent(Menu.this,MainActivity.class));
            finish();
        }
    }

    //Este método comprueba que hemos cerrado sesión adecuadamente
    private void cerrarSesion(){
        auth.signOut();
        startActivity(new Intent(Menu.this,MainActivity.class));
        Toast.makeText(Menu.this,"Has cerrado sesión", Toast.LENGTH_SHORT).show();
    }

    //Método para realizar una consulta a la base de datos

    private void consulta(){
        Query query = usuarios.orderByChild("Correo").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Recorremos los datos para llegar al jugador en específico y poder mostrar su perfil

                for(DataSnapshot ds : snapshot.getChildren()){

                    String score = ""+ds.child("Score").getValue();
                    String uid = ""+ds.child("IdUsuario").getValue();
                    String email = ""+ds.child("Correo").getValue();
                    String usuario = ""+ds.child("Usuario").getValue();

                    //Los datos recuperados en la consulta los asignamos a las variables de nuestro xml

                    Score.setText(score);
                    IdU.setText(uid);
                    Mail.setText(email);
                    User.setText(usuario);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}