package com.example.diskotekee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class ModificarEliminar extends AppCompatActivity {
    // Declaración de variables para la interfaz
    EditText txtId, txtNombreMod, txtApellidoMod, txtCorreoMod, txtContra, txtContra2;
    ImageView regreso;
    Button btnModificar;
    Switch swMatch, swAmistad;
    ConexionDbHelper dbHelper;

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
        txtContra2 = findViewById(R.id.txtcontra2);
        regreso = findViewById(R.id.regreso);
        btnModificar = findViewById(R.id.btn_modificar);
        swMatch = findViewById(R.id.sw_match);
        swAmistad = findViewById(R.id.sw_amistad);

        // Deshabilitar edición en el campo de ID
        txtId.setEnabled(false);

        // Inicializar el helper de la base de datos
        dbHelper = new ConexionDbHelper(this);

        // Obtener la instancia del usuario
        Usuario usuario = Usuario.getInstance();

        // Verificar si el usuario está inicializado (autenticado)
        if (usuario.getEmail() != null) {
            // Mostrar los datos del usuario en los EditText
            txtId.setText(String.valueOf(usuario.getId()));
            txtNombreMod.setText(usuario.getNombre());
            txtApellidoMod.setText(usuario.getApellido());
            txtCorreoMod.setText(usuario.getEmail());
            txtContra.setText(usuario.getClave());
            txtContra2.setText(usuario.getClave());

            // Configurar los switches según los valores de matchs y amistades
            swMatch.setChecked(usuario.getMatchs() == 1);
            swAmistad.setChecked(usuario.getAmistades() == 1);

            // Logs para depuración
            Log.d("ModificarEliminar", "Nombre: " + usuario.getNombre());
            Log.d("ModificarEliminar", "Apellido: " + usuario.getApellido());
            Log.d("ModificarEliminar", "Email: " + usuario.getEmail());
            Log.d("ModificarEliminar", "Clave: " + usuario.getClave());
            Log.d("ModificarEliminar", "Matchs: " + usuario.getMatchs());
            Log.d("ModificarEliminar", "Amistades: " + usuario.getAmistades());
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        // Configurar el botón de regreso
        regreso.setOnClickListener(view -> finish());

        // Configurar el botón Modificar con un AlertDialog
        btnModificar.setOnClickListener(view -> {
            // Crear y mostrar el AlertDialog
            new AlertDialog.Builder(ModificarEliminar.this)
                    .setTitle("Confirmar Modificación")
                    .setMessage("¿Estás seguro de que deseas modificar los datos del usuario?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        // Guardar los cambios en la instancia de usuario
                        usuario.setNombre(txtNombreMod.getText().toString());
                        usuario.setApellido(txtApellidoMod.getText().toString());
                        usuario.setEmail(txtCorreoMod.getText().toString());
                        usuario.setClave(txtContra.getText().toString());

                        // Actualizar los valores de matchs y amistades según el estado de los switches
                        usuario.setMatchs(swMatch.isChecked() ? 1 : 0);
                        usuario.setAmistades(swAmistad.isChecked() ? 1 : 0);

                        // Llamar al método para actualizar el usuario en la base de datos
                        boolean actualizado = dbHelper.actualizarUsuario(
                                (int) usuario.getId(),
                                usuario.getNombre(),
                                usuario.getApellido(),
                                usuario.getEmail(),
                                usuario.getClave(),
                                usuario.getMatchs(),
                                usuario.getAmistades()
                        );

                        if (actualizado) {
                            Toast.makeText(ModificarEliminar.this, "Datos modificados correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ModificarEliminar.this, "Error al modificar los datos", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }
}
