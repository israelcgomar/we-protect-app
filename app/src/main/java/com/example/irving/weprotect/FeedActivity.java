package com.example.irving.weprotect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.irving.weprotect.Adapter.EventoAdapter;
import com.example.irving.weprotect.models.Evento;
import com.example.irving.weprotect.models.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<Evento> eventos = new ArrayList<>();
    EventoAdapter adapter;
    RequestQueue requestQueue;
    Singleton singleton;
    Boolean refresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        fab = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.rv_feed);


        singleton = Singleton.getInstance(this);
        requestQueue = singleton.getmRequestQueue();

        recyclerView.setHasFixedSize(true);
        getData();




    }

    public void onClickFab(View v){
        Intent intent = new Intent(this,NuevoPost.class);
        startActivityForResult(intent,1);
    }

    private void getData(){
        String url = "http://159.89.155.107:8001/api/v1/post";
        final StringRequest stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("post");
                    if (refresh)
                        eventos.clear();
                    for (int i = 0; i < jsonArray.length(); i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        eventos.add(new Evento(
                                jsonObject.getString("title"),
                                jsonObject.getString("place"),
                                jsonObject.getString("createdAt"),
                                jsonObject.getString("description"),
                                null,
                                jsonObject.getString("name")
                                ));
                    }
                    if (refresh){
                        adapter.notifyDataSetChanged();
                        refresh = false;
                    }
                    else{
                        adapter = new EventoAdapter(eventos, FeedActivity.this);
                        recyclerView.setLayoutManager(new LinearLayoutManager(FeedActivity.this));
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);

                    }
                });


        requestQueue.add(stringRequest);



        /*
        eventos.add(new Evento("Asalto","Cerca de Bodas de Fígaro","13:45 24/02/2018","Hola, sólo para reportar que hace un rato asaltaron a mi hermano en Bodas de fígaro. Eran tres weyes, uno de sudadera azul, uno de sudadera roja y al otro no lo vio bien. \"casualmente\" al del mototaxi en que iba \"se le ponchó\" la llanta por lo que tuvo que bajarse y un poco más adelante lo agarraron y le sacaron todo (o bueno, la mochila con sus apuntes que a esos analfabetas no les vaa servir de nada y un celular). Para que se anden con cuidado en la Miguel Hidalgo y más con los mototaxis que ahora ya no me parecen tan confiables.",
        null));
        eventos.add(new Evento("Pregunta","Canal","13:40 24/02/2018","Acaban de encontrar 3 personas muertas cerca del canal que divide santa Cecilia y la Selene Algún dato ?",null));
        eventos.add(new Evento("Auto abandonado", "Calle Sirenas", "14:45 23/08/2019", "Este vehículo lleva 3 días abandonado. Placas AFG543", null));
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK){
            refresh = true;
            getData();

        }
    }


}
