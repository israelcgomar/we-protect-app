package com.example.irving.weprotect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.irving.weprotect.models.Singleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NuevoPost extends AppCompatActivity implements OnMapReadyCallback {

    Toolbar toolbar;
    private GoogleMap mMap;
    MarkerOptions marker;
    LatLng position;
    ImageView imageView;
    TextView title, descripcion;

    RequestQueue requestQueue;
    Singleton singleton;
    String route = "";

    final int INDEX_CAMARA = 0;
    final int INDEX_GALERIA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_post);
        toolbar = findViewById(R.id.toolbar_new_post);
        imageView = findViewById(R.id.ic_camara);

        title = findViewById(R.id.edtxt_title);
        descripcion = findViewById(R.id.edtxt_decripcion);

        setSupportActionBar(toolbar);

        singleton = Singleton.getInstance(this);
        requestQueue = singleton.getmRequestQueue();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        new Runnable() {
            @Override
            public void run() {
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(NuevoPost.this);
            }
        }.run();



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
                    @Override
                    public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {



                        if (index == INDEX_CAMARA)
                        {
                            Intent gallery = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(gallery, INDEX_CAMARA);
                            dialog.dismiss();
                        }
                        else {
                            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            startActivityForResult(gallery, INDEX_GALERIA);
                            dialog.dismiss();
                        }
                    }
                });
                final int paddiginIcon = 5;
                adapter.add(new MaterialSimpleListItem.Builder(NuevoPost.this)
                        .content("Tomar foto")
                        .icon(R.drawable.ic_camera_alt)
                        .iconPaddingDp(paddiginIcon)
                        .backgroundColor(getResources().getColor(R.color.whiteColor))
                        .build());
                adapter.add(new MaterialSimpleListItem.Builder(NuevoPost.this)
                        .content("Elegir foto")
                        .icon(R.drawable.ic_galeria)
                        .iconPaddingDp(paddiginIcon)
                        .backgroundColor(getResources().getColor(R.color.whiteColor))
                        .build());
                MaterialDialog dialog = new MaterialDialog.Builder(NuevoPost.this)
                        .adapter(adapter, null)
                        .show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        GoogleMap.OnMarkerDragListener listener = new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                NuevoPost.this.marker.position(marker.getPosition());
                System.out.println(marker.getPosition().toString());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),15));
            }
        };

        mMap.setOnMarkerDragListener(listener);

        position = new LatLng(19.403251, -99.138531);
        marker = new MarkerOptions().position(position).title("Lugar de los hechos").draggable(true);
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,15));


    }

    public void onClickAceptar(View view){
        if (title.getText().toString().isEmpty()){
            Snackbar.make(view,"El titulo no puede estar vacío",Snackbar.LENGTH_SHORT).show();
        }
        else if (descripcion.getText().toString().isEmpty()){
            Snackbar.make(view,"La descripción no puede estar vacía",Snackbar.LENGTH_SHORT).show();
        }
        else{
            geocoding();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Snackbar.make(toolbar,"Imagen cargada con éxito",Snackbar.LENGTH_SHORT).show();
        /*
        if(resultCode == RESULT_OK && requestCode == INDEX_GALERIA){

            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String filePath = cursor.getString(columnIndex);
            cursor.close();

            File file = new File(filePath);
            file = saveBitmapToFile(file);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image"), file.getAbsoluteFile());
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
            String auth = getString(R.string.bearer) + " " + usuario.getAccessToken();
            String url = "/api/v1/user/"+ usuario.getUser() + getString(R.string.url_foto);

            final retrofit2.Call<okhttp3.ResponseBody> req = imageService.postImage(body, auth, url);
            req.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    final int CREATED_CODE = 201;
                    if (response.code() == CREATED_CODE){
                        Toast.makeText(AdmonCuentaActivity.this, "Su imagen ha sido subida exitosamente.", Toast.LENGTH_SHORT).show();
                        Log.e("----->", "Imagen OK");
                        Log.e("response", response.message());
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                        foto.setImageBitmap(bitmap);
                        cambioFoto = true;
                    }
                    else
                    {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());

                            String respuesta = jsonObject.getString("message");
                            Toast.makeText(AdmonCuentaActivity.this, respuesta, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    Log.e("----->", "ERROR al subir imagen");
                }
            });
            AuthToken.refreshToken(this,requestQueue);


        }
        else if (resultCode == RESULT_OK && requestCode == INDEX_CAMARA)
        {
            final Bitmap bitmap = data.getParcelableExtra("data");
            File file = bitmapToFile(bitmap);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image"), file.getAbsoluteFile());
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
            String auth = getString(R.string.bearer) + " " + usuario.getAccessToken();
            String url = "/api/v1/user/"+ usuario.getUser() + getString(R.string.url_foto);

            final retrofit2.Call<okhttp3.ResponseBody> req = imageService.postImage(body, auth, url);
            req.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    final int CREATED_CODE = 201;
                    if (response.code() == CREATED_CODE){
                        Toast.makeText(AdmonCuentaActivity.this, "Su imagen ha sido subida exitosamente.", Toast.LENGTH_SHORT).show();
                        Log.e("----->", "Imagen OK");
                        Log.e("response", response.message());
                        foto.setImageBitmap(bitmap);
                        cambioFoto = true;
                    }
                    else
                    {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());

                            String respuesta = jsonObject.getString("message");
                            Toast.makeText(AdmonCuentaActivity.this, respuesta, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    Log.e("----->", "ERROR al subir imagen");
                }
            });
            AuthToken.refreshToken(this,requestQueue);
        }
        */
    }

    private void subirPost(){

        String url = "http://159.89.155.107:8001/api/v1/post";
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        setResult(RESULT_OK);
                        NuevoPost.this.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("title",title.getText().toString());
                map.put("description",descripcion.getText().toString());
                map.put("length",String.valueOf(marker.getPosition().longitude));
                map.put("latitude",String.valueOf(marker.getPosition().latitude));
                map.put("place",route);
                map.put("date","12/04/2018 04:30");
                return map;
            }
        };
        requestQueue.add(request);

    }

    private void geocoding(){
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                String.valueOf(marker.getPosition().latitude) +
                "," +
                String.valueOf(marker.getPosition().longitude) +
                "&key=AIzaSyAtAoa55JZ1BHPaR76YszDJvucQsLFtR7U";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            jsonObject = jsonArray.getJSONObject(0);
                            jsonArray = jsonObject.getJSONArray("address_components");
                            jsonObject =jsonArray.getJSONObject(1);
                            route = jsonObject.getString("long_name");
                            System.out.println(route);
                            subirPost();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            subirPost();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(request);

    }

}
