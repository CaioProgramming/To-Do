package com.myself.todo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.myself.todo.Beans.User;
import com.myself.todo.Database.DadosOpenHelper;
import com.myself.todo.Database.UserRepository;

import de.mateware.snacky.Snacky;

public class Login extends AppCompatActivity {

    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private UserRepository usuarioRepositorio,usuarioRepositoriy;
    EditText user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Typeface Atelas = Typeface.createFromAsset(getAssets(),"fonts/SF-Pro-Display-Black.ttf");

        user = (findViewById(R.id.user));
        TextView title = (findViewById(R.id.title));
        title.setTypeface(Atelas);
        pass = (findViewById(R.id.pass));
        //setStatusBarColor();
        criarConexao();



    }



    private void criarConexao(){
        dadosOpenHelper = new DadosOpenHelper(this);

        conexao = dadosOpenHelper.getWritableDatabase();

        Snacky.builder()
                .setActivity(this)
                .setText("Conexão criada com sucesso")
                .setDuration(Snacky.LENGTH_SHORT)
                .info()
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

    public void registrar(){
        criarConexao();
        String nome = user.getText().toString();
        String senha = pass.getText().toString();
         User usuario = new User();
        usuario.setUser(nome);
        usuario.setPassword(senha);
        UserRepository usuarioRepository = new UserRepository(conexao);
        usuarioRepository.inserir(usuario);
        MessageRegister();



    }

    public void register(View view){
        if(user.getText().toString().equals("")){

            MessageError();
        } else{registrar();}
    }

    public void start(){
        Intent i = new Intent(this,Mylist.class);
        i.putExtra("usuario",user.getText());
        startActivity(i);

    }


    public void MessageError(){
        Snacky.builder()
                .setActivity(this)
                .setText("Deu erro")
                .setDuration(Snacky.LENGTH_SHORT)
                .error()
                .show();
    }

    public void validarLogin() {
        String usuario = user.getText().toString();
        String senha = pass.getText().toString();


        try {
            boolean isValid = usuarioRepositorio.validaLogin(usuario, senha);
            if (isValid) {
                Snacky.builder()
                        .setActivity(this)
                        .setText("Bem-vindo " + usuario)
                        .setDuration(Snacky.LENGTH_SHORT)
                        .success()
                        .show();

                start();
                //Toast.makeText(this,"Usuario e senha validados com sucesso!",Toast.LENGTH_SHORT).show();


            } else {
                MessageError();
            }
        } catch (Exception e) {
                MessageError();
                e.printStackTrace();
        }
    }
    public void login(View view){
        validarLogin();

    }



}
