package com.myself.todo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.myself.todo.Beans.Events;

public class ObjRepository {

    private static final String nome_banco = "agenda.db";
    private SQLiteDatabase banco;
    private DadosOpenHelper bancoListaOpenHelper;
    private String tblname = "Agenda";

    public ObjRepository(Context context) {
        bancoListaOpenHelper = new DadosOpenHelper(context);
    }

    public void abrir() throws SQLException {
        banco = bancoListaOpenHelper.getWritableDatabase();
    }

    public void fecha() {
        if (banco != null) banco.close();
    }

    public void inserir(Events events, String usuario) {
        String status = "N";
        ContentValues novoEvento = new ContentValues();
        if (events.getEvento().equals("") || events.getDescricao().equals("")) {
            return;
        }
        novoEvento.put("item", events.getEvento());
        novoEvento.put("descricao", events.getDescricao());
        novoEvento.put("status", status);
        abrir();
        banco.insert(tblname, null, novoEvento);
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


    public Cursor obterEventos(String usuario) {
        return banco.query(tblname, null, "STATUS = 'N'  ", null, null, null, "ITEM");
    }

    public Cursor obterall(String usuario) {
        return banco.query(tblname, null, null, null, null, null, "ITEM");
    }

    public Cursor obterEventosconcluidos(String usuario) {
        return banco.query(tblname, null, "STATUS = 'C'  ", null, null, null, "ITEM");
    }

    public Cursor obterFavoritos(String usuario) {
        return banco.query(tblname, null, "STATUS = 'F' ", null, null, null, "ITEM");
    }

    public Cursor obterUmEvento(long id) {
        return banco.query(tblname, null, "_id = " + id, null, null, null, "ITEM");
    }


    public Events criaevento(Cursor resultado) {

        Events events = new Events();
        if (resultado.getCount() == 0) {


            return null;
        }

        String evento = resultado.getString(resultado.getColumnIndexOrThrow("ITEM"));
        String descricao = resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO"));
        String dia = resultado.getString(resultado.getColumnIndexOrThrow("DIA"));
        int id = resultado.getInt(resultado.getColumnIndexOrThrow("_id"));

        events.setId(id);
        events.setData(dia);
        events.setEvento(evento);
        events.setDescricao(descricao);

        return events;


    }
}
