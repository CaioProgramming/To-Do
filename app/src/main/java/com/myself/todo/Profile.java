package com.myself.todo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.bumptech.glide.Glide;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.myself.todo.Beans.User;
import com.myself.todo.Database.AlbumRepository;
import com.myself.todo.Database.DadosOpenHelper;
import com.myself.todo.Database.ObjRepository;
import com.myself.todo.Database.UserRepository;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

public class Profile extends AppCompatActivity {
    UserRepository userRepository;
    ObjRepository objRepository;
    AlbumRepository albumRepository;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final LoadingButton wipeoutbtn = findViewById(R.id.wipeoutbtn);
        final LoadingButton savebtn = findViewById(R.id.savebtn);
        final LoadingButton fotosbtn = findViewById(R.id.fotosbtn);
        final LoadingButton objetivesbtn = findViewById(R.id.objetivesbtn);
        final LinearLayout options = findViewById(R.id.options);
        final ProgressBar progress = findViewById(R.id.progress);
        final LinearLayout form = findViewById(R.id.form);
        final AutoCompleteTextView usersenha = findViewById(R.id.usersenha);
        final AutoCompleteTextView usernameedit = findViewById(R.id.usernameedit);
        final RelativeLayout top = findViewById(R.id.top);
        TextView username = findViewById(R.id.username);
        TextView userid = findViewById(R.id.userid);
        final CircleImageView profilepic = findViewById(R.id.profile_pic);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picalert();
            }
        });
        ImageView picback = findViewById(R.id.picback);
        criarConexao();
        user = userRepository.findByLogin(Objects.requireNonNull(getIntent().getExtras()).getString("usuario"), getIntent().getExtras().getString("senha"));
        username.setText(user.getUser());
        usernameedit.setText(user.getUser());
        usersenha.setText(user.getPassword());
        userid.setText(String.valueOf(user.getCodigo()));
        Glide.with(this).load(user.getProfilepic()).into(picback);
        Glide.with(this).load(user.getProfilepic()).into(profilepic);
        final Animation myanim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final Animation myanim2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        final Animation myanim3 = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        objetivesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objetivesbtn.startLoading();
                albumRepository.apagarAll(user.getUser());
                CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        fotosbtn.loadingSuccessful();
                        AllDeleted("Você apagou todas as suas fotos");
                    }
                }.start();


            }
        });
        fotosbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotosbtn.startLoading();
                objRepository.apagarAll(user.getUser());
                CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        objetivesbtn.loadingSuccessful();
                        AllDeleted("Você apagou todos os seus objetivos");
                    }
                }.start();
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savebtn.startLoading();
                final User newuser = new User();
                newuser.setUser(usernameedit.getText().toString());
                newuser.setPassword(usernameedit.getText().toString());
                userRepository.update(user);
                albumRepository.UpdateUser(user.getUser(), newuser.getUser());
                objRepository.UpdateUser(user.getUser(), newuser.getUser());

                CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        savebtn.loadingSuccessful();
                        user = newuser;
                        UserUpdate(user.getUser());

                    }
                }.start();

            }
        });

        wipeoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wipeoutbtn.startLoading();
                userRepository.excluir(user.getCodigo());
                albumRepository.apagarAll(user.getUser());
                objRepository.apagarAll(user.getUser());
                CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        wipeoutbtn.loadingSuccessful();
                        AllDeleted("Você apagou todos os seus dados");
                        top.startAnimation(myanim3);
                        form.startAnimation(myanim3);
                        options.startAnimation(myanim3);
                        CountDownTimer countDownTimer1 = new CountDownTimer(2000, 100) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                top.setVisibility(View.INVISIBLE);
                                form.setVisibility(View.INVISIBLE);
                                options.setVisibility(View.INVISIBLE);
                                GoBack();
                            }


                        }.start();

                    }
                }.start();

            }
        });


        CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                progress.startAnimation(myanim3);
                progress.setVisibility(View.INVISIBLE);

                top.setVisibility(View.VISIBLE);
                top.startAnimation(myanim2);

                options.setVisibility(View.VISIBLE);
                options.startAnimation(myanim2);

                form.setVisibility(View.VISIBLE);
                form.startAnimation(myanim);

            }
        }.start();


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

    private void GoBack() {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, Mylist.class);
        i.putExtra("usuario", user.getUser());
        i.putExtra("senha", user.getPassword());
        startActivity(i);
        this.finish();
    }

    private void UserUpdate(String newuser) {
        Snacky.builder().setActivity(this).setText("Usuario " + user.getUser() + "alterado para " + user).success().show();
    }

    private void AllDeleted(String msg) {
        Snacky.builder().setActivity(this).success().setText(msg).show();
    }

    private void criarConexao() {
        DadosOpenHelper dadosOpenHelper = new DadosOpenHelper(this);

        SQLiteDatabase conexao = dadosOpenHelper.getWritableDatabase();


        userRepository = new UserRepository(conexao);
        objRepository = new ObjRepository(this);
        albumRepository = new AlbumRepository(this);

        //Toast.makeText(this,"CONEXÃO CRIADA COM SUCESSO!", Toast.LENGTH_SHORT).show();


    }

}
