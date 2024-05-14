package com.example.playground;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CatBounce extends AppCompatActivity {

    private ImageView ballImageView;
    private TextView scoreTextView;
    private int score = 0;
    private float ballSpeedY = 0; // Velocidad vertical inicial de la pelota
    private float ballSpeedX = 0; // Velocidad horizontal inicial de la pelota
    private boolean ballMoving = false;
    private Handler handler;
    private final int INTERVAL = 20; // Intervalo de actualización en milisegundos (20 ms)
    private final float GRAVITY = 1.0f; // Gravedad aplicada a la pelota

    private final int FLOOR_HEIGHT_OFFSET = 170; // Offset de altura para el suelo
    private int highScore = 0; // Variable para almacenar el puntaje máximo
    private TextView highScoreTextView; // Declaración de la variable TextView para el puntaje máximo
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catbounce_layout);


        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_notifications:
                        // Acción para el ítem de notificaciones (sonido)
                        // Agrega aquí el código que deseas ejecutar cuando se haga clic en el ítem de notificaciones
                        Toast.makeText(CatBounce.this, "Sonido seleccionado", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_home:
                        // Acción para el ítem de inicio (home)
                        // Agrega aquí el código que deseas ejecutar cuando se haga clic en el ítem de inicio
                        Toast.makeText(CatBounce.this, "Inicio seleccionado", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_dashboard:
                        // Acción para el ítem de dashboard (ajustes)
                        // Agrega aquí el código que deseas ejecutar cuando se haga clic en el ítem de ajustes
                        Toast.makeText(CatBounce.this, "Ajustes seleccionados", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        ballImageView = findViewById(R.id.ball);
        scoreTextView = findViewById(R.id.scoreTextView);




        // Dentro de onCreate después de inicializar los elementos de la interfaz de usuario
        highScoreTextView = findViewById(R.id.highScoreTextView);
        updateHighScore(); // Actualizar el TextView con el puntaje máximo inicial



        ballImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!ballMoving) {
                    startBallMovement();
                    ballMoving = true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ballSpeedY = -20; // Reiniciar la velocidad vertical cuando el usuario toca la pelota
                    ballSpeedX = (float) (score * 1.5); // Cambiar la velocidad horizontal aleatoriamente
                    score++; // Incrementar el contador de aciertos
                    scoreTextView.setText(String.valueOf(score)); // Actualizar el TextView
                    ballSpeedY -= score * 0.7; // Aumentar la velocidad vertical con cada toque
                    return true;
                }
                if (score > highScore) {
                    highScore = score; // Actualizar el puntaje máximo si el puntaje actual es mayor
                    updateHighScore(); // Actualizar el TextView del puntaje máximo
                }

                return false;
            }
        });

        handler = new Handler();
    }

    private void updateHighScore() {
        highScoreTextView.setText(""+highScore);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopBallMovement();
    }

    private void startBallMovement() {
        handler.postDelayed(ballRunnable, INTERVAL);
    }

    private void stopBallMovement() {
        ballMoving = false;
        handler.removeCallbacks(ballRunnable);
    }

    private Runnable ballRunnable = new Runnable() {
        @Override
        public void run() {
            moveBall();
            if (ballMoving) {
                handler.postDelayed(this, INTERVAL);
            }
        }
    };

    private void moveBall() {
        // Calcular nueva posición de la pelota
        float newX = ballImageView.getX() + ballSpeedX;
        float newY = ballImageView.getY() + ballSpeedY;
        // Verificar si la pelota ha tocado los bordes horizontales
        if (newX <= 0) {
            newX = 0; // Asegurarse de que la pelota no se salga del borde izquierdo
            ballSpeedX = -ballSpeedX; // Invertir la dirección horizontal
        } else if (newX >= getWindow().getDecorView().getWidth() - ballImageView.getWidth()) {
            newX = getWindow().getDecorView().getWidth() - ballImageView.getWidth(); // Asegurarse de que la pelota no se salga del borde derecho
            ballSpeedX = -ballSpeedX; // Invertir la dirección horizontal
        }

        // Obtener la posición del "suelo" (altura de la pantalla - offset)
        int floorPosition = getWindow().getDecorView().getHeight() - FLOOR_HEIGHT_OFFSET;

        // Verificar si la pelota ha tocado el suelo
        if (newY >= floorPosition - ballImageView.getHeight()) {
            stopBallMovement(); // Detener el movimiento de la pelota
            ballImageView.setVisibility(View.INVISIBLE); // Ocultar la pelota
            showGameOverMessage(); // Mostrar mensaje de juego terminado
            resetGame(); // Reiniciar el juego
            return; // Salir del método moveBall()
        }

        // Aplicar gravedad a la velocidad vertical
        ballSpeedY += GRAVITY;

        // Actualizar la posición de la pelota
        ballImageView.setX(newX);
        ballImageView.setY(newY);

        // Agregar rotación a la pelota
        ballImageView.setRotation(ballImageView.getRotation() + 3); // Cambia el ángulo de rotación gradualmente
    }

    private void showGameOverMessage() {
        Toast.makeText(this, "Juego terminado. Puntuación: " + score, Toast.LENGTH_LONG).show();
    }

    private void resetGame() {
        score = 0;
        scoreTextView.setText("0");

        // Calcular las coordenadas del centro de la pantalla
        int screenWidth = getWindow().getDecorView().getWidth();
        int screenHeight = getWindow().getDecorView().getHeight();
        int ballWidth = ballImageView.getWidth();
        int ballHeight = ballImageView.getHeight();
        float centerX = (screenWidth - ballWidth) / 2f;
        float centerY = (screenHeight - ballHeight) / 2f;

        // Establecer la posición inicial de la pelota en el centro de la pantalla
        ballImageView.setX(centerX);
        ballImageView.setY(centerY);

        ballSpeedX = 0;
        ballSpeedY = 0;
        ballMoving = false;
        ballImageView.setVisibility(View.VISIBLE);
    }
}


