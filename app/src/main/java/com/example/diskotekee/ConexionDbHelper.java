package com.example.diskotekee;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionDbHelper extends SQLiteOpenHelper {

    // SQL para crear la tabla USUARIOS con los nuevos campos MATCH y AMISTADES
    String sql = "CREATE TABLE USUARIOS (" +
            "ID INTEGER PRIMARY KEY, " +
            "NOMBRE TEXT, " +
            "APELLIDO TEXT, " +
            "EMAIL TEXT, " +
            "CLAVE TEXT, " +
            "MATCHS INTEGER DEFAULT 0, " +  // Agregar el campo MATCH con valor por defecto 0
            "AMISTADES INTEGER DEFAULT 0" +  // Agregar el campo AMISTADES con valor por defecto 0
            ")";

    public ConexionDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Crear la tabla USUARIOS al crear la base de datos
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Borrar la tabla USUARIOS si existe y crearla nuevamente (se pierde la información)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USUARIOS");
        sqLiteDatabase.execSQL(sql);
    }
}
