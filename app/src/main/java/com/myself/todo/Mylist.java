package com.myself.todo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.myself.todo.Database.ListRepository;
import com.myself.todo.Database.UserRepository;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

public class Mylist extends AppCompatActivity {

    private TextView mTextMessage;
    int cod_usuario;
    String usuario;

    private CircleImageView profilepic;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private UserRepository usuarioRepositorio;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            BottomNavigationView navigation2 = findViewById(R.id.navigation);
            RelativeLayout top = findViewById(R.id.topnavigation);


            Activity activity = new Mylist();

            switch (item.getItemId()) {
                case R.id.navigation_newev:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        top.setBackgroundColor(Color.WHITE);
                    }
                    navigation2.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
                    navigation2.setItemIconTintList(ColorStateList.valueOf(Color.BLACK));
                    Semevento();
                    getSupportFragmentManager()
                                .beginTransaction()
                            .replace(R.id.fragment, new NewEvent())
                                .commit();
                     return true;
                case R.id.navigation_home:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        top.setBackgroundColor(getColor(R.color.blue_300));
                    }
                    navigation2.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
                    navigation2.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
                    Semevento();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new BlankFragment())
                            .commit();

                    return true;
                case R.id.navigation_favorites:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        top.setBackgroundColor(getColor(R.color.yellow_500));
                    }
                    navigation2.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
                    navigation2.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
                    Semevento();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new NextEvents())
                            .commit();

                    return true;

                case R.id.navigation_succes:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        top.setBackgroundColor(getColor(R.color.green_200));
                    }
                    navigation2.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
                    navigation2.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
                    Semevento();
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

    }

    private BottomNavigationView Semevento() {
        BottomNavigationView navigation2 = findViewById(R.id.navigation);
        ListRepository listRepository = new ListRepository(this);
        listRepository.abrir();
        Cursor evento = listRepository.obterEventosconcluidos();
        Cursor evento1 = listRepository.obterEventos();
        Cursor evento2 = listRepository.obterFavoritos();

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
        CircleImageView selfiebtn = myDialog.findViewById(R.id.selfie);
        CircleImageView gallerybtn = myDialog.findViewById(R.id.galeria);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageselect) {
        User usuarioB = new User();
        usuarioB.setUser(usuario);
        usuarioB.setCodigo(cod_usuario);

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
                    try {
                        usuarioRepositorio.update(usuarioB);
                        succes(usuarioB);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageselect.getData();
                    profilepic.setImageURI(selectedImage);
                    usuarioB.setProfilepic(selectedImage.toString());
                    succes(usuarioB);
                }
                break;
        }
    }

    private void succes(User usuarioB) {
        Snacky.builder()
                .setActivity(this)
                .setText("Foto alterada " + usuarioB.getUser())
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show();
    }
}
