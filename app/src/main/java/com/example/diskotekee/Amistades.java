package com.example.diskotekee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

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
        // URL del servidor PHP que devuelve los usuarios con matchs activados
        String url = "http://192.168.1.3/diskotekee/amistades.php"; // Cambia la URL a la ubicación de tu archivo PHP

        // Crear la solicitud de Volley
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Crear una cadena para almacenar los usuarios
                        StringBuilder usuariosConMatch = new StringBuilder();

                        // Recorrer el JSON con los usuarios
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject usuario = response.getJSONObject(i);
                                String nombre = usuario.getString("nombre");
                                String apellido = usuario.getString("apellido");
                                String edad = usuario.getString("edad");
                                String gustos = usuario.getString("gustos");
                                String instagram = usuario.getString("instagram");
                                String ocupacion = usuario.getString("ocupacion");

                                // Agregar los datos al StringBuilder
                                usuariosConMatch.append("Nombre: ").append(nombre)
                                        .append(" ").append(apellido).append("\n")
                                        .append("Edad: ").append(edad).append("\n")
                                        .append("Gustos: ").append(gustos).append("\n")
                                        .append("Instagram: ").append(instagram).append("\n")
                                        .append("Ocupación: ").append(ocupacion).append("\n\n");
                            }

                            // Establecer el texto en el TextView
                            if (usuariosConMatch.length() > 0) {
                                tvUsuarios.setText(usuariosConMatch.toString());
                            } else {
                                Toast.makeText(Amistades.this, "No hay usuarios con Match activado.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Amistades.this, "Error al procesar los datos.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar error de la solicitud
                        Toast.makeText(Amistades.this, "Error al realizar la solicitud", Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(request);
    }
}
