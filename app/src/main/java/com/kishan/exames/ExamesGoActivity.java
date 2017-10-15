package com.kishan.exames;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sothree.slidinguppanel.ScrollableViewHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;


public class ExamesGoActivity extends AppCompatActivity {

    public PDFView pdfView, pdfPanel;
    private SlidingUpPanelLayout slideUp;
    private TextView pushtext, notice;
    private LinearLayout lere;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_exames_go);

        mInterstitialAd = new InterstitialAd(ExamesGoActivity.this);
        mInterstitialAd.setAdUnitId(getString(R.string.intersticial_adId));
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else{
                    Log.d("adMan", "woah, intersticial ad was not loaded man!");
                }
            }
        });

        // init the PDFView:
        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfPanel = (PDFView) findViewById(R.id.pdfPanel);
        slideUp = (SlidingUpPanelLayout) findViewById(R.id.activity_exames_go);
        pushtext = (TextView) findViewById(R.id.dragView);
        notice = (TextView) findViewById(R.id.tvNotice);
        lere = (LinearLayout) findViewById(R.id.lere);

        notice.setVisibility(View.GONE);


        // Get the file from "MaterialFilePicker_LIBRARY" to the Main PDF View (pdfView):
        Intent intent = getIntent();
        String filePath = intent.getStringExtra("FILE_PDF");


        // Show the PDFFile via PDFView:
        FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(filePath);

                pdfView.fromStream(fileInputStream)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .defaultPage(0)
                        .enableAnnotationRendering(false)
                        .onLoad(new OnLoadCompleteListener() {
                            @Override
                            public void loadComplete(int nbPages) {
                                Toast.makeText(ExamesGoActivity.this, "Aberto com sucesso.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .onError(new OnErrorListener() {
                            @Override
                            public void onError(Throwable t) {
                                Toast.makeText(ExamesGoActivity.this, "Ocorreu um erro. Baixe o enunciado novamente.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .load();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }



        // Get the file from "MaterialFilePicker_LIBRARY" to the SlideUpPanel PDF View (pdfPanel):
        Intent intss = getIntent();
        final String filee = intss.getStringExtra("GUIA_PDF");


        File doesTheFileExist = new File(filee);

        if(doesTheFileExist.exists()){

            FileInputStream fileeinspit = null;
            try {
                fileeinspit = new FileInputStream(filee);

                pdfPanel.fromStream(fileeinspit)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .defaultPage(0)
                        .enableAnnotationRendering(false)
                        .onLoad(new OnLoadCompleteListener() {
                            @Override
                            public void loadComplete(int nbPages) {
                                Toast.makeText(ExamesGoActivity.this, "Guia, acima disponível...", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .onError(new OnErrorListener() {
                            @Override
                            public void onError(Throwable t) {
                                Toast.makeText(ExamesGoActivity.this, "Ocorreu um erro. Baixe a guia novamente.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .load();




            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else if(!doesTheFileExist.exists()){
            // Gets the layout params that will allow you to resize the layout
            pushtext.setText("...");
            notice.setVisibility(View.VISIBLE);
            pdfPanel.setVisibility(View.GONE);
            slideUp.setAnchorPoint(0.5f);
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

            String dd = slideUp.getPanelState().toString();

            if (!dd.equals("COLLAPSED")) {
                slideUp.setPanelState(COLLAPSED);
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Clique novamente para sair", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 1800);

       /* super.onBackPressed();*/
    }

}
