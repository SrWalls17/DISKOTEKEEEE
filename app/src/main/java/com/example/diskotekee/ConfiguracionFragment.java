package com.example.diskotekee;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ConfiguracionFragment extends Fragment {

    public ConfiguracionFragment() {
        // Required empty public constructor
    }

    public static ConfiguracionFragment newInstance(String param1, String param2) {
        ConfiguracionFragment fragment = new ConfiguracionFragment();
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
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);

        // Configuración del botón Salir
        Button btnSalir = view.findViewById(R.id.btn_salir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoSalida();
            }
        });

        // Configuración del botón Modificar
        Button modificarButton = view.findViewById(R.id.comprar);
        modificarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ModificarEliminar.class);
                    startActivity(intent);
            }
        });
        return view;
    }

    // Método para mostrar el AlertDialog
    private void mostrarDialogoSalida() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cerrar sesión y regresar a la actividad principal
                        cerrarSesionYRegresar();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cancelar el diálogo
                        dialog.dismiss();
                    }
                });
        // Crear y mostrar el AlertDialog
        builder.create().show();
    }

    // Método para cerrar sesión y regresar a la actividad principal
    private void cerrarSesionYRegresar() {
        // Aquí puedes agregar la lógica para cerrar sesión (ej. limpiar datos de usuario)

        // Intent para regresar a la actividad principal
        Intent intent = new Intent(getActivity(), Principal.class); // Asegúrate de cambiar "MainActivity" por el nombre correcto de tu actividad principal
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish(); // Opcional: cerrar la actividad actual
    }
}
