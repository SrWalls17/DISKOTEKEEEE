package com.example.diskotekee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Busqueda extends AppCompatActivity {

    private static final String URL = "http://192.168.1.3/diskotekee/buscar.php";  // Cambia la URL por la de tu servidor
    private TextView tvUsuarios;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        // Inicializar la cola de solicitudes
        requestQueue = Volley.newRequestQueue(this);

        // Obtener la referencia del TextView
        tvUsuarios = findViewById(R.id.tv_usuarios);

        // Configurar el botón de regreso
        ImageView regresoImage = findViewById(R.id.regreso);
        regresoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // Simplemente finalizar la actividad y volver
            }
        });

        // Obtener la lista de usuarios
        obtenerUsuarios();
    }

    private void obtenerUsuarios() {
        // Crear la solicitud para obtener los datos de los usuarios desde el servidor
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Mostrar la respuesta en el TextView
                        tvUsuarios.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar error de conexión
                        Toast.makeText(Busqueda.this, "Error al obtener usuarios", Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(stringRequest);
    }
}
