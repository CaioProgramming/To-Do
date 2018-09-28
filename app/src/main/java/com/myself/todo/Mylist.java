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
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mmin18.widget.RealtimeBlurView;
import com.myself.todo.Beans.User;
import com.myself.todo.Database.AlbumRepository;
import com.myself.todo.Database.DadosOpenHelper;
import com.myself.todo.Database.ObjRepository;
import com.myself.todo.Database.UserRepository;
import com.myself.todo.Fragments.FotosFragment;
import com.myself.todo.Fragments.MusicFragment;
import com.myself.todo.Fragments.NewEvent;
import com.myself.todo.Fragments.ObjFragment;
import com.myself.todo.Fragments.ProfileFragment;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

public class Mylist extends AppCompatActivity {

    private TextView mTextMessage, albumcount, musicount, objectivecount;
    int cod_usuario;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    String usuario, senha;
    private CircleImageView profilepic;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private UserRepository usuarioRepositorio;
    private ObjRepository objRepository;
    private AlbumRepository albumRepository;
    private User usuarioB;
    Toolbar myToolbar;

    RealtimeBlurView blur;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            BottomNavigationView navigation2 = findViewById(R.id.navigation);
            switch (item.getItemId()) {

                case R.id.navigation_album:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //tb.setBackground(getDrawable(R.drawable.gradalbum));
                        //navigation2.setBackground(getDrawable(R.color.orange_A100));

                    }
                    getSupportActionBar().show();
                    //CountFotos();
                    //CountObjectives();
                    FotosFragment fotosFragment = new FotosFragment();
                    fotosFragment.setBlur(blur);
                    getSupportFragmentManager()
                                .beginTransaction()
                            .replace(R.id.fragment, fotosFragment)
                                .commit();
                     return true;
                case R.id.navigation_objectives:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //tb.setBackground(getDrawable(R.drawable.gradobjectives));
                        //navigation2.setBackground(getDrawable(R.drawable.gradobjectives));


                    }
                    getSupportActionBar().show();
                    //CountFotos();
                    //CountObjectives();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new ObjFragment())
                            .commit();

                    return true;
                case R.id.navigation_musics:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //tb.setBackground(getDrawable(R.drawable.gradmusic));
                        //navigation2.setBackground(getDrawable(R.drawable.gradmusic));

                    }
                    getSupportActionBar().show();
                    // Semevento();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new MusicFragment())
                            .commit();

                    return true;

                case R.id.navigation_you:


                    //CountFotos();
                    //CountObjectives();
                    getSupportActionBar().hide();
                    myToolbar.hideOverflowMenu();
                    myToolbar.collapseActionView();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new ProfileFragment())
                            .commit();

                    return true;
            }
            //CountFotos();
            //CountObjectives();
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist);
        TextView user = findViewById(R.id.usertxt);
        profilepic = findViewById(R.id.userpic);
        blur = findViewById(R.id.rootblur);
        //albumcount = findViewById(R.id.albumcount);
        //objectivecount = findViewById(R.id.objectivecount);
        //musicount = findViewById(R.id.musicount);
        ///ImageView genr = findViewById(R.id.genre);
        Typeface Atelas = Typeface.createFromAsset(getAssets(), "fonts/Atelas_PersonalUseOnly.ttf");
        user.setTypeface(Atelas);
        criarConexao();
        checkPermissionREAD_EXTERNAL_STORAGE(this);

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment,new NewEvent())
                    .commit();


        }

        myToolbar = findViewById(R.id.my_toolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar();
        Animation myanim2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);

        myToolbar.startAnimation(myanim2);
        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        //Semevento();


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_objectives);

        //CountObjectives();

        //CountFotos();


        SetProfilePic(user, profilepic);

    }


    private void SetProfilePic(TextView user, ImageView pic) {
        Intent intent = getIntent();


        usuario = intent.getExtras().getString("usuario");
        cod_usuario = intent.getExtras().getInt("codigo");
        senha = intent.getExtras().getString("senha");

        usuarioRepositorio = new UserRepository(conexao);
        usuarioB = usuarioRepositorio.findByLogin(usuario, senha);
        System.out.println(usuarioB.getProfilepic());
        System.out.println(usuarioB.getUser());
        System.out.println(usuarioB.getSexo());

        System.out.println(usuario);
        user.setText(usuario);
        if (usuarioB.getProfilepic() == null) {

        } else {
            Glide.with(this).load(usuarioB.getProfilepic().toString()).into(pic);
        }
    }

    /*private void CountFotos() {
        albumRepository = new AlbumRepository(this);
        albumRepository.abrir();
        Cursor fotos = albumRepository.obterFotos(null);
        albumcount.setText(String.valueOf(fotos.getCount()));
        if (fotos.getCount() == 0 ){
            albumcount.setTextColor(Color.RED);
        }
        albumRepository.fecha();
    }

    private void CountObjectives() {
        objRepository = new ObjRepository(this);
        objRepository.abrir();
        Cursor objetivos = objRepository.obterEventos(null);
        objectivecount.setText(String.valueOf(objetivos.getCount()));
        if (objetivos.getCount() == 0 ){
            objectivecount.setTextColor(Color.RED);
        }
        objRepository.fecha();
    }
*/


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
        Button selfiebtn = myDialog.findViewById(R.id.selfie);
        Button gallerybtn = myDialog.findViewById(R.id.galeria);

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

    public void newfoto(View view) {
        Intent i = new Intent(this, NewPicActivity.class);
        i.putExtra("usuario", usuario);
        startActivity(i);

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
