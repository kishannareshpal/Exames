package com.kishan.exames;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;


public class MediasActivity extends AppCompatActivity {

    private FancyButton btDez, btDoze;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_medias);


        //Initializing elements:
        btDez = (FancyButton) findViewById(R.id.btDez);
        btDoze = (FancyButton) findViewById(R.id.btDoze);

        btDez.setVisibility(View.GONE);
        btDoze.setVisibility(View.GONE);
//
//        //Formatting the button text programatically:
//        btDez.getTextViewObject().setText(fromHtml("<b>" + getString(R.string.Media10a) + "</b>"));
//        btDoze.getTextViewObject().setText(fromHtml("<b>" + getString(R.string.Media12a) + "</b>"));
//
//        //Setting Action for the Buttons (10a Classe && 12a Classe):
//        btDez.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent executeMedia10 = new Intent(MediasActivity.this, media10Activity.class);
//                startActivity(executeMedia10);
//            }
//        });
//
//
//        btDoze.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent executeMedia12 = new Intent(MediasActivity.this, media12Activity.class);
//                startActivity(executeMedia12);
//            }
//        });

        tv = (TextView) findViewById(R.id.textView4);
        tv.setText("Este serviço está temporariamente indisponível.");




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