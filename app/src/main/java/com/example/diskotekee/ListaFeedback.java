package com.example.diskotekee;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ListaFeedback extends AppCompatActivity {

    private TextView tvFeedback;
    private ImageView btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_feedack);

        tvFeedback = findViewById(R.id.tv_feedback);
        btnRegresar = findViewById(R.id.regreso);

        // Llenar el TextView con los comentarios de feedback (estáticos)
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
        String comentariosDeFeedback = "Comentario 1: Muy buena app, me gusta mucho!\n" +
                "Comentario 2: Fácil de usar, pero necesita más funcionalidades.\n" +
                "Comentario 3: Me encanta, la interfaz es increíble.\n" +
                "Comentario 4: Necesita mejorar la velocidad, se congela a veces.";

        // Establecer el texto en el TextView
        tvFeedback.setText(comentariosDeFeedback);
    }
}
