package com.example.playground;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class LoadImageTask extends AsyncTask<Integer, Void, Bitmap[]> {
    private ImageView[] pieces;
    private Context context;
    private ImageButton button;
    private Rompecabezas activity;

    public LoadImageTask(Context context, ImageView[] pieces, ImageButton button, Rompecabezas activity) {
        this.context = context;
        this.pieces = pieces;
        this.button = button;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Desactivar el botón antes de comenzar la tarea
        button.setEnabled(false);
    }

    @Override
    protected Bitmap[] doInBackground(Integer... params) {
        int imageResource = params[0];

        // Obtener la imagen original
        Drawable drawable = context.getResources().getDrawable(imageResource);
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

        return dividedBitmaps;
    }

    @Override
    protected void onPostExecute(Bitmap[] bitmaps) {
        // Asignar cada parte a una pieza del rompecabezas
        for (int i = 0; i < pieces.length; i++) {
            pieces[i].setImageBitmap(bitmaps[i]);
            pieces[i].setTag(i);  // Establecer el índice como tag para rastrear las piezas
        }

        // Habilitar el botón nuevamente
        button.setEnabled(true);

        // Generar y revolver el rompecabezas con la nueva imagen
        activity.generateAndShufflePuzzleFromImage((Integer) button.getTag());
    }
}



