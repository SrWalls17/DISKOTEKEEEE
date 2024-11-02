package com.example.diskotekee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Amistades extends AppCompatActivity {

    private TextView tvUsuarios;
    private ImageView btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amistades);

        tvUsuarios = findViewById(R.id.tv_usuarios);
        btnRegresar = findViewById(R.id.regreso);

        // Cargar usuarios con amistades activadas
        cargarAmistades();

        // Acción del botón de regreso
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Amistades.this, Registro.class); // Cambia esto según sea necesario
                startActivity(intent);
                finish();
            }
        });
    }

    private void cargarAmistades() {
        ConexionDbHelper helper = new ConexionDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Consulta para obtener los usuarios con amistades activadas
        String[] columns = {"NOMBRE", "APELLIDO", "EMAIL"};
        String selection = "AMISTADES = 1"; // Cambia esto a 1 para amigos activados
        Cursor cursor = db.query("USUARIOS", columns, selection, null, null, null, null);

        // Imprimir los nombres de las columnas para depuración
        String[] columnNames = cursor.getColumnNames();
        for (String columnName : columnNames) {
            Log.d("Column Name", columnName); // Asegúrate de tener permisos de logging
        }

        // Verifica si hay resultados
        if (cursor.getCount() > 0) {
            StringBuilder usuariosList = new StringBuilder();
            while (cursor.moveToNext()) {
                int nombreIndex = cursor.getColumnIndex("NOMBRE");
                int apellidoIndex = cursor.getColumnIndex("APELLIDO");
                int emailIndex = cursor.getColumnIndex("EMAIL");

                // Verifica si los índices son válidos
                if (nombreIndex != -1 && apellidoIndex != -1 && emailIndex != -1) {
                    String nombre = cursor.getString(nombreIndex);
                    String apellido = cursor.getString(apellidoIndex);
                    String email = cursor.getString(emailIndex);
                    usuariosList.append("Nombre: ").append(nombre)
                            .append(" ").append(apellido).append(" - Email: ").append(email).append("\n");
                } else {
                    Log.e("Column Index Error", "Uno de los índices es -1");
                }
            }
            tvUsuarios.setText(usuariosList.toString());
        } else {
            // Mostrar mensaje si no hay usuarios
            Toast.makeText(this, "No hay usuarios con amistades activadas", Toast.LENGTH_SHORT).show();
        }

        // Cerrar el cursor y la base de datos
        cursor.close();
        db.close();
    }
}
