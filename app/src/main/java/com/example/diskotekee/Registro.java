package com.example.diskotekee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {
    EditText nombre, apellido, email, clave, clave2;
    Button btn_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        nombre = (EditText) findViewById(R.id.txtnombre);
        apellido = (EditText) findViewById(R.id.txtapellido);
        email = (EditText) findViewById(R.id.txtemail);
        clave = (EditText) findViewById(R.id.txtclave);
        clave2 = (EditText) findViewById(R.id.txtclave2);
        btn_reg = (Button) findViewById(R.id.btn_registro);

        ImageView regresoImage = findViewById(R.id.regreso);

        regresoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registro.this, Busqueda.class);
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

                boolean showAlert = false;
                StringBuilder alertMessage = new StringBuilder();

                if (nombreStr.isEmpty()) {
                    nombre.setError("Campo Nombre requerido");
                    nombre.requestFocus();
                    showAlert = true;
                }
                else if (!isValidName(nombreStr)) {
                    nombre.setError("El nombre no debe contener números");
                    nombre.requestFocus();
                    showAlert = true;
                }

                if (apellidoStr.isEmpty()) {
                    apellido.setError("Campo Apellido requerido");
                    apellido.requestFocus();
                    showAlert = true;
                }
                else if (!isValidName(apellidoStr)) {
                    apellido.setError("El apellido no debe contener números");
                    apellido.requestFocus();
                    showAlert = true;
                }

                if (emailStr.isEmpty()) {
                    email.setError("Campo Correo requerido");
                    email.requestFocus();
                    showAlert = true;
                } else if (!isValidEmail(emailStr)) {
                    email.setError("No cumple con el formato requerido");
                    email.requestFocus();
                    showAlert = true;
                }

                if (claveStr.isEmpty()) {
                    clave.setError("Campo Contraseña requerido");
                    clave.requestFocus();
                    showAlert = true;
                } else if (!isValidPassword(claveStr)) {
                    clave.setError("La contraseña no cumple con los requisitos");
                    clave.requestFocus();
                    showAlert = true;
                }

                if (clave2Str.isEmpty()) {
                    clave2.setError("Campo Contraseña requerido");
                    clave2.requestFocus();
                    showAlert = true;
                } else if (!claveStr.equals(clave2Str)) {
                    clave2.setError("Las contraseñas no coinciden");
                    clave2.requestFocus();
                    showAlert = true;
                }

                if (!showAlert) {
                    guardar(nombreStr, apellidoStr, emailStr, claveStr);
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.compile(passwordPattern).matcher(password).matches();
    }
    public void guardar(String nom, String ape, String mai, String cla) {
        ConexionDbHelper helper = new ConexionDbHelper(this, "APPSQLITE", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            ContentValues datos = new ContentValues();
            datos.put("Nombre", nom);
            datos.put("Apellido", ape);
            datos.put("Email", mai);
            datos.put("Clave", cla);
            db.insert("USUARIOS", null, datos);
            Toast.makeText(this, "Datos Ingresados Sin Problemas", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private boolean isValidName(String name) {
        // Utiliza una expresión regular para verificar que el nombre no contenga números
        String namePattern = "^[A-Za-zñÑáéíóúÁÉÍÓÚüÜ\\s]+$";
        return Pattern.compile(namePattern).matcher(name).matches();
    }
}