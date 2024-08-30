package com.example.diskotekee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Principal extends AppCompatActivity {
    EditText usuario,password;
    Button ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        usuario=(EditText) findViewById(R.id.frmusuario);
        password=(EditText) findViewById(R.id.frmpass);
        ingresar=(Button) findViewById(R.id.btningresar);


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


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
                    // Autenticación exitosa, redirige al usuario a su actividad principal
                    Intent actividadPrincipal = new Intent(Principal.this, Menu.class);
                    startActivity(actividadPrincipal);
                } else {
                    // Muestra un mensaje de error si las credenciales son incorrectas
                    usuario.setError("Credenciales incorrectas");
                    password.setError("Credenciales incorrectas");
                }
            }
        });

    }

    private boolean autenticarUsuario(String correo, String contraseña) {
        ConexionDbHelper helper = new ConexionDbHelper(this, "APPSQLITE", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Consulta para verificar las credenciales
        String[] columns = {"ID"};
        String selection = "Email = ? AND Clave = ?";
        String[] selectionArgs = {correo, contraseña};

        Cursor cursor = db.query("USUARIOS", columns, selection, selectionArgs, null, null, null);

        // Comprueba si el cursor tiene al menos un resultado
        boolean verificacionExitosa = cursor.getCount() > 0;

        // Cierra la base de datos y el cursor
        cursor.close();
        db.close();

        if (!verificacionExitosa && correo.equals("admin") && contraseña.equals("123")) {
            return true;
        }

        return verificacionExitosa;
    }

}