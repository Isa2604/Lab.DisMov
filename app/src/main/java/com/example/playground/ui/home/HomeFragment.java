package com.example.playground.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.playground.Animals;
import com.example.playground.R;
import com.example.playground.databinding.FragmentHomeBinding;
import com.example.playground.CatBounce;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtener referencias a los botones
        Button button = root.findViewById(R.id.button);
        Button button2 = root.findViewById(R.id.button2);
        Button button3 = root.findViewById(R.id.button3);

        // Configurar OnClickListener para cada botón
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar al hacer clic en el primer botón
                // Por ejemplo, iniciar una nueva actividad o fragmento
                Intent intent = new Intent(getActivity(), Animals.class);
                startActivity(intent);
            }


        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar al hacer clic en el segundo botón
                Intent intent = new Intent(getActivity(), CatBounce.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar al hacer clic en el tercer botón
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}