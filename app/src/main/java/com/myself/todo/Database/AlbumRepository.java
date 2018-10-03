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
        if (album.getFotouri().equals("") || album.getDescription().equals("") || usuario == null || usuario.equals("")) {
            System.out.println("usuario nao informado");

            return;
        }
        novaFoto.put("FOTO", album.getFotouri());
        novaFoto.put("DESCRICAO", album.getDescription());
        novaFoto.put("STATUS ", status);
        novaFoto.put("USUARIO", usuario);
        abrir();
        banco.insert(tblname, null, novaFoto);
        fecha();
    }

    public void apagar(long id, String usuario) {
        abrir();
        banco.delete(tblname, "_id = " + id + " and USUARIO = ?", new String[]{usuario});
        fecha();


    }

    public void apagarAll(String usuario) {
        abrir();
        banco.delete(tblname, "USUARIO = ?", new String[]{usuario});
        fecha();


    }

    public void UpdateUser(String usuarioantigo, String usuarionovo) {
        ContentValues produtoAlterado = new ContentValues();
        produtoAlterado.put("USUARIO", usuarionovo);
        abrir();
        banco.update(tblname, produtoAlterado, "USUARIO = ? ", new String[]{usuarioantigo});
        fecha();



    }


    public void favoritar(long id, String usuario) {
        String favorito= "F";
        ContentValues FotoAlterada = new ContentValues();
        FotoAlterada.put("status", favorito);
        abrir();
        banco.update(tblname, FotoAlterada, "_id = " + id + " and USUARIO = ?", new String[]{usuario});
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


    public void unfavoritar(long id, String usuario) {
        String favorito= "N";
        ContentValues FotoAlterada = new ContentValues();
        FotoAlterada.put("status", favorito);
        abrir();
        banco.update(tblname, FotoAlterada, "_id = " + id + " and USUARIO = ?", new String[]{usuario});
        fecha();

    }


    public Cursor obterFotos(String usuario) {
        return banco.query(tblname, null, "USUARIO = ?", new String[]{usuario}, null, null, "STATUS");
    }

    public Cursor obterFotosRecentes(String usuario) {
        return banco.query(tblname, null, "USUARIO = ?", new String[]{usuario}, null, null, "HORA");
    }


    public Cursor obterFavoritos(String usuario) {
        return banco.query(tblname, null, "USUARIO = ? AND STATUS = 'F' ", new String[]{usuario}, null, null, "FOTO");
    }

    public Cursor obterUmEvento(long id){
        return banco.query(tblname,null,"_id = "+id,null,null,null,"ITEM");
    }

    public int contar(String usuario) {
        abrir();
        int count;
        Cursor mCount = banco.query(tblname, null, "USUARIO = ? ", new String[]{usuario}, null, null, null);
        mCount.moveToFirst();
        if (mCount.getCount() == 0) {
            count = 0;
            return count;
        } else {
            count = mCount.getInt(0);
            mCount.close();
            fecha();
            return count;
        }
    }

    public Album criafoto(Cursor resultado) {

        Album album = new Album();
        if(resultado.getCount() == 0){


            return null;
        }

        String uri = resultado.getString(resultado.getColumnIndexOrThrow("FOTO"));
        String descricao = resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO"));
        String dia = resultado.getString(resultado.getColumnIndexOrThrow("DIA"));
        String status = resultado.getString(resultado.getColumnIndexOrThrow("STATUS"));
        int id = resultado.getInt(resultado.getColumnIndexOrThrow("_id"));

        album.setId(String.valueOf(id));
        album.setDia(dia);
        album.setFotouri(uri);
        album.setDescription(descricao);
        album.setStatus(status);

        return album;



    }
}
