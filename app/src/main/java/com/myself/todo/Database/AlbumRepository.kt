package com.myself.todo.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.myself.todo.Beans.Album
import org.junit.runner.RunWith

class AlbumRepository(context: Context?) {
    private var banco: SQLiteDatabase? = null
    private val bancoListaOpenHelper: DadosOpenHelper?
    private val tblname: String? = "Album"
    @Throws(SQLException::class)
    fun abrir() {
        banco = bancoListaOpenHelper.getWritableDatabase()
    }

    fun fecha() {
        if (banco != null) banco.close()
    }

    fun inserir(album: Album?, usuario: String?) {
        val status = "N"
        val novaFoto = ContentValues()
        if (album.getFotouri() == "" || album.getDescription() == "" || usuario == null || usuario == "") {
            println("usuario nao informado")
            return
        }
        novaFoto.put("FOTO", album.getFotouri())
        novaFoto.put("DESCRICAO", album.getDescription())
        novaFoto.put("STATUS ", status)
        novaFoto.put("USUARIO", usuario)
        abrir()
        banco.insert(tblname, null, novaFoto)
        fecha()
    }

    fun apagar(id: Long, usuario: String?) {
        abrir()
        banco.delete(tblname, "_id = $id and USUARIO = ?", arrayOf(usuario))
        fecha()
    }

    fun apagarAll(usuario: String?) {
        abrir()
        banco.delete(tblname, "USUARIO = ?", arrayOf(usuario))
        fecha()
    }

    fun UpdateUser(usuarioantigo: String?, usuarionovo: String?) {
        val produtoAlterado = ContentValues()
        produtoAlterado.put("USUARIO", usuarionovo)
        abrir()
        banco.update(tblname, produtoAlterado, "USUARIO = ? ", arrayOf(usuarioantigo))
        fecha()
    }

    fun favoritar(id: Long, usuario: String?) {
        val favorito = "F"
        val FotoAlterada = ContentValues()
        FotoAlterada.put("status", favorito)
        abrir()
        banco.update(tblname, FotoAlterada, "_id = $id and USUARIO = ?", arrayOf(usuario))
        fecha()
    }

    fun concluir(id: Long) {
        val favorito = "C"
        val produtoAlterado = ContentValues()
        produtoAlterado.put("status", favorito)
        abrir()
        banco.update(tblname, produtoAlterado, "_id = $id", null)
        fecha()
    }

    fun unfavoritar(id: Long, usuario: String?) {
        val favorito = "N"
        val FotoAlterada = ContentValues()
        FotoAlterada.put("status", favorito)
        abrir()
        banco.update(tblname, FotoAlterada, "_id = $id and USUARIO = ?", arrayOf(usuario))
        fecha()
    }

    fun obterFotos(usuario: String?): Cursor? {
        return banco.query(tblname, null, "USUARIO = ?", arrayOf(usuario), null, null, "STATUS")
    }

    fun obterFotosRecentes(usuario: String?): Cursor? {
        return banco.query(tblname, null, "USUARIO = ?", arrayOf(usuario), null, null, "HORA")
    }

    fun obterFavoritos(usuario: String?): Cursor? {
        return banco.query(tblname, null, "USUARIO = ? AND STATUS = 'F' ", arrayOf(usuario), null, null, "FOTO")
    }

    fun obterUmEvento(id: Long): Cursor? {
        return banco.query(tblname, null, "_id = $id", null, null, null, "ITEM")
    }

    fun contar(usuario: String?): Int {
        abrir()
        val count: Int
        val mCount = banco.query(tblname, null, "USUARIO = ? ", arrayOf(usuario), null, null, null)
        mCount.moveToFirst()
        return if (mCount.count == 0) {
            count = 0
            count
        } else {
            count = mCount.getInt(0)
            mCount.close()
            fecha()
            count
        }
    }

    fun criafoto(resultado: Cursor?): Album? {
        val album = Album()
        if (resultado.getCount() == 0) {
            return null
        }
        val uri = resultado.getString(resultado.getColumnIndexOrThrow("FOTO"))
        val descricao = resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO"))
        val dia = resultado.getString(resultado.getColumnIndexOrThrow("DIA"))
        val status = resultado.getString(resultado.getColumnIndexOrThrow("STATUS"))
        val id = resultado.getInt(resultado.getColumnIndexOrThrow("_id"))
        album.id = id.toString()
        album.dia = dia
        album.fotouri = uri
        album.description = descricao
        album.status = status
        return album
    }

    companion object {
        private val nome_banco: String? = "agenda.db"
    }

    init {
        bancoListaOpenHelper = DadosOpenHelper(context)
    }
}