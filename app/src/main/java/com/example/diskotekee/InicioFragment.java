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
            // Configuración adicional si se necesitan argumentos
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        Button feedbackButton = view.findViewById(R.id.feedback);
        Button listaFeedbackButton = view.findViewById(R.id.btn_lista_feedback);
        Button comprarButton = view.findViewById(R.id.comprar);

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateUsDialog rateUsDialog = new RateUsDialog(getActivity());
                rateUsDialog.setCancelable(false);
                rateUsDialog.show();
            }
        });

        listaFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListaFeedback.class);
                startActivity(intent);
            }
        });

        // Listener para redirigir a la actividad Eventos
        comprarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), eventos.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
