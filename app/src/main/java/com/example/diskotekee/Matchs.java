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

public class Matchs extends AppCompatActivity {

    private TextView tvUsuarios;
    private ImageView btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchs);

        tvUsuarios = findViewById(R.id.tv_usuarios);
        btnRegresar = findViewById(R.id.regreso);

        // Llenar el TextView con usuarios que tienen activado el Match
        mostrarUsuariosConMatch();

        // Acción del botón de regresar
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Matchs.this, Principal.class); // Cambia esto según sea necesario
                startActivity(intent);
                finish();
            }
        });
    }

    private void mostrarUsuariosConMatch() {
        // Crear una cadena para almacenar los usuarios
        StringBuilder usuariosConMatch = new StringBuilder();

        // Conectar a la base de datos
        ConexionDbHelper helper = new ConexionDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Realizar la consulta para obtener los usuarios que tienen activado "Match"
        String[] columns = {"ID", "NOMBRE", "APELLIDO", "EMAIL"}; // Asegúrate de que estos sean los nombres correctos de tus columnas
        String selection = "MATCHS = ?"; // Suponiendo que tienes una columna 'MATCHS'
        String[] selectionArgs = {"1"}; // Suponiendo que "1" significa activado

        Cursor cursor = db.query("USUARIOS", columns, selection, selectionArgs, null, null, null);

        // Verificar que el cursor no esté vacío
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("ID");
                int nombreIndex = cursor.getColumnIndex("NOMBRE");
                int apellidoIndex = cursor.getColumnIndex("APELLIDO");
                int emailIndex = cursor.getColumnIndex("EMAIL");

                // Comprobar que los índices son válidos antes de usarlos
                if (idIndex != -1 && nombreIndex != -1 && apellidoIndex != -1 && emailIndex != -1) {
                    String id = cursor.getString(idIndex);
                    String nombre = cursor.getString(nombreIndex);
                    String apellido = cursor.getString(apellidoIndex);
                    String email = cursor.getString(emailIndex);

                    usuariosConMatch.append("ID: ").append(id)
                            .append(", Nombre: ").append(nombre)
                            .append(" ").append(apellido)
                            .append(", Email: ").append(email).append("\n");
                } else {
                    // Si alguno de los índices es -1, imprimir un mensaje de error
                    Toast.makeText(this, "Error: Columnas no encontradas.", Toast.LENGTH_SHORT).show();
                }
            } while (cursor.moveToNext());
        } else {
            // Si el cursor es nulo o no tiene resultados
            Toast.makeText(this, "No hay usuarios con Match activado.", Toast.LENGTH_SHORT).show();
        }

        // Cerrar el cursor y la base de datos
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        // Establecer el texto en el TextView
        if (usuariosConMatch.length() > 0) {
            tvUsuarios.setText(usuariosConMatch.toString());
        } else {
            // Mostrar un mensaje Toast si no hay usuarios con "Match" activado
            Toast.makeText(this, "No hay usuarios con Match activado.", Toast.LENGTH_SHORT).show();
        }
    }

}
