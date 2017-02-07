package com.kishan.exames;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
                .addItem(new Element().setTitle("Version: BETA-1.2"))
                .addGroup("Connect with us")
                .addEmail("kishan_jadav@hotmail.com")
                .addPlayStore("com.kishan.exames")
                .addGitHub("ExamesGithub")
                .create();
        setContentView(aboutPage);




    }
}
