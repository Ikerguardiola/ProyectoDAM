package com.example.proyectodam;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import androidx.activity.EdgeToEdge;

import java.util.HashMap;
import java.util.Random;

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

public class Juego extends AppCompatActivity {

    String idU, nombres, verduras, maximo;
    TextView jugadorActivo, contadorMuestra, tiempoMuestra;
    ImageView verdura, bomba, bomba1, bomba2, bomba3;

    //Firebase

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference USUARIOS;

    // Declarar objetos MediaPlayer
    private MediaPlayer mediaPlayerVerdura;
    private MediaPlayer mediaPlayerBomba;
    private MediaPlayer mediaPlayerFondo;
    private CountDownTimer countDownTimer;
    int contador = 0,contadorBomba = 0;
    Dialog miDialog;
    long tiempoActual = 29000;
    long tiempoBombas = 1000;

    private boolean gameEnded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_juego);

        verdura = findViewById(R.id.Verdura);
        bomba = findViewById(R.id.Bomba);
        bomba1 = findViewById(R.id.Bomba1);
        bomba2 = findViewById(R.id.Bomba2);
        bomba3 = findViewById(R.id.Bomba3);

        miDialog = new Dialog(Juego.this);

        jugadorActivo = findViewById(R.id.JugadorActivo);
        contadorMuestra = findViewById(R.id.Contador);
        tiempoMuestra = findViewById(R.id.Tiempo);


        //Iniciamos la musica
        musicaFondo();
        mediaPlayerVerdura = MediaPlayer.create(this,R.raw.corte);
        mediaPlayerBomba = MediaPlayer.create(this,R.raw.gameover);

        // Obtener los extras del intent
        Bundle intent = getIntent().getExtras();

        idU = intent.getString("IdUsuario");
        nombres = intent.getString("Usuario");
        verduras = intent.getString("Score");


        jugadorActivo.setText(nombres);
        contadorMuestra.setText("0");

        //Inicializar Firebase

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        USUARIOS = firebaseDatabase.getReference("USUARIOS");


        verdura.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!gameEnded && event.getAction() == MotionEvent.ACTION_DOWN) {
                    contador++;
                    mediaPlayerVerdura.start();
                    temporizadorGeneral();
                    contadorMuestra.setText(String.valueOf(contador));
                    cambioImagenVerdura();
                    moverVerdura();
                    return true;
                }
                return false;
            }
        });

        bomba.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!gameEnded && event.getAction() == MotionEvent.ACTION_DOWN) {
                    endGame();
                    temporizadorGeneral();
                    return true;
                }
                return false;
            }
        });

        bomba1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!gameEnded && event.getAction() == MotionEvent.ACTION_DOWN) {
                    endGame();
                    temporizadorGeneral();
                    return true;
                }
                return false;
            }
        });

        bomba2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!gameEnded && event.getAction() == MotionEvent.ACTION_DOWN) {
                    endGame();
                    temporizadorGeneral();
                    return true;
                }
                return false;
            }
        });

        bomba3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!gameEnded && event.getAction() == MotionEvent.ACTION_DOWN) {
                    endGame();
                    temporizadorGeneral();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        consultaMaximo();
        moverBomba();
        moverVerdura();
        mediaPlayerFondo.start();
        temporizadorGeneral();
        cambioImagenVerdura();
    }

    //Metodo para mover raton
    private void moverVerdura() {
        if (!gameEnded) {
            Random random = new Random();
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;

            // Mover las verduras
            int XVerdura = random.nextInt(screenWidth - 300);
            int YVerdura = random.nextInt(screenHeight - 300);
            verdura.setX(XVerdura);
            verdura.setY(YVerdura);
        }
    }

    //Metodo para mover la bomba
    private void moverBomba() {
        if (!gameEnded) {
            Random random = new Random();
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;

            // Mover la bomba
            int XBomba = random.nextInt(screenWidth - 200);
            int YBomba = random.nextInt(screenHeight - 200);
            bomba.setX(XBomba);
            bomba.setY(YBomba);

            // Move bomb again after 1 second if it hasn't been touched
            new CountDownTimer(tiempoBombas, 1000) {

                public void onTick(long millisUntilFinished) {

                }
                public void onFinish() {
                    if (!gameEnded) {
                        moverBomba();
                        contadorBomba++;
                        if(contadorBomba >= 15){
                            moverBomba1();
                            if(contadorBomba >=30){
                                moverBomba2();
                                if(contadorBomba >=45){
                                    moverBomba3();
                                    if(contadorBomba == 60){
                                        //para que las bombas se muevan más rápido
                                        tiempoBombas = 500;
                                        if(contadorBomba == 100){
                                            tiempoBombas = 250;
                                            if(contadorBomba == 150){
                                                tiempoBombas = 100;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }.start();
        }
    }
    private void moverBomba1() {
        if (!gameEnded) {
            Random random = new Random();
            int screenWidth1 = getResources().getDisplayMetrics().widthPixels;
            int screenHeight1 = getResources().getDisplayMetrics().heightPixels;

            // Mover la bomba
            int XBomba1 = random.nextInt(screenWidth1 - 200);
            int YBomba1 = random.nextInt(screenHeight1 - 200);
            bomba1.setX(XBomba1);
            bomba1.setY(YBomba1);
        }
    }
    private void moverBomba2() {
        if (!gameEnded) {
            Random random = new Random();
            int screenWidth2 = getResources().getDisplayMetrics().widthPixels;
            int screenHeight2 = getResources().getDisplayMetrics().heightPixels;

            // Mover la bomba
            int XBomba2 = random.nextInt(screenWidth2 - 200);
            int YBomba2 = random.nextInt(screenHeight2 - 200);
            bomba2.setX(XBomba2);
            bomba2.setY(YBomba2);

        }
    }
    private void moverBomba3() {
        if (!gameEnded) {
            Random random = new Random();
            int screenWidth3 = getResources().getDisplayMetrics().widthPixels;
            int screenHeight3 = getResources().getDisplayMetrics().heightPixels;

            // Mover la bomba
            int XBomba3 = random.nextInt(screenWidth3 - 200);
            int YBomba3 = random.nextInt(screenHeight3 - 200);
            bomba3.setX(XBomba3);
            bomba3.setY(YBomba3);

        }
    }
    private void temporizadorGeneral(){

        //Si el juego ha terminado, cancela el temporizador y setearlo a 0



            // Cancelar el temporizador actual si existe para que no se solapen los tiempos
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            countDownTimer = new CountDownTimer(tiempoActual + 1000, 1000) {
                public void onTick(long millisUntilFinished) {

                    //Si el juego ha terminado se setea a 0.
                    if (!gameEnded) {

                        // Pasamos el tiempo a segundos para mostrarlo por pantalla

                        long tiempoRestante = millisUntilFinished / 1000;
                        tiempoMuestra.setText(tiempoRestante + "S");

                        //Actualizamos el tiempo para que sume 1 segundo a la cuenta atrás
                        tiempoActual = millisUntilFinished;

                    }else{
                        onFinish();
                        tiempoMuestra.setText("0S");
                    }
                }
                public void onFinish() {
                    if (!gameEnded) {
                        endGame();
                    }
                }
            }.start();

    }

    private void cambioImagenVerdura(){

        //Cuando hagamos click en la verdura, se eliminará y saldrá otra verdura diferente

        int numeroAleatorio = generateRandomNumber(5);

        if(numeroAleatorio == 0){

            verdura.setImageResource(R.drawable.berenjena);

        }else if(numeroAleatorio == 1){

            verdura.setImageResource(R.drawable.cebolla);

        }else if(numeroAleatorio == 2){

            verdura.setImageResource(R.drawable.pepino);

        }else if(numeroAleatorio == 3){

            verdura.setImageResource(R.drawable.tomate);

        }else{
            verdura.setImageResource(R.drawable.zanahoria);
        }
    }
    private static int generateRandomNumber(int num) {
        Random random = new Random();
        return random.nextInt(num); // Genera un número aleatorio entre 0 y 4 (inclusive)
    }
    private void musicaFondo(){
        int num = generateRandomNumber(7);
        if(num == 0){

            mediaPlayerFondo = MediaPlayer.create(this, R.raw.musicauno);
            mediaPlayerFondo.setLooping(true);
        }else if(num == 1){

            mediaPlayerFondo = MediaPlayer.create(this, R.raw.musicados);
            mediaPlayerFondo.setLooping(true);

        }else if(num == 2){
            mediaPlayerFondo = MediaPlayer.create(this, R.raw.musicatres);
            mediaPlayerFondo.setLooping(true);

        }else if(num == 3){
            mediaPlayerFondo = MediaPlayer.create(this, R.raw.musicacuatro);
            mediaPlayerFondo.setLooping(true);

        }else if(num == 4){
            mediaPlayerFondo = MediaPlayer.create(this, R.raw.musicacinco);
            mediaPlayerFondo.setLooping(true);

        }else if(num == 5){
            mediaPlayerFondo = MediaPlayer.create(this, R.raw.musicaseis);
            mediaPlayerFondo.setLooping(true);

        }else{
            mediaPlayerFondo = MediaPlayer.create(this, R.raw.musicasiete);
            mediaPlayerFondo.setLooping(true);
        }

    }

    private void mensajeGameOver(){

        //Ubicación de nuestra fuente

        String ubicacion = "fuentes/ChillPixels-Maximal.otf";
        Typeface tf = Typeface.createFromAsset(Juego.this.getAssets(),ubicacion);

        TextView textoEliminacion, total, textoEliminacionUno;
        Button jugarDeNuevo, irMenu;

        miDialog.setContentView(R.layout.gameover);

        textoEliminacion = miDialog.findViewById(R.id.TextoEliminacion);
        total = miDialog.findViewById(R.id.Total);
        textoEliminacionUno = miDialog.findViewById(R.id.TextoEliminacionUno);
        jugarDeNuevo = miDialog.findViewById(R.id.JugarDeNuevo);
        irMenu = miDialog.findViewById(R.id.IrMenu);

        //Pasamos nuestro contador de verduras a string para mostrarlo en pantalla
        String verdurasCortadas = String.valueOf(contador);
        total.setText(verdurasCortadas);

        //Cambiamos la fuente
        textoEliminacion.setTypeface(tf);
        total.setTypeface(tf);
        textoEliminacionUno.setTypeface(tf);
        jugarDeNuevo.setTypeface(tf);
        irMenu.setTypeface(tf);

        miDialog.show();

        jugarDeNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador = 0;
                contadorBomba = 0;
                consultaMaximo();
                contadorMuestra.setText("0");
                gameEnded = false;
                tiempoActual = 29000;
                tiempoBombas = 1000;
                musicaFondo();
                onStart();
                miDialog.dismiss();
            }
        });

        irMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contadorMuestra.setText("0");
                Intent intent = new Intent(Juego.this,Menu.class);
                startActivity(intent);
            }
        });

    }
    private void consultaMaximo(){
        Query query = USUARIOS.orderByChild("Correo").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Recorremos los datos para llegar al jugador en específico y poder mostrar su perfil

                for(DataSnapshot ds : snapshot.getChildren()){

                    maximo = ""+ds.child("Score").getValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void guardarPuntuacion(String key, int verdurasCortadas){

        int maximoInt = Integer.parseInt(maximo);
        HashMap <String, Object> hashMap = new HashMap<>();
        if(verdurasCortadas > maximoInt){
            hashMap.put(key,verdurasCortadas);
        }else{
            hashMap.put(key,maximoInt);
        }
        USUARIOS.child(firebaseUser.getUid()).updateChildren(hashMap);
    }

    private void endGame() {
        gameEnded = true;
        mensajeGameOver();
        guardarPuntuacion("Score",contador);
        mediaPlayerBomba.start();
        mediaPlayerFondo.stop();
        mediaPlayerFondo.release(); // Liberar recursos de MediaPlayer
        Toast.makeText(Juego.this, "Game Over. Score: " + contador, Toast.LENGTH_SHORT).show();
    }


}