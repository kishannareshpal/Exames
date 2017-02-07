package com.kishan.exames;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {

    public FancyButton btExames, btMedias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        //calling Functions:
        btExames = (FancyButton) findViewById(R.id.btExames);
        btMedias = (FancyButton) findViewById(R.id.btMedias);

        //Formatting the button text programatically:
        btExames.getTextViewObject().setText(fromHtml("<b>" + "<b>" + getString(R.string.btExams) + "</b>" + "</b>"));
        btMedias.getTextViewObject().setText(fromHtml("<b>" + "<b>" + getString(R.string.btMedias) + "</b>" + "</b>"));


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

    //Text Formatting method
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

}
