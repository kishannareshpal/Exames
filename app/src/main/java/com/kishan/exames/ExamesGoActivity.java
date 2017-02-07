package com.kishan.exames;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ExamesGoActivity extends AppCompatActivity {

    public PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_exames_go);

        // init the PDFView:
        pdfView = (PDFView) findViewById(R.id.pdfView);

        // Get the file choosen/selected from the "MaterialFilePicker_LIBRARY" on ExamesActivity:
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
                        .load();

                Toast.makeText(ExamesGoActivity.this, "exame aberto.", Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Whoops, o exame parece estar corrumpido. Tente baixar denovo.", Toast.LENGTH_LONG).show();
            }




    }

}
