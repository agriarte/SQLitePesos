package com.example.pedro.sqlitepesos.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pedro on 14/09/2017.
 */

public class PesoDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pesobasedatos.db";
    public static final int DATABASE_VERSION = 1;

    public PesoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PESO_TABLE = "CREATE TABLE " + PesoContract.PesoEntry.TABLE_NAME + " ("
                +PesoContract.PesoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +PesoContract.PesoEntry.COLUM_PESO_FECHA + " TEXT NOT NULL, "
                +PesoContract.PesoEntry.COLUM_PESO_PESO + " TEXT );";
        db.execSQL(SQL_CREATE_PESO_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }




}
