package com.kishan.exames;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import mehdi.sakout.fancybuttons.FancyButton;


public class WelcomeActivity extends AppCompatActivity {


    public Button btGetStarted;
    public FancyButton btAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

            //calling functions:
            btGetStarted = (Button) findViewById(R.id.btGetStarted);
            btAbout = (FancyButton) findViewById(R.id.btAbout);

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                btGetStarted.setTextColor(Color.BLACK);
            }

            //On Avançar button clicked, open SelectionActivity:
            btGetStarted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent executeSelection = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(executeSelection);
                }
            });

            //On Sobre Button clicked, open AboutActivity:
            btAbout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent executeAboutActivity = new Intent(WelcomeActivity.this, AboutActivity.class);
                    startActivity(executeAboutActivity);
                }
            });
        }



    }


