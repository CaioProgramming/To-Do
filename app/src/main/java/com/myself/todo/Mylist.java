package com.myself.todo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.bumptech.glide.Glide;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.myself.todo.Beans.User;
import com.myself.todo.Database.DadosOpenHelper;
import com.myself.todo.Database.UserRepository;
import com.myself.todo.Fragments.EventsFragment;
import com.myself.todo.Fragments.FotosFragment;
import com.myself.todo.Fragments.MusicFragment;
import com.myself.todo.Fragments.ProfileFragment;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

public class Mylist extends AppCompatActivity implements BSImagePicker.OnSingleImageSelectedListener {

    private TextView mTextMessage, albumcount, musicount, objectivecount;
    int cod_usuario;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 112;
    String usuario, senha;
    private CircleImageView profilepic;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private UserRepository usuarioRepositorio;
    private User usuarioB;
    Toolbar myToolbar;
    private FirebaseAuth mAuth;


    RealtimeBlurView blur;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_album:

                    getSupportActionBar().show();
                    //CountFotos();
                    //CountObjectives();
                    FotosFragment fotosFragment = new FotosFragment();
                    getSupportFragmentManager()
                                .beginTransaction()
                            .replace(R.id.fragment, fotosFragment)
                                .commit();
                     return true;
                case R.id.navigation_objectives:

                    getSupportActionBar().show();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new EventsFragment())
                            .commit();

                    return true;
                case R.id.navigation_musics:

                    getSupportActionBar().show();
                    // Semevento();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new MusicFragment())
                            .commit();

                    return true;

                case R.id.navigation_you:


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
        TextView usertxt = findViewById(R.id.usertxt);

        profilepic = findViewById(R.id.userpic);
        blur = findViewById(R.id.rootblur);
        Typeface Atelas = Typeface.createFromAsset(getAssets(), "fonts/Atelas_PersonalUseOnly.ttf");
        usertxt.setTypeface(Atelas);
        checkPermissionREAD_EXTERNAL_STORAGE(this);

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment, new EventsFragment())
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usuarioB = new User();
        if (user != null) {
            usuarioB.setUser(user.getDisplayName());
            usuarioB.setProfilepic(String.valueOf(user.getPhotoUrl()));
            usertxt.setText(usuarioB.getUser());
            Glide.with(this).load(usuarioB.getProfilepic()).into(profilepic);
        }

        //CountObjectives();

        //CountFotos();


        //SetProfilePic(user, profilepic);

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




    public void foto(View view) {
        BSImagePicker singleSelectionPicker = new BSImagePicker.Builder("com.myself.fileprovider")
                .hideGalleryTile()//Default: Integer.MAX_VALUE. Don't worry about performance :)
                .setSpanCount(3) //Default: 3. This is the number of columns
                .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                .setPeekHeight(Utils.dp2px(360))//Default: 360dp. This is the initial height of the dialog.
                .setOverSelectTextColor(R.color.black)
                .setMultiSelectDoneTextColor(R.color.blue_300)
                .build();

        singleSelectionPicker.show(getSupportFragmentManager(), "picker");
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

    public void updateprofilepic(final Uri path) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Context context = this;
        final Activity activity = this;
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(path.toString()))
                .build();
        if (user != null) {
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Glide.with(context).load(path).into(profilepic);
                                Snacky.builder().setActivity(activity).success().setText("Foto de perfil alterada").show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onSingleImageSelected(Uri uri) {
        updateprofilepic(uri);


        /*/usuarioB.setProfilepic(uri.toString());
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
        }*/
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
