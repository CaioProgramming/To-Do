package com.myself.todo.Database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.myself.todo.Beans.User
import org.junit.runner.RunWith

class UserRepository(private val conexao: SQLiteDatabase?) {
    fun inserir(usuario: User?) {
        val contentValues = ContentValues()
        contentValues.put("NOME", usuario.getUser())
        contentValues.put("SENHA", usuario.getPassword())
        contentValues.put("PROFILEPIC", usuario.getProfilepic())
        conexao.insertOrThrow("USUARIO", null, contentValues)
    }

    fun excluir(codigo: Int) {
        val parametros = arrayOfNulls<String?>(1)
        parametros[0] = codigo.toString()
        conexao.delete("USUARIO", "CODIGO = ?", parametros)
    }

    fun update(usuario: User?) {
        val contentValues = ContentValues()
        contentValues.put("NOME", usuario.getUser())
        contentValues.put("SENHA", usuario.getPassword())
        contentValues.put("PROFILEPIC", usuario.getProfilepic())
        val parametros = arrayOfNulls<String?>(1)
        parametros[0] = usuario.getCodigo().toString()
        conexao.update("USUARIO", contentValues, "CODIGO = ?", parametros)
    }

    fun montaUsuario(resultado: Cursor?): User? {
        val usuario = User()
        if (resultado.getCount() == 0) {
            return null
        }
        val id = resultado.getInt(resultado.getColumnIndex("CODIGO"))
        val nome = resultado.getString(resultado.getColumnIndex("NOME"))
        val senha = resultado.getString(resultado.getColumnIndex("SENHA"))
        val foto = resultado.getString(resultado.getColumnIndex("PROFILEPIC"))
        usuario.codigo = id
        usuario.user = nome
        usuario.password = senha
        usuario.profilepic = foto
        return usuario
    }

    fun findByLogin(usuario: String?, senha: String?): User? {
        val sql = "SELECT * FROM USUARIO WHERE NOME = ? AND SENHA = ?"
        val selectionArgs = arrayOf(usuario, senha)
        val resultado = conexao.rawQuery(sql, selectionArgs)
        resultado.moveToFirst()
        return montaUsuario(resultado)
    }

    fun validaLogin(usuario: String?, senha: String?): Boolean {
        val user = findByLogin(usuario, senha)
        if (user == null || user.user == null || user.password == null) {
            return false
        }
        val informado = usuario + senha
        val esperado = user.user + user.password
        return informado == esperado
    }

    fun findByPassword(telefone: String?, email: String?): User? {
        val sql = "SELECT * FROM USUARIO WHERE TELEFONE = ? AND EMAIL = ?"
        val selectionArgs = arrayOf(telefone, email)
        val resultado = conexao.rawQuery(sql, selectionArgs)
        resultado.moveToFirst()
        return montaUsuario(resultado)
    }

}