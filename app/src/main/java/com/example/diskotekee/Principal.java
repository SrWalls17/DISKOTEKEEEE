package com.example.diskotekee;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log; // Importar Log para depuración
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
                } else if (autenticarUsuario(correo, contraseña)) {
                    // Autenticación exitosa, redirige al usuario a la actividad Modificar
                    Intent actividadModificar = new Intent(Principal.this, Menu.class);
                    actividadModificar.putExtra("EMAIL", correo); // Puedes pasar el correo si lo necesitas
                    startActivity(actividadModificar);
                } else {
                    // Muestra un mensaje de error si las credenciales son incorrectas
                    usuario.setError("Credenciales incorrectas");
                    password.setError("Credenciales incorrectas");
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

    private boolean autenticarUsuario(String correo, String contraseña) {
        ConexionDbHelper helper = new ConexionDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {"ID", "NOMBRE", "APELLIDO", "EMAIL", "CLAVE", "MATCHS", "AMISTADES"};
        String selection = "Email = ? AND Clave = ?";
        String[] selectionArgs = {correo, contraseña};

        Cursor cursor = db.query("USUARIOS", columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex("ID"));
            String nombre = cursor.getString(cursor.getColumnIndex("NOMBRE"));
            String apellido = cursor.getString(cursor.getColumnIndex("APELLIDO"));
            String email = cursor.getString(cursor.getColumnIndex("EMAIL"));
            String clave = cursor.getString(cursor.getColumnIndex("CLAVE"));
            int matchs = cursor.getInt(cursor.getColumnIndex("MATCHS"));
            int amistades = cursor.getInt(cursor.getColumnIndex("AMISTADES"));

            cursor.close();
            db.close();

            // Guarda los datos del usuario en Usuario (Singleton)
            Usuario usuario = Usuario.getInstance();
            usuario.setId(id);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(email);
            usuario.setClave(clave);
            usuario.setMatchs(matchs);
            usuario.setAmistades(amistades);

            // Inicia la actividad Menu sin pasar datos adicionales
            Intent intent = new Intent(Principal.this, Menu.class);
            startActivity(intent);
            return true;
        }

        cursor.close();
        db.close();
        return false;
    }


}
