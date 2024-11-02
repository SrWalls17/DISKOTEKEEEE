package com.example.diskotekee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {
    EditText nombre, apellido, email, clave, clave2;
    Button btn_reg;
    Switch switchMatch, switchAmistades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        nombre = findViewById(R.id.txtnombre);
        apellido = findViewById(R.id.txtapellido);
        email = findViewById(R.id.txtemail);
        clave = findViewById(R.id.txtclave);
        clave2 = findViewById(R.id.txtclave2);
        btn_reg = findViewById(R.id.btn_registro);
        switchMatch = findViewById(R.id.switch_match);
        switchAmistades = findViewById(R.id.switch_amistades);

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
                    guardar(nombreStr, apellidoStr, emailStr, claveStr);
                }
            }
        });
    }

    public void guardar(String nom, String ape, String mai, String cla) {
        ConexionDbHelper helper = new ConexionDbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase(); // Cambia a getWritableDatabase()
        try {
            ContentValues datos = new ContentValues();
            datos.put("Nombre", nom);
            datos.put("Apellido", ape);
            datos.put("Email", mai);
            datos.put("Clave", cla);

            // Obtener los valores de los SwitchButtons
            int matchValue = switchMatch.isChecked() ? 1 : 0;
            int amistadesValue = switchAmistades.isChecked() ? 1 : 0;

            datos.put("MATCHS", matchValue);  // Guardar valor de match
            datos.put("AMISTADES", amistadesValue);  // Guardar valor de amistades

            long newRowId = db.insert("USUARIOS", null, datos); // Guarda y obtiene el ID de la nueva fila
            if (newRowId != -1) {
                Toast.makeText(this, "Datos ingresados correctamente. ID: " + newRowId, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Error al registrar los datos", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close(); // Asegúrate de cerrar la base de datos
        }
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
