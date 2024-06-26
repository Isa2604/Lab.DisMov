package com.example.playground;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Random;

public class Animals extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private int[] images = {R.drawable.leon, R.drawable.jirafa, R.drawable.elefante, R.drawable.oveja, R.drawable.tortuga, R.drawable.zebra};
    private GestureDetector gestureDetector;
    private TextView textViewWord;
    private String[] animalWords = {"Leon", "Jirafa", "Elefante", "Oveja", "Tortuga", "Zebra"};
    private HashMap<String, Integer> wordImageMap;
    private MediaPlayer mediaPlayerCorrecto;
    private MediaPlayer mediaPlayerIncorrecto;
    private Toast currentToast;
    private TextView levelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animals_layout);

        // Ocultar la barra de herramientas
        getSupportActionBar().hide();

        // Obtener referencia al ViewFlipper y al TextView
        viewFlipper = findViewById(R.id.viewFlipper);
        textViewWord = findViewById(R.id.textViewWord);

        // Configurar un Gesture Detector para detectar gestos de deslizamiento
        gestureDetector = new GestureDetector(this, new SwipeGestureDetector());

        // Crear el mapa de palabras e imágenes
        createWordImageMap();

        shuffleImages(); // Mostrar imágenes de manera aleatoria al iniciar la actividad
        showRandomWord();// Mostrar una palabra aleatoria en el TextView

        Button btnCheck = findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCombinationAndChangeWord();
            }
        });

        // Inicializar MediaPlayer para reproducir el sonido "correcto"
        mediaPlayerCorrecto = MediaPlayer.create(this, R.raw.correcto);
        // Inicializar MediaPlayer para reproducir el sonido "incorrecto"
        mediaPlayerIncorrecto = MediaPlayer.create(this, R.raw.incorrecto);

        // Obtener referencias a los botones
        Button button12 = findViewById(R.id.button12);

        // Configurar OnClickListener para el button12
        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelar cualquier Toast existente antes de iniciar la nueva actividad
                cancelCurrentToast();

                // Detener la reproducción de los sonidos si están sonando
                if (mediaPlayerCorrecto != null && mediaPlayerCorrecto.isPlaying()) {
                    mediaPlayerCorrecto.stop();
                }
                if (mediaPlayerIncorrecto != null && mediaPlayerIncorrecto.isPlaying()) {
                    mediaPlayerIncorrecto.stop();
                }

                // Crear un Intent para iniciar la actividad MainActivity
                Intent intent = new Intent(Animals.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancelar el Toast actual antes de destruir la actividad
        cancelCurrentToast();
    }

    private void createWordImageMap() {
        wordImageMap = new HashMap<>();
        wordImageMap.put("Leon", R.drawable.leon);
        wordImageMap.put("Jirafa", R.drawable.jirafa);
        wordImageMap.put("Elefante", R.drawable.elefante);
        wordImageMap.put("Oveja", R.drawable.oveja);
        wordImageMap.put("Tortuga", R.drawable.tortuga);
        wordImageMap.put("Zebra", R.drawable.zebra);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void showNextImage() {
        viewFlipper.setInAnimation(this, R.anim.slide_in_right);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
        viewFlipper.showNext();
    }

    private void showPreviousImage() {
        viewFlipper.setInAnimation(this, R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_right);
        viewFlipper.showPrevious();
    }

    private void showRandomWord() {
        Random random = new Random();
        int randomIndex = random.nextInt(animalWords.length);
        String randomWord = animalWords[randomIndex];
        textViewWord.setText(randomWord);
    }

    private void shuffleImages() {
        Random random = new Random();
        for (int i = 0; i < images.length; i++) {
            int randomIndexToSwap = random.nextInt(images.length);
            int temp = images[randomIndexToSwap];
            images[randomIndexToSwap] = images[i];
            images[i] = temp;
        }

        // Actualizar imágenes en el ViewFlipper
        viewFlipper.removeAllViews();
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(images[i]);
            imageView.setTag(images[i]); // Establecer la etiqueta con la ID de recurso de imagen
            viewFlipper.addView(imageView);
        }
    }

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();
            if (Math.abs(diffX) > Math.abs(diffY) &&
                    Math.abs(diffX) > SWIPE_THRESHOLD &&
                    Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    showPreviousImage();
                } else {
                    showNextImage();
                }
                return true;
            }
            return false;
        }
    }

    private void checkCombination() {
        String currentWord = textViewWord.getText().toString();
        ImageView currentImageView = (ImageView) viewFlipper.getCurrentView();
        int currentImageId = (int) currentImageView.getTag();

        if (wordImageMap.containsKey(currentWord) && wordImageMap.get(currentWord) == currentImageId) {
            // La combinación es correcta
            mediaPlayerCorrecto.start(); // Reproducir el sonido "correcto"
            showAnimation(R.drawable.palomita);// La respuesta es correcta animacion
            changeWord(); // Cambiar la palabra solo si la combinación es correcta
        } else {
            // La combinación es incorrecta
            showAnimation(R.drawable.tacha);// La respuesta es incorrecta animacion
            mediaPlayerIncorrecto.start(); // Reproducir el sonido "incorrecto"
        }
    }

    private void cancelCurrentToast() {
        if (currentToast != null) {
            currentToast.cancel();
        }
    }

    private void changeWord() {
        Random random = new Random();
        int randomIndex = random.nextInt(animalWords.length);
        String randomWord = animalWords[randomIndex];
        textViewWord.setText(randomWord);
    }

    private void checkCombinationAndChangeWord() {
        checkCombination();
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
            }
        }, 2000);

        // Mostrar el diálogo
        dialog.show();
    }
}
