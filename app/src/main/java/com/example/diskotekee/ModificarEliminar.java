package com.example.diskotekee;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ModificarEliminar extends AppCompatActivity {
    // Declaración de variables para la interfaz
    TextView tvUsuarios;
    ImageView regreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_eliminar);

        // Inicializar las vistas
        tvUsuarios = findViewById(R.id.tv_usuario);
        regreso = findViewById(R.id.regreso);

        // Obtener la instancia del usuario
        Usuario usuario = Usuario.getInstance();

        // Verificar si el usuario está inicializado (autenticado)
        if (usuario.getEmail() != null) {
            // Mostrar los datos del usuario en el TextView
            String usuarioInfo = "Nombre: " + usuario.getNombre() + "\n" +
                    "Apellido: " + usuario.getApellido() + "\n" +
                    "Email: " + usuario.getEmail() + "\n" +
                    "Clave: " + usuario.getClave() + "\n" +
                    "Matchs: " + usuario.getMatchs() + "\n" +
                    "Amistades: " + usuario.getAmistades();

            tvUsuarios.setText(usuarioInfo);

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
        regreso.setOnClickListener(view -> {
            finish(); // Cerrar la actividad actual y volver a la anterior
        });
    }
}
