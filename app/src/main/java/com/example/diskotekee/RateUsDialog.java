package com.example.diskotekee;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RateUsDialog extends Dialog {
    private float userRate = 0;
    private RatingBar ratingBar;
    private EditText comentarioEditText;
    private Context context;
    private ImageView ratingImage;
    private static final String TAG = "RateUsDialog";

    public RateUsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_us_dialog_layout);

        ratingBar = findViewById(R.id.ratingBar);
        comentarioEditText = findViewById(R.id.comentario);
        ratingImage = findViewById(R.id.ratingImage); // Imagen que cambiará
        AppCompatButton rateNowBtn = findViewById(R.id.rateNowBtn);
        AppCompatButton laterBtn = findViewById(R.id.laterBtn);

        // Cambia la imagen según la calificación seleccionada
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            userRate = rating;
            actualizarImagenSegunEstrellas((int) userRate);
        });

        rateNowBtn.setOnClickListener(v -> enviarFeedback());
        laterBtn.setOnClickListener(v -> dismiss());
    }

    private void actualizarImagenSegunEstrellas(int estrellas) {
        switch (estrellas) {
            case 1:
                ratingImage.setImageResource(R.drawable.one_star);
                break;
            case 2:
                ratingImage.setImageResource(R.drawable.two_star);
                break;
            case 3:
                ratingImage.setImageResource(R.drawable.three_star);
                break;
            case 4:
                ratingImage.setImageResource(R.drawable.four_star);
                break;
            case 5:
                ratingImage.setImageResource(R.drawable.five_star);
                break;
            default:
                ratingImage.setImageResource(R.drawable.three_star); // Imagen predeterminada
                break;
        }
    }

    private void enviarFeedback() {
        String url = "http://192.168.66.1/diskotekee/submit_feedback.php";
        int estrellas = (int) userRate;
        String comentario = comentarioEditText.getText().toString();

        if (estrellas == 0) {
            Toast.makeText(context, "Por favor, selecciona una calificación.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (comentario.isEmpty()) {
            Toast.makeText(context, "Por favor, escribe un comentario.", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, url,
                response -> {
                    Log.i(TAG, "Feedback enviado correctamente: " + response);
                    Toast.makeText(context, "Gracias por tu opinión.", Toast.LENGTH_SHORT).show();
                    dismiss();
                },
                error -> {
                    Log.e(TAG, "Error al enviar feedback", error);
                    Toast.makeText(context, "Error al enviar feedback. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("estrellas", String.valueOf(estrellas));
                params.put("comentario", comentario);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}

