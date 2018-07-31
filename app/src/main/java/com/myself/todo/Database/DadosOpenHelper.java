package com.myself.todo.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DadosOpenHelper extends SQLiteOpenHelper {
    private static int version = 1;
    private static String name = "agenda.db";
    protected SQLiteDatabase database;
    public DadosOpenHelper(Context context ) {
        super(context, name,null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL( ScriptDLL.getCreateTableCliente() );
        sqLiteDatabase.execSQL( ScriptDLL.getCreateTableAgenda() );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
