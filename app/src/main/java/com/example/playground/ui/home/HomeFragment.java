package com.example.playground.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
import com.example.playground.Piano;
import com.example.playground.R;
import com.example.playground.databinding.FragmentHomeBinding;
import com.example.playground.CatBounce;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializar el MediaPlayer con la música de fondo
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.musicafondo);
        mediaPlayer.setLooping(true); // Repetir la música en un loop
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtener estado del sonido desde SharedPreferences
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean isSoundEnabled = preferences.getBoolean("sound_enabled", true);
        // Inicializar el MediaPlayer con la música de fondo si el sonido está habilitado
        if (isSoundEnabled) {
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.musicafondo);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        // Obtener referencias a los botones
        Button button = root.findViewById(R.id.button);
        Button button2 = root.findViewById(R.id.button2);
        Button button3 = root.findViewById(R.id.button3);
        Button button5 = root.findViewById(R.id.button5);

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
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar al hacer clic en el tercer botón
                Intent intent = new Intent(getActivity(), Piano.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Verificar si el MediaPlayer no es nulo antes de reanudar la reproducción
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Verificar si el MediaPlayer no es nulo y está reproduciendo antes de pausar
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // No necesitas liberar recursos del MediaPlayer aquí
        // Ya que lo liberarás en onDestroy() del fragmento
        // Detener y liberar el MediaPlayer cuando el fragmento se destruye
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Liberar recursos del MediaPlayer cuando el fragmento se destruye
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}