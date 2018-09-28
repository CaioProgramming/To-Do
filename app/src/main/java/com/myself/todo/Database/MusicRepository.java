package com.myself.todo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaMetadataRetriever;

import com.myself.todo.Beans.Music;

public class MusicRepository {

    private static final String nome_banco = "agenda.db";
    private SQLiteDatabase banco;
    private DadosOpenHelper bancoListaOpenHelper;
    private String tblname = "Musicas";

    public MusicRepository(Context context) {
        bancoListaOpenHelper = new DadosOpenHelper(context);
    }

    public boolean findMusic(String uri) {

        String sql = "SELECT * FROM Musicas WHERE Musica = ?";

        String[] selectionArgs = new String[]{uri};
        abrir();
        Cursor resultado = banco.rawQuery(sql, selectionArgs);
        return resultado != null;


    }


    public void abrir() throws SQLException {
        banco = bancoListaOpenHelper.getWritableDatabase();
    }

    public void fecha() {
        if (banco != null) banco.close();
    }

    public void inserir(Music music) {
        String status = "F";
        ContentValues novaMusica = new ContentValues();
        novaMusica.put("Musica", music.getMusicuri());
        novaMusica.put("STATUS", status);
        abrir();
        banco.insert(tblname, null, novaMusica);

    }

    public Cursor Verificar(Music music) {
        return banco.query(tblname, null, "Musica = '" + music.getMusicuri() + "'", null, null, null, null);

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


    public void favoritar(String uri) {
        String favorito = "F";
        ContentValues MusicaAlterada = new ContentValues();
        MusicaAlterada.put("STATUS", favorito);
        abrir();
        banco.update(tblname, MusicaAlterada, "Musica = '" + uri + "'", null);
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


    public void unfavoritar(String uri) {
        String favorito = "N";
        ContentValues MusicaAlterada = new ContentValues();
        MusicaAlterada.put("STATUS", favorito);
        abrir();
        banco.update(tblname, MusicaAlterada, "Musica = '" + uri + "'", null);
        fecha();

    }


    public Cursor obterMusicas(String usuario) {
        return banco.query(tblname, null, null, null, null, null, null);
    }


    public Cursor obterFavoritos(String usuario) {
        return banco.query(tblname, null, "STATUS = 'F' ", null, null, null, "DIA");
    }

    public Cursor obterUmEvento(long id) {
        return banco.query(tblname, null, "_id = " + id, null, null, null, "DIA");
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

        String uri = resultado.getString(resultado.getColumnIndexOrThrow("Musica"));
        String descricao = resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO"));
        String dia = resultado.getString(resultado.getColumnIndexOrThrow("DIA"));
        int id = resultado.getInt(resultado.getColumnIndexOrThrow("_id"));
        String status = resultado.getString(resultado.getColumnIndexOrThrow("STATUS"));
        MediaMetadataRetriever myRetriever = new MediaMetadataRetriever();

        music.setMusicuri(uri);

        System.out.println(music.getMusicuri());
        music.setId(id);
        music.setStatus(status);
        music.setDia(dia);
        music.setDescription(descricao);
        music.setMusic(myRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        music.setAlbum(myRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));




        return music;


    }

}
