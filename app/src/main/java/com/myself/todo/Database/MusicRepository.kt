package com.myself.todo.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.media.MediaMetadataRetriever
import com.myself.todo.Beans.Music
import org.junit.runner.RunWith

class MusicRepository(context: Context?) {
    private var banco: SQLiteDatabase? = null
    private val bancoListaOpenHelper: DadosOpenHelper?
    private val tblname: String? = "Musicas"
    fun findMusic(uri: String?): Boolean {
        val sql = "SELECT * FROM Musicas WHERE Musica = ?"
        val selectionArgs = arrayOf(uri)
        abrir()
        val resultado = banco.rawQuery(sql, selectionArgs)
        return resultado != null
    }

    @Throws(SQLException::class)
    fun abrir() {
        banco = bancoListaOpenHelper.getWritableDatabase()
    }

    fun fecha() {
        if (banco != null) banco.close()
    }

    fun inserir(music: Music?) {
        val status = "F"
        val novaMusica = ContentValues()
        novaMusica.put("Musica", music.getMusicuri())
        novaMusica.put("STATUS", status)
        abrir()
        banco.insert(tblname, null, novaMusica)
    }

    fun Verificar(music: Music?): Cursor? {
        return banco.query(tblname, null, "Musica = '" + music.getMusicuri() + "'", null, null, null, null)
    }

    fun alteraEvento(id: Long, nome: String?) {
        val produtoAlterado = ContentValues()
        produtoAlterado.put("item", nome)
        abrir()
        banco.update(tblname, produtoAlterado, "_id = $id", null)
        fecha()
    }

    fun apagar(id: Long) {
        abrir()
        banco.delete(tblname, "_id = $id", null)
        fecha()
    }

    fun favoritar(uri: String?) {
        val favorito = "F"
        val MusicaAlterada = ContentValues()
        MusicaAlterada.put("STATUS", favorito)
        abrir()
        banco.update(tblname, MusicaAlterada, "Musica = '$uri'", null)
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

    fun unfavoritar(uri: String?) {
        val favorito = "N"
        val MusicaAlterada = ContentValues()
        MusicaAlterada.put("STATUS", favorito)
        abrir()
        banco.update(tblname, MusicaAlterada, "Musica = '$uri'", null)
        fecha()
    }

    fun obterMusicas(usuario: String?): Cursor? {
        return banco.query(tblname, null, null, null, null, null, null)
    }

    fun obterFavoritos(usuario: String?): Cursor? {
        return banco.query(tblname, null, "STATUS = 'F' ", null, null, null, "DIA")
    }

    fun obterUmEvento(id: Long): Cursor? {
        return banco.query(tblname, null, "_id = $id", null, null, null, "DIA")
    }

    fun countEventos(): Int {
        abrir()
        val mCount = banco.rawQuery("select count(*) from Lista", null)
        mCount.moveToFirst()
        val count = mCount.getInt(0)
        mCount.close()
        fecha()
        return count
    }

    fun criamusic(resultado: Cursor?): Music? {
        val music = Music()
        if (resultado.getCount() == 0) {
            return null
        }
        val uri = resultado.getString(resultado.getColumnIndexOrThrow("Musica"))
        val descricao = resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO"))
        val dia = resultado.getString(resultado.getColumnIndexOrThrow("DIA"))
        val id = resultado.getInt(resultado.getColumnIndexOrThrow("_id"))
        val status = resultado.getString(resultado.getColumnIndexOrThrow("STATUS"))
        val myRetriever = MediaMetadataRetriever()
        music.musicuri = uri
        println(music.musicuri)
        music.id = id
        music.status = status
        music.dia = dia
        music.description = descricao
        music.music = myRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        music.album = myRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        return music
    }

    companion object {
        private val nome_banco: String? = "agenda.db"
    }

    init {
        bancoListaOpenHelper = DadosOpenHelper(context)
    }
}