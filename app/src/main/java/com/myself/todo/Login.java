package com.myself.todo;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.myself.todo.Beans.User;
import com.myself.todo.Database.DadosOpenHelper;
import com.myself.todo.Database.UserRepository;
import com.myself.todo.Utils.Utilities;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

public class Login extends AppCompatActivity {

    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    Button entrar;
    EditText user,pass;
    User usuarioB;
    CircleImageView profilepic;
    private UserRepository usuarioRepositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuarioB = new User();
        user = (findViewById(R.id.user));
        pass = (findViewById(R.id.pass));
        TextView title = (findViewById(R.id.title));
        profilepic = (findViewById(R.id.profile_pic));
        entrar = (findViewById(R.id.loginconfirm));
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
        Typeface Atelas = Typeface.createFromAsset(getAssets(), "fonts/SF-Pro-Text-Regular.ttf");

        title.setTypeface(Atelas);

        //setStatusBarColor();
        pass.setOnKeyListener(new View.OnKeyListener() {
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
                                validarLogin();
                                break;
                        }
                        return true;
                    }

                }
                return false; // pass on to other listeners.

            }
        });
        criarConexao();



    }



    private void criarConexao(){
        dadosOpenHelper = new DadosOpenHelper(this);

        conexao = dadosOpenHelper.getWritableDatabase();

        Snacky.builder()
                .setActivity(this)
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show();
        usuarioRepositorio = new UserRepository(conexao);

        //Toast.makeText(this,"CONEXÃO CRIADA COM SUCESSO!", Toast.LENGTH_SHORT).show();


    }

    public void MessageRegister(){
        Snacky.builder()
                .setActivity(this)
                .setText("Cadastro concluído")
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show();
    }


    public void register(View view) {
        final User usuario = new User();
        final Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.modalregistro);
        final EditText usarioR, senha;
        final RadioButton masc, fem;
        final RadioGroup genres;
        Button register;
        //genres = myDialog.findViewById(R.id.generos);
        usarioR = myDialog.findViewById(R.id.user);
        senha = myDialog.findViewById(R.id.pass);
        // masc = myDialog.findViewById(R.id.masc);
        //fem = myDialog.findViewById(R.id.fem);
        register = myDialog.findViewById(R.id.confirm);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usarioR.getText().toString().equals("") || senha.getText().toString().equals("")) {
                    MessageError("Sério? você quer cadastrar o nada?");


                } else {

                    criarConexao();
                    usuario.setUser(user.getText().toString());
                    usuario.setPassword(senha.getText().toString());
                    UserRepository usuarioRepository = new UserRepository(conexao);
                    usuarioRepository.inserir(usuario);
                    MessageRegister();
                    myDialog.dismiss();
                    user.setText(usuario.getUser());
                    pass.setText(usuario.getPassword());
                }

            }
        });

        myDialog.show();



    }

    public void start(){
        Intent i = new Intent(this,Mylist.class);
        i.putExtra("usuario", usuarioB.getUser());
        i.putExtra("codigo", usuarioB.getCodigo());
        i.putExtra("senha", usuarioB.getPassword());

        startActivity(i);
        this.finish();

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
        String usuario = user.getText().toString();
        String senha = pass.getText().toString();
        final Animation myanim2 = AnimationUtils.loadAnimation(this, R.anim.popin);

        try {
            boolean isValid = usuarioRepositorio.validaLogin(usuario, senha);
            if (isValid) {

                usuarioRepositorio.findByLogin(usuario, senha);
                usuarioB = usuarioRepositorio.findByLogin(usuario, senha);
                System.out.println(usuarioB.getProfilepic());
                if (usuarioB.getProfilepic() == null) {

                    Snacky.builder()
                            .setActivity(this)
                            .setText("Bem-vindo " + usuarioB.getUser())
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();

                    entrar.setVisibility(View.VISIBLE);
                    entrar.setAnimation(myanim2);


                } else {
                    Snacky.builder()
                            .setActivity(this)
                            .setText("Bem-vindo " + usuarioB.getUser())
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();

                    entrar.setVisibility(View.VISIBLE);
                    entrar.setAnimation(myanim2);

                    profilepic.setVisibility(View.VISIBLE);
                    profilepic.setAnimation(myanim2);
                    profilepic.setImageBitmap(Utilities.handleSamplingAndRotationBitmap(this, Uri.parse(usuarioB.getProfilepic())));
                }


                //Toast.makeText(this,"Usuario e senha validados com sucesso!",Toast.LENGTH_SHORT).show();


            } else {
                MessageError("Usuário e/ou senha incorretos");
            }
        } catch (Exception e) {
            MessageError("Erro ao fazer login " + e);
                e.printStackTrace();
        }
    }
    public void login(View view){
        validarLogin();

    }


}
