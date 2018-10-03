package com.myself.todo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myself.todo.Beans.Album;
import com.myself.todo.Database.AlbumRepository;
import com.myself.todo.Utils.Utilities;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import de.mateware.snacky.Snacky;

public class NewPicActivity extends AppCompatActivity implements BSImagePicker.OnSingleImageSelectedListener {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    ImageView fotopic;
    EditText desc;
    Album album;
    Uri photouri;

    AlbumRepository albRepository;
    String usuario;
    DatabaseReference raiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pic);
        raiz = FirebaseDatabase.getInstance().getReference("album");
        album = new Album();
        fotopic = findViewById(R.id.pic);
        desc = findViewById(R.id.picdesc);
        desc.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH ||
                        i == EditorInfo.IME_ACTION_DONE ||
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                                keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    if (!keyEvent.isShiftPressed()) {
                        Log.v("AndroidEnterKeyActivity", "Enter Key Pressed!");
                        switch (view.getId()) {
                            case 1:
                                save();
                                break;
                        }
                        return true;
                    }

                }
                return false; // pass on to other listeners.

            }
        });
        Picalert();
        fotopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picalert();
            }
        });
    }

    private void Picalert() {
        BSImagePicker singleSelectionPicker = new BSImagePicker.Builder("com.myself.fileprovider")
                //Default: Integer.MAX_VALUE. Don't worry about performance :)
                .hideGalleryTile()
                .setSpanCount(3) //Default: 3. This is the number of columns
                .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                .setPeekHeight(Utils.dp2px(360))//Default: 360dp. This is the initial height of the dialog.
                .setOverSelectTextColor(R.color.black)
                .setMultiSelectDoneTextColor(R.color.blue_300)
                .build();

        singleSelectionPicker.show(getSupportFragmentManager(), "picker");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageselect) {


        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageselect.getData();
                    try {

                        fotopic.setImageBitmap(Utilities.handleSamplingAndRotationBitmap(this, selectedImage));
                        album.setFotouri(selectedImage.toString());
                        onBackPressed();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //fotopic.setImageURI(selectedImage);
                    //album.setFotouri(Objects.requireNonNull(selectedImage).toString());
                    System.out.println(album.getFotouri());
                    if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageselect.getData();
                    try {
                        fotopic.setImageBitmap(Utilities.handleSamplingAndRotationBitmap(this, selectedImage));
                        album.setFotouri(selectedImage.toString());
                        onBackPressed();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println(album.getFotouri());
                    if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {

                    }
                }
                break;
        }


    }

    private void succes() {
        Snacky.builder()
                .setActivity(this)
                .setText("Foto adcionada,pressione o botão de voltar para sair!")
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show();

        //onBackPressed();
    }


    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("Armazenamento externo", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permissão");
        alertBuilder.setMessage(msg + " permissão necessária");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }


    public void salvar(View view) {

        if (desc.getText().toString().equals("")) {
            Snacky.builder()
                    .setActivity(this)
                    .setText("Escreva alguma coisa sobre a foto!")
                    .setDuration(Snacky.LENGTH_SHORT)
                    .error()
                    .show();

        } else {

            save();

        }


    }

    private void save() {
        Date datenow = Calendar.getInstance().getTime();
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        //String time = String.valueOf(today);
        String dia = String.valueOf(datenow);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!TextUtils.isEmpty(desc.toString())) {
            String id = raiz.push().getKey();
            Album album = new Album(id, photouri.toString(), desc.getText().toString(), dia, "N", user.getUid());
            raiz.child(id).setValue(album).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        succes("Foto adicionada ");
                    } else {
                        error("Erro " + task.getException());
                    }
                }
            });

        } else {
            Snacky.builder().setActivity(this).error().setText("Escreva algo sobre a foto").show();
        }


    }

    public void succes(String s) {
        Snacky.builder().setActivity(this).success().setText(s).show();
    }

    public void error(String s) {
        Snacky.builder().setActivity(this).success().setText(s).show();
    }

    @Override
    public void onSingleImageSelected(Uri uri) {
        photouri = uri;
        Glide.with(this).load(uri).into(fotopic);
        album.setFotouri(uri.toString());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
