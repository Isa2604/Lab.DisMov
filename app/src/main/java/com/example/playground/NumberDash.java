package com.example.playground;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class NumberDash extends AppCompatActivity {

    private GridLayout objectGridLayout;
    private Button[] countButtons;
    private TextView levelTextView;
    private int[] objectImages = {
            R.drawable.balon,
            R.drawable.manzana,
            R.drawable.rosa,
            R.drawable.frijol,
            R.drawable.dulce,
            R.drawable.perrito
    };

    private int numberOfObjects;
    private int currentLevel = 1; // Variable para rastrear el nivel actual
    private boolean isFirstTime = true; // Bandera para identificar si es la primera vez que se ejecuta la aplicación

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_dash_layout);

        objectGridLayout = findViewById(R.id.objectGridLayout);
        countButtons = new Button[]{
                findViewById(R.id.buttonCount1),
                findViewById(R.id.buttonCount2),
                findViewById(R.id.buttonCount3),
                findViewById(R.id.buttonCount4)
        };
        levelTextView = findViewById(R.id.levelTextView); // Obtener la referencia del TextView de nivel
        showLevelTextInCenter();

        // Configurar OnClickListener para los botones de conteo
        for (Button button : countButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener el texto del botón presionado
                    String buttonText = ((Button) v).getText().toString();

                    // Verificar si la respuesta es correcta
                    if (buttonText.equals(String.valueOf(numberOfObjects))) {
                        // La respuesta es correcta
                        showAnimation(R.drawable.palomita);
                        currentLevel++; // Incrementar el nivel al seleccionar la respuesta correcta
                        if (currentLevel <= 10) {
                            generateObjects(); // Generar un nuevo nivel después de responder correctamente
                        } else {
                            // Fin del juego
                            showCongratulationsMessage();
                        }
                    } else {
                        // La respuesta es incorrecta
                        showAnimation(R.drawable.tacha);
                        // No hacer nada si la respuesta es incorrecta
                    }
                }
            });
        }

        // Generar los objetos y las opciones de conteo al iniciar
        generateObjects();
    }

    private void generateObjects() {
        // Limpiar el GridLayout antes de agregar nuevos objetos
        objectGridLayout.removeAllViews();

        // Generar un número aleatorio de objetos según el nivel
        Random random = new Random();
        int minObjects = 1 + (currentLevel - 1) / 2; // Número mínimo de objetos
        int maxObjects = Math.min(6 + currentLevel, 15); // Máximo de 15 objetos o más, dependiendo del nivel

        numberOfObjects = random.nextInt(maxObjects - minObjects + 1) + minObjects;

        // Elegir aleatoriamente una imagen para el objeto del nivel
        int objectImageIndex = random.nextInt(objectImages.length);
        int correctCount = numberOfObjects; // El número de objetos mostrados es la respuesta correcta

        // Configurar el GridLayout con el número adecuado de columnas y filas
        objectGridLayout.setColumnCount((int) Math.ceil(Math.sqrt(numberOfObjects))); // Ajusta el número de columnas según la raíz cuadrada del número de objetos
        objectGridLayout.setRowCount((int) Math.ceil((double) numberOfObjects / objectGridLayout.getColumnCount())); // Calcula el número de filas necesario para distribuir uniformemente los objetos

        // Mostrar los objetos en el GridLayout
        for (int i = 0; i < numberOfObjects; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(objectImages[objectImageIndex]);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setAdjustViewBounds(true);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Especificar la columna con peso 1
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Especificar la fila con peso 1
            imageView.setLayoutParams(params);
            objectGridLayout.addView(imageView);
        }

        // Mostrar opciones de conteo aleatorias
        int correctButtonIndex = random.nextInt(countButtons.length);
        countButtons[correctButtonIndex].setText(String.valueOf(correctCount)); // Correct count is the number of objects

        // Generar otros números aleatorios para los botones de conteo
        Set<Integer> usedCounts = new HashSet<>();
        usedCounts.add(correctCount);
        for (int i = 0; i < countButtons.length; i++) {
            if (i != correctButtonIndex) {
                int count;
                do {
                    count = random.nextInt(10) + 1; // Entre 1 y 10
                } while (usedCounts.contains(count));
                usedCounts.add(count);
                countButtons[i].setText(String.valueOf(count));
            }
        }
    }

    private void showAnimation(int imageResource) {
        // Crear la vista ImageView para mostrar la animación
        ImageView animationImageView = new ImageView(this);
        animationImageView.setImageResource(imageResource);

        // Cargar la animación desde el archivo XML
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);

        // Asignar la animación a la vista ImageView
        animationImageView.startAnimation(scaleAnimation);

        // Crear un diálogo personalizado con un fondo transparente
        Dialog dialog = new Dialog(this, R.style.TransparentDialog);
        dialog.setContentView(animationImageView);
        dialog.setCancelable(true); // Permitir al usuario cerrar el diálogo tocando fuera de él

        // Configurar un temporizador para cerrar el diálogo después de 1 segundo
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss(); // Cerrar el diálogo después de 1 segundo
                if (imageResource == R.drawable.palomita) { // Mostrar el mensaje de nivel solo si se muestra la imagen de la palomita
                    showLevelTextInCenter(); // Mostrar el texto en el centro después de la animación
                }
            }
        }, 1000);

        // Mostrar el diálogo
        dialog.show();
    }

    private void showLevelTextInCenter() {
        // Mostrar el texto en el centro con el nivel actual
        levelTextView.setVisibility(View.VISIBLE);
        levelTextView.setText("Nivel " + currentLevel);
        levelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36); // Tamaño de texto más grande

        // Ocultar el texto después de 1 segundo
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                levelTextView.setVisibility(View.GONE);
            }
        }, 1000);
    }

    private void showCongratulationsMessage() {
        // Mostrar un mensaje de felicitaciones al completar todos los niveles
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.congratulations_dialog);
        dialog.setCancelable(false); // Evitar que se cierre tocando fuera del diálogo
        dialog.show();
    }
}
