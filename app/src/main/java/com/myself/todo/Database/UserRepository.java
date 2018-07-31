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


        conexao.insertOrThrow("USUARIO", null, contentValues);
    }

    public void excluir(int codigo) {

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(codigo);

        conexao.delete("USUARIO", "CODIGO = ?", parametros);
    }

    public void update(User usuario) throws Exception {


        ContentValues contentValues = new ContentValues();
        contentValues.put("NOME", usuario.getUser());
        contentValues.put("SENHA", usuario.getPassword());


        String[] parametros = new String[1];

        parametros[0] = String.valueOf(usuario.getCodigo());

        conexao.update("CLIENTE", contentValues, "CODIGO = ?", parametros);

    }

    public User montaUsuario(Cursor resultado) {

        User usuario = new User();

        if (resultado.getCount() == 0) {
            return null;
        }
        Integer id = resultado.getInt(resultado.getColumnIndex("CODIGO"));
        String nome = resultado.getString(resultado.getColumnIndex("NOME"));
        String senha = resultado.getString(resultado.getColumnIndex("SENHA"));

        usuario.setCodigo(id);
        usuario.setUser(nome);
        usuario.setPassword(senha);

        return usuario;
    }


    public User findByLogin(String usuario, String senha) {

        String sql = "SELECT * FROM USUARIO WHERE NOME = ? AND SENHA = ?";

        String[] selectionArgs = new String[]{usuario, senha};

        Cursor resultado = conexao.rawQuery(sql, selectionArgs);

        resultado.moveToFirst();
        return montaUsuario(resultado);

    }

    public boolean validaLogin(String usuario, String senha) throws Exception {

        User user = findByLogin(usuario, senha);

        if (user == null || user.getUser() == null || user.getPassword() == null) {
            return false;
        }

        String informado = usuario + senha;

        String esperado = user.getUser() + user.getPassword();

        if (informado.equals(esperado)) {
            return true;
        }
        return false;
    }

    public User findByPassword(String telefone, String email) {

        String sql = "SELECT * FROM USUARIO WHERE TELEFONE = ? AND EMAIL = ?";

        String[] selectionArgs = new String[]{telefone, email};

        Cursor resultado = conexao.rawQuery(sql, selectionArgs);

        resultado.moveToFirst();
        return montaUsuario(resultado);
    }


}
