package com.kishan.exames;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class media10Activity extends AppCompatActivity {

    // declaring views:
    Button btCalculate10;
    TextView tvResult10;
    EditText et8a10, et9a10, et10a10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_media10);

        // calling the views:
        btCalculate10 = (Button) findViewById(R.id.btCalculate10);
        tvResult10 = (TextView) findViewById(R.id.tvResult10);
        et8a10 = (EditText) findViewById(R.id.et8a10);
        et9a10 = (EditText) findViewById(R.id.et9a10);
        et10a10 = (EditText) findViewById(R.id.et10a10);


        // on the button click:
        // 1st retrieve the values;
        // 2nd do the math;
        // finally show the result on-screen.
        btCalculate10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide the Keyboard
                hideKeyboard(v);

                //Check if teh user typed anything:
                if(et8a10.getText().toString().equals("") |
                   et9a10.getText().toString().equals("") |
                   et10a10.getText().toString().equals(""))
                  {
                    // he didn't type a thing and clicked the "Calcular >" button :/
                    //Show teh error toast:
                    Toast.makeText(media10Activity.this, "Insira todas as médias por favor.", Toast.LENGTH_SHORT).show();
                }else {

                    // getting teh values from the EditText:
                    int eight = Integer.parseInt(et8a10.getText().toString());
                    int nine = Integer.parseInt(et9a10.getText().toString());
                    int ten = Integer.parseInt(et10a10.getText().toString());

                    // do teh math:
                    int mediafinal = ((eight + nine) + (2 * ten)) / 4;
                    String stMediaFinal = String.valueOf(mediafinal);

                    // show teh result :P
                    // but we must change the color to RED (if it's low mark) or to GREEN (if it's high)
                    if (mediafinal < 9) {
                        // if it's low mark:
                        tvResult10.setText(stMediaFinal);
                        tvResult10.setTextColor(Color.RED);
                    } else {
                        // if it's a high mark:
                        tvResult10.setText(stMediaFinal);
                        tvResult10.setTextColor(Color.WHITE);
                    }
                }
            }
        });


        //Hide the keyboard
        et8a10.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        //Hide the keyboard
        et9a10.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        //Hide the keyboard
        et10a10.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


    }
    //setting the hideKeyboard method:
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
