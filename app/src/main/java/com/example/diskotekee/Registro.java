package com.example.diskotekee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {
    EditText nombre, apellido, email, clave, clave2;
    Button btn_reg;

    // Para Volley
    RequestQueue requestQueue;
    private static final String URL = "http://192.168.1.3/diskotekee/save.php";  // URL de tu servidor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Inicializar los campos de entrada
        nombre = findViewById(R.id.txtnombre);
        apellido = findViewById(R.id.txtapellido);
        email = findViewById(R.id.txtemail);
        clave = findViewById(R.id.txtclave);
        clave2 = findViewById(R.id.txtclave2);
        btn_reg = findViewById(R.id.btn_registro);

        // Inicializar Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        ImageView regresoImage = findViewById(R.id.regreso);
        regresoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registro.this, Principal.class);
                startActivity(intent);
                finish();
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreStr = nombre.getText().toString().trim();
                String apellidoStr = apellido.getText().toString().trim();
                String emailStr = email.getText().toString().trim();
                String claveStr = clave.getText().toString();
                String clave2Str = clave2.getText().toString();

                // Validaciones de campos
                if (!isValidName(nombreStr)) {
                    Toast.makeText(Registro.this, "Nombre inválido", Toast.LENGTH_SHORT).show();
                } else if (!isValidName(apellidoStr)) {
                    Toast.makeText(Registro.this, "Apellido inválido", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(emailStr)) {
                    Toast.makeText(Registro.this, "Email inválido", Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword(claveStr)) {
                    Toast.makeText(Registro.this, "Contraseña inválida", Toast.LENGTH_SHORT).show();
                } else if (!claveStr.equals(clave2Str)) {
                    Toast.makeText(Registro.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else {
                    registrarUsuario(nombreStr, apellidoStr, emailStr, claveStr);
                }
            }
        });
    }

    // Método para registrar el usuario
    private void registrarUsuario(final String nombre, final String apellido, final String email, final String clave) {

        // Realizar la solicitud POST con Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Si la respuesta es exitosa
                        Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        finish();  // Finaliza la actividad
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // En caso de error
                        Toast.makeText(Registro.this, "Error al registrar: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Pasamos los datos a la solicitud
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("apellido", apellido);
                params.put("email", email);
                params.put("clave", clave);
                return params;
            }
        };

        // Añadimos la solicitud a la cola de Volley
        requestQueue.add(stringRequest);
    }

    // Métodos de validación
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.compile(passwordPattern).matcher(password).matches();
    }

    private boolean isValidName(String name) {
        String namePattern = "^[A-Za-zñÑáéíóúÁÉÍÓÚüÜ\\s]+$";
        return Pattern.compile(namePattern).matcher(name).matches();
    }
}
