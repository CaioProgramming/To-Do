package com.myself.todo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.myself.todo.Beans.Album;

public class AlbumRepository {

    private static final String nome_banco = "agenda.db";
    private SQLiteDatabase banco;
    private DadosOpenHelper bancoListaOpenHelper;
    private String tblname = "Album";

    public AlbumRepository(Context context) {
        bancoListaOpenHelper = new DadosOpenHelper(context);
    }

    public void abrir() throws SQLException {
        banco = bancoListaOpenHelper.getWritableDatabase();
    }

    public void fecha() {
        if (banco != null) banco.close();
    }

    public void inserir(Album album, String usuario) {
        String status = "N";
        ContentValues novaFoto = new ContentValues();
        if (album.getFotouri().equals("") || album.getDescription().equals("")) {
            return;
        }
        novaFoto.put("FOTO", album.getFotouri());
        novaFoto.put("DESCRICAO", album.getDescription());
        novaFoto.put("STATUS ", status);
        abrir();
        banco.insert(tblname, null, novaFoto);
        fecha();
    }
    public void alteraEvento(long id,String nome){
        ContentValues produtoAlterado = new ContentValues();
        produtoAlterado.put("item",nome);
        abrir();
        banco.update(tblname,produtoAlterado,"_id = "+ id,null);
        fecha();
    }
    public void apagar(long id){
        abrir();
        banco.delete(tblname, "_id = " +id, null);
        fecha();



    }


    public void favoritar(long id){
        String favorito= "F";
        ContentValues produtoAlterado = new ContentValues();
        produtoAlterado.put("status",favorito);
        abrir();
        banco.update(tblname,produtoAlterado,"_id = "+ id,null);
        fecha();

    }

    public void concluir(long id){
        String favorito= "C";
        ContentValues produtoAlterado = new ContentValues();
        produtoAlterado.put("status",favorito);
        abrir();
        banco.update(tblname,produtoAlterado,"_id = "+ id,null);
        fecha();

    }


    public void unfavoritar(long id){
        String favorito= "N";
        ContentValues produtoAlterado = new ContentValues();
        produtoAlterado.put("status",favorito);
        abrir();
        banco.update(tblname,produtoAlterado,"_id = "+ id,null);
        fecha();

    }


    public Cursor obterFotos(String usuario) {
        return banco.query(tblname, null, "STATUS = 'N'  ", null, null, null, "FOTO");
    }


    public Cursor obterFavoritos(String usuario) {
        return banco.query(tblname, null, "STATUS = 'F' ", null, null, null, "FOTO");
    }

    public Cursor obterUmEvento(long id){
        return banco.query(tblname,null,"_id = "+id,null,null,null,"ITEM");
    }

    public int countEventos(){
        abrir();
        Cursor mCount= banco.rawQuery("select count(*) from Lista" , null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();
        fecha();
         return count;
    }

    public Album criafoto(Cursor resultado) {

        Album album = new Album();
        if(resultado.getCount() == 0){


            return null;
        }

        String uri = resultado.getString(resultado.getColumnIndexOrThrow("FOTO"));
        String descricao = resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO"));
        String dia = resultado.getString(resultado.getColumnIndexOrThrow("DIA"));
        int id = resultado.getInt(resultado.getColumnIndexOrThrow("_id"));

        album.setId(id);
        album.setDia(dia);
        album.setFotouri(uri);
        album.setDescription(descricao);

        return album;



    }
}
