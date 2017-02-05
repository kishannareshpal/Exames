package com.kishan.exames;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class WelcomeActivity extends Activity {


    public Button btGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

            //calling functions:
            btGetStarted = (Button) findViewById(R.id.btGetStarted);

            //On Avançar button clicked, open SelectionActivity:
            btGetStarted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent executeSelection = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(executeSelection);
                }
            });
        }



    }


