package com.kishan.exames;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_about);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription(getString(R.string.about))
                .addItem(new Element().setTitle("Version: BETA-1.2"))
                .addGroup("Connect with us")
                .addEmail("mailto://kishan_jadav@hotmail.com")
                .addPlayStore("com.kishan.exames")
                .addGitHub("kishannareshpal/ExamesGithub")
                .addItem(new Element().setTitle("Licences").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder da = new AlertDialog.Builder(AboutActivity.this)
                                .setTitle("LICENSES:")
                                .setMessage(getString(R.string.Licenses));
                        da.show();
                    }
                }))
                .addItem(getCopyRightsElement())
                .create();
        setContentView(aboutPage);




    }

    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        copyRightsElement.setTitle("Copyright © - 2017 Kishan Nareshpal Jadav");
        copyRightsElement.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, "Thank you for downloading this app.", Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

}
