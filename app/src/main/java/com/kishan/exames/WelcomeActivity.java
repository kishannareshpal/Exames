package com.kishan.exames;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dynamitechetan.flowinggradient.FlowingGradientClass;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import mehdi.sakout.fancybuttons.FancyButton;


public class WelcomeActivity extends AppCompatActivity {

    public FancyButton btAbout, btGetStarted;
    public RelativeLayout r1;

    //Serious money (ad) business. No, really, this is serious! Not sure why i didn't initialized it... hm!
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_welcome);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //Setup AdMOB
        MobileAds.initialize(WelcomeActivity.this, "ca-app-pub-2181029585862550/9588862026");

        // Initialize AdviewBanner and start networking.
        AdView adView = (AdView) findViewById(R.id.adVieww);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

            //Call the functions:
            btGetStarted = (FancyButton) findViewById(R.id.btGetStarted);
            btAbout = (FancyButton) findViewById(R.id.btAbout);


            //Call the background for flowing-gradient:
            r1 = (RelativeLayout) findViewById(R.id.r1);
            FlowingGradientClass grad = new FlowingGradientClass();
            grad.setBackgroundResource(R.drawable.translate)
                    .onRelativeLayout(r1)
                    .setTransitionDuration(4000);

            //check shared Preference (onclick changes teh background)
            SharedPreferences sharedPreferences = getSharedPreferences("gradToggle", MODE_PRIVATE);

            if(sharedPreferences.getString("check", "").equals("true")){
                //enable gradient
                grad.start();

            } else if (sharedPreferences.getString("check", "").equals("false")){
                //gradient is disabled
            }


            //Formatting the button text programatically:
            btAbout.getTextViewObject().setText(fromHtml("<b>" + getString(R.string.aboutbutton) + "</b>"));
            btGetStarted.getTextViewObject().setText(fromHtml("<b>" + getString(R.string.btGetStarted) + "</b>"));


            //On Avançar (>) button clicked, open SelectionActivity:
            btGetStarted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent executeSelection = new Intent(WelcomeActivity.this, MainActivity.class);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(WelcomeActivity.this, btGetStarted ,"trans");
                    startActivity(executeSelection, optionsCompat.toBundle());


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

    //Back button action setter:
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Clique novamente para sair", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

       /* super.onBackPressed();*/
    }
}


