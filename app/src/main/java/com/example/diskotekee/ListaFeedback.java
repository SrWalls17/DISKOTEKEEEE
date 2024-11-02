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

public class ListaFeedback extends AppCompatActivity {

    private TextView tvFeedback;
    private ImageView btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_feedack);

        tvFeedback = findViewById(R.id.tv_feedback);
        btnRegresar = findViewById(R.id.regreso);

        // Llenar el TextView con los comentarios de feedback
        mostrarComentariosDeFeedback();

        // Acción del botón de regresar
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaFeedback.this, Principal.class); // Cambia según sea necesario
                startActivity(intent);
                finish();
            }
        });
    }

    private void mostrarComentariosDeFeedback() {
        // Crear una cadena para almacenar los comentarios
        StringBuilder comentariosDeFeedback = new StringBuilder();

        // Conectar a la base de datos
        ConexionDbHelper helper = new ConexionDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Realizar la consulta para obtener los comentarios de la tabla FEEDBACK
        String[] columns = {"ID", "COMENTARIO", "ESTRELLAS"};
        Cursor cursor = db.query("FEEDBACK", columns, null, null, null, null, null);

        // Verificar que el cursor no esté vacío
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("ID");
                int comentarioIndex = cursor.getColumnIndex("COMENTARIO");
                int estrellasIndex = cursor.getColumnIndex("ESTRELLAS");

                // Comprobar que los índices son válidos antes de usarlos
                if (idIndex != -1 && comentarioIndex != -1 && estrellasIndex != -1) {
                    String id = cursor.getString(idIndex);
                    String comentario = cursor.getString(comentarioIndex);
                    String estrellas = cursor.getString(estrellasIndex);

                    comentariosDeFeedback.append("ID: ").append(id)
                            .append(", Comentario: ").append(comentario)
                            .append(", Estrellas: ").append(estrellas).append("\n");
                } else {
                    // Si alguno de los índices es -1, imprimir un mensaje de error
                    Toast.makeText(this, "Error: Columnas no encontradas.", Toast.LENGTH_SHORT).show();
                }
            } while (cursor.moveToNext());
        } else {
            // Si el cursor es nulo o no tiene resultados
            Toast.makeText(this, "No hay comentarios de feedback.", Toast.LENGTH_SHORT).show();
        }

        // Cerrar el cursor y la base de datos
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        // Establecer el texto en el TextView
        if (comentariosDeFeedback.length() > 0) {
            tvFeedback.setText(comentariosDeFeedback.toString());
        } else {
            // Mostrar un mensaje Toast si no hay comentarios
            Toast.makeText(this, "No hay comentarios de feedback.", Toast.LENGTH_SHORT).show();
        }
    }
}
