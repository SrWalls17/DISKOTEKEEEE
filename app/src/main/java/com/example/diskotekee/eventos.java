package com.example.diskotekee;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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
    private TextView totalPrecioTextView;
    private int totalPrecio = 0;
    private String idUsuario;

    ImageView btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        // Referencias
        checkBoxBombofica = findViewById(R.id.checkbox_bombofica);
        checkBoxBandaTropical = findViewById(R.id.checkbox_bandatropical);
        checkBoxFrancoElGorila = findViewById(R.id.checkbox_francoelgorila);
        buttonComprar = findViewById(R.id.button_comprar);
        totalPrecioTextView = findViewById(R.id.text_precio_total);

        // Listener para actualizar el precio
        checkBoxBombofica.setOnCheckedChangeListener((buttonView, isChecked) -> actualizarPrecio(isChecked, 10000));
        checkBoxBandaTropical.setOnCheckedChangeListener((buttonView, isChecked) -> actualizarPrecio(isChecked, 10000));
        checkBoxFrancoElGorila.setOnCheckedChangeListener((buttonView, isChecked) -> actualizarPrecio(isChecked, 10000));

        // ID del usuario
        SharedPreferences sharedPreferences = getSharedPreferences("Usuario", MODE_PRIVATE);
        idUsuario = sharedPreferences.getString("id", "");

        // Cargar eventos comprados previamente
        cargarEventosComprados();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Botón Comprar
        buttonComprar.setOnClickListener(v -> {
            StringBuilder eventosSeleccionados = new StringBuilder();
            StringBuilder detalleCompra = new StringBuilder();

            if (checkBoxBombofica.isChecked()) {
                detalleCompra.append("Evento: Bombofica\nPrecio: $10.000\n\n");
                eventosSeleccionados.append("2,");
            }
            if (checkBoxBandaTropical.isChecked()) {
                detalleCompra.append("Evento: Banda Tropical\nPrecio: $10.000\n\n");
                eventosSeleccionados.append("3,");
            }
            if (checkBoxFrancoElGorila.isChecked()) {
                detalleCompra.append("Evento: Franco El Gorila\nPrecio: $10.000\n\n");
                eventosSeleccionados.append("4,");
            }

            if (totalPrecio > 0) {
                Toast.makeText(eventos.this, detalleCompra.toString(), Toast.LENGTH_LONG).show();
                new EnviarCompraTask().execute(idUsuario, eventosSeleccionados.toString());
            } else {
                Toast.makeText(eventos.this, "Seleccione al menos un evento para comprar.", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegresar = findViewById(R.id.regreso);
        // Acción del botón de regresar
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cierra la actividad actual y vuelve a la anterior
                finish();
            }
        });
    }

    private void cargarEventosComprados() {
        SharedPreferences sharedPreferences = getSharedPreferences("EventosComprados_" + idUsuario, MODE_PRIVATE);

        if (sharedPreferences.getBoolean("eventoBombofica", false)) {
            checkBoxBombofica.setChecked(true);
            checkBoxBombofica.setEnabled(false); // Bloquear el CheckBox
        }
        if (sharedPreferences.getBoolean("eventoBandaTropical", false)) {
            checkBoxBandaTropical.setChecked(true);
            checkBoxBandaTropical.setEnabled(false);
        }
        if (sharedPreferences.getBoolean("eventoFrancoElGorila", false)) {
            checkBoxFrancoElGorila.setChecked(true);
            checkBoxFrancoElGorila.setEnabled(false);
        }
    }

    private void actualizarPrecio(boolean isChecked, int precioEvento) {
        if (isChecked) {
            totalPrecio += precioEvento;
        } else {
            totalPrecio -= precioEvento;
        }
        totalPrecioTextView.setText("Total: $" + totalPrecio);
    }

    private class EnviarCompraTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String idUsuario = params[0];
            String eventosSeleccionados = params[1];
            String[] eventos = eventosSeleccionados.split(",");

            try {
                URL url = new URL("http://192.168.66.1/diskotekee/insertarcompra.php");
                StringBuilder response = new StringBuilder();
                HttpURLConnection connection;

                for (String evento : eventos) {
                    if (!evento.isEmpty()) {
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                        int precioEvento = 10000;
                        String postData = "id_usuario=" + idUsuario + "&id_evento=" + evento + "&precio=" + precioEvento;

                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(postData.getBytes());
                        outputStream.flush();
                        outputStream.close();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                    }
                }

                // Guardar eventos comprados para el usuario actual
                SharedPreferences sharedPreferences = getSharedPreferences("EventosComprados_" + idUsuario, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (checkBoxBombofica.isChecked()) editor.putBoolean("eventoBombofica", true);
                if (checkBoxBandaTropical.isChecked()) editor.putBoolean("eventoBandaTropical", true);
                if (checkBoxFrancoElGorila.isChecked()) editor.putBoolean("eventoFrancoElGorila", true);
                editor.apply();

                return "Compra procesada correctamente.";
            } catch (Exception e) {
                e.printStackTrace();
                return "Error de conexión: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(eventos.this, result, Toast.LENGTH_LONG).show();
            Log.d("Resultado de Compra", result);
        }
    }
}
