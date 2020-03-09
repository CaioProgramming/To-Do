package com.myself.todo.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.junit.runner.RunWith

class DadosOpenHelper(context: Context?) : SQLiteOpenHelper(context, name, null, version) {
    protected var database: SQLiteDatabase? = null
    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        sqLiteDatabase.execSQL(ScriptDLL.getCreateTableCliente())
        sqLiteDatabase.execSQL(ScriptDLL.getCreateTableAgenda())
        sqLiteDatabase.execSQL(ScriptDLL.getCreateTableFotos())
        sqLiteDatabase.execSQL(ScriptDLL.getCreateTableMusicas())
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, i: Int, i1: Int) {}

    companion object {
        private const val version = 1
        private val name: String? = "agenda.db"
    }
}