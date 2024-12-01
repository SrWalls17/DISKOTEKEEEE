package com.example.diskotekee;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PerfilSocial extends AppCompatActivity {

    EditText edad, gustos, ocupacion, instagram;
    Button btnEnviar, btnModificar, btnEliminar;
    Switch switchMatchs, switchAmistades;

    ImageView btnRegresar;

    // URL del servidor
    String url = "http://192.168.66.1/diskotekee/perfilsocial.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_social);

        // Referenciar los componentes de la UI
        edad = findViewById(R.id.edad);
        gustos = findViewById(R.id.descripcion);
        ocupacion = findViewById(R.id.ocupacion);
        instagram = findViewById(R.id.instagram);
        switchMatchs = findViewById(R.id.switchMatchs);
        switchAmistades = findViewById(R.id.switchAmistades);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnModificar = findViewById(R.id.btnModificar);
        btnRegresar = findViewById(R.id.regreso);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cierra la actividad actual y vuelve a la anterior
                finish();
            }
        });

        // Obtener el ID de usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Usuario", MODE_PRIVATE);
        String id_usuario = sharedPreferences.getString("id", "");

        // Ocultar botones inicialmente
        btnEliminar.setVisibility(View.GONE);
        btnModificar.setVisibility(View.GONE);

        // Obtener datos del perfil
        obtenerDatosPerfil(id_usuario);

        // Listener para el botón de Enviar
        btnEnviar.setOnClickListener(v -> {
            if (!(switchMatchs.isChecked() || switchAmistades.isChecked())) {
                Toast.makeText(this, "Debes activar al menos uno de los switches", Toast.LENGTH_SHORT).show();
                return;
            }

            if (id_usuario.isEmpty()) {
                Toast.makeText(this, "Error: ID de usuario no encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            enviarDatos(id_usuario);
        });

        // Listener para el botón de Desactivar
        btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmación")
                    .setMessage("¿Estás seguro de que deseas eliminarte de las listas de matchs y amistades?")
                    .setPositiveButton("Sí", (dialog, which) -> eliminarUsuario(id_usuario))
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Listener para el botón de Eliminar
        btnModificar.setOnClickListener(v -> modificarUsuario(id_usuario));
    }

    private void obtenerDatosPerfil(String id_usuario) {
        String url = "http://192.168.66.1/diskotekee/obtenerPerfil.php?id_usuario=" + id_usuario;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        edad.setText(response.getString("edad"));
                        gustos.setText(response.getString("gustos"));
                        ocupacion.setText(response.getString("ocupacion"));
                        instagram.setText(response.getString("instagram"));

                        switchMatchs.setChecked(response.getInt("matchs") == 1);
                        switchAmistades.setChecked(response.getInt("amistades") == 1);

                        // Mostrar botones de acción y ocultar botón Enviar
                        btnEliminar.setVisibility(View.VISIBLE);
                        btnModificar.setVisibility(View.VISIBLE);
                        btnEnviar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error ->{
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void enviarDatos(String id_usuario) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, "Datos enviados correctamente", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error en el envío", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", id_usuario);
                params.put("edad", edad.getText().toString());
                params.put("gustos", gustos.getText().toString());
                params.put("ocupacion", ocupacion.getText().toString());
                params.put("instagram", instagram.getText().toString());
                params.put("matchs", switchMatchs.isChecked() ? "1" : "0");
                params.put("amistades", switchAmistades.isChecked() ? "1" : "0");
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


    // Método para eliminar el usuario
    private void eliminarUsuario(String id_usuario) {
        String url = "http://192.168.66.1/diskotekee/eliperfilsocial.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        // Log para inspeccionar la respuesta del servidor
                        Log.d("EliminarUsuario", "Response: " + response);

                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("message")) {
                            Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            finish(); // Cerrar la actividad después de eliminar
                        } else if (jsonResponse.has("error")) {
                            Toast.makeText(this, jsonResponse.getString("error"), Toast.LENGTH_SHORT).show();
                            Log.e("EliminarUsuario", "Error: " + jsonResponse.getString("error"));
                        }
                    } catch (JSONException e) {
                        Log.e("EliminarUsuario", "JSON Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Log para inspeccionar el error de Volley
                    Log.e("EliminarUsuario", "Volley Error: " + error.toString());
                    Toast.makeText(this, "Error al eliminar el usuario", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", id_usuario);

                // Log para verificar los parámetros enviados al servidor
                Log.d("EliminarUsuario", "Params: " + params.toString());
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void modificarUsuario(String id_usuario) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Estás seguro de que deseas modificar este perfil?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // URL correcto para la modificación
                    String urlModificacion = "http://192.168.66.1/diskotekee/modperfilsocial.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlModificacion,
                            response -> {
                                Toast.makeText(this, "Perfil modificado correctamente", Toast.LENGTH_SHORT).show();
                                obtenerDatosPerfil(id_usuario); // Actualizar los datos después de la modificación
                                finish();
                            },
                            error -> Toast.makeText(this, "Error al modificar el perfil", Toast.LENGTH_SHORT).show()) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("id_usuario", id_usuario);
                            params.put("edad", edad.getText().toString());
                            params.put("gustos", gustos.getText().toString());
                            params.put("ocupacion", ocupacion.getText().toString());
                            params.put("instagram", instagram.getText().toString());
                            params.put("matchs", switchMatchs.isChecked() ? "1" : "0");
                            params.put("amistades", switchAmistades.isChecked() ? "1" : "0");
                            return params;
                        }
                    };

                    Volley.newRequestQueue(this).add(stringRequest);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    private void guardarDatosEnSharedPreferences(String edad, String gustos, String ocupacion, String instagram) {
        SharedPreferences sharedPreferences = getSharedPreferences("Usuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("edad", edad);
        editor.putString("gustos", gustos);
        editor.putString("ocupacion", ocupacion);
        editor.putString("instagram", instagram);
        editor.apply();
    }

}
