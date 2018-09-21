package com.myself.todo.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.myself.todo.Beans.Album;
import com.myself.todo.Database.AlbumRepository;
import com.myself.todo.R;
import com.myself.todo.Utils.Utilities;

import java.io.IOException;

import de.mateware.snacky.Snacky;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class NewFoto extends Fragment {


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    ImageView fotopic;
    EditText desc;
    Album album;
    AlbumRepository albRepository;
    Button save;

    public NewFoto() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newfoto, container, false);


        save = view.findViewById(R.id.savebtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvar();
            }
        });
        fotopic = view.findViewById(R.id.slctpic);
        desc = view.findViewById(R.id.picdesc);

        fotopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photo();
            }
        });
        photo();
        return view;


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageselect) {
        save.setEnabled(true);
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageselect.getData();
                    try {
                        System.out.println(selectedImage.toString());
                        fotopic = getView().findViewById(R.id.slctpic);
                        fotopic.setImageBitmap(Utilities.handleSamplingAndRotationBitmap(getContext(), selectedImage));
                        album.setFotouri(selectedImage.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //fotopic.setImageURI(selectedImage);
                    //album.setFotouri(Objects.requireNonNull(selectedImage).toString());
                    System.out.println(album.getFotouri());
                    if (checkPermissionREAD_EXTERNAL_STORAGE(getContext())) {
                        try {
                            System.out.println(selectedImage.toString());
                            fotopic = getView().findViewById(R.id.slctpic);
                            fotopic.setImageBitmap(Utilities.handleSamplingAndRotationBitmap(getContext(), selectedImage));
                            album.setFotouri(selectedImage.toString());
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
                        System.out.println(selectedImage.toString());
                        fotopic = getView().findViewById(R.id.slctpic);
                        fotopic.setImageBitmap(Utilities.handleSamplingAndRotationBitmap(getContext(), selectedImage));
                        album.setFotouri(selectedImage.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println(album.getFotouri());
                    if (checkPermissionREAD_EXTERNAL_STORAGE(getContext())) {

                    }
                }
                break;
        }
    }

    public void photo() {
        Dialog myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.alertoptions);
        TextView selfiebtn = myDialog.findViewById(R.id.selfie);
        TextView gallerybtn = myDialog.findViewById(R.id.galeria);

        selfiebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });

        gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);

            }
        });
        myDialog.show();
    }

    private void succes() {
        Snacky.builder()
                .setActivity(getActivity())
                .setText("Foto adcionada,pressione o botão de voltar para sair!")
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show();

        //onBackPressed();
    }

    public void salvar() {

        if (desc.getText().toString().equals("")) {
            Snacky.builder()
                    .setActivity(getActivity())
                    .setText("Escreva alguma coisa sobre a foto!")
                    .setDuration(Snacky.LENGTH_SHORT)
                    .error()
                    .show();

        } else {

            album.setDescription(String.valueOf(desc.getText()));
            albRepository = new AlbumRepository(getContext());
            albRepository.abrir();
            albRepository.inserir(album, null);
            succes();

        }


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


}
