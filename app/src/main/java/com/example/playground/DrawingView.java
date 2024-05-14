package com.example.playground;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;


public class DrawingView extends View {

    private List<Stroke> strokes = new ArrayList<>();
    private Paint paint;
    private Path path;
    private int currentColor = Color.BLACK;
    private int strokeWidth = 10; // Grosor de trazo predeterminado

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(currentColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(strokeWidth);

        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Dibujar trazos anteriores
        for (Stroke stroke : strokes) {
            paint.setColor(stroke.getColor());
            paint.setStrokeWidth(stroke.getStrokeWidth());
            canvas.drawPath(stroke.getPath(), paint);
        }

        // Dibujar trazo actual
        paint.setColor(currentColor);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                strokes.add(new Stroke(new Path(path), currentColor, strokeWidth));
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void setColor(int color) {
        currentColor = color;
    }

    public void setThickness(int thickness) {
        strokeWidth = thickness;
    }

    public void clear() {
        strokes.clear();
        invalidate();
    }

    private static class Stroke {
        private Path path;
        private int color;
        private int strokeWidth;

        Stroke(Path path, int color, int strokeWidth) {
            this.path = path;
            this.color = color;
            this.strokeWidth = strokeWidth;
        }

        Path getPath() {
            return path;
        }

        int getColor() {
            return color;
        }

        int getStrokeWidth() {
            return strokeWidth;
        }
    }
}
