package com.example.diskotekee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionDbHelper extends SQLiteOpenHelper {

    // SQL para crear la tabla USUARIOS
    private static final String sqlUsuarios = "CREATE TABLE USUARIOS (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "NOMBRE TEXT, " +
            "APELLIDO TEXT, " +
            "EMAIL TEXT UNIQUE, " +
            "CLAVE TEXT, " +
            "MATCHS INTEGER DEFAULT 0, " +
            "AMISTADES INTEGER DEFAULT 0" +
            ")";

    // SQL para crear la tabla FEEDBACK
    private static final String sqlFeedback = "CREATE TABLE FEEDBACK (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "USUARIO_ID INTEGER, " +
            "COMENTARIO TEXT, " +
            "ESTRELLAS INTEGER, " +
            "FOREIGN KEY(USUARIO_ID) REFERENCES USUARIOS(ID) ON DELETE CASCADE" +
            ")";

    public ConexionDbHelper(@Nullable Context context) {
        super(context, "diskotekee.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlUsuarios);
        sqLiteDatabase.execSQL(sqlFeedback);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS FEEDBACK");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USUARIOS");
        onCreate(sqLiteDatabase);
    }

    // Método para obtener usuario por correo
    public Cursor obtenerUsuarioPorEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM USUARIOS WHERE EMAIL = ?", new String[]{email});
    }

    // Método para agregar un nuevo usuario
    public long agregarUsuario(String nombre, String apellido, String email, String clave) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NOMBRE", nombre);
        values.put("APELLIDO", apellido);
        values.put("EMAIL", email);
        values.put("CLAVE", clave);
        return db.insert("USUARIOS", null, values);
    }

    // Método para actualizar un usuario, incluyendo matchs y amistades
    public boolean actualizarUsuario(int id, String nombre, String apellido, String email, String clave, int matchs, int amistades) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NOMBRE", nombre);
        values.put("APELLIDO", apellido);
        values.put("EMAIL", email);
        values.put("CLAVE", clave);
        values.put("MATCHS", matchs);
        values.put("AMISTADES", amistades);

        int rowsAffected = db.update("USUARIOS", values, "ID = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0; // Retorna true si al menos una fila fue actualizada
    }

    // Método para eliminar un usuario
    public boolean eliminarUsuario(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("USUARIOS", "ID = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0; // Retorna true si al menos una fila fue eliminada
    }

    // Método para agregar feedback
    public long agregarFeedback(int usuarioId, String comentario, int estrellas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USUARIO_ID", usuarioId);
        values.put("COMENTARIO", comentario);
        values.put("ESTRELLAS", estrellas);
        return db.insert("FEEDBACK", null, values);
    }

    // Método para actualizar feedback
    public boolean actualizarFeedback(int id, String comentario, int estrellas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("COMENTARIO", comentario);
        values.put("ESTRELLAS", estrellas);

        int rowsAffected = db.update("FEEDBACK", values, "ID = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    // Método para obtener feedback por usuario
    public Cursor obtenerFeedbackPorUsuario(int usuarioId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM FEEDBACK WHERE USUARIO_ID = ?", new String[]{String.valueOf(usuarioId)});
    }
}
