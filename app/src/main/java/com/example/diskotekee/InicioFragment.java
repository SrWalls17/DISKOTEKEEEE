package com.example.diskotekee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class InicioFragment extends Fragment {

    public InicioFragment() {
        // Required empty public constructor
    }

    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        // Identificar el botón de feedback
        Button feedbackButton = view.findViewById(R.id.feedback);

        // Agregar listener para redirigir a la actividad Feedback
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar el diálogo de calificación cuando el botón de feedback sea presionado
                RateUsDialog rateUsDialog = new RateUsDialog(getActivity());
                rateUsDialog.setCancelable(false);
                rateUsDialog.show();
            }
        });

        return view;
    }
}
