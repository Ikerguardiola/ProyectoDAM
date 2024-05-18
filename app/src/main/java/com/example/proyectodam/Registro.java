package com.example.proyectodam;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Registro extends AppCompatActivity {

    EditText Email, Pass, User;
    TextView Fech;
    Button Btn_Registro;
    FirebaseAuth auth; //Autenticar en firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("test");

        //Conectamos las variables a la vista

        Email = findViewById(R.id.Correo);
        Pass = findViewById(R.id.Password);
        User = findViewById(R.id.Usuario);
        Fech = findViewById(R.id.Fecha);
        Btn_Registro = findViewById(R.id.Registro);

        //Instanciamos el auth

        auth = FirebaseAuth.getInstance();

        //Ubicación de nuestra fuente

        String ubicacion = "fuentes/ChillPixels-Maximal.otf";
        Typeface tf = Typeface.createFromAsset(Registro.this.getAssets(),ubicacion);

        Email.setTypeface(tf);
        Pass.setTypeface(tf);
        User.setTypeface(tf);
        Fech.setTypeface(tf);
        Btn_Registro.setTypeface(tf);

        //Creamos un objeto date para la fecha y le damos formato

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d' de' MMMM 'del' yyyy"); //Dia de Mes del Año
        String StringFecha = fecha.format(date);
        Fech.setText(StringFecha);

        //Cuando hagamos click en el botón, nos registrará el usuario, o en su defecto nos saltará un mensaje de error

        Btn_Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                String password = Pass.getText().toString();

                //Validamos los datos para que no escriban correos ni contraseñas incorrectas

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Email.setError("Correo no válido");

                    //Esto es para que al mostrar error, nos lleve el puntero justo donde se encuentra ese mismo error
                    Email.setFocusable(true);
                } else if(password.length()<6){
                    Pass.setError("La contraseña debe ser mayor a 6");
                    Pass.setFocusable(true);
                }else{
                    RegistrarUsuario(email,password);
                }
            }
        });
    }

    //Método para registrar al usuario
    private void RegistrarUsuario(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Si el usuario ha sido registrado correctamente, se guardará en la base de datos
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();

                            //Realizamos un contador para asignar un número a cada usuario
                            int contador = 0;

                            assert user != null; //El usuario no es nulo

                            String IdUsuario = user.getUid(); //Al hacer click en registro, se nos generará una clave automática, y esa clave la guardaremos en IdUsuario
                            String correo = Email.getText().toString();
                            String pass = Pass.getText().toString();
                            String usuario = User.getText().toString();
                            String fecha = Fech.getText().toString();

                            //El hashmap nos permitirá guardar cada valor en una clave a la que le daremos nombre a continuación, dentro de la base de datos

                            HashMap<Object,Object> DatosJugador = new HashMap<>();


                            DatosJugador.put("IdUsuario",IdUsuario);
                            DatosJugador.put("Correo",correo);
                            DatosJugador.put("Contraseña",pass);
                            DatosJugador.put("Usuario",usuario);
                            DatosJugador.put("FechaCreación",fecha);
                            DatosJugador.put("Score",contador);


                            //Instanciamos la base de datos
                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            //Referenciamos el nombre de la base de datos creada
                            DatabaseReference reference = database.getReference("USUARIOS");

                            //Referenciamos para que a la base de datos creada se registren los datos de "DATOSJUGADOR"
                            reference.child(IdUsuario).setValue(DatosJugador);

                            //Creamos un intent para que nos lleve a la siguiente vista, que será nuestro menu de juego
                            startActivity(new Intent(Registro.this,Menu.class));

                            //Por último, mostramos un mensaje para afirmar la creación del usuario
                            Toast.makeText(Registro.this,"USUARIO CREADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();

                            finish();
                        }else{
                            Toast.makeText(Registro.this, "HA OCURRIDO UN ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                //Si el registro da error, nos saltará un mensaje
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Hacemos un toast para que muestre el mensaje de error
                        Toast.makeText(Registro.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }

}