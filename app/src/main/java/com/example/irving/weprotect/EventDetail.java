package com.example.irving.weprotect;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.irving.weprotect.Adapter.CommentAdapter;
import com.example.irving.weprotect.Adapter.EventoAdapter;
import com.example.irving.weprotect.models.Comentario;
import com.example.irving.weprotect.models.Evento;
import com.example.irving.weprotect.models.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDetail extends AppCompatActivity {

    private Evento evento;
    Toolbar toolbar;
    List<Comentario> lista = new ArrayList<>();
    CommentAdapter commentAdapter;
    RecyclerView recyclerView;
    EditText comentario;
    Boolean refresh = false;


    RequestQueue requestQueue;
    Singleton singleton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        evento = (Evento) getIntent().getSerializableExtra("evento");

        toolbar = findViewById(R.id.toolbar_detail);
        comentario = findViewById(R.id.edtxt_comment);
        toolbar.setTitle(evento.getNombreEvento());
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv_comments);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        singleton = Singleton.getInstance(this);
        requestQueue = singleton.getmRequestQueue();

        recyclerView.setHasFixedSize(true);
        getComments();

    }


    private void getComments(){
        String url = "http://159.89.155.107:8001/api/v1/comment";
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("comments");
                            for (int i = 0; i < jsonArray.length(); i++){
                                jsonObject = jsonArray.getJSONObject(i);
                                lista.add(new Comentario(
                                        jsonObject.getString("name"),
                                        jsonObject.getString("comment"),
                                        jsonObject.getString("createdAt")
                                ));
                            }

                            if (refresh){
                                commentAdapter.notifyDataSetChanged();
                                refresh= false;
                            }
                            else {
                                commentAdapter = new CommentAdapter(lista, EventDetail.this);
                                recyclerView.setLayoutManager(new LinearLayoutManager(EventDetail.this));
                                recyclerView.setAdapter(commentAdapter);
                                commentAdapter.notifyDataSetChanged();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        requestQueue.add(stringRequest);

    }

    public void newComment(View v){
        if (comentario.getText().toString().isEmpty()){
            Snackbar.make(v,"Escribe un comentario",Snackbar.LENGTH_SHORT).show();
        }
        else{
            lanzaPeticion();

        }
    }

    private void lanzaPeticion(){
        String url = "http://159.89.155.107:8001/api/v1/comment";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Snackbar.make(toolbar,"Comentario añadido",Snackbar.LENGTH_SHORT).show();
                        lista.clear();
                        refresh = true;
                        getComments();
                        comentario.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        comentario.setText("");

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("comment",comentario.getText().toString());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
