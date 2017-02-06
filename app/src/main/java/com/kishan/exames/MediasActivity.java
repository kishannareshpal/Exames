package com.kishan.exames;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;

import mehdi.sakout.fancybuttons.FancyButton;


public class MediasActivity extends AppCompatActivity {

    public FancyButton btDez, btDoze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_medias);


        //Initializing elements:
        btDez = (FancyButton) findViewById(R.id.btDez);
        btDoze = (FancyButton) findViewById(R.id.btDoze);

        //Setting Action for the Buttons (10a Classe && 12a Classe):
        btDez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent executeMedia10 = new Intent(MediasActivity.this, media10Activity.class);
                startActivity(executeMedia10);
            }
        });


        btDoze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent executeMedia12 = new Intent(MediasActivity.this, media12Activity.class);
                startActivity(executeMedia12);
            }
        });





    }
}