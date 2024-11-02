package com.example.diskotekee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionDbHelper extends SQLiteOpenHelper {

    // SQL para crear la tabla USUARIOS
    String sqlUsuarios = "CREATE TABLE USUARIOS (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "NOMBRE TEXT, " +
            "APELLIDO TEXT, " +
            "EMAIL TEXT UNIQUE, " +  // Agregar UNIQUE para evitar correos duplicados
            "CLAVE TEXT, " +
            "MATCHS INTEGER DEFAULT 0, " +
            "AMISTADES INTEGER DEFAULT 0" +
            ")";

    // SQL para crear la tabla FEEDBACK
    String sqlFeedback = "CREATE TABLE FEEDBACK (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "USUARIO_ID INTEGER, " + // Clave foránea para referenciar al usuario
            "COMENTARIO TEXT, " +
            "ESTRELLAS INTEGER, " +
            "FOREIGN KEY(USUARIO_ID) REFERENCES USUARIOS(ID) ON DELETE CASCADE" +
            ")";

    public ConexionDbHelper(@Nullable Context context) {
        super(context, "diskotekee.db", null, 1); // Cambiar el nombre de la base de datos si es necesario
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Crear las tablas al crear la base de datos
        sqLiteDatabase.execSQL(sqlUsuarios);
        sqLiteDatabase.execSQL(sqlFeedback); // Crear la tabla FEEDBACK
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Borrar las tablas si existen y crearlas nuevamente (se pierde la información)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS FEEDBACK");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USUARIOS");
        onCreate(sqLiteDatabase); // Crear tablas nuevamente
    }

    // Método para obtener usuario por correo
    public Cursor obtenerUsuarioPorEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM USUARIOS WHERE EMAIL = ?", new String[]{email});
    }

    // Método para agregar un nuevo usuario (ejemplo)
    public long agregarUsuario(String nombre, String apellido, String email, String clave) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NOMBRE", nombre);
        values.put("APELLIDO", apellido);
        values.put("EMAIL", email);
        values.put("CLAVE", clave);
        return db.insert("USUARIOS", null, values);
    }

    // Aquí podrías agregar otros métodos según lo que necesites (actualizar, eliminar, etc.)
}
