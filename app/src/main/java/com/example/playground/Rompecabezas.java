package com.example.playground;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Random;

public class Rompecabezas extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {

    private ImageView[] pieces;
    private int[] originalOrder = {R.drawable.piece1, R.drawable.piece2, R.drawable.piece3, R.drawable.piece4};
    private int[] currentOrder = {R.drawable.piece3, R.drawable.piece2, R.drawable.piece4, R.drawable.piece1};
    private ImageView draggedPiece;
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

        updatePieces();

        // Configurar el listener de arrastre para cada ImageView
        for (ImageView piece : pieces) {
            piece.setOnTouchListener(new PieceTouchListener());
            piece.setOnDragListener(this);
        }

        findViewById(R.id.shuffleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shufflePieces();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    private class PieceTouchListener implements View.OnTouchListener {
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
                // Obtener la pieza que se soltó y la que estaba siendo arrastrada
                ImageView droppedPiece = (ImageView) v;
                ImageView draggedPieceView = (ImageView) event.getLocalState();

                // Intercambiar las imágenes
                Drawable tempDrawable = droppedPiece.getDrawable();
                droppedPiece.setImageDrawable(draggedPieceView.getDrawable());
                draggedPieceView.setImageDrawable(tempDrawable);

                // Restablecer el color de fondo de la vista donde se soltó la pieza
                v.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                return true;
            default:
                return false;
        }
    }
    private boolean isPuzzleSolved() {
        return currentOrder[0] == originalOrder[0] &&  // Verificar la posición de la pieza 1
                currentOrder[1] == originalOrder[1] &&  // Verificar la posición de la pieza 2
                currentOrder[2] == originalOrder[2] &&  // Verificar la posición de la pieza 3
                currentOrder[3] == originalOrder[3];    // Verificar la posición de la pieza 4
    }
    private void updatePieces() {
        for (int i = 0; i < pieces.length; i++) {
            pieces[i].setImageResource(currentOrder[i]);
        }

        if (isPuzzleSolved()) {
            Toast.makeText(this, "¡Felicidades! Has completado el rompecabezas.", Toast.LENGTH_SHORT).show();
        }
    }



    private void shufflePieces() {
        Random random = new Random();

        for (int i = currentOrder.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = currentOrder[index];
            currentOrder[index] = currentOrder[i];
            currentOrder[i] = temp;
        }

        updatePieces();
    }

}




