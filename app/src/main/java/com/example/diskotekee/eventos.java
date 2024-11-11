package com.example.diskotekee;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class eventos extends AppCompatActivity {

    private TextView tvEventos;
    private Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);


        // Realizar solicitud para obtener los eventos
        fetchEventos();
    }

    private void fetchEventos() {
        // URL del archivo PHP que devuelve los eventos en formato JSON
        String url = "http://198.168.1.3/diskotekee/consulta_img.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        StringBuilder eventosText = new StringBuilder();

                        // Iteramos sobre los eventos recibidos
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject evento = response.getJSONObject(i);
                                String nombreEvento = evento.getString("nombre_evento");
                                String imagenEvento = evento.getString("img_evento");

                                // Agregamos el nombre del evento y la URL de la imagen al texto
                                eventosText.append("Evento: ").append(nombreEvento).append("\n")
                                        .append("Imagen: ").append(imagenEvento).append("\n\n");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Actualizamos el TextView con los eventos
                        tvEventos.setText(eventosText.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(eventos.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
}
