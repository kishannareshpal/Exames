package com.kishan.exames;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import mehdi.sakout.fancybuttons.FancyButton;


public class WelcomeActivity extends AppCompatActivity {


    public FancyButton btAbout, btGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

            //calling functions:
            btGetStarted = (FancyButton) findViewById(R.id.btGetStarted);
            btAbout = (FancyButton) findViewById(R.id.btAbout);

            //Formatting the button text programatically:
            btAbout.getTextViewObject().setText(fromHtml("<b>" + getString(R.string.aboutbutton) + "</b>"));
            btGetStarted.getTextViewObject().setText(fromHtml("<b>" + getString(R.string.btGetStarted) + "</b>"));


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


    //Text Formatting stuff
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }



    }


