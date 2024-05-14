package com.example.playground;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Piano extends AppCompatActivity {

    MediaPlayer music,music2,music3,music4,music5,music6,music7;
    private SoundPool soundPool;
    private Integer integerSoundIDa,integerSoundIDb,integerSoundIDc,integerSoundIDd,integerSoundIDe,integerSoundIDf,integerSoundIDg,
            integerSoundBlack,integerSoundBlack2,integerSoundBlack3,integerSoundBlack4,integerSoundBlack5,integerSoundBlack6,integerSoundIDh;
    private float floatSpeed = 1.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piano_layout);
        getSupportActionBar().hide();

        SoundPool.Builder builder = new SoundPool.Builder();
        soundPool = builder.build();
        integerSoundIDa = soundPool.load(this,R.raw.a,1);
        integerSoundIDb = soundPool.load(this,R.raw.b,1);
        integerSoundIDc = soundPool.load(this,R.raw.c,1);
        integerSoundIDd = soundPool.load(this,R.raw.d,1);
        integerSoundIDe = soundPool.load(this,R.raw.e,1);
        integerSoundIDf = soundPool.load(this,R.raw.f,1);
        integerSoundIDg = soundPool.load(this,R.raw.g,1);
        integerSoundIDh = soundPool.load(this,R.raw.h,1);
        integerSoundBlack = soundPool.load(this,R.raw.c_hash,1);
        integerSoundBlack2 = soundPool.load(this,R.raw.d_hash,1);
        integerSoundBlack3 = soundPool.load(this,R.raw.g_hash,1);
        integerSoundBlack4 = soundPool.load(this,R.raw.a_hash,1);
        integerSoundBlack5 = soundPool.load(this,R.raw.b_hash,1);
        integerSoundBlack6 = soundPool.load(this,R.raw.c2,1);

        // Obtener referencias a los boton
        Button button14 = findViewById(R.id.button14);

        // Configurar OnClickListener para el button13
        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar la actividad MainActivity
                Intent intent = new Intent(Piano.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void la(View view){
        soundPool.play(integerSoundIDa,1,1,1,0,floatSpeed);
    }

    public void si(View view){
        soundPool.play(integerSoundIDb,1,1,1,0,floatSpeed);
    }

    public void ddo(View view){
        soundPool.play(integerSoundIDc,1,1,1,0,floatSpeed);
    }

    public void re(View view){
        soundPool.play(integerSoundIDd,1,1,1,0,floatSpeed);
    }

    public void mi(View view){
        soundPool.play(integerSoundIDe,1,1,1,0,floatSpeed);
    }

    public void fa(View view){
        soundPool.play(integerSoundIDf,1,1,1,0,floatSpeed);
    }
    public void dddo(View view){
        soundPool.play(integerSoundIDh,1,1,1,0,floatSpeed);
    }
    public void sol(View view){
        soundPool.play(integerSoundIDg,1,1,1,0,floatSpeed);
    }

    public void black(View view){
        soundPool.play(integerSoundBlack,1,1,1,0,floatSpeed);
    }

    public void black2(View view){
        soundPool.play(integerSoundBlack2,1,1,1,0,floatSpeed);
    }

    public void black3(View view){
        soundPool.play(integerSoundBlack3,1,1,1,0,floatSpeed);
    }

    public void black4(View view){
        soundPool.play(integerSoundBlack4,1,1,1,0,floatSpeed);
    }

    public void black5(View view){
        soundPool.play(integerSoundBlack5,1,1,1,0,floatSpeed);
    }

    public void black6(View view){
        soundPool.play(integerSoundBlack6,1,1,1,0,floatSpeed);
    }

}
