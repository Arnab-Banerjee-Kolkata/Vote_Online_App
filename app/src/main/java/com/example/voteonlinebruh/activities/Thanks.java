package com.example.voteonlinebruh.activities;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Animatable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.voteonlinebruh.R;

public class Thanks extends AppCompatActivity {
    public static boolean threadStop = false;
    public static boolean success = false;
    private static boolean play = false;
    private ImageView checkView;
    private Handler handler = new Handler();
    private CardView cardView;
    private ProgressBar progressBar;
    private TextView textView;
    private Toolbar toolbar;
    private SoundPool soundPool;
    private Vibrator v;
    private AudioManager am;
    private int themeId = MainActivity.TM.getThemeId(), alertToneS, alertToneF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(themeId);
        setContentView(R.layout.activity_thanks);
        checkView = findViewById(R.id.checkView);
        cardView = findViewById(R.id.checkContainer);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textMessage);
        toolbar = findViewById(R.id.toolbarEnd);
        if (themeId == R.style.AppTheme_Light)
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        else
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final Animation scale = AnimationUtils.loadAnimation(this, R.anim.scaleslow),
                fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in),
                fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadein.setDuration(600);
        fadeout.setDuration(600);
        play = false;
        soundPool = new SoundPool.Builder()
                .build();
        alertToneS = soundPool.load(this, R.raw.tone_success, 1);
        alertToneF = soundPool.load(this, R.raw.tone_failed, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                play = true;
            }
        });
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        class myRunnable implements Runnable {
            @Override
            public void run() {
                while (!threadStop || !play) ;
                try {
                    Thread.sleep(1000);
                    if (!success) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                toolbar.setTitle("Failed");
                                textView.startAnimation(fadeout);
                                cardView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_light)));
                                progressBar.setVisibility(View.GONE);
                                cardView.setVisibility(View.VISIBLE);
                                cardView.startAnimation(scale);
                                am.setStreamVolume(AudioManager.STREAM_MUSIC,
                                        am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                            }
                        });
                        Thread.sleep(400);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText("Sorry your vote was rejected! Please show it to the polling officer.");
                                textView.startAnimation(fadein);
                                checkView.setImageDrawable(getDrawable(R.drawable.animated_vector_cross));
                                ((Animatable) checkView.getDrawable()).start();
                                soundPool.play(alertToneF, 1, 1, 1, 0, 1);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                                } else {
                                    v.vibrate(1000);
                                }
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                toolbar.setTitle("Success");
                                textView.startAnimation(fadeout);
                                progressBar.setVisibility(View.GONE);
                                cardView.setVisibility(View.VISIBLE);
                                cardView.startAnimation(scale);
                                am.setStreamVolume(AudioManager.STREAM_MUSIC,
                                        am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                            }
                        });
                        Thread.sleep(400);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText("Your vote has been successfully registered. Thank you for being a responsible citizen!");
                                textView.startAnimation(fadein);
                                ((Animatable) checkView.getDrawable()).start();
                                soundPool.play(alertToneS, 1, 1, 1, 0, 1);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                } else {
                                    v.vibrate(500);
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        myRunnable thread = new myRunnable();
        new Thread(thread).start();
    }

    @Override
    public void onBackPressed() {
        if (threadStop)
            super.onBackPressed();
    }
}
