package com.example.diskotekee;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarFragment extends Fragment {
    public BarFragment() {
        // Required empty public constructor
    }
    public static BarFragment newInstance(String param1, String param2) {
        BarFragment fragment = new BarFragment();
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
        return inflater.inflate(R.layout.fragment_bar, container, false);
    }
}