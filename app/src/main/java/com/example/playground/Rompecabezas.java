package com.example.playground;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.example.playground.R;

import java.util.Arrays;
import java.util.Random;

public class Rompecabezas extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {

    private ImageView[] pieces;
    private int[] originalOrder; // Se establecerá dinámicamente
    private int[] currentOrder; // Se establecerá dinámicamente

    // Variable de instancia para el Toast
    private Toast customToast;
    private int currentImageIndex = 0;
    private MediaPlayer mediaPlayer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rompecabezas);
        getSupportActionBar().hide();

        pieces = new ImageView[4];
        pieces[0] = findViewById(R.id.piece1);
        pieces[1] = findViewById(R.id.piece2);
        pieces[2] = findViewById(R.id.piece3);
        pieces[3] = findViewById(R.id.piece4);

        Button button17 = findViewById(R.id.button17);
        ImageButton changeImageButton = findViewById(R.id.changeImageButton);

        // Inicializar el MediaPlayer
        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.puzzle);
        mediaPlayer = MediaPlayer.create(this, soundUri);

        // Configurar el listener de arrastre para cada ImageView
        for (ImageView piece : pieces) {
            piece.setOnTouchListener(this);
            piece.setOnDragListener(this);
        }

        findViewById(R.id.shuffleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shufflePieces();
            }
        });

        button17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ocultar el Toast si está mostrándose
                if (customToast != null) {
                    customToast.cancel();
                    customToast = null;
                }

                // Crear un Intent para iniciar la actividad MainActivity
                Intent intent = new Intent(Rompecabezas.this, MainActivity.class);
                startActivity(intent);
            }
        });

        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextImageResource = getNextAnimalImage();
                changeImageButton.setTag(nextImageResource); // Aquí estableces el tag
                new LoadImageTask(getApplicationContext(), pieces, changeImageButton, Rompecabezas.this).execute(nextImageResource);
            }
        });

        // Generar el rompecabezas con una imagen aleatoria
        int randomImageResource = getRandomAnimalImage();
        generateAndShufflePuzzleFromImage(randomImageResource);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Iniciar el arrastre
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(data, shadowBuilder, v, 0);
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (event.getClipDescription() != null && event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    return true;
                } else {
                    return false;
                }
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                return true;
            case DragEvent.ACTION_DROP:
                // Obtener la vista de la pieza que se está arrastrando
                View draggedView = (View) event.getLocalState();
                // Obtener el índice de las piezas arrastradas y la vista objetivo
                int draggedIndex = getPieceIndex(draggedView.getId());
                int targetIndex = getPieceIndex(v.getId());

                if (draggedIndex != -1 && targetIndex != -1) {
                    // Intercambiar las imágenes de las piezas
                    Drawable draggedDrawable = pieces[draggedIndex].getDrawable();
                    Drawable targetDrawable = pieces[targetIndex].getDrawable();
                    pieces[draggedIndex].setImageDrawable(targetDrawable);
                    pieces[draggedIndex].setTag(targetIndex);
                    pieces[targetIndex].setImageDrawable(draggedDrawable);
                    pieces[targetIndex].setTag(draggedIndex);

                    // Actualizar el orden actual
                    int temp = currentOrder[draggedIndex];
                    currentOrder[draggedIndex] = currentOrder[targetIndex];
                    currentOrder[targetIndex] = temp;

                    // Verificar si el rompecabezas está resuelto
                    if (isPuzzleSolved()) {
                        // Inflar el diseño personalizado del Toast
                        View toastView = getLayoutInflater().inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));

                        // Configurar el texto del Toast
                        TextView toastText = toastView.findViewById(R.id.toast_text);
                        toastText.setText("¡Puzzle resuelto!");

                        // Mostrar el Toast personalizado
                        customToast = new Toast(getApplicationContext());
                        customToast.setDuration(Toast.LENGTH_SHORT);
                        customToast.setView(toastView);
                        customToast.show();

                        mediaPlayer.start();
                    }
                }
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                return true;
            default:
                return false;
        }
    }

    void generateAndShufflePuzzleFromImage(int imageResource) {
        // Generar el rompecabezas con la imagen
        generatePuzzleFromImage(imageResource);

        // Revolver las piezas
        shufflePieces();
    }

    private int getRandomAnimalImage() {
        // Lista de recursos de imágenes de animales disponibles
        int[] animalImages = {
                R.drawable.capibara,
                R.drawable.foca,
                R.drawable.rana,
                R.drawable.coco,
                // Agrega aquí más recursos de imágenes de animales según sea necesario
        };

        // Obtener un índice aleatorio dentro del rango de la lista de imágenes
        Random random = new Random();
        int randomIndex = random.nextInt(animalImages.length);

        // Obtener la imagen correspondiente al índice aleatorio
        int randomImageResource = animalImages[randomIndex];

        return randomImageResource;
    }
    void generatePuzzleFromImage(int imageResource) {
        // Obtener la imagen original
        Drawable drawable = getResources().getDrawable(imageResource);
        Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();

        // Obtener las dimensiones de la imagen original
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        // Dividir la imagen en cuatro partes
        Bitmap[] dividedBitmaps = new Bitmap[4];
        dividedBitmaps[0] = Bitmap.createBitmap(originalBitmap, 0, 0, width / 2, height / 2);
        dividedBitmaps[1] = Bitmap.createBitmap(originalBitmap, width / 2, 0, width / 2, height / 2);
        dividedBitmaps[2] = Bitmap.createBitmap(originalBitmap, 0, height / 2, width / 2, height / 2);
        dividedBitmaps[3] = Bitmap.createBitmap(originalBitmap, width / 2, height / 2, width / 2, height / 2);

        // Liberar recursos no utilizados
        originalBitmap.recycle();

        // Asignar cada parte a una pieza del rompecabezas
        for (int i = 0; i < pieces.length; i++) {
            pieces[i].setImageBitmap(dividedBitmaps[i]);
            pieces[i].setTag(i);  // Establecer el índice como tag para rastrear las piezas
        }

        // Guardar el orden original y el orden actual
        originalOrder = new int[]{0, 1, 2, 3};
        currentOrder = originalOrder.clone();
    }

    private void updatePieces() {
        for (int i = 0; i < pieces.length; i++) {
            int pieceIndex = currentOrder[i];
            Drawable drawable = pieces[pieceIndex].getDrawable();
            pieces[i].setImageDrawable(drawable);
            pieces[i].setTag(pieceIndex);
        }
    }

    private void shufflePieces() {
        Random random = new Random();

        do {
            for (int i = currentOrder.length - 1; i > 0; i--) {
                int index = random.nextInt(i + 1);
                int temp = currentOrder[index];
                currentOrder[index] = currentOrder[i];
                currentOrder[i] = temp;

                // Intercambiar las imágenes asociadas a las piezas
                Drawable tempDrawable = pieces[index].getDrawable();
                pieces[index].setImageDrawable(pieces[i].getDrawable());
                pieces[i].setImageDrawable(tempDrawable);
            }
        } while (isPuzzleSolved());  // Volver a mezclar si el rompecabezas está resuelto

        // Actualizar el orden actual
        for (int i = 0; i < pieces.length; i++) {
            pieces[i].setTag(currentOrder[i]);
        }
    }

    private boolean isPuzzleSolved() {
        for (int i = 0; i < currentOrder.length; i++) {
            if (currentOrder[i] != originalOrder[i]) {
                return false;
            }
        }
        return true;
    }

    private int getNextAnimalImage() {
        // Lista de recursos de imágenes de animales disponibles
        int[] animalImages = {
                R.drawable.capibara,
                R.drawable.foca,
                R.drawable.rana,
                R.drawable.coco,
                // Agrega aquí más recursos de imágenes de animales según sea necesario
        };

        // Verificar si el contador ha alcanzado el final de la lista
        if (currentImageIndex >= animalImages.length) {
            currentImageIndex = 0; // Reiniciar el contador si llega al final de la lista
        }

        // Obtener la siguiente imagen en la lista
        int nextImageResource = animalImages[currentImageIndex];

        // Incrementar el contador para la próxima llamada
        currentImageIndex++;

        return nextImageResource;
    }

    private int getPieceIndex(int pieceId) {
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].getId() == pieceId) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar la memoria de las imágenes en las piezas del rompecabezas
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].getDrawable() != null) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) pieces[i].getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                bitmap.recycle();
            }
        }
        // Cancelar y eliminar la instancia del Toast
        if (customToast != null) {
            customToast.cancel();
            customToast = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}











