package com.example.diskotekee;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class ListaFeedback extends AppCompatActivity {

    private TextView tvFeedback;
    private ImageView btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_feedack);

        tvFeedback = findViewById(R.id.tv_feedback);
        btnRegresar = findViewById(R.id.regreso);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Mostrar los comentarios de feedback desde la API
        mostrarComentariosDeFeedback();

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cierra la actividad actual y vuelve a la anterior
                finish();
            }
        });
    }

    private void mostrarComentariosDeFeedback() {
        String url = "http://192.168.66.1/diskotekee/consulta_feedback.php";  // URL de tu API

        // Crear la solicitud GET
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Si la respuesta contiene feedback
                            if (response.length() > 0) {
                                StringBuilder comentariosDeFeedback = new StringBuilder();

                                // Recorrer el array de feedbacks y agregar los comentarios y las estrellas
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject feedback = response.getJSONObject(i);
                                    String comentario = feedback.getString("comentario");  // Comentario
                                    int estrellas = feedback.getInt("estrellas");  // Estrellas

                                    // Agregar el comentario y la cantidad de estrellas
                                    comentariosDeFeedback.append("Comentario ").append(i + 1).append(": ").append(comentario)
                                            .append("\nEstrellas: ").append(estrellas).append("\n\n");
                                }

                                // Mostrar los comentarios y estrellas en el TextView
                                tvFeedback.setText(comentariosDeFeedback.toString());
                            } else {
                                // Si no hay comentarios, mostrar un mensaje
                                tvFeedback.setText("No hay feedback disponibles");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ListaFeedback.this, "Error al obtener los comentarios", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                error -> {
                    Toast.makeText(ListaFeedback.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                });

        // Añadir la solicitud a la cola de Volley
        requestQueue.add(jsonArrayRequest);
    }
}
