package com.example.proyectodam;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Puntuaciones extends AppCompatActivity {

    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    ListaDinamica listaDinamica;
    List<Usuarios> usuariosList;
    FirebaseAuth firebaseAuth; //Para obtener usuario actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_puntuaciones);

        //Inicializamos las variables

        layoutManager = new LinearLayoutManager(this);
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.RecyclerView);


        layoutManager.setReverseLayout(true); //Ordena los datos del ultimo al primero
        layoutManager.setStackFromEnd(true); //
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        usuariosList = new ArrayList<>();
        obtenerTodosUsuarios();

    }

    private void obtenerTodosUsuarios() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //Obtenemos usuario actual
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIOS");
        reference.orderByChild("Score").addValueEventListener(new ValueEventListener() { //Ordenamos los datos por  Score y actualizamos cada vez que haya un cambio
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usuariosList.clear(); //Limpiamos la lista para actualizarla

                for (DataSnapshot ds : snapshot.getChildren()){
                    Usuarios usuario = ds.getValue(Usuarios.class);

                    usuariosList.add(usuario); //Añadimos los usuarios actualizados a la lista

                    listaDinamica = new ListaDinamica(Puntuaciones.this,usuariosList);
                    recyclerView.setAdapter(listaDinamica);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}