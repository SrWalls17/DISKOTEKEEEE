package com.example.diskotekee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // Importar Log para depuración
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Principal extends AppCompatActivity {

    EditText usuario, password;
    TextView crear_cuenta; // Cambiado a TextView
    Button ingresar;

    ImageView ivAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        usuario = findViewById(R.id.frmusuario);
        password = findViewById(R.id.frmpass);
        ingresar = findViewById(R.id.btningresar);
        crear_cuenta = findViewById(R.id.crear_cuenta); // Inicializa el TextView "Crear Cuenta"

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Redirigir al hacer clic en "Ingresar"
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = usuario.getText().toString();
                String contraseña = password.getText().toString();

                if (correo.isEmpty() || contraseña.isEmpty()) {
                    // Muestra un mensaje de error si uno o ambos campos están vacíos
                    usuario.setError("Debe rellenar campos");
                    password.setError("Debe rellenar campos");
                } else {
                    autenticarUsuario(correo, contraseña);
                }
            }
        });

        // Redirigir a la ventana Registro al hacer clic en "Crear Cuenta"
        crear_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registroIntent = new Intent(Principal.this, Registro.class);
                startActivity(registroIntent);
            }
        });
    }

    private void autenticarUsuario(final String correo, final String contraseña) {
        String urlString = "http://192.168.66.1/diskotekee/login.php"; // URL de la API

        // Crear un nuevo hilo para la solicitud HTTP (es necesario porque las solicitudes de red no se deben hacer en el hilo principal)
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Configurar la conexión HTTP
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    // Configurar los parámetros de la solicitud
                    String postData = "email=" + correo + "&clave=" + contraseña;
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(postData);
                    writer.flush();
                    writer.close();
                    os.close();

                    // Obtener la respuesta del servidor
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                        BufferedReader in = new BufferedReader(inputStreamReader);
                        StringBuilder response = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Procesar la respuesta JSON
                        JSONObject jsonResponse = new JSONObject(response.toString());

                        if (jsonResponse.getString("status").equals("true")) {
                            // Autenticación exitosa, redirigir al usuario a la actividad principal
                            String id = jsonResponse.getString("id");
                            String nombre = jsonResponse.getString("nombre");
                            String apellido = jsonResponse.getString("apellido");
                            String email = jsonResponse.getString("email");
                            String clave = jsonResponse.getString("clave");


                            // Guardar todos los datos en SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("Usuario", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email);  // Guardamos el email
                            editor.putString("nombre", nombre); // Guardar el nombre
                            editor.putString("apellido", apellido); // Guardar el apellido
                            editor.putString("id", id); // Guardar el id
                            editor.putString("clave", clave); // Guardar la clave
                            editor.apply();

                            // Redirigir a la actividad Menu
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Principal.this, Menu.class);
                                    startActivity(intent);
                                    finish();  // Finaliza esta actividad para evitar que el usuario regrese al login con el botón de atrás
                                }
                            });
                        } else {
                            // Mostrar error de credenciales incorrectas
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    usuario.setError("Credenciales incorrectas");
                                    password.setError("Credenciales incorrectas");
                                }
                            });
                        }
                    } else {
                        Log.e("Principal", "Error en la conexión: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Principal", "Error en la autenticación", e);
                }
            }
        }).start();
    }
}
