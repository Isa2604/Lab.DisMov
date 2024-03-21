package com.example.playground;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.playground.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Definir destinos de nivel superior para la configuración de la barra de herramientas
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard, R.id.navigation_home, R.id.navigation_notifications)
                .build();

        // Configurar el controlador de navegación y la barra de herramientas
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Ocultar la barra de herramientas
        getSupportActionBar().hide();

        // Configurar la navegación con la barra de herramientas
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}