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
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myself.todo.Beans.User;
import com.myself.todo.Database.DadosOpenHelper;
import com.myself.todo.Database.UserRepository;
import com.myself.todo.Utils.Utilities;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

import static com.myself.todo.Mylist.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

public class Login extends AppCompatActivity {

    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    LoadingButton entrar;
    EditText userinput, pass;
    User usuarioB;
    CircleImageView profilepic;
    private UserRepository usuarioRepositorio;
    private TextView title;
    private android.widget.LinearLayout form;
    private LoadingButton btnlogin;
    private EditText useregister;
    private EditText passreg;
    private android.widget.LinearLayout formregister;
    private android.widget.RelativeLayout registerform;
    private LoadingButton reg;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth mAuth;
    private com.github.mmin18.widget.RealtimeBlurView welcomeblur;
    private TextView usernamelogin;
    private Button formregbtn;
    private ImageView profileback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        this.profileback = findViewById(R.id.profileback);
        this.formregbtn = findViewById(R.id.formregbtn);
        this.usernamelogin = findViewById(R.id.usernamelogin);
        this.welcomeblur = findViewById(R.id.welcomeblur);
        mAuth = FirebaseAuth.getInstance();
        this.reg = findViewById(R.id.reg);
        this.registerform = findViewById(R.id.registerform);
        this.formregister = findViewById(R.id.formregister);
        this.passreg = findViewById(R.id.passreg);
        this.useregister = findViewById(R.id.useregister);
        this.btnlogin = findViewById(R.id.btnlogin);
        this.form = findViewById(R.id.form);
        this.title = findViewById(R.id.title);
        usuarioB = new User();
        userinput = (findViewById(R.id.user));
        pass = (findViewById(R.id.pass));
        TextView title = (findViewById(R.id.title));
        profilepic = (findViewById(R.id.profile_pic));
        entrar = findViewById(R.id.btnlogin);
        Typeface Atelas = Typeface.createFromAsset(getAssets(), "fonts/SF-Pro-Text-Regular.ttf");

        title.setTypeface(Atelas);
        userinput.setTypeface(Atelas);

        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                    validarLogin();
                }
                return false;
            }
        });
        criarConexao();
        checkPermissionREAD_EXTERNAL_STORAGE(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null) {
            begin();
        }
        //begin();

    }

    private void criarConexao() {
        dadosOpenHelper = new DadosOpenHelper(this);

        conexao = dadosOpenHelper.getWritableDatabase();

        usuarioRepositorio = new UserRepository(conexao);

        //Toast.makeText(this,"CONEXÃO CRIADA COM SUCESSO!", Toast.LENGTH_SHORT).show();


    }

    public void MessageRegister() {
        Snacky.builder()
                .setActivity(this)
                .setText("Cadastro concluído")
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show();
    }


    public void register(View view) {
        Animation myanim2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
        this.registerform.setVisibility(View.VISIBLE);
        this.registerform.startAnimation(myanim2);


    }

    public void begin() {
        this.form.setVisibility(View.INVISIBLE);
        this.btnlogin.setVisibility(View.INVISIBLE);
        this.reg.setVisibility(View.INVISIBLE);
        this.formregbtn.setVisibility(View.INVISIBLE);

        this.welcomeblur.setVisibility(View.VISIBLE);
        Glide.with(this).load(user.getPhotoUrl()).into(profileback);


        this.usernamelogin.setText("Olá " + (user != null ? user.getDisplayName() : null));
        final Animation in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        this.welcomeblur.startAnimation(in);
        this.profileback.startAnimation(in);
        this.usernamelogin.startAnimation(in);
        CountDownTimer countDownTimer = new CountDownTimer(5000, 300) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                StartApp();

            }
        }.start();


    }

    public void StartApp() {
        final Intent i = new Intent(this, WelcomeActivity.class);
        final Intent i2 = new Intent(this, Mylist.class);

        if (Objects.equals(user.getDisplayName(), null) || Objects.equals(user.getDisplayName(), "")) {
            startActivity(i);
            this.finish();

        } else {
            startActivity(i2);
            this.finish();
        }
    }

    public boolean userDefined() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        boolean usuario = false;
        if (user != null) {
            return user.getDisplayName() != null;
        } else {
            return false;
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


    public void MessageError(String mensagem) {
        Snacky.builder()
                .setActivity(this)
                .setText(mensagem)
                .setDuration(Snacky.LENGTH_SHORT)
                .error()
                .show();
    }


    public void validarLogin() {

        String usuario = userinput.getText().toString();
        String senha = pass.getText().toString();
        final Animation myanim2 = AnimationUtils.loadAnimation(this, R.anim.popin);
        entrar.startLoading();
        try {
            boolean isValid = usuarioRepositorio.validaLogin(usuario, senha);
            if (isValid) {

                usuarioRepositorio.findByLogin(usuario, senha);
                usuarioB = usuarioRepositorio.findByLogin(usuario, senha);
                System.out.println(usuarioB.getProfilepic());

                if (usuarioB.getProfilepic() == null) {

                    Snacky.builder()
                            .setActivity(this)
                            .setText("Sem foto de perfil " + usuarioB.getUser())
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();

                    CountDownTimer counter = new CountDownTimer(2000, 100) {
                        @Override
                        public void onTick(long l) {
                            entrar.loadingSuccessful();
                        }

                        @Override
                        public void onFinish() {
                            begin();
                        }
                    };
                    counter.start();


                } else {
                    Snacky.builder()
                            .setActivity(this)
                            .setText("Bem-vindo " + usuarioB.getUser())
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();


                    profilepic.setVisibility(View.VISIBLE);
                    profilepic.setAnimation(myanim2);
                    profilepic.setImageBitmap(Utilities.handleSamplingAndRotationBitmap(this, Uri.parse(usuarioB.getProfilepic())));

                    CountDownTimer counter = new CountDownTimer(2000, 100) {
                        @Override
                        public void onTick(long l) {
                            entrar.loadingSuccessful();
                        }

                        @Override
                        public void onFinish() {
                            begin();
                        }
                    };
                    counter.start();
                }


                //Toast.makeText(this,"Usuario e senha validados com sucesso!",Toast.LENGTH_SHORT).show();


            } else {
                MessageError("Usuário e/ou senha incorretos");
                CountDownTimer counter = new CountDownTimer(2000, 100) {
                    @Override
                    public void onTick(long l) {
                        entrar.loadingFailed();

                    }

                    @Override
                    public void onFinish() {
                        userinput.setFocusable(true);
                        entrar.reset();
                    }
                };
                counter.start();

            }
        } catch (Exception e) {
            MessageError("Erro ao fazer login " + e);
            e.printStackTrace();
        }
    }

    private void LoginSucess(String usuario) {
        Snacky.builder()
                .setActivity(this)
                .success()
                .setText("Bem-vindo " + usuario)
                .show();

    }

    public void login(View view) {
        mAuth.signInWithEmailAndPassword(userinput.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            LoginSucess(user.getDisplayName());
                            SetProfilePic(user.getPhotoUrl());
                            begin();
                        } else {
                            MessageError("Erro ao fazer login " + task.getException());
                        }
                    }
                });
        validarLogin();

    }

    public void SetProfilePic(Uri uri) {
        Glide.with(this).load(uri).into(profilepic);
    }

    public void cadastrar(View view) {


        this.reg.startLoading();


        final RelativeLayout registerform = this.registerform;
        final LoadingButton loadingButton = this.reg;
        criarConexao();
        User usuario = new User();
        usuario.setUser(this.useregister.getText().toString());
        usuario.setPassword(this.passreg.getText().toString());
        mAuth.createUserWithEmailAndPassword(usuario.getUser(), usuario.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loadingButton.loadingSuccessful();
                    FirebaseUser user = mAuth.getCurrentUser();
                    usuarioB.setUser(user.getDisplayName());
                    usuarioB.setProfilepic(String.valueOf(user.getPhotoUrl()));
                    begin();
                    MessageRegister();
                } else {
                    MessageError("Erro ao cadastrar " + task.getException());
                }
            }
        });
        MessageRegister();
        final Animation out = AnimationUtils.loadAnimation(this, R.anim.fade_out);


        userinput.setText(usuario.getUser());
        pass.setText(usuario.getPassword());
        registerform.startAnimation(out);

        CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
            @Override
            public void onTick(long l) {
                loadingButton.loadingSuccessful();
            }

            @Override
            public void onFinish() {
                registerform.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

}
