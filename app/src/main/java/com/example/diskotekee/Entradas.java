package com.example.diskotekee;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Entradas extends AppCompatActivity {

    private TextView tvEntradas; // TextView donde mostraremos las entradas

    ImageView btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entradas);

        tvEntradas = findViewById(R.id.tv_entradas); // Inicializa el TextView

        btnRegresar = findViewById(R.id.regreso);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Acción del botón de regresar
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cierra la actividad actual y vuelve a la anterior
                finish();
            }
        });

        // Recuperar el id_usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Usuario", MODE_PRIVATE);
        String id_usuario = sharedPreferences.getString("id", "No encontrado");

        // Realizar la solicitud para obtener las entradas compradas
        obtenerEntradas(id_usuario);
    }

    // Método para obtener las entradas del servidor
    private void obtenerEntradas(final String id_usuario) {
        // Habilitar políticas estrictas para conexión en el hilo principal
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // URL del script PHP
        String urlString = "http:/192.168.66.1/diskotekee/entradas.php"; // Cambiar por la URL correcta

        try {
            // Configurar la conexión HTTP
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Enviar el id_usuario en la solicitud POST
            String postData = "id_usuario=" + id_usuario;
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes());
            os.flush();
            os.close();

            // Obtener la respuesta del servidor
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Procesar la respuesta JSON
                JSONObject jsonResponse = new JSONObject(response.toString());

                if (jsonResponse.getString("status").equals("success")) {
                    // Si la respuesta es exitosa, obtener los datos de las entradas
                    JSONArray entradas = jsonResponse.getJSONArray("data");
                    StringBuilder entradasText = new StringBuilder();

                    for (int i = 0; i < entradas.length(); i++) {
                        JSONObject entrada = entradas.getJSONObject(i);
                        String idEvento = entrada.getString("id_evento");
                        String fechaCompra = entrada.getString("fecha_compra");
                        String precio = entrada.getString("precio");

                        // Concatenar la información de las entradas
                        entradasText.append("ID Evento: ").append(idEvento)
                                .append("\nFecha de Compra: ").append(fechaCompra)
                                .append("\nPrecio: ").append(precio)
                                .append("\n\n");
                    }

                    // Mostrar las entradas en el TextView
                    final String entradasFinal = entradasText.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvEntradas.setText(entradasFinal);
                        }
                    });
                } else {
                    // Si no se encuentran entradas, mostrar mensaje
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvEntradas.setText("No se encontraron entradas compradas.");
                        }
                    });
                }
            } else {
                Log.e("Entradas", "Error en la conexión: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Entradas", "Error en la solicitud HTTP", e);
        }
    }
}
