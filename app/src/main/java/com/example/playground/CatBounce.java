package com.example.playground;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CatBounce extends AppCompatActivity {

    private ImageView ballImageView;
    private TextView scoreTextView;
    private int score = 0;
    private float ballSpeedY = 0;
    private float ballSpeedX = 0;
    private boolean ballMoving = false;
    private Handler handler;
    private final int INTERVAL = 20;
    private final float GRAVITY = 1.0f;

    private final int FLOOR_HEIGHT_OFFSET = 170;
    private int highScore = 0;
    private TextView highScoreTextView;
    private MediaPlayer clickSoundPlayer;
    private MediaPlayer scoreSoundPlayer;

    private Toast gameOverToast; // Para guardar la referencia del Toast de juego terminado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catbounce_layout);

        clickSoundPlayer = MediaPlayer.create(this, R.raw.click_sound);
        scoreSoundPlayer = MediaPlayer.create(this, R.raw.score_sound);

        getSupportActionBar().hide();

        ballImageView = findViewById(R.id.ball);
        scoreTextView = findViewById(R.id.scoreTextView);

        highScoreTextView = findViewById(R.id.highScoreTextView);
        updateHighScore();

        ballImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!ballMoving) {
                    startBallMovement();
                    ballMoving = true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ballSpeedY = -20;
                    ballSpeedX = (float) (score * 1.5);
                    score++;
                    scoreTextView.setText(String.valueOf(score));
                    ballSpeedY -= score * 0.7;

                    clickSoundPlayer.start();

                    if (score % 5 == 0) {
                        scoreSoundPlayer.start();
                    }
                    return true;
                }
                if (score > highScore) {
                    highScore = score;
                    updateHighScore();
                }

                return false;
            }
        });

        handler = new Handler();

        Button button13 = findViewById(R.id.button13);

        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Detener el movimiento de la pelota
                stopBallMovement();
                // Cancelar el Toast de juego terminado si está mostrándose
                if (gameOverToast != null) {
                    gameOverToast.cancel();
                }
                // Crear un Intent para iniciar la actividad MainActivity
                Intent intent = new Intent(CatBounce.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateHighScore() {
        highScoreTextView.setText(String.valueOf(highScore));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Detener el movimiento de la pelota al pausar la actividad
        stopBallMovement();
        // Liberar los recursos de los MediaPlayer
        clickSoundPlayer.release();
        scoreSoundPlayer.release();
        // Cancelar el Toast de juego terminado si está mostrándose
        if (gameOverToast != null) {
            gameOverToast.cancel();
        }
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
        float newX = ballImageView.getX() + ballSpeedX;
        float newY = ballImageView.getY() + ballSpeedY;

        if (newX <= 0) {
            newX = 0;
            ballSpeedX = -ballSpeedX;
        } else if (newX >= getWindow().getDecorView().getWidth() - ballImageView.getWidth()) {
            newX = getWindow().getDecorView().getWidth() - ballImageView.getWidth();
            ballSpeedX = -ballSpeedX;
        }

        int floorPosition = getWindow().getDecorView().getHeight() - FLOOR_HEIGHT_OFFSET;

        if (newY >= floorPosition - ballImageView.getHeight()) {
            stopBallMovement();
            ballImageView.setVisibility(View.INVISIBLE);
            showGameOverMessage();
            resetGame();
            return;
        }

        ballSpeedY += GRAVITY;

        ballImageView.setX(newX);
        ballImageView.setY(newY);

        ballImageView.setRotation(ballImageView.getRotation() + 3);
    }

    private void showGameOverMessage() {
        gameOverToast = Toast.makeText(this, "Game Over. Score: " + score, Toast.LENGTH_LONG);
        gameOverToast.show();
    }

    private void resetGame() {
        score = 0;
        scoreTextView.setText("0");

        int screenWidth = getWindow().getDecorView().getWidth();
        int screenHeight = getWindow().getDecorView().getHeight();
        int ballWidth = ballImageView.getWidth();
        int ballHeight = ballImageView.getHeight();
        float centerX = (screenWidth - ballWidth) / 2f;
        float centerY = (screenHeight - ballHeight) / 2f;

        ballImageView.setX(centerX);
        ballImageView.setY(centerY);

        ballSpeedX = 0;
        ballSpeedY = 0;
        ballMoving = false;
        ballImageView.setVisibility(View.VISIBLE);
    }
}



