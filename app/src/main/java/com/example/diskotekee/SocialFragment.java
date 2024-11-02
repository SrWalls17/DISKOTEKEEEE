package com.example.diskotekee;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SocialFragment extends Fragment {

    public SocialFragment() {
        // Required empty public constructor
    }

    public static SocialFragment newInstance(String param1, String param2) {
        SocialFragment fragment = new SocialFragment();
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
        View view = inflater.inflate(R.layout.fragment_social, container, false);

        // Encuentra los botones por su ID
        Button btnMatch = view.findViewById(R.id.btn_match);
        Button btnAmistades = view.findViewById(R.id.btn_amistades);
        Button busquedaButton = view.findViewById(R.id.busqueda);

        // Configura el evento de clic para el botón de "Ver Matches"
        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de Matches
                Intent intent = new Intent(getActivity(), Matchs.class);
                startActivity(intent);
            }
        });

        // Configura el evento de clic para el botón de "Ver Amistades"
        btnAmistades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de Amistades
                Intent intent = new Intent(getActivity(), Amistades.class);
                startActivity(intent);
            }
        });

        // Configura el evento de clic para el botón de "Búsqueda"
        busquedaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de búsqueda
                Intent intent = new Intent(getActivity(), Busqueda.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
