package com.example.proyectodam;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {


    EditText log, pass;
    Button entrar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Conectamos las variables con el xml

        log = findViewById(R.id.CorreoLogin);
        pass = findViewById(R.id.PasswordLogin);
        entrar = findViewById(R.id.Btn_Entrar);
        auth = FirebaseAuth.getInstance();

        //Ubicación de nuestra fuente

        String ubicacion = "fuentes/ChillPixels-Maximal.otf";
        Typeface tf = Typeface.createFromAsset(Login.this.getAssets(),ubicacion);

        log.setTypeface(tf);
        pass.setTypeface(tf);
        entrar.setTypeface(tf);



        //Al hacer click en el botón de "entrar"

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String correo = log.getText().toString();
            String password = pass.getText().toString();

            //Validamos los datos para que no escriban correos ni contraseñas incorrectas

                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    log.setError("Correo electrónico incorrecto");

                    //Esto es para que al mostrar error, nos lleve el puntero justo donde se encuentra ese mismo error

                    log.setFocusable(true);
                } else if(password.length()<6){
                    pass.setError("Contraseña incorrecta");
                    pass.setFocusable(true);
                }else {
                    LogJugador(correo, password);
                }
            }
        });

    }

    //Método de login del jugador
    private void LogJugador(String correo, String password) {
        auth.signInWithEmailAndPassword(correo,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){ //Comprobamos si el usuario está registrado en la BBDD;
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(Login.this,Menu.class));
                            assert user != null; //Para no introducir un usuario nulo
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}