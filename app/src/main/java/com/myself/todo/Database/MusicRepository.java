package com.myself.todo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.myself.todo.Beans.Music;

public class MusicRepository {

    private static final String nome_banco = "agenda.db";
    private SQLiteDatabase banco;
    private DadosOpenHelper bancoListaOpenHelper;
    private String tblname = "Musicas";

    public MusicRepository(Context context) {
        bancoListaOpenHelper = new DadosOpenHelper(context);
    }

    public void abrir() throws SQLException {
        banco = bancoListaOpenHelper.getWritableDatabase();
    }

    public void fecha() {
        if (banco != null) banco.close();
    }

    public void inserir(Music music) {
        String status = "N";
        ContentValues novaFoto = new ContentValues();
        if (music.getMusic().equals(null) || music.getDescription().equals("")) {
            return;
        }
        novaFoto.put("Musica", music.getMusicuri());
        novaFoto.put("DESCRICAO", music.getDescription());
        novaFoto.put("STATUS ", status);
        abrir();
        banco.insert(tblname, null, novaFoto);
        fecha();
    }

    public void alteraEvento(long id, String nome) {
        ContentValues produtoAlterado = new ContentValues();
        produtoAlterado.put("item", nome);
        abrir();
        banco.update(tblname, produtoAlterado, "_id = " + id, null);
        fecha();
    }

    public void apagar(long id) {
        abrir();
        banco.delete(tblname, "_id = " + id, null);
        fecha();


    }


    public void favoritar(long id) {
        String favorito = "F";
        ContentValues produtoAlterado = new ContentValues();
        produtoAlterado.put("status", favorito);
        abrir();
        banco.update(tblname, produtoAlterado, "_id = " + id, null);
        fecha();

    }

    public void concluir(long id) {
        String favorito = "C";
        ContentValues produtoAlterado = new ContentValues();
        produtoAlterado.put("status", favorito);
        abrir();
        banco.update(tblname, produtoAlterado, "_id = " + id, null);
        fecha();

    }


    public void unfavoritar(long id) {
        String favorito = "N";
        ContentValues produtoAlterado = new ContentValues();
        produtoAlterado.put("status", favorito);
        abrir();
        banco.update(tblname, produtoAlterado, "_id = " + id, null);
        fecha();

    }


    public Cursor obterFotos(String usuario) {
        return banco.query(tblname, null, "STATUS = 'N'  ", null, null, null, "FOTO");
    }


    public Cursor obterFavoritos(String usuario) {
        return banco.query(tblname, null, "STATUS = 'F' ", null, null, null, "FOTO");
    }

    public Cursor obterUmEvento(long id) {
        return banco.query(tblname, null, "_id = " + id, null, null, null, "ITEM");
    }

    public int countEventos() {
        abrir();
        Cursor mCount = banco.rawQuery("select count(*) from Lista", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        fecha();
        return count;
    }

    public Music criamusic(Cursor resultado) {

        Music music = new Music();
        if (resultado.getCount() == 0) {


            return null;
        }

        String uri = resultado.getString(resultado.getColumnIndexOrThrow("FOTO"));
        String descricao = resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO"));
        String dia = resultado.getString(resultado.getColumnIndexOrThrow("DIA"));
        int id = resultado.getInt(resultado.getColumnIndexOrThrow("_id"));

        music.setMusic(uri);
        music.setDia(dia);
        music.setDescription(descricao);

        return music;


    }
}
