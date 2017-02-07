package com.kishan.exames;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;

public class media12Activity extends AppCompatActivity {

    EditText et12a12, et11a12;
    TextView tvResult12;
    FancyButton btCalculate12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_media12);

        // initialize views:
        et12a12 = (EditText) findViewById(R.id.et12a12);
        et11a12 = (EditText) findViewById(R.id.et11a12);
        btCalculate12 = (FancyButton) findViewById(R.id.btCalculate12);
        tvResult12 = (TextView) findViewById(R.id.tvResult12);


        //Formatting the button text programatically:
        btCalculate12.getTextViewObject().setText(fromHtml("<b>" + "<i>" + " Calcular > " + "</i>"+ "</b>"));


        // on the button click:
        // 1st retrieve the values;
        // 2nd do the math;
        // finally show the result on-screen.
        btCalculate12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Hide the keyboard
                hideKeyboard(v);

                //Check if teh user typed anything:
                if(et11a12.getText().toString().equals("") | et12a12.getText().toString().equals("")){
                    // he didn't type a thing and clicked the "Calcular >" button :/
                    //Show teh error toast:
                    Toast.makeText(media12Activity.this, "Insira todas as médias por favor.", Toast.LENGTH_SHORT).show();
                }else{

                    // get teh values from the editText:
                    int eleven = Integer.parseInt(et11a12.getText().toString());
                    int twelve = Integer.parseInt(et12a12.getText().toString());

                    // perform the math:
                    int mediafinal = ((twelve*2)+eleven)/3;
                    String stMediaFinal = String.valueOf(mediafinal);

                    // show teh result :P
                    // but we must change the color to RED (if it's low mark) or to GREEN (if it's high)
                    if(mediafinal < 9){
                        // if it's low mark:
                        tvResult12.setText(stMediaFinal);
                        tvResult12.setTextColor(Color.RED);
                    }else{
                        // if it's a high mark:
                        tvResult12.setText(stMediaFinal);
                        tvResult12.setTextColor(Color.WHITE);
                    }

                }
            }
        });

        //Hide the keyboard.
        et11a12.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        //Hide the Keyboard.
        et12a12.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


    }

    //Setting the hideKeyboard method.
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
