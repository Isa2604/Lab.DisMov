package com.example.playground;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CatBounce extends AppCompatActivity {

    private ImageView ballImageView;
    private TextView scoreTextView;
    private int score = 0;
    private float ballSpeedY = 10; // Velocidad vertical de la pelota
    private float ballSpeedX = 0; // Velocidad horizontal de la pelota
    private boolean ballMoving = true; // Indica si la pelota está en movimiento
    private Animation bounceAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catbounce_layout);

        ballImageView = findViewById(R.id.ballImageView);
        scoreTextView = findViewById(R.id.scoreTextView);

        // Cargar la animación de rebote
        bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation);

        ballImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Incrementar el contador de aciertos
                score++;
                scoreTextView.setText(""+score);

                // Realizar un rebote más grande
                performBounce();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Iniciar el movimiento de la pelota
            ballMoving = true;
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Iniciar el movimiento de la pelota cuando la actividad se reanuda
        startBallMovement();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Detener el movimiento de la pelota cuando la actividad se pausa
        stopBallMovement();
    }

    private void startBallMovement() {
        // Iniciar el bucle de actualización para mover la pelota
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (ballMoving) {
                        // Actualizar la posición de la pelota
                        moveBall();
                    }
                    try {
                        Thread.sleep(20); // Intervalo de actualización en milisegundos (por ejemplo, 20 ms)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void stopBallMovement() {
        // Detener el movimiento de la pelota
        ballMoving = false;
    }

    private void moveBall() {
        // Actualizar la posición vertical de la pelota
        ballImageView.setY(ballImageView.getY() + ballSpeedY);
        // Actualizar la posición horizontal de la pelota
        ballImageView.setX(ballImageView.getX() + ballSpeedX);

        // Verificar si la pelota ha alcanzado los límites de la pantalla
        if (ballImageView.getY() <= 0 || ballImageView.getY() >= getWindow().getDecorView().getHeight() - ballImageView.getHeight()) {
            // Invertir la dirección vertical de la pelota para simular el rebote
            ballSpeedY = -ballSpeedY;
        }
        if (ballImageView.getX() <= 0 || ballImageView.getX() >= getWindow().getDecorView().getWidth() - ballImageView.getWidth()) {
            // Invertir la dirección horizontal de la pelota para simular el rebote
            ballSpeedX = -ballSpeedX;
        }
    }

    private void performBounce() {
        // Mostrar la animación de rebote
        ballImageView.startAnimation(bounceAnimation);
        // Incrementar la velocidad vertical para un rebote más grande
        ballSpeedY *= 1.5f;
        // Cambiar la dirección horizontal de la pelota aleatoriamente
        ballSpeedX = (float) (Math.random() * 20 - 10);
    }
}