package com.example.diskotekee;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class eventos extends AppCompatActivity {

    private CheckBox checkBoxBombofica, checkBoxBandaTropical, checkBoxFrancoElGorila;
    private Button buttonComprar;
    private TextView totalPrecioTextView;  // Nueva variable para mostrar el precio total
    private int totalPrecio = 0;  // Variable para almacenar el precio total

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        // Referenciar los CheckBox y el botón
        checkBoxBombofica = findViewById(R.id.checkbox_bombofica);
        checkBoxBandaTropical = findViewById(R.id.checkbox_bandatropical);
        checkBoxFrancoElGorila = findViewById(R.id.checkbox_francoelgorila);
        buttonComprar = findViewById(R.id.button_comprar);
        totalPrecioTextView = findViewById(R.id.text_precio_total); // Referenciar el TextView para el total

        // Establecer los OnCheckedChangeListeners para cada CheckBox
        checkBoxBombofica.setOnCheckedChangeListener((buttonView, isChecked) -> actualizarPrecio(isChecked, 10000));
        checkBoxBandaTropical.setOnCheckedChangeListener((buttonView, isChecked) -> actualizarPrecio(isChecked, 10000));
        checkBoxFrancoElGorila.setOnCheckedChangeListener((buttonView, isChecked) -> actualizarPrecio(isChecked, 10000));

        // Obtener datos del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Usuario", MODE_PRIVATE);
        String idUsuario = sharedPreferences.getString("id", ""); // Obtener el ID del usuario

        // Configurar el botón de Comprar
        buttonComprar.setOnClickListener(v -> {
            StringBuilder eventosSeleccionados = new StringBuilder();
            StringBuilder detalleCompra = new StringBuilder();

            // Agregar los eventos seleccionados al detalle de compra
            if (checkBoxBombofica.isChecked()) {
                detalleCompra.append("Evento: Bombofica\nPrecio: $10.000\n\n");
                eventosSeleccionados.append("2,"); // id_evento = 2
            }
            if (checkBoxBandaTropical.isChecked()) {
                detalleCompra.append("Evento: Banda Tropical\nPrecio: $10.000\n\n");
                eventosSeleccionados.append("3,"); // id_evento = 3
            }
            if (checkBoxFrancoElGorila.isChecked()) {
                detalleCompra.append("Evento: Franco El Gorila\nPrecio: $10.000\n\n");
                eventosSeleccionados.append("4,"); // id_evento = 4
            }

            // Verificar que al menos un evento fue seleccionado
            if (totalPrecio > 0) {
                // Mostrar el detalle de compra como Toast
                Toast.makeText(eventos.this, detalleCompra.toString(), Toast.LENGTH_LONG).show();

                // Mostrar el ID del usuario y los eventos seleccionados
                Toast.makeText(eventos.this, "ID Usuario: " + idUsuario + "\nEventos Seleccionados: " + eventosSeleccionados.toString(), Toast.LENGTH_LONG).show();

                // Enviar los datos al servidor para insertar en la base de datos, incluyendo el ID del usuario
                new EnviarCompraTask().execute(idUsuario, eventosSeleccionados.toString());
            } else {
                Toast.makeText(eventos.this, "Seleccione al menos un evento para comprar.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para actualizar el precio total
    private void actualizarPrecio(boolean isChecked, int precioEvento) {
        if (isChecked) {
            totalPrecio += precioEvento;  // Agregar precio si el CheckBox está marcado
        } else {
            totalPrecio -= precioEvento;  // Restar precio si el CheckBox está desmarcado
        }
        totalPrecioTextView.setText("Total: $" + totalPrecio);  // Actualizar el TextView con el total
    }

    private class EnviarCompraTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String idUsuario = params[0];  // ID del usuario
            String eventosSeleccionados = params[1]; // Recibir los id_evento seleccionados

            String[] eventos = eventosSeleccionados.split(",");

            try {
                // URL del archivo PHP que maneja la inserción
                URL url = new URL("http://192.168.1.3/diskotekee/insertarcompra.php");

                // Crear la conexión HTTP
                HttpURLConnection connection;
                int responseCode = -1;
                StringBuilder response = new StringBuilder();

                // Enviar una consulta por cada evento seleccionado
                for (String evento : eventos) {
                    if (!evento.isEmpty()) {
                        // Crear conexión para cada evento
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                        // Suponiendo un precio fijo de 10,000 por evento
                        int precioEvento = 10000;

                        // Crear el cuerpo de la solicitud POST para cada evento
                        String postData = "id_usuario=" + idUsuario + "&id_evento=" + evento + "&precio=" + precioEvento;

                        // Escribir los datos en el flujo de salida de la conexión
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(postData.getBytes());
                        outputStream.flush();
                        outputStream.close();

                        // Leer la respuesta del servidor
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // Verificar el código de respuesta
                        responseCode = connection.getResponseCode();
                    }
                }

                // Log para mostrar la respuesta completa
                Log.d("Respuesta del Servidor", "Código de respuesta: " + responseCode);
                Log.d("Respuesta del Servidor", "Respuesta: " + response.toString());

                // Verificar si todo salió bien
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return "Compra procesada correctamente. Respuesta: " + response.toString();
                } else {
                    return "Error en la compra: " + responseCode + " - " + response.toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log para errores en la conexión
                Log.e("Error de Conexión", e.getMessage(), e);
                return "Error de conexión: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Mostrar el resultado del servidor en un Toast
            Toast.makeText(eventos.this, result, Toast.LENGTH_LONG).show();
            // También registrar el resultado en Logcat
            Log.d("Resultado de Compra", result);
        }
    }
}
