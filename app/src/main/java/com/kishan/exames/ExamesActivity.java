package com.kishan.exames;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dynamitechetan.flowinggradient.FlowingGradientClass;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.util.regex.Pattern;

import mehdi.sakout.fancybuttons.FancyButton;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class ExamesActivity extends AppCompatActivity {
    private RelativeLayout rel1;
    private TextView tvInfo;
    public Spinner spClasse, spAno, spDisciplina, spEpoca;
    private ArrayAdapter adapterClasse,
            adapterDisc10,
            adapterDisc12,
            adapterEpoca,
            adapterNull;

    //adapter Anos
    private ArrayAdapter aBio10,
            aBio12,
            aDes12,
            aFil12,
            aMat10,
            aMat12,
            aPort10,
            aPort12,
            aFis10,
            aFis12,
            aFran12,
            aIng10,
            aIng12,
            aGeog10,
            aGeog12,
            aHist10,
            aHist12,
            aQuim10,
            aQuim12;

    //adapter Epocas
    private ArrayAdapter onlyFirst,
            onlySeccond;

    private ArrayAdapter onlyDecima, onlyDecimaSegunda;

    public Firebase mRef;
    private StorageReference mStorageRef, pdfRef, guiasRef;
    private FancyButton bDownload, bLook, btGetBackTo;
    private File localFile, localguiaFile;
    public FileDownloadTask downloadTask;

    public ProgressDialog progressDialog;

    private String Classe, Epoca, Disciplina, Ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check shared Preference (onclick changes teh background)
        SharedPreferences sharedPreferences = getSharedPreferences("gradToggle", MODE_PRIVATE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_exames);

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        //calling the background for flowing-gradient:
        rel1 = (RelativeLayout) findViewById(R.id.activity_exames);
        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onRelativeLayout(rel1)
                .setTransitionDuration(3000);



        if(sharedPreferences.getString("check", "").equals("true")){
            //enable gradient
            grad.start();

        } else if (sharedPreferences.getString("check", "").equals("false")){
            //gradient is disabled
        }


        //Calling functions:
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        bLook = (FancyButton) findViewById(R.id.bLook);
        spClasse = (Spinner) findViewById(R.id.spClasse);
        spAno = (Spinner) findViewById(R.id.spAno);
        spDisciplina = (Spinner) findViewById(R.id.spDisciplina);
        spEpoca = (Spinner) findViewById(R.id.spEpoca);
        mRef = new Firebase("https://examesproject.firebaseio.com/Anos");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        bDownload = (FancyButton) findViewById(R.id.bDownload);

        final ExamesActivity temp = this;
        progressDialog = new ProgressDialog(this);


        //Formatting the button text programatically:
        bLook.getTextViewObject().setText(fromHtml("<b>" + getString(R.string.Look) + "</b>"));
        bDownload.getTextViewObject().setText(fromHtml("<b>" + getString(R.string.DownloadButton) + "</b>"));

        //Calling Adapter:
        adapterClasse = ArrayAdapter.createFromResource(this, R.array.arClasse, android.R.layout.simple_spinner_dropdown_item);
        adapterDisc10 = ArrayAdapter.createFromResource(this, R.array.arDisciplina10, android.R.layout.simple_spinner_dropdown_item);
        adapterDisc12 = ArrayAdapter.createFromResource(this, R.array.arDisciplina12, android.R.layout.simple_spinner_dropdown_item);
        adapterEpoca = ArrayAdapter.createFromResource(this, R.array.arEpoca, android.R.layout.simple_spinner_dropdown_item);
        adapterNull = ArrayAdapter.createFromResource(this, R.array.arNull, android.R.layout.simple_spinner_dropdown_item);

        aBio10 = ArrayAdapter.createFromResource(this, R.array.arBio10, android.R.layout.simple_spinner_dropdown_item);
        aBio12 = ArrayAdapter.createFromResource(this, R.array.arBio12, android.R.layout.simple_spinner_dropdown_item);
        aDes12 = ArrayAdapter.createFromResource(this, R.array.arDes12, android.R.layout.simple_spinner_dropdown_item);
        aFil12 = ArrayAdapter.createFromResource(this, R.array.arFil12, android.R.layout.simple_spinner_dropdown_item);
        aMat10 = ArrayAdapter.createFromResource(this, R.array.arMat10, android.R.layout.simple_spinner_dropdown_item);
        aMat12 = ArrayAdapter.createFromResource(this, R.array.arMat12, android.R.layout.simple_spinner_dropdown_item);
        aPort10 = ArrayAdapter.createFromResource(this, R.array.arPort10, android.R.layout.simple_spinner_dropdown_item);
        aPort12 = ArrayAdapter.createFromResource(this, R.array.arPort12, android.R.layout.simple_spinner_dropdown_item);
        aFis10 = ArrayAdapter.createFromResource(this, R.array.arFis10, android.R.layout.simple_spinner_dropdown_item);
        aFis12 = ArrayAdapter.createFromResource(this, R.array.arFis12, android.R.layout.simple_spinner_dropdown_item);
        aFran12 = ArrayAdapter.createFromResource(this, R.array.arFran12, android.R.layout.simple_spinner_dropdown_item);
        aIng10 = ArrayAdapter.createFromResource(this, R.array.arIng10, android.R.layout.simple_spinner_dropdown_item);
        aIng12 = ArrayAdapter.createFromResource(this, R.array.arIng12, android.R.layout.simple_spinner_dropdown_item);
        aGeog10 = ArrayAdapter.createFromResource(this, R.array.arGeog10, android.R.layout.simple_spinner_dropdown_item);
        aGeog12 = ArrayAdapter.createFromResource(this, R.array.arGeog12, android.R.layout.simple_spinner_dropdown_item);
        aHist10 = ArrayAdapter.createFromResource(this, R.array.arHist10, android.R.layout.simple_spinner_dropdown_item);
        aHist12 = ArrayAdapter.createFromResource(this, R.array.arHist12, android.R.layout.simple_spinner_dropdown_item);
        aQuim10 = ArrayAdapter.createFromResource(this, R.array.arQuim10, android.R.layout.simple_spinner_dropdown_item);
        aQuim12 = ArrayAdapter.createFromResource(this, R.array.arQuim12, android.R.layout.simple_spinner_dropdown_item);

        onlyFirst = ArrayAdapter.createFromResource(this, R.array.onlyFirst, android.R.layout.simple_spinner_dropdown_item);
        onlySeccond = ArrayAdapter.createFromResource(this, R.array.onlySecond, android.R.layout.simple_spinner_dropdown_item);

        onlyDecima = ArrayAdapter.createFromResource(this, R.array.onlyDecima, android.R.layout.simple_spinner_dropdown_item);
        onlyDecimaSegunda = ArrayAdapter.createFromResource(this, R.array.onlyDecimaSegunda, android.R.layout.simple_spinner_dropdown_item);


        // Remove the Padding from the Layout. That is, teh background will look full screen.
        // The Navigation bar and the Status Bar's color/background will match the Layout Background for a Uniform Look.
        // But only on devices with android Lollipop and Above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //Set the Back Button action on the GetBack button:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btGetBackTo = (FancyButton) findViewById(R.id.btGetBackTo);
            btGetBackTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }


        //Setting Adapter:
        spClasse.setAdapter(adapterClasse);
        spEpoca.setAdapter(adapterEpoca);

        //spDisiplina:
        AdapterView.OnItemSelectedListener adapterViewDisciplina = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spClasse.getSelectedItem().equals("10a Classe")) {
                    spDisciplina.setAdapter(adapterDisc10);
                } else if (spClasse.getSelectedItem().equals("12a Classe")) {
                    spDisciplina.setAdapter(adapterDisc12);
                } else {
                    spDisciplina.setAdapter(adapterNull);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spClasse.setOnItemSelectedListener(adapterViewDisciplina);

        //spAno:
        AdapterView.OnItemSelectedListener adapterViewAno = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spDisciplina.getSelectedItem().equals("Biologia")) {
                    if (spClasse.getSelectedItem().equals("10a Classe")) {
                        spAno.setAdapter(aBio10);
                    } else if (spClasse.getSelectedItem().equals("12a Classe")) {
                        spAno.setAdapter(aBio12);
                    } else {
                        spAno.setAdapter(adapterNull);
                    }


                } else if (spDisciplina.getSelectedItem().equals("Desenho")) {
                    spAno.setAdapter(aDes12);


                } else if (spDisciplina.getSelectedItem().equals("Filosofia")) {
                    spAno.setAdapter(aFil12);


                } else if (spDisciplina.getSelectedItem().equals("Matematica")) {
                    if (spClasse.getSelectedItem().equals("10a Classe")) {
                        spAno.setAdapter(aMat10);
                    } else if (spClasse.getSelectedItem().equals("12a Classe")) {
                        spAno.setAdapter(aMat12);
                    } else {
                        spAno.setAdapter(adapterNull);
                    }


                } else if (spDisciplina.getSelectedItem().equals("Portugues")) {
                    if (spClasse.getSelectedItem().equals("10a Classe")) {
                        spAno.setAdapter(aPort10);
                    } else if (spClasse.getSelectedItem().equals("12a Classe")) {
                        spAno.setAdapter(aPort12);
                    } else {
                        spAno.setAdapter(adapterNull);
                    }


                } else if (spDisciplina.getSelectedItem().equals("Fisica")) {
                    if (spClasse.getSelectedItem().equals("10a Classe")) {
                        spAno.setAdapter(aFis10);
                    } else if (spClasse.getSelectedItem().equals("12a Classe")) {
                        spAno.setAdapter(aFis12);
                    } else {
                        spAno.setAdapter(adapterNull);
                    }


                } else if (spDisciplina.getSelectedItem().equals("Frances")) {
                    spAno.setAdapter(aFran12);


                } else if (spDisciplina.getSelectedItem().equals("Ingles")) {
                    if (spClasse.getSelectedItem().equals("10a Classe")) {
                        spAno.setAdapter(aIng10);
                    } else if (spClasse.getSelectedItem().equals("12a Classe")) {
                        spAno.setAdapter(aIng12);
                    } else {
                        spAno.setAdapter(adapterNull);
                    }


                } else if (spDisciplina.getSelectedItem().equals("Geografia")) {
                    if (spClasse.getSelectedItem().equals("10a Classe")) {
                        spAno.setAdapter(aGeog10);
                    } else if (spClasse.getSelectedItem().equals("12a Classe")) {
                        spAno.setAdapter(aGeog12);
                    } else {
                        spAno.setAdapter(adapterNull);
                    }


                } else if (spDisciplina.getSelectedItem().equals("Historia")) {
                    if (spClasse.getSelectedItem().equals("10a Classe")) {
                        spAno.setAdapter(aHist10);
                    } else if (spClasse.getSelectedItem().equals("12a Classe")) {
                        spAno.setAdapter(aHist12);
                    } else {
                        spAno.setAdapter(adapterNull);
                    }


                } else if (spDisciplina.getSelectedItem().equals("Quimica")) {
                    if (spClasse.getSelectedItem().equals("10a Classe")) {
                        spAno.setAdapter(aQuim10);
                    } else if (spClasse.getSelectedItem().equals("12a Classe")) {
                        spAno.setAdapter(aQuim12);
                    } else {
                        spAno.setAdapter(adapterNull);
                    }
                } else {
                    spAno.setAdapter(adapterNull);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spDisciplina.setOnItemSelectedListener(adapterViewAno);

        //spEpoca:
        AdapterView.OnItemSelectedListener adapterViewEpoca = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spAno.getSelectedItem().equals("2005")) {
                    spEpoca.setAdapter(onlyFirst);

                } else if(spClasse.getSelectedItem().equals("12a Classe") && (spDisciplina.getSelectedItem().equals("Portugues") || spDisciplina.getSelectedItem().equals("Matematica") || spDisciplina.getSelectedItem().equals("Biologia") || spDisciplina.getSelectedItem().equals("Fisica"))  && spAno.getSelectedItem().equals("2016")) {
                    spEpoca.setAdapter(onlyFirst);

                }else if (spClasse.getSelectedItem().equals("10a Classe") && (spDisciplina.getSelectedItem().equals("Historia") || spDisciplina.getSelectedItem().equals("Portugues")) && spAno.getSelectedItem().equals("2015")){
                    spEpoca.setAdapter(onlyFirst);

                } else if(spClasse.getSelectedItem().equals("10a Classe") && (spDisciplina.getSelectedItem().equals("Ingles") || spDisciplina.getSelectedItem().equals("Quimica") || spDisciplina.getSelectedItem().equals("Matematica"))  && spAno.getSelectedItem().equals("2016")) {
                    spEpoca.setAdapter(onlyFirst);

                } else if (spClasse.getSelectedItem().equals("10a Classe") && spDisciplina.getSelectedItem().equals("Biologia") && spAno.getSelectedItem().equals("2009")) {
                    spEpoca.setAdapter(adapterEpoca);

                } else if (spClasse.getSelectedItem().equals("10a Classe") && spDisciplina.getSelectedItem().equals("Fisica") && spAno.getSelectedItem().equals("2010")) {
                    spEpoca.setAdapter(onlyFirst);

                } else if (spClasse.getSelectedItem().equals("10a Classe") && spAno.getSelectedItem().equals("2009")) {
                    spEpoca.setAdapter(onlySeccond);

                } else if (spClasse.getSelectedItem().equals("12a Classe") && spDisciplina.getSelectedItem().equals("Portugues") && spAno.getSelectedItem().equals("2008")) {
                    spEpoca.setAdapter(onlySeccond);

                } else if (spDisciplina.getSelectedItem().equals("Filosofia") && spAno.getSelectedItem().equals("2003")) {
                    spEpoca.setAdapter(onlyFirst);

                } else if (spClasse.getSelectedItem().equals("10a Classe") && spDisciplina.getSelectedItem().equals("Portugues") && spAno.getSelectedItem().equals("2003")) {
                    spEpoca.setAdapter(onlySeccond);
                } else if (spAno.getSelectedItem().equals("...")) {
                    spEpoca.setAdapter(adapterNull);

                } else {
                    spEpoca.setAdapter(adapterEpoca);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spAno.setOnItemSelectedListener(adapterViewEpoca);


        // Init ProgressDialog
        progressDialog.setTitle("Baixando o Exame. Por favor aguarde...");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadTask.cancel();

                Toast.makeText(ExamesActivity.this, "Download cancelado.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


        //Download the File on Button(Download) click:
        bDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initializing teh Spinner-to-String functions:
                Classe = spClasse.getSelectedItem().toString();
                Epoca = spEpoca.getSelectedItem().toString();
                Disciplina = spDisciplina.getSelectedItem().toString();
                Ano = spAno.getSelectedItem().toString();

                //Download the File:
                //First Check if ON the Spinner, everything is checked. It should be. If not, show error Toast.
                if (Classe.equals("...") | Epoca.equals("...") | Disciplina.equals("...") | Ano.equals("...") | Ano.equals("")) {
                    //Show the The Error Toast:
                    Toast.makeText(ExamesActivity.this, "Todas as opções devem ser escolhidas.", Toast.LENGTH_SHORT).show();

                } else {
                    ExamesActivityPermissionsDispatcher.getFileeWithCheck(temp);
                }//end of else

            }
        });

        bLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileBrowser();
            }
        });


        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(ExamesActivity.this);
                ad.setTitle("Informação")
                        .setMessage("Para visualizar os exames baixados clique no butão Meus Exames." +
                                "\n\nPodes visualizar a guia do enunciado em simultâneo ao abrires um exame." +
                                "\n\nOs Exames baixados ficam guardados no armazenamento do seu telemóvel em uma pasta com o nome: Exames-App." +
                                "\n\nDica: Utilize esse directório para compartilhar/imprimir/apagar os exames baixados.")//

                        .setPositiveButton(getString(R.string.ok_forDialog), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                ad.show();
            }
        });


    }//end of the onCreate Method

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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


    //opens FileBrowser with the use of "Material Library"
    private void openFileBrowser() {

        //Selecting the "EXAMES_APP" Folder as default
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Exames-App/");
        String directoryy = dir.toString();

        //Giving the FilePicker a custom Title:
        String title = "Selecione um dos Exames";

        new MaterialFilePicker()
                .withActivity(ExamesActivity.this)
                .withRequestCode(1)
                .withFilter(Pattern.compile(".*\\.pdf$"))
                .withFilterDirectories(false) // Set directories filterable (false by default)
                .withHiddenFiles(false) // Show hidden files and folders
                .withRootPath(directoryy)
                .withTitle(title)
                .start();
    }

    //What happens when a "PDF" file from the FileBrowser is choosen/clicked? Well, this happens:
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

            Intent executeExamesGo = new Intent(ExamesActivity.this, ExamesGoActivity.class);
            executeExamesGo.putExtra("FILE_PDF", filePath);
            executeExamesGo.putExtra("GUIA_PDF", finalGuiaPath);
            startActivity(executeExamesGo);
        } else if (requestCode == 0 && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Whoops! Ocorreu um erro.", Toast.LENGTH_SHORT).show();
        }

    }



    public void downloadExame(){

        new MaterialStyledDialog.Builder(ExamesActivity.this)
                .setTitle("O Exame selecionado é")
                .setDescription("Classe: " + Classe + "\n" + "Disciplina: " + Disciplina + "\n" + "Ano: " + Ano + "\n" + "Época: " + Epoca)

                //When presses the Enunciado Button
                .setPositiveText("Enunciado")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //How the Enunciado dir in database should look: "Disciplina/Classe/Ano-Epoca.ext"
                        pdfRef = mStorageRef.child(Disciplina + "/" + Classe + "/" + Ano + "-" + Epoca + ".pdf");
                        File root = Environment.getExternalStorageDirectory();
                        final File dir = new File(root.getAbsolutePath() + "/Exames-App/");

                        if (!dir.exists()) {
                            dir.mkdir();
                        }

                        //set the Download location as well as it name
                        localFile = new File(dir, Disciplina + "-" + Ano + "-" + Classe + "-" + Epoca + ".pdf");

                        //Fisrt check if the Enunciado exists in the folder.
                        if (localFile.exists()) {
                            //If enunciado exists in the directory:
                            Toast.makeText(ExamesActivity.this, "Já tens este enunciado!", Toast.LENGTH_SHORT).show();
                        } else if (!(localFile.exists())) {
                            //If enunciado does not exist in the directory:
                            Toast.makeText(ExamesActivity.this, "Download Iniciado", Toast.LENGTH_SHORT).show();
                            progressDialog.show();

                            //Start the download
                            downloadTask = (FileDownloadTask) pdfRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    // Local temp file has been created
                                    progressDialog.dismiss();
                                    Toast.makeText(ExamesActivity.this, "Enunciado foi baixado com sucesso!️", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    progressDialog.dismiss();

                                    if (!(downloadTask.isCanceled())) {
                                        Toast.makeText(ExamesActivity.this, "Enunciado não encontrado no servidor.", Toast.LENGTH_LONG).show();
                                        Log.e("TAG", "simpsons: " + exception);
                                    } else if (downloadTask.isCanceled()) {
                                        if (localFile.exists()) {
                                            localFile.delete();
                                            Log.v("TAG", "simpsons: corrupted Enunciado deleted");
                                        }
                                    }

                                }

                            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    //Some math to get the Percentage of the Download :)
                                    double progressPercentage = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                    long rawFileSize = taskSnapshot.getTotalByteCount();

                                    if (rawFileSize <= 999999) {
                                        long sizeInKB = rawFileSize / 1000;
                                        progressDialog.setMessage("Tamanho: " + sizeInKB + "KB" + "\n" + "Progresso: " + ((int) progressPercentage) + "%" + "\n" + "\n" + "Se o download levar mais de 3 minutos para iniciar, verifique a sua conexao com a internet!");
                                    } else if (rawFileSize > 999999) {
                                        long sizeInMB = rawFileSize / 1000000;
                                        progressDialog.setMessage("Tamanho: " + sizeInMB + "MB" + "\n" + "Progresso: " + ((int) progressPercentage) + "%" + "\n" + "\n" + "Se o download levar mais de 3 minutos para iniciar, verifique a sua conexao com a internet!");
                                    }
                                }
                            });

                        }
                    }
                })


                .setNegativeText("Guia")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        //How the Enunciado dir in database should look: "Disciplina/Classe/Ano-Epoca.ext"
                        guiasRef = mStorageRef.child("Guias" + "/" + Disciplina + "/" + Classe + "/" + Ano + "-" + Epoca + ".pdf");
                        File root = Environment.getExternalStorageDirectory();
                        final File dir = new File(root.getAbsolutePath() + "/Exames-App/Guias");

                        if (!dir.exists()) {
                            dir.mkdir();
                        }

                        //set the Download location as well as it name
                        localguiaFile = new File(dir, "Guia" + "-" + Disciplina + "-" + Ano + "-" + Classe + "-" + Epoca + ".pdf");


                        //Check if the file exists before downloading:
                        if (localguiaFile.exists()) {
                            //What if The file exists on my phone storage:
                            Toast.makeText(ExamesActivity.this, "Já tens esta guia.", Toast.LENGTH_SHORT).show();
                        } else if (!(localguiaFile.exists())) {
                            //Nope, the file does not exist in your phone's directory. Begin the download!
                            Toast.makeText(ExamesActivity.this, "Download Iniciado", Toast.LENGTH_SHORT).show();
                            progressDialog.show();

                            //Start the download
                            downloadTask = (FileDownloadTask) guiasRef.getFile(localguiaFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    // Local temp file has been created
                                    progressDialog.dismiss();
                                    Toast.makeText(ExamesActivity.this, "Guia foi baixado com sucesso!️", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    progressDialog.dismiss();

                                    if (!(downloadTask.isCanceled())) {
                                        Toast.makeText(ExamesActivity.this, "Guia não encontrado no servidor.", Toast.LENGTH_LONG).show();
                                        Log.e("TAG", "simpsons: " + exception);
                                    } else if (downloadTask.isCanceled()) {
                                        if (localguiaFile.exists()) {
                                            localguiaFile.delete();
                                            Log.v("TAG", "simpsons: corrupted Guia deleted");
                                        }
                                    }

                                }

                            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    //Some math to get the Percentage of the Download :)
                                    double progressPercentage = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                    long rawFileSize = taskSnapshot.getTotalByteCount();

                                    if (rawFileSize <= 999999) {
                                        long sizeInKB = rawFileSize / 1000;
                                        progressDialog.setMessage("Tamanho: " + sizeInKB + "KB" + "\n" + "Progresso: " + ((int) progressPercentage) + "%" + "\n" + "\n" + "Se o download levar mais de 3 minutos para iniciar, verifique a sua conexao com a internet!");
                                    } else if (rawFileSize > 999999) {
                                        long sizeInMB = rawFileSize / 1000000;
                                        progressDialog.setMessage("Tamanho: " + sizeInMB + "MB" + "\n" + "Progresso: " + ((int) progressPercentage) + "%" + "\n" + "\n" + "Se o download levar mais de 3 minutos para iniciar, verifique a sua conexao com a internet!");
                                    }
                                }
                            });
                        }
                    }
                })



                .setNeutralText("Cancelar")
                .onNeutral(null)
                .setStyle(Style.HEADER_WITH_ICON)
                .withIconAnimation(false)
                .setIcon(R.drawable.header_download)
                .show();

    }



    //These FOUR "Overrided Methods" bellow, are solely for checking the permission on Run Time for ANDROID_MARSHMALLOW(sdk 23) and above:
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void getFilee() {
        //if HOPEFULLY the Permission has been granted by the user. Phew..:
        downloadExame();
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //get the permission result (that is, if it has been granted or denied and store it as an Integer:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ExamesActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void getFileRationale(final PermissionRequest request) {

        // If the permission has been denied (but the user didn't check "NEVER_SHOW") show an Alert Dialog explaining why the user need this permission to be granted:

        new AlertDialog.Builder(this)
                .setMessage("O pedido que lhe será apresentado logo a seguir, é crucial para que o seu exame seja baixado. Obrigado")
                .setPositiveButton("Avançar >", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("Discordar.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();

    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void getFileNever() {
        // If the permission while the NEVER_SHOW was checked, show error toast:
        Toast.makeText(this, "Permissão foi negada. Vá para as definições deste App para permitir o uso de armazenamento e conseguir baixar o arquivo.", Toast.LENGTH_LONG).show();
    }
}//end of the activity.



