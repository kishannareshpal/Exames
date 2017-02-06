package com.kishan.exames;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import mehdi.sakout.fancybuttons.FancyButton;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class ExamesActivity extends AppCompatActivity {

    public Spinner spClasse, spAno, spDisciplina, spEpoca;
    public ArrayAdapter adapterClasse, adapterDisc10, adapterDisc12, adapterEpoca, adapterNull;
    public Firebase mRef;
    private StorageReference mStorageRef, pdfRef;
    private ArrayList<String> anosArray;
    private FancyButton bDownload;
    private File localFile;
    public FileDownloadTask downloadTask;

    public ProgressDialog progressDialog;

    String Classe, Epoca, Disciplina, Ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_exames);

        //Calling functions:
        FancyButton bLook = (FancyButton) findViewById(R.id.bLook);
        spClasse = (Spinner) findViewById(R.id.spClasse);
        spAno = (Spinner) findViewById(R.id.spAno);
        spDisciplina = (Spinner) findViewById(R.id.spDisciplina);
        spEpoca = (Spinner) findViewById(R.id.spEpoca);
        mRef = new Firebase("https://examesproject.firebaseio.com/Anos");
        anosArray = new ArrayList<>();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        bDownload = (FancyButton) findViewById(R.id.bDownload);

        final ExamesActivity temp = this;

        progressDialog = new ProgressDialog(this);

        //hiding the TryAgain (tvTA) TextView:

        //Calling Adapter:
        adapterClasse = ArrayAdapter.createFromResource(this, R.array.arClasse, android.R.layout.simple_spinner_dropdown_item);
        adapterDisc10 = ArrayAdapter.createFromResource(this, R.array.arDisciplina10, android.R.layout.simple_spinner_dropdown_item);
        adapterDisc12 = ArrayAdapter.createFromResource(this, R.array.arDisciplina12, android.R.layout.simple_spinner_dropdown_item);
        adapterEpoca = ArrayAdapter.createFromResource(this, R.array.arEpoca, android.R.layout.simple_spinner_dropdown_item);
        adapterNull = ArrayAdapter.createFromResource(this, R.array.arNull, android.R.layout.simple_spinner_dropdown_item);


        //Setting Adapter with Firebase:
        //Anos Adapter
        //1st: Add "SOME_YEARS" for Error reasons.
        anosArray.add("...");
        anosArray.add("2011");
        anosArray.add("2012");
        anosArray.add("2013");
        anosArray.add("2014");


        //2nd: Get the value
        final ArrayAdapter<String> adapteranos = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, anosArray);
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                anosArray.add(value);
                adapteranos.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapteranos.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapteranos.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                adapteranos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(ExamesActivity.this, "Oops, ocorreu um erro. Conexao com o servidor falhou.", Toast.LENGTH_SHORT).show();
                bDownload.setEnabled(false);
            }

        });


        //Setting Adapter:
        spClasse.setAdapter(adapterClasse);
        spAno.setAdapter(adapteranos);
        spEpoca.setAdapter(adapterEpoca);
        //spDisiplina:
        AdapterView.OnItemSelectedListener adapterView1 = new AdapterView.OnItemSelectedListener() {
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
        spClasse.setOnItemSelectedListener(adapterView1);


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
                        Toast.makeText(ExamesActivity.this, "Todas as opçoes devem ser escolhidas.", Toast.LENGTH_SHORT).show();

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

    }//end of the onCreate Method



    //opens FileBrowser with the use of "Material Library"
    private void openFileBrowser(){

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

    //What happens when a "PDF" file from the FileBrowser is choosen/click? Well, this happens:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with filePath, that is, file.

            Intent executeExamesGo = new Intent(ExamesActivity.this, ExamesGoActivity.class);
            executeExamesGo.putExtra("FILE_PDF", filePath);
            startActivity(executeExamesGo);
        }
        else if(requestCode == 0 && resultCode == RESULT_CANCELED){
            Toast.makeText(this, "Whoops! Ocorreu um erro.", Toast.LENGTH_SHORT).show();
        }

    }

    //This is the method responsible to get the PDF File from the Firebase, and downloading it to the mobile:
    public void downloadExame(){

        //show the alertdialog
        new AlertDialog.Builder(ExamesActivity.this)
                .setTitle("Download")
                .setMessage("O Exame que foi selecionado: "+ "\n" + "\n" + Classe + "\n" + Disciplina + "\n" + Ano + "\n" + Epoca)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(ExamesActivity.this, "Download Iniciado", Toast.LENGTH_SHORT).show();

                        progressDialog.show();

                        //How the dir in database should look: "Disciplina/Classe/Ano-Epoca.ext"
                        pdfRef = mStorageRef.child(Disciplina + "/" + Classe + "/" + Ano + "-" + Epoca + ".pdf");
                        File root = android.os.Environment.getExternalStorageDirectory();
                        final File dir = new File(root.getAbsolutePath() + "/Exames-App/");

                        if (!dir.exists()) {
                            dir.mkdir();
                        }

                        //set the Download location as well as it name
                        localFile = new File(dir, Disciplina + "-" + Ano + "-" + Classe + "-" + Epoca + ".pdf");

                        //Start the download
                        downloadTask = (FileDownloadTask) pdfRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                // Local temp file has been created
                                progressDialog.dismiss();
                                Toast.makeText(ExamesActivity.this, "Exame foi baixado com sucesso!️", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                progressDialog.dismiss();

                                if (!(downloadTask.isCanceled())){
                                    Toast.makeText(ExamesActivity.this, "Exame não encontrado no servidor.", Toast.LENGTH_LONG).show();
                                    Log.e("TAG", "simpsons: " + exception);
                                }
                            }

                        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                //Some math to get the Percentage of the Download :)
                                double progressPercentage = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                long rawFileSize = taskSnapshot.getTotalByteCount();

                                if(rawFileSize < 100000){
                                    long sizeInKB = rawFileSize / 1000000;
                                    progressDialog.setMessage("Tamanho: " + sizeInKB + "KB" + "\n" + "Progresso: " +((int) progressPercentage)+"%" + "\n" + "\n" + "Se o download levar mais de 3 minutos para iniciar, verifique a sua conexao com a internet!");
                                }
                                else if(rawFileSize > 1000000){
                                    long sizeInMB = rawFileSize / 1000000000;
                                    progressDialog.setMessage("Tamanho: " + sizeInMB + "MB" + "\n" + "Progresso: " + ((int) progressPercentage)+"%" + "\n" + "\n"+ "Se o download levar mais de 3 minutos tempo para iniciar, verifique a sua conexao com a internet!");
                                }

                            }
                        });

                        //Cancel Button on the Alert Dialog:
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }



//These FOUR "Overrided Methods" bellow, are solely for checking the permission on Run Time for ANDROID_MARSHMALLOW(sdk 23) and above:
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void getFilee() {
        //if the Permission is grant:
        downloadExame();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //get teh permission result (that is, if it has been granted or denied and store it as an Integer:
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
        Toast.makeText(this, "Permissão foi negada. Vá para as Definições do app para aceitar manualmente.", Toast.LENGTH_SHORT).show();
    }
//Those FOUR "Overrided Methods" above, are solely for checking the permission on Run Time for ANDROID_MARSHMALLOW(sdk 23) and above:



}//end of the activity.



