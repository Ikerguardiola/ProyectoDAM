package com.example.proyectodam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListaDinamica extends RecyclerView.Adapter<ListaDinamica.MyHolder>{

    public Context context;
    public List<Usuarios> usuarioList;


    public ListaDinamica(Context context, List<Usuarios> usuarioList) {
        this.context = context;
        this.usuarioList = usuarioList;
    }

    @NonNull
    @Override
    //Con este método mostraremos el diseño de la cartajugador del XML
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cartajugador,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        //Obtenemos los datos del objeto usuario

        String nombres = usuarioList.get(position).getUsuario();
        String correo = usuarioList.get(position).getCorreo();
        int score = usuarioList.get(position).getScore();

        //Convertir a string para manejar los datos

        String scoreString = String.valueOf(score);

        //Seteamos los datos

        holder.NombreJugador.setText(nombres);
        holder.CorreoJugador.setText(correo);
        holder.Puntuacion.setText(scoreString);
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {  //El método hereda de RecyclerView para hacer una lista dinámica para nuestras puntuaciones

        ImageView ImagenJugador;
        TextView NombreJugador, CorreoJugador, Puntuacion;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //Inicializamos variables del XML

            ImagenJugador = itemView.findViewById(R.id.ImagenJugador);
            NombreJugador = itemView.findViewById(R.id.NombreJugador);
            CorreoJugador = itemView.findViewById(R.id.CorreoJugador);
            Puntuacion = itemView.findViewById(R.id.Puntuacion);


        }
    }
}
