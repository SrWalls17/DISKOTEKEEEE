package com.example.diskotekee;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

public class QR extends AppCompatActivity {

    private ImageView qrImageView;
    private ImageView regresoImageView;

    private void consultarExistenciaDatos(String id_usuario) {
        String url = "http://192.168.66.1/diskotekee/validar_datos.php?id_usuario=" + id_usuario;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean existe = response.getBoolean("existe");

                            if (existe) {
                                // Si los datos existen, realizar la consulta para obtenerlos
                                consultarUsuario(id_usuario);
                            } else {
                                // Si no existen datos, mostrar un diálogo
                                mostrarDialogoCrearPerfil();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(QR.this, "Error al procesar la validación.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(QR.this, "Error en la solicitud de validación.", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        qrImageView = findViewById(R.id.qrImageView);
        regresoImageView = findViewById(R.id.regreso);

        // Configurar el clic en la imagen de regreso
        regresoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual y vuelve a la anterior
            }
        });

        // Obtener el ID de usuario desde SharedPreferences
        String id_usuario = getSharedPreferences("Usuario", MODE_PRIVATE).getString("id", "");

        if (!id_usuario.isEmpty()) {
            // Verificar si existen datos antes de continuar
            consultarExistenciaDatos(id_usuario);
        } else {
            Toast.makeText(this, "ID de usuario no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void consultarUsuario(String id_usuario) {
        String url = "http://192.168.66.1/diskotekee/qr.php?id_usuario=" + id_usuario;

        // Crear la solicitud GET a la API
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obtener los datos del usuario de la respuesta JSON
                            String nombre = response.getString("nombre");
                            String apellido = response.getString("apellido");
                            String edad = response.getString("edad");
                            String gustos = response.getString("gustos");
                            String instagram = response.getString("instagram");
                            String ocupacion = response.getString("ocupacion");

                            // Generar el código QR con los datos del usuario
                            String usuarioDatos = "Nombre: " + nombre + " " + apellido + "\n"
                                    + "Edad: " + edad + "\n"
                                    + "Gustos: " + gustos + "\n"
                                    + "Instagram: " + instagram + "\n"
                                    + "Ocupación: " + ocupacion;

                            generarCodigoQR(usuarioDatos);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(QR.this, "Error al procesar los datos.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(QR.this, "Error en la solicitud.", Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(request);
    }

    private void generarCodigoQR(String datos) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(datos, BarcodeFormat.QR_CODE, 512, 512);

            Bitmap bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565);
            for (int x = 0; x < 512; x++) {
                for (int y = 0; y < 512; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            qrImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al generar el código QR", Toast.LENGTH_SHORT).show();
        }
    }
    private void mostrarDialogoCrearPerfil() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Perfil Social requerido")
                .setMessage("Para compartir tu perfil, debes crear primero tu Perfil Social.")
                .setPositiveButton("Crear Perfil", (dialog, which) -> {
                    // Redirigir al usuario a la actividad para crear el Perfil Social
                    Intent intent = new Intent(QR.this, PerfilSocial.class); // Cambia CrearPerfilActivity por el nombre de tu actividad
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false) // Deshabilita la capacidad de cerrar el diálogo tocando fuera de él
                .show();
    }


}
