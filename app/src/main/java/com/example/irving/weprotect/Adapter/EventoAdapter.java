package com.example.irving.weprotect.Adapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.irving.weprotect.EventDetail;
import com.example.irving.weprotect.R;
import com.example.irving.weprotect.models.Evento;

import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private List<Evento> eventoList;
    private Context context;
    private String nombre;

    public EventoAdapter(List<Evento> eventoList, Context context) {
        this.eventoList = eventoList;
        this.context = context;

    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_noticia, parent, false);
        return new EventoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, final int position) {
        holder.evento.setText(eventoList.get(position).getNombreEvento());
        holder.lugar.setText(eventoList.get(position).getLugarEvento());
        holder.fechaHora.setText(eventoList.get(position).getFechaHora());
        holder.descripcion.setText(less(eventoList.get(position).getDescripcion()));
        holder.nombre.setText(eventoList.get(position).getNombre());

        if (eventoList.get(position).getImagen() == null){
            holder.imagen.setVisibility(View.GONE);
        }
        else{
            holder.imagen.setImageBitmap(eventoList.get(position).getImagen());
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetail.class);
                intent.putExtra("evento",eventoList.get(position));
                ((Activity)context).startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder{

        public TextView evento, lugar, fechaHora,descripcion, nombre;
        public ImageView imagen;
        public CardView cardView;

        public EventoViewHolder(final View itemView) {

            super(itemView);
            evento = itemView.findViewById(R.id.txt_evento);
            lugar = itemView.findViewById(R.id.txt_lugar);
            fechaHora = itemView.findViewById(R.id.txt_fecha_hora);
            descripcion = itemView.findViewById(R.id.txt_descripcion);
            imagen = itemView.findViewById(R.id.ic_foto);
            cardView = itemView.findViewById(R.id.cardview_noticia);
            nombre = itemView.findViewById(R.id.txt_user_name);

        }
    }
    private String less(String descripcion){
        if (descripcion.length() > 150){
            return descripcion.substring(0,150) + "...";
        }
        else{
            return descripcion;
        }
    }
}
