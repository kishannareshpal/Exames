package com.kishan.exames;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

//From the AdMob
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

//from About Page Library
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends Activity {

    //SETTING THE AD, fullscreen_Ad or just Interstitial_ad:
    protected InterstitialAd mInterstitialAd;
    public Boolean isOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        mInterstitialAd = new InterstitialAd(AboutActivity.this);
        mInterstitialAd.setAdUnitId(getString(R.string.intersticial_adId));
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else{
                    Log.d("adMan", "woah, intersticial ad was not loaded dude!");
                }
            }
        });

        //Get the preference
        SharedPreferences sharedPreferences = getSharedPreferences("gradToggle", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription(getString(R.string.about))
                .addItem(new Element().setTitle("Versão do app: 1.5.2"))
                .addItem(new Element().setTitle("Alternar fundo").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder da = new AlertDialog.Builder(AboutActivity.this)
                                .setTitle("Deseja alternar a animação?")
                                .setMessage("Não gosta da animação das cores no fundo?\n•Desligue-a clicando no botão 'desativar'. Para reativar clique no 'ativar'.\n\n(Reinicie o app após a alteração)")
                                .setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        editor.putString("check", "true");
                                        editor.apply();
                                        isOn = true;
                                        Toast.makeText(AboutActivity.this, "Animação ativada com sucesso.\nPor favor, reinicie o app para fazer efeito.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("Desactivar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        editor.putString("check", "false");
                                        editor.apply();
                                        isOn = false;
                                        Toast.makeText(AboutActivity.this, "Animação desativada com sucesso.\nPor favor, reinicie o app para fazer efeito.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        da.show();
                    }
                }).setIcon(R.drawable.about_icon_backgroundchange).setAutoIconColor(false))
                .addGroup("")
                .addItem(new Element().setTitle("www.exames.tk").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Open our website
                        Uri uri = Uri.parse("http://www.exames.tk"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }).setIcon(R.drawable.about_icon_exameslink).setAutoIconColor(false)
                )
                .addFacebook("exames.moz")
                .addPlayStore("com.kishan.exames")
                .addEmail("kishan_jadav@hotmail.com")
                .addGroup("")
                .addItem(new Element().setTitle("Agradecimentos").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder da = new AlertDialog.Builder(AboutActivity.this)
                                .setTitle("Agradecimentos:")
                                .setMessage(getString(R.string.Licenses))
                                .setPositiveButton("Licença indiv.", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/QxN0mI"));
                                        startActivity(browserIntent);
                                    }
                                })
                                .setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        da.show();
                    }
                }).setAutoIconColor(false).setIcon(R.drawable.about_icon_agradecimentos))
                .addItem(getCopyRightsElement())
                .create();
        setContentView(aboutPage);

    }


    private Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        copyRightsElement.setTitle(getString(R.string.copy_right));
        copyRightsElement.setColor(ContextCompat.getColor(this, R.color.greyish_green));
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, "Alguns Direitos Reservados.", Toast.LENGTH_SHORT).show();

                //SHOW AN AD WHEN THE COPY_RIGHT ELEMENT IS CLICKED:
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("adMan", "woah, intersticial ad was not loaded man!");
                        }
                    }
                });


            }
        });
        return copyRightsElement;
    }

}