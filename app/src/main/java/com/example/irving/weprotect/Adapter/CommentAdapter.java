package com.example.irving.weprotect.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.irving.weprotect.models.Comentario;
import com.example.irving.weprotect.models.Evento;

import org.w3c.dom.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comentario> commentList;
    private Context context;

    public CommentAdapter(List<Comentario> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, final int position) {
        holder.nombre.setText(commentList.get(position).getNombre());
        holder.comment.setText(commentList.get(position).getComment());
        holder.fechaHora.setText(commentList.get(position).getFecha());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{

        public TextView nombre, comment, fechaHora;

        public CommentViewHolder(final View itemView) {

            super(itemView);
            nombre = itemView.findViewById(R.id.txt_user_name_comment);
            comment = itemView.findViewById(R.id.txt_comment);
            fechaHora = itemView.findViewById(R.id.txt_fecha_hora_comment);

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
