package com.example.diskotekee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModificarEliminar extends AppCompatActivity {

    private EditText txtNombre, txtApellido, txtCorreo, txtContra;
    private Button btnModificar;

    // URL del servidor para modificar usuario
    private static final String URL_MODIFICAR = "http://192.168.1.3/diskotekee/modificarperfil.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_eliminar);

        // Vincular EditTexts y botón
        txtNombre = findViewById(R.id.txtnombremod);
        txtApellido = findViewById(R.id.txtapellidomod);
        txtCorreo = findViewById(R.id.txtcorreomod);
        txtContra = findViewById(R.id.txtcontra);
        btnModificar = findViewById(R.id.btn_modificar);

        // Recuperar datos desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Usuario", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "ID no disponible");
        String nombre = sharedPreferences.getString("nombre", "");
        String apellido = sharedPreferences.getString("apellido", "");
        String correo = sharedPreferences.getString("email", "");
        String clave = sharedPreferences.getString("clave", "");

        // Mostrar el ID en un Toast
        Toast.makeText(this, "ID del Usuario: " + id, Toast.LENGTH_SHORT).show();

        // Cargar los datos en los campos de texto
        txtNombre.setText(nombre);
        txtApellido.setText(apellido);
        txtCorreo.setText(correo);
        txtContra.setText(clave);

        // Configurar el botón para modificar perfil
        btnModificar.setOnClickListener(v -> {
            String nuevoNombre = txtNombre.getText().toString();
            String nuevoApellido = txtApellido.getText().toString();
            String nuevoCorreo = txtCorreo.getText().toString();
            String nuevaContra = txtContra.getText().toString();

            if (nuevoNombre.isEmpty() || nuevoApellido.isEmpty() || nuevoCorreo.isEmpty() || nuevaContra.isEmpty()) {
                Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Confirmación")
                        .setMessage("¿Estás seguro de que deseas modificar tus datos?")
                        .setPositiveButton("Sí", (dialog, which) -> modificarUsuario(id, nuevoNombre, nuevoApellido, nuevoCorreo, nuevaContra))
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
    }

    private void modificarUsuario(String id_usuario, String nuevoNombre, String nuevoApellido, String nuevoCorreo, String nuevaContra) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MODIFICAR,
                response -> {
                    try {
                        Log.d("ModificarUsuario", "Response: " + response);
                        JSONObject jsonResponse = new JSONObject(response);

                        if (jsonResponse.has("message")) {
                            Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();

                            // Actualizar SharedPreferences con los nuevos datos
                            SharedPreferences sharedPreferences = getSharedPreferences("Usuario", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("nombre", nuevoNombre);
                            editor.putString("apellido", nuevoApellido);
                            editor.putString("email", nuevoCorreo);
                            editor.putString("clave", nuevaContra);
                            editor.apply(); // Guardar los nuevos datos en SharedPreferences

                            finish(); // Cerrar la actividad después de modificar
                        } else if (jsonResponse.has("error")) {
                            Toast.makeText(this, jsonResponse.getString("error"), Toast.LENGTH_SHORT).show();
                            Log.e("ModificarUsuario", "Error: " + jsonResponse.getString("error"));
                        }
                    } catch (JSONException e) {
                        Log.e("ModificarUsuario", "JSON Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("ModificarUsuario", "Volley Error: " + error.toString());
                    Toast.makeText(this, "Error al modificar los datos", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", id_usuario);
                params.put("nombre", nuevoNombre);
                params.put("apellido", nuevoApellido);
                params.put("correo", nuevoCorreo);
                params.put("clave", nuevaContra);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
