package com.example.diskotekee;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class VerdadoReto extends AppCompatActivity {

    // Listas de preguntas de verdad y retos
    private ArrayList<String> verdades;
    private ArrayList<String> retos;

    // Elementos de la interfaz
    private Button btnVerdad, btnReto, btnAleatorio;
    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verdado_reto);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Inicializando las listas de preguntas de verdad y retos
        verdades = new ArrayList<>();
        retos = new ArrayList<>();

        // Llenamos las listas con 6 preguntas de verdad y 6 retos
        verdades.add("1. ¿Qué harías si te enamoraras de alguien que no puedes tener?");
        verdades.add("2. ¿Cuál ha sido tu mayor mentira?");
        verdades.add("3. ¿Alguna vez has tenido un amor no correspondido?");
        verdades.add("4. ¿Qué es lo más vergonzoso que te ha pasado en público?");
        verdades.add("5. ¿Has robado alguna vez algo? ¿Qué fue?");
        verdades.add("6. ¿Alguna vez has dicho te amo sin sentirlo realmente?");

        retos.add("1. Saca a bailar a alguien.");
        retos.add("2. Invita a alguien un trago.");
        retos.add("3. Da un beso a alguien.");
        retos.add("4. Haz 10 flexiones.");
        retos.add("5. Consigue el número de teléfono de alguien.");
        retos.add("6. Tómate un shot.");

        // Inicializando los elementos de la interfaz
        btnVerdad = findViewById(R.id.btnVerdad);
        btnReto = findViewById(R.id.btnReto);
        btnAleatorio = findViewById(R.id.btnAleatorio);
        txtResultado = findViewById(R.id.txtResultado);

        // Configurando el botón de "Verdad"
        btnVerdad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarVerdad();
            }
        });

        // Configurando el botón de "Reto"
        btnReto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarReto();
            }
        });

        // Configurando el botón de "Aleatorio"
        btnAleatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAleatorio();
            }
        });
    }

    // Función para mostrar una pregunta de "Verdad"
    private void mostrarVerdad() {
        Random rand = new Random();
        int index = rand.nextInt(verdades.size());
        txtResultado.setText(verdades.get(index));
    }

    // Función para mostrar un "Reto"
    private void mostrarReto() {
        Random rand = new Random();
        int index = rand.nextInt(retos.size());
        txtResultado.setText(retos.get(index));
    }

    // Función para mostrar aleatoriamente una "Verdad" o un "Reto"
    private void mostrarAleatorio() {
        Random rand = new Random();
        int randomChoice = rand.nextInt(2); // 0 para verdad, 1 para reto

        if (randomChoice == 0) {
            mostrarVerdad();
        } else {
            mostrarReto();
        }
    }
}
