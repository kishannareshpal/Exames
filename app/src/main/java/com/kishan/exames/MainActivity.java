package com.kishan.exames;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public Button btExames, btMedias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        //calling Functions:
        btExames = (Button) findViewById(R.id.btExames);
        btMedias = (Button) findViewById(R.id.btMedias);

        //set the button text color to BLACK IF running Less then Lollipop.
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for versions bellow lollipop
            btExames.setTextColor(Color.BLACK);
            btMedias.setTextColor(Color.BLACK);
        }


        btExames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent executeExamesActivity = new Intent(MainActivity.this, ExamesActivity.class);
                startActivity(executeExamesActivity);
            }
        });

        btMedias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent executePautasActivity = new Intent(MainActivity.this, MediasActivity.class);
                startActivity(executePautasActivity);
            }
        });

    }
}
