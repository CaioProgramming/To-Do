package com.myself.todo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myself.todo.Beans.User;
import com.myself.todo.Database.DadosOpenHelper;
import com.myself.todo.Database.ObjRepository;
import com.myself.todo.Database.UserRepository;
import com.myself.todo.Fragments.AlbFragment;
import com.myself.todo.Fragments.NewEvent;
import com.myself.todo.Fragments.NextEvents;
import com.myself.todo.Fragments.ObjFragment;
import com.myself.todo.Fragments.SuccesEvents;
import com.myself.todo.Utils.Utilities;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

public class Mylist extends AppCompatActivity {

    private TextView mTextMessage;
    int cod_usuario;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    String usuario, senha;
    private CircleImageView profilepic;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private UserRepository usuarioRepositorio;
    private User usuarioB;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            BottomNavigationView navigation2 = findViewById(R.id.navigation);
            RelativeLayout top = findViewById(R.id.topnavigation);
            switch (item.getItemId()) {
                case R.id.navigation_album:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        top.setBackground(getDrawable(R.drawable.gradalbum));
                    }

                    //Semevento();
                    getSupportFragmentManager()
                                .beginTransaction()
                            .replace(R.id.fragment, new AlbFragment())
                                .commit();
                     return true;
                case R.id.navigation_objectives:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        top.setBackground(getDrawable(R.drawable.gradobjectives));
                    }

                    Semevento();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new ObjFragment())
                            .commit();

                    return true;
                case R.id.navigation_musics:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        top.setBackground(getDrawable(R.drawable.gradmusic));
                    }

                    // Semevento();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new NextEvents())
                            .commit();

                    return true;

                case R.id.navigation_you:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        top.setBackground(getDrawable(R.drawable.gradyou));
                    }

                    //Semevento();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new SuccesEvents())
                            .commit();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist);
        RelativeLayout top = findViewById(R.id.topnavigation);
        TextView user = findViewById(R.id.usertxt);
        Button pic = findViewById(R.id.photobtn);

        profilepic = findViewById(R.id.userpic);
        Intent intent = getIntent();

        usuario = intent.getExtras().getString("usuario");
        cod_usuario = intent.getExtras().getInt("codigo");
        senha = intent.getExtras().getString("senha");

        criarConexao();
        usuarioRepositorio = new UserRepository(conexao);
        usuarioB = usuarioRepositorio.findByLogin(usuario, senha);
        System.out.println(usuarioB.getProfilepic());
        System.out.println(usuarioB.getUser());

        if (usuarioB.getProfilepic() == null) {

        } else {

            try {
                profilepic.setImageBitmap(Utilities.handleSamplingAndRotationBitmap(this, Uri.parse(usuarioB.getProfilepic())));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        System.out.println(usuario);
        user.setText(usuario);


        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment,new NewEvent())
                    .commit();


        }


        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        Semevento();



        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_objectives);

    }

    private BottomNavigationView Semevento() {
        BottomNavigationView navigation2 = findViewById(R.id.navigation);
        ObjRepository objRepository = new ObjRepository(this);
        objRepository.abrir();
        Cursor evento = objRepository.obterEventosconcluidos(usuario);
        Cursor evento1 = objRepository.obterEventos(usuario);
        Cursor evento2 = objRepository.obterFavoritos(usuario);

        if (evento.getCount() == 0 || evento1.getCount() == 0|| evento2.getCount() == 0){
            navigation2.setItemTextColor(ColorStateList.valueOf(Color.RED));
            navigation2.setItemIconTintList(ColorStateList.valueOf(Color.RED));

        }else{

            return navigation2;
        }

        return navigation2;
    }

    public void foto(View view) {

        Dialog myDialog = new Dialog(this);
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

    private void succes(User usuarioB) {
        Snacky.builder()
                .setActivity(this)
                .setText("Foto alterada " + usuarioB.getUser())
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show();
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
                    profilepic.setImageURI(selectedImage);
                    usuarioB.setProfilepic(selectedImage.toString());
                    System.out.println(usuarioB.getProfilepic());
                    if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
                        try {
                            criarConexao();
                            usuarioRepositorio = new UserRepository(conexao);
                            usuarioRepositorio.update(usuarioB);

                            succes(usuarioB);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageselect.getData();
                    profilepic.setImageURI(selectedImage);
                    usuarioB.setProfilepic(selectedImage.toString());
                    System.out.println(usuarioB.getProfilepic());
                    if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
                        criarConexao();
                        usuarioRepositorio = new UserRepository(conexao);
                        usuarioRepositorio.update(usuarioB);

                        succes(usuarioB);
                    }
                }
                break;
        }
    }

    private void criarConexao() {
        dadosOpenHelper = new DadosOpenHelper(this);

        conexao = dadosOpenHelper.getWritableDatabase();


        usuarioRepositorio = new UserRepository(conexao);

        //Toast.makeText(this,"CONEXÃO CRIADA COM SUCESSO!", Toast.LENGTH_SHORT).show();


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
