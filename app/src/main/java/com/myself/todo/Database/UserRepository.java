package com.myself.todo.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myself.todo.Beans.User;

public class UserRepository {

    private SQLiteDatabase conexao;

    public UserRepository(SQLiteDatabase conexao) {
        this.conexao = conexao;
    }

    public void inserir(User usuario) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("NOME", usuario.getUser());
        contentValues.put("SENHA", usuario.getPassword());
        contentValues.put("PROFILEPIC", usuario.getProfilepic());



        conexao.insertOrThrow("USUARIO", null, contentValues);
    }

    public void excluir(int codigo) {

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(codigo);

        conexao.delete("USUARIO", "CODIGO = ?", parametros);
    }

    public void update(User usuario) {


        ContentValues contentValues = new ContentValues();
        contentValues.put("NOME", usuario.getUser());
        contentValues.put("SENHA", usuario.getPassword());
        contentValues.put("PROFILEPIC", usuario.getProfilepic());



        String[] parametros = new String[1];

        parametros[0] = String.valueOf(usuario.getCodigo());

        conexao.update("USUARIO", contentValues, "CODIGO = ?", parametros);

    }


    public User montaUsuario(Cursor resultado) {

        User usuario = new User();

        if (resultado.getCount() == 0) {
            return null;
        }
        Integer id = resultado.getInt(resultado.getColumnIndex("CODIGO"));
        String nome = resultado.getString(resultado.getColumnIndex("NOME"));
        String senha = resultado.getString(resultado.getColumnIndex("SENHA"));
        String foto = resultado.getString(resultado.getColumnIndex("PROFILEPIC"));


        usuario.setCodigo(id);
        usuario.setUser(nome);
        usuario.setPassword(senha);
        usuario.setProfilepic(foto);

        return usuario;
    }


    public User findByLogin(String usuario, String senha) {

        String sql = "SELECT * FROM USUARIO WHERE NOME = ? AND SENHA = ?";

        String[] selectionArgs = new String[]{usuario, senha};

        Cursor resultado = conexao.rawQuery(sql, selectionArgs);

        resultado.moveToFirst();
        return montaUsuario(resultado);

    }

    public boolean validaLogin(String usuario, String senha) {

        User user = findByLogin(usuario, senha);

        if (user == null || user.getUser() == null || user.getPassword() == null) {
            return false;
        }

        String informado = usuario + senha;

        String esperado = user.getUser() + user.getPassword();

        return informado.equals(esperado);
    }

    public User findByPassword(String telefone, String email) {

        String sql = "SELECT * FROM USUARIO WHERE TELEFONE = ? AND EMAIL = ?";

        String[] selectionArgs = new String[]{telefone, email};

        Cursor resultado = conexao.rawQuery(sql, selectionArgs);

        resultado.moveToFirst();
        return montaUsuario(resultado);
    }


}
