package com.example.diskotekee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

public class ModificarEliminar extends AppCompatActivity {
    // Declaración de variables para la interfaz
    EditText txtId, txtNombreMod, txtApellidoMod, txtCorreoMod, txtContra, txtContra2, txtMatchs, txtAmistades;
    ImageView regreso;
    Button btnModificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_eliminar);

        // Inicializar las vistas
        txtId = findViewById(R.id.txtid);
        txtNombreMod = findViewById(R.id.txtnombremod);
        txtApellidoMod = findViewById(R.id.txtapellidomod);
        txtCorreoMod = findViewById(R.id.txtcorreomod);
        txtContra = findViewById(R.id.txtcontra);
        txtMatchs = findViewById(R.id.txtmatch);
        txtAmistades = findViewById(R.id.txtamistades);
        btnModificar = findViewById(R.id.btn_modificar);
        regreso = findViewById(R.id.regreso); // Inicializado el botón de regreso

        // Deshabilitar edición en el campo de ID
        txtId.setEnabled(false);

        // Obtener los datos desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Usuario", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String nombre = sharedPreferences.getString("nombre", "");
        String apellido = sharedPreferences.getString("apellido", "");
        int matchs = sharedPreferences.getInt("matchs", 0);  // Recuperar el valor de matchs
        int amistades = sharedPreferences.getInt("amistades", 0);  // Recuperar el valor de amistades

        // Mostrar los datos recuperados en los EditTexts correspondientes
        txtCorreoMod.setText(email);
        txtNombreMod.setText(nombre);
        txtApellidoMod.setText(apellido);
        txtMatchs.setText(matchs == 1 ? "Sí" : "No");
        txtAmistades.setText(amistades == 1 ? "Sí" : "No");

        // Configurar el botón de regreso para finalizar la actividad
        regreso.setOnClickListener(view -> finish());

        // Configurar el botón Modificar con un AlertDialog
        btnModificar.setOnClickListener(view -> {
            // Crear y mostrar el AlertDialog
            new AlertDialog.Builder(ModificarEliminar.this)
                    .setTitle("Confirmar Modificación")
                    .setMessage("¿Estás seguro de que deseas modificar los datos del usuario?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        // Validar que las contraseñas coincidan antes de proceder
                        String nuevaClave = txtContra.getText().toString();
                        String nuevaClave2 = txtContra2.getText().toString();

                        if (!nuevaClave.equals(nuevaClave2)) {
                            // Si las contraseñas no coinciden, mostrar un error
                            Toast.makeText(ModificarEliminar.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        } else {
                            // Guardar los cambios en los campos de texto
                            String nuevoNombre = txtNombreMod.getText().toString();
                            String nuevoApellido = txtApellidoMod.getText().toString();
                            String nuevoCorreo = txtCorreoMod.getText().toString();
                            int nuevoMatchs = txtMatchs.getText().toString().equals("Sí") ? 1 : 0;
                            int nuevoAmistades = txtAmistades.getText().toString().equals("Sí") ? 1 : 0;

                            // Guardar los nuevos datos en SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("nombre", nuevoNombre);
                            editor.putString("apellido", nuevoApellido);
                            editor.putString("email", nuevoCorreo);
                            editor.putInt("matchs", nuevoMatchs);
                            editor.putInt("amistades", nuevoAmistades);

                            if (!nuevaClave.isEmpty()) {
                                // Si se ingresó una nueva contraseña, guardarla
                                editor.putString("password", nuevaClave);
                            }
                            editor.apply();

                            // Mostrar un mensaje confirmando la modificación
                            Toast.makeText(ModificarEliminar.this, "Datos modificados correctamente", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }
}
