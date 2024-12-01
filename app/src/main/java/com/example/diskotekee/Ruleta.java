package com.example.diskotekee;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Ruleta extends AppCompatActivity {

    private static final String[] sectors = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private static final int[] sectorDegrees = new int[sectors.length];
    private int degree = 0;
    private boolean isSpinning = false;
    private static final Random random = new Random();

    private ImageView wheel;

    // Lista de retos, uno por cada sector de la ruleta
    private static final String[] retos = {
            "1. Saca a bailar a alguien.",
            "2. Invita a alguien un trago.",
            "3. Da un beso a alguien.",
            "4. Haz 10 flexiones.",
            "5. Consigue un Instagram.",
            "6. Consigue el número de teléfono.",
            "7. Tómate un shot.",
            "8. Regala un shot a alguien.",
            "9. Tómate el vaso completo.",
            "10. Haz una broma telefónica.",
            "11. Juega beso o cachetada.",
            "12. ¡Te salvaste!"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleta);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Mostrar el AlertDialog con los retos al inicio
        showRetosDialog();

        final ImageView spinBtn = findViewById(R.id.spinbtn);
        wheel = findViewById(R.id.wheel);

        getDegreeForSectors();

        spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSpinning) {
                    spin();
                    isSpinning = true;
                }
            }
        });
    }

    private void showRetosDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Bienvenido a la Ruleta de Retos!");
        builder.setMessage("Aquí están los retos que puedes obtener:\n\n" + getRetosList());
        builder.setPositiveButton("¡Entendido!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción al hacer clic en "¡Entendido!"
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private String getRetosList() {
        StringBuilder retosList = new StringBuilder();
        for (String reto : retos) {
            retosList.append(reto).append("\n");
        }
        return retosList.toString();
    }

    private void spin() {
        degree = random.nextInt(sectors.length); // Cambié -1 por 'sectors.length' para incluir el último número

        RotateAnimation rotateAnimation = new RotateAnimation(0, (360 * sectors.length) + sectorDegrees[degree],
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(3600);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // No hace nada en este caso.
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Aquí mostramos el reto dependiendo de lo que salió en la ruleta
                int sectorIndex = sectors.length - (degree + 1); // Obtiene el índice del sector
                String reto = retos[sectorIndex]; // Asocia el reto con el sector
                Toast.makeText(Ruleta.this, "Tu reto: " + reto, Toast.LENGTH_LONG).show();
                isSpinning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // No hace nada en este caso.
            }
        });

        wheel.startAnimation(rotateAnimation);
    }

    private void getDegreeForSectors() {
        int sectorDegree = 360 / sectors.length;
        for (int i = 0; i < sectors.length; i++) {
            sectorDegrees[i] = (i + 1) * sectorDegree;
        }
    }
}
