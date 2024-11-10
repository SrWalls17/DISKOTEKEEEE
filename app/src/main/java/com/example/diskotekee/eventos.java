package com.example.diskotekee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class eventos extends AppCompatActivity {

    private int selectedCount = 0;
    private Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        ImageView evento1 = findViewById(R.id.evento1);
        ImageView evento2 = findViewById(R.id.evento2);
        ImageView evento3 = findViewById(R.id.evento3);
        buyButton = findViewById(R.id.buyButton);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                if (view.isSelected()) {
                    selectedCount++;
                    view.setAlpha(0.5f);  // Indica que está seleccionado
                } else {
                    selectedCount--;
                    view.setAlpha(1.0f);  // Vuelve al estado normal
                }
                updateButton();
            }
        };

        evento1.setOnClickListener(clickListener);
        evento2.setOnClickListener(clickListener);
        evento3.setOnClickListener(clickListener);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción de compra
                // Ejemplo: mostrar un mensaje o iniciar otra actividad
            }
        });
    }

    private void updateButton() {
        buyButton.setText("Comprar (" + selectedCount + " seleccionados)");
    }
}
