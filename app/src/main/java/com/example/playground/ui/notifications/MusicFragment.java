package com.example.playground.ui.notifications;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.playground.R;

public class MusicFragment extends Fragment {

    private Switch soundSwitch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_music, container, false);

        soundSwitch = root.findViewById(R.id.sound_switch);

        // Recuperar el estado del Switch guardado en SharedPreferences
        SharedPreferences preferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        boolean isSoundEnabled = preferences.getBoolean("sound_enabled", true);
        soundSwitch.setChecked(isSoundEnabled);

        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Guardar el estado del Switch en SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("sound_enabled", isChecked);
                editor.apply();
            }
        });

        return root;
    }
}