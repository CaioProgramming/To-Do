package com.myself.todo;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

public class Profile extends AppCompatActivity {

    DatabaseReference raiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final AutoCompleteTextView emailedit = findViewById(R.id.emailedit);
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
        if (user != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(picback);
            Glide.with(this).load(user.getPhotoUrl()).into(profilepic);
            username.setText(user.getDisplayName());
            usernameedit.setText(user.getDisplayName());
            userid.setText(user.getUid());

        }
        final Animation myanim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final Animation myanim2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        final Animation myanim3 = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        objetivesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objetivesbtn.startLoading();
                DatabaseReference eventsreference = FirebaseDatabase.getInstance().getReference("events");
                if (user != null) {
                    eventsreference.child("userID").child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
                }

                CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        objetivesbtn.reset();
                        Message("Você apagou todos seus objetivos");
                    }
                }.start();


            }
        });
        fotosbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotosbtn.startLoading();
                raiz = FirebaseDatabase.getInstance().getReference("album");
                raiz.keepSynced(true);
                if (user != null) {
                    raiz.child("userID").child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Message("Você removeu todas as fotos");
                            fotosbtn.loadingSuccessful();
                        }
                    });
                }
                //objRepository.apagarAll(user.getUser());
                CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        fotosbtn.reset();
                    }
                }.start();
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savebtn.startLoading();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(usernameedit.getText().toString())
                        .build();
                if (user != null) {
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Message("Nome alterado");
                            }
                        }
                    });
                }
                if (user != null) {
                    user.updateEmail(emailedit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Message("Email alterado");
                            }
                        }
                    });
                }
                if (user != null) {
                    user.updatePassword(usersenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Message("Senha alterada");
                            }
                        }
                    });
                }

                CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        savebtn.loadingSuccessful();


                    }
                }.start();

            }
        });

        wipeoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wipeoutbtn.startLoading();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    raiz = FirebaseDatabase.getInstance().getReference("album");
                    raiz.child("userID").child(user.getUid()).removeValue();

                    raiz = FirebaseDatabase.getInstance().getReference("events");
                    raiz.child("userID").child(user.getUid()).removeValue();
                    user.delete();

                }

                //objRepository.apagarAll(user.getUser());
                CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        wipeoutbtn.loadingSuccessful();
                        Message("Você apagou todos os seus dados");
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
        this.finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, Mylist.class);
        startActivity(i);
        this.finish();
    }


    private void Message(String msg) {
        Snacky.builder().setActivity(this).success().setText(msg).show();
    }


}
