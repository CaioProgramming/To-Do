package com.myself.todo.Database;

public class ScriptDLL {
    public static String getCreateTableCliente(){

        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE IF NOT EXISTS USUARIO( ");
        sql.append(     "CODIGO INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,");
        sql.append(     "NOME VARCHAR(200) NOT NULL DEFAULT '', ");
        sql.append("SENHA VARCHAR(200) NOT NULL DEFAULT '',PROFILEPIC VARCHAR(200) DEFAULT '') ");


        return  sql.toString();

    }


    public static String getCreateTableAgenda() {
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE IF NOT EXISTS Agenda( ");
        sql.append(     "_id INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,");
        sql.append(     "ITEM VARCHAR(200) NOT NULL,DESCRICAO VARCHAR(50)," +
                    "STATUS CHAR(1) NOT NULL DEFAULT 'N', DIA DATE default CURRENT_DATE, USUARIO VARCHAR(40)) ");

        return sql.toString();
    }
}
