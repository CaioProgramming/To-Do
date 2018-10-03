package com.myself.todo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

public class WelcomeActivity extends AppCompatActivity implements BSImagePicker.OnSingleImageSelectedListener {

    FirebaseAuth mAuth;
    RelativeLayout act;
    LinearLayout profileform;
    CircleImageView profilepic;
    RealtimeBlurView blur;
    EditText username;
    TextView messagewelcome;
    LinearLayout nameform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        act = findViewById(R.id.act);
        profileform = findViewById(R.id.profileform);
        nameform = findViewById(R.id.nameform);
        profilepic = findViewById(R.id.profile_pic);
        blur = findViewById(R.id.blur);
        username = findViewById(R.id.username);
        messagewelcome = findViewById(R.id.messagewelcome);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Animation in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final Animation slidein = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        final Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out);
        CountDownTimer countDownTimer = new CountDownTimer(4000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                messagewelcome.startAnimation(out);
                nameform.startAnimation(slidein);
                nameform.setVisibility(View.VISIBLE);
                messagewelcome.setVisibility(View.INVISIBLE);
            }
        }.start();


    }

    public void Photo(View view) {
        if (username.getText() != null) {
            Animation myanim2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
            this.blur.setVisibility(View.VISIBLE);
            this.blur.setBlurRadius(45);
            this.profileform.startAnimation(myanim2);
            profileform.setVisibility(View.VISIBLE);
            CountDownTimer countDownTimer = new CountDownTimer(2000, 100) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    BSImagePicker singleSelectionPicker = new BSImagePicker.Builder("com.myself.fileprovider")
                            .hideGalleryTile()//Default: Integer.MAX_VALUE. Don't worry about performance :)
                            .setSpanCount(5) //Default: 3. This is the number of columns
                            .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                            .setPeekHeight(Utils.dp2px(360))//Default: 360dp. This is the initial height of the dialog.
                            .setOverSelectTextColor(R.color.black)
                            .setMultiSelectDoneTextColor(R.color.blue_300)
                            .build();

                    singleSelectionPicker.show(getSupportFragmentManager(), "picker");
                }
            }.start();
        } else {
            Message("Escreva o seu nome antes!", "e");
        }

    }


    public void updateprofilepic(final Uri path) {
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Context context = this;
        final Activity activity = this;
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(path.toString()))
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Glide.with(context).load(path).into(profilepic);
                            profilepic.startAnimation(animation);
                            Snacky.builder().setActivity(activity).success().setText("Foto de perfil alterada").show();

                        }
                    }
                });
    }

    public void Message(String message, String type) {
        if (type.equals("e")) {
            Snacky.builder().setActivity(this).error().setText("Erro " + message).show();
        } else {
            Snacky.builder().setActivity(this).error().setText(message).show();
        }
    }

    public void save(View view) {
        final Animation out = AnimationUtils.loadAnimation(this, R.anim.pop_out);
        final Intent i = new Intent(this, Mylist.class);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username.getText().toString())
                .build();
        if (user != null) {
            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        act.startAnimation(out);
                        Message("Cadastro concluído com sucesso " + user.getDisplayName(), "s");
                        CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                startActivity(i);
                            }
                        }.start();

                    }
                }
            });
        }


    }

    @Override
    public void onSingleImageSelected(Uri uri) {
        updateprofilepic(uri);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
