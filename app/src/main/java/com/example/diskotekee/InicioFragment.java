package com.example.diskotekee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        // Configuración del botón para el feedback
        Button feedbackButton = view.findViewById(R.id.feedback);
        Button listaFeedbackButton = view.findViewById(R.id.btn_lista_feedback);
        ImageView atrasButton = view.findViewById(R.id.atras);

        // Listener para redirigir a la actividad Feedback
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateUsDialog rateUsDialog = new RateUsDialog(getActivity());
                rateUsDialog.setCancelable(false);
                rateUsDialog.show();
            }
        });

        // Listener para redirigir a la actividad ListaFeedback
        listaFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListaFeedback.class);
                startActivity(intent);
            }
        });

        // Listener para redirigir a la actividad ModificarEliminar
        atrasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el email del usuario (por ejemplo, de SharedPreferences)
                String email = "usuario@example.com"; // Cambia esto por el valor real del email

                Intent intent = new Intent(getActivity(), ModificarEliminar.class);
                intent.putExtra("EMAIL", email);
                startActivity(intent);
            }
        });

        return view;
    }

}
