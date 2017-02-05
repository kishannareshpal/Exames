package com.kishan.exames;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public Button btExames, btPautas;
    public TextView tvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        //calling Functions:
        btExames = (Button) findViewById(R.id.btExames);
        btPautas = (Button) findViewById(R.id.btPautas);
        tvAbout = (TextView) findViewById(R.id.tvAbout);

        btExames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent executeExamesActivity = new Intent(MainActivity.this, ExamesActivity.class);
                startActivity(executeExamesActivity);
            }
        });

        btPautas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent executePautasActivity = new Intent(MainActivity.this, MediasActivity.class);
                startActivity(executePautasActivity);
            }
        });


        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent executeAboutActivity = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(executeAboutActivity);
            }
        });

    }
}
