package com.example.irving.weprotect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class Presentacion extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);

        addSlide(new SlideFragmentBuilder()
        .image(R.drawable.ic_alarm)
        .title("Notifica riesgos para tu comunidad").build());
    }
}
