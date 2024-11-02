package com.example.diskotekee;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Busqueda extends AppCompatActivity {

    TextView tvUsuarios;
    ImageView ivAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        tvUsuarios = findViewById(R.id.tv_usuarios);
        ivAtras = findViewById(R.id.regreso);

        // Cargar los usuarios desde la base de datos y mostrarlos en el TextView
        cargarUsuarios();

        // Configurar el clic en la imagen "Atras" para regresar a la actividad Registro
        ivAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Regresar a la actividad Registro
                Intent intent = new Intent(Busqueda.this, Registro.class);
                startActivity(intent);
                finish(); // Finaliza la actividad actual
            }
        });
    }

    private void cargarUsuarios() {
        ConexionDbHelper helper = new ConexionDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = null;
        StringBuilder usuarios = new StringBuilder();

        try {
            cursor = db.rawQuery("SELECT * FROM USUARIOS", null);

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No hay usuarios registrados", Toast.LENGTH_SHORT).show();
                return;
            }

            // Recorre el cursor y añade cada registro a la variable StringBuilder
            while (cursor.moveToNext()) {
                // Obtener valores de las columnas de la tabla USUARIOS
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("ID")); // Obtener el ID
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE"));
                String apellido = cursor.getString(cursor.getColumnIndexOrThrow("APELLIDO"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"));
                String clave = cursor.getString(cursor.getColumnIndexOrThrow("CLAVE"));
                int match = cursor.getInt(cursor.getColumnIndexOrThrow("MATCHS"));
                int amistades = cursor.getInt(cursor.getColumnIndexOrThrow("AMISTADES"));

                // Añadir la información al StringBuilder, incluyendo el ID
                usuarios.append("ID: ").append(id).append("\n")  // Mostrar el ID
                        .append("Nombre: ").append(nombre).append("\n")
                        .append("Apellido: ").append(apellido).append("\n")
                        .append("Email: ").append(email).append("\n")
                        .append("Clave: ").append(clave).append("\n")
                        .append("Matchs: ").append(match).append("\n")
                        .append("Amistades: ").append(amistades).append("\n\n"); // Añadir un salto de línea entre usuarios
            }

            // Muestra los usuarios en el TextView
            tvUsuarios.setText(usuarios.toString());

        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar usuarios: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close(); // Cierra el cursor después de usarlo
            }
            db.close(); // Cierra la conexión a la base de datos
        }
    }
}
