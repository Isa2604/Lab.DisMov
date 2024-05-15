package com.example.playground;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Colorea extends AppCompatActivity {
    private DrawingView drawingView;
    private Button redButton, greenButton, blueButton, yellowButton,
            purpleButton, blackButton, brownButton, orangeButton, buttonTop;
    private ImageButton clearButton, thickButton, normalButton;
    private int[] imageIds = {R.drawable.balon, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.your_image, R.drawable.image1 /* agregar las demás imágenes aquí */};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colorea_layout);

        // Obtener una referencia al ImageView
        ImageView imageView = findViewById(R.id.image_view);

        // Seleccionar una imagen al azar
        int randomIndex = new Random().nextInt(imageIds.length);
        int randomImageId = imageIds[randomIndex];

        // Establecer la imagen seleccionada en el ImageView
        imageView.setImageResource(randomImageId);

        drawingView = findViewById(R.id.drawing_view);

        redButton = findViewById(R.id.button_red);
        greenButton = findViewById(R.id.button_green);
        blueButton = findViewById(R.id.button_blue);
        yellowButton = findViewById(R.id.button_yellow);
        purpleButton = findViewById(R.id.button_purple);
        blackButton = findViewById(R.id.button_black);
        brownButton = findViewById(R.id.button_brown);
        orangeButton = findViewById(R.id.button_orange);

        clearButton = findViewById(R.id.button_clear);
        thickButton = findViewById(R.id.button_thick);
        normalButton = findViewById(R.id.button_normal);
        buttonTop = findViewById(R.id.button_top);

        buttonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clear();
                int randomIndex = new Random().nextInt(imageIds.length);
                int randomImageId = imageIds[randomIndex];

                // Establecer la imagen seleccionada en el ImageView
                imageView.setImageResource(randomImageId);
                Toast.makeText(Colorea.this, "Selected Red", Toast.LENGTH_SHORT).show();
            }
        });
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setColor(Color.RED);
                Toast.makeText(Colorea.this, "Selected Red", Toast.LENGTH_SHORT).show();
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setColor(Color.GREEN);
                Toast.makeText(Colorea.this, "Selected Green", Toast.LENGTH_SHORT).show();
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setColor(Color.BLUE);
                Toast.makeText(Colorea.this, "Selected Blue", Toast.LENGTH_SHORT).show();
            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setColor(Color.YELLOW);
                Toast.makeText(Colorea.this, "Selected Yellow", Toast.LENGTH_SHORT).show();
            }
        });

        purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setColor(Color.parseColor("#800080")); // Purple color
                Toast.makeText(Colorea.this, "Selected Purple", Toast.LENGTH_SHORT).show();
            }
        });

        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setColor(Color.BLACK);
                Toast.makeText(Colorea.this, "Selected Black", Toast.LENGTH_SHORT).show();
            }
        });

        brownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setColor(Color.parseColor("#A52A2A")); // Brown color
                Toast.makeText(Colorea.this, "Selected Brown", Toast.LENGTH_SHORT).show();
            }
        });

        orangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setColor(Color.parseColor("#FFA500"));
                Toast.makeText(Colorea.this, "Selected Orange", Toast.LENGTH_SHORT).show();
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setColor(Color.WHITE); //
                Toast.makeText(Colorea.this, "Cleared Drawing", Toast.LENGTH_SHORT).show();
            }
        });

        thickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setThickness(40); // Ajusta el grosor del trazo
                Toast.makeText(Colorea.this, "Thick Stroke", Toast.LENGTH_SHORT).show();
            }
        });

        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setThickness(10); // Restaura el grosor del trazo normal
                Toast.makeText(Colorea.this, "Normal Stroke", Toast.LENGTH_SHORT).show();
            }
        });

        // Obtener referencias a los boton
        Button button15 = findViewById(R.id.button15);

        // Configurar OnClickListener para el button15
        button15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar la actividad MainActivity
                Intent intent = new Intent(Colorea.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
