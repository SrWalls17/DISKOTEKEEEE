package com.example.diskotekee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class Ruleta extends AppCompatActivity {

    private static final String [] sectors = {"1","2","3","4","5","6","7","8","9","10","11","12",};
    private static final int [] sectorDegrees= new int [sectors.length];
    private int degree = 0;
    private boolean isSpinning = false;
    private static final Random random = new Random();

    private ImageView wheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleta);

        final ImageView spinBtn = findViewById(R.id.spinbtn);
        wheel = findViewById(R.id.wheel);

        getDegreeForSectors();

        spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSpinning){
                    spin();
                    isSpinning=true;

                }
            }
        });
    }

    private void spin(){
        degree = random.nextInt(sectors.length-1);

        RotateAnimation rotateAnimation = new RotateAnimation(0,(360 * sectors.length)+ sectorDegrees[degree],
                RotateAnimation.RELATIVE_TO_SELF,0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(3600);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(Ruleta.this, "Tienes "+sectors[sectors.length-(degree+1)]+" puntos", Toast.LENGTH_SHORT).show();
                isSpinning=false;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        wheel.startAnimation(rotateAnimation);

    }

    private void getDegreeForSectors(){
        int sectorDegree = 360/ sectors.length;
        for(int i =0; i < sectors.length;i++){

            sectorDegrees[i]=(i+1)* sectorDegree;
        }
    }
}