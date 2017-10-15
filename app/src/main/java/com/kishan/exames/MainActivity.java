package com.kishan.exames;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamitechetan.flowinggradient.FlowingGradientClass;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.util.regex.Pattern;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {

    public FancyButton btExames, btMeusExames, btGetBack;
    private RelativeLayout re1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        //calling the background for flowing-gradient:
        re1 = (RelativeLayout) findViewById(R.id.activity_selection);
        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onRelativeLayout(re1)
                .setTransitionDuration(4000);

        //check shared Preference (onclick changes teh background)
        SharedPreferences sharedPreferences = getSharedPreferences("gradToggle", MODE_PRIVATE);

        if(sharedPreferences.getString("check", "").equals("true")){
            //enable gradient
            grad.start();

        } else if (sharedPreferences.getString("check", "").equals("false")){
            //gradient is disabled
        }


        //calling Functions:
        btExames = (FancyButton) findViewById(R.id.btExames);
        btGetBack = (FancyButton) findViewById(R.id.btGetBack);
        btMeusExames = (FancyButton) findViewById(R.id.btMeusExames);

        //Formatting the button text programatically:
        btExames.getTextViewObject().setText(fromHtml("<b>" + "<b>" + getString(R.string.btExams) + "</b>" + "</b>"));
        btMeusExames.getTextViewObject().setText(fromHtml("<b>" + "<b>" + getString(R.string.btMeusExames) + "</b>" + "</b>"));


        btExames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent executeExamesActivity = new Intent(MainActivity.this, ExamesActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, btExames ,"transExames");
                startActivity(executeExamesActivity, optionsCompat.toBundle());
            }
        });

        btMeusExames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        openFileBrowser();
            }
        });


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            btGetBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }

    }

    //Text Formatting method
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void openFileBrowser() {

        //Selecting the "EXAMES_APP" Folder as default
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Exames-App/");
        String directoryy = dir.toString();

        //Giving the FilePicker a custom Title:
        String title = "Selecione um dos Exames";

        new MaterialFilePicker()
                .withActivity(MainActivity.this)
                .withRequestCode(1)
                .withFilter(Pattern.compile(".*\\.pdf$"))
                .withFilterDirectories(false) // Set directories filterable (false by default)
                .withHiddenFiles(false) // Show hidden files and folders
                .withRootPath(directoryy)
                .withTitle(title)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            File root = android.os.Environment.getExternalStorageDirectory();
            File dire = new File(root.getAbsolutePath() + "/Exames-App/Guias");
            String guiaPDFDir = dire.toString();

            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            String guiaPath = "/Guia-" + filePath.substring(filePath.lastIndexOf("/") + 1);

            String finalGuiaPath = guiaPDFDir + guiaPath;

            Log.v("TAG", "guiaPath: " + guiaPath);
            Log.v("TAG", "guiaPDFDir: " + guiaPDFDir);

            Log.v("TAG", "finalGuiaPath: " + finalGuiaPath);
            // Do anything with filePath, that is, file.

            Intent executeExamesGo = new Intent(MainActivity.this, ExamesGoActivity.class);
            executeExamesGo.putExtra("FILE_PDF", filePath);
            executeExamesGo.putExtra("GUIA_PDF", finalGuiaPath);
            startActivity(executeExamesGo);
        } else if (requestCode == 0 && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Whoops! Ocorreu um erro.", Toast.LENGTH_SHORT).show();
        }

    }
}
