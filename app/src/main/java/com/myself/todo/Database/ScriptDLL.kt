package com.myself.todo.Database

import org.junit.runner.RunWith

object ScriptDLL {
    fun getCreateTableCliente(): String? {
        val sql = StringBuilder()
        sql.append("CREATE TABLE IF NOT EXISTS USUARIO( ")
        sql.append("CODIGO INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,")
        sql.append("NOME VARCHAR(200) NOT NULL DEFAULT '', ")
        sql.append("SENHA VARCHAR(200) NOT NULL DEFAULT '',PROFILEPIC VARCHAR(200) DEFAULT '') ")
        return sql.toString()
    }

    fun getCreateTableAgenda(): String? {
        val sql = StringBuilder()
        sql.append("CREATE TABLE IF NOT EXISTS Agenda( ")
        sql.append("_id INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,")
        sql.append("ITEM VARCHAR(200) NOT NULL,DESCRICAO VARCHAR(50)," +
                "STATUS CHAR(1) NOT NULL DEFAULT 'N', DIA DATE default CURRENT_DATE, HORA DATETIME DEFAULT (datetime('now','localtime')),USUARIO VARCHAR(40)) ")
        return sql.toString()
    }

    fun getCreateTableFotos(): String? {
        val sql = StringBuilder()
        sql.append("CREATE TABLE IF NOT EXISTS Album( ")
        sql.append("_id INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,")
        sql.append("FOTO VARCHAR(200) NOT NULL,DESCRICAO VARCHAR(50), STATUS CHAR(1) NOT NULL DEFAULT 'N'," +
                "DIA DATE default CURRENT_DATE, USUARIO VARCHAR(40),HORA DATETIME DEFAULT (datetime('now','localtime'))) ")
        return sql.toString()
    }

    fun getCreateTableMusicas(): String? {
        val sql = StringBuilder()
        sql.append("CREATE TABLE IF NOT EXISTS Musicas( ")
        sql.append("_id INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,")
        sql.append("Musica  VARCHAR(500) NOT NULL,DESCRICAO VARCHAR(50),STATUS CHAR(1) DEFAULT 'N'," +
                "DIA DATE default CURRENT_DATE, USUARIO VARCHAR(40)) ")
        return sql.toString()
    }
}