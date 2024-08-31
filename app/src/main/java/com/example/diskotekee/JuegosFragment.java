package com.example.diskotekee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class JuegosFragment extends Fragment {

    public JuegosFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_juegos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the ImageView by their IDs
        View imageViewRuleta = view.findViewById(R.id.imageView);
        View imageViewVerdadReto = view.findViewById(R.id.imageView2);

        // Set onClick listeners
        imageViewRuleta.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Ruleta.class);
            startActivity(intent);
        });

        imageViewVerdadReto.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), VerdadoReto.class);
            startActivity(intent);
        });
    }
}
