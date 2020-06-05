package com.example.voteonlinebruh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.utility.ThemeManager;

public class WaitScreen extends AppCompatActivity {
    private Handler dotHandler = new Handler();
    private ImageView left, right, imageView, imageView1;
    private TextView label;
    private Animation translate_up, translate_down, hands, handsback, mikeback, mike;
    private boolean threadStop;
    private int resid;
    public static boolean terminate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeManager.getThemeId());
        setContentView(R.layout.activity_wait_screen);
        Intent intent = getIntent();
        String text = intent.getStringExtra("LABEL");
        label = findViewById(R.id.label);
        label.setText(text);
        left = findViewById(R.id.bg_left);
        right = findViewById(R.id.bg_right);
        resid = R.drawable.wait_bg_left;
        Glide.with(this).load(resid).into(left);
        resid = R.drawable.wait_bg_right;
        Glide.with(this).load(resid).into(right);

        imageView = findViewById(R.id.votehand);
        imageView1 = findViewById(R.id.votemike);

        resid = R.drawable.votehand;
        Glide.with(this).load(resid).into(imageView);
        resid = R.drawable.votemike;
        Glide.with(this).load(resid).into(imageView1);

        translate_up = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        translate_down = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        hands = AnimationUtils.loadAnimation(this, R.anim.entry_hands);
        handsback = AnimationUtils.loadAnimation(this, R.anim.entry_hands_retract);
        mike = AnimationUtils.loadAnimation(this, R.anim.entry_mike);
        mikeback = AnimationUtils.loadAnimation(this, R.anim.entry_mike_retract);
        hands.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageView.startAnimation(handsback);
                        imageView.setVisibility(View.VISIBLE);
                        mike.setAnimationListener(
                                new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        imageView1.startAnimation(mikeback);
                                        imageView1.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                    }
                                });
                        imageView1.startAnimation(mike);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
        imageView.startAnimation(hands);

        class myRunnable implements Runnable {
            @Override
            public void run() {
                try {
                    for (int i = 0; i >= 0 && !threadStop; i++) {
                        Log.d("Wait Thread", "i=" + i);
                        if (terminate) finish();
                        switch (i % 4) {
                            case 0:
                                dotHandler.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                left.startAnimation(translate_up);
                                                right.startAnimation(translate_down);
                                            }
                                        });
                                break;
                            case 1:
                                dotHandler.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                right.startAnimation(translate_up);
                                                left.startAnimation(translate_down);
                                            }
                                        });
                                break;
                            case 2:
                                dotHandler.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                right.startAnimation(translate_down);
                                                left.startAnimation(translate_up);
                                            }
                                        });
                                break;
                            case 3:
                                dotHandler.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                left.startAnimation(translate_down);
                                                right.startAnimation(translate_up);
                                            }
                                        });

                                break;
                        }
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        myRunnable thread = new myRunnable();
        threadStop = false;
        new Thread(thread).start();
    }

    @Override
    public void onBackPressed() {
        // LOCKING BACK BUTTON
    }

    @Override
    public void finish() {
        Log.d("Wait Thread", "Terminated");
        terminate = false;
        threadStop = true;
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
