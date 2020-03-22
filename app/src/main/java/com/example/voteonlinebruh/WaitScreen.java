package com.example.voteonlinebruh;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class WaitScreen extends AppCompatActivity {
    private Handler dotHandler=new Handler();
    private boolean threadStop;
    protected static boolean terminate=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MainActivity.TM.getThemeId());
        setContentView(R.layout.activity_wait_screen);
        Intent intent=getIntent();
        String text=intent.getStringExtra("LABEL");
        TextView label=findViewById(R.id.label);
        label.setText(text);

        final ImageView left,right;
        left=findViewById(R.id.bg_left);
        right=findViewById(R.id.bg_right);

        int resid=R.drawable.wait_bg_left;
        Glide
                .with(this)
                .load(resid).into(left);
        resid=R.drawable.wait_bg_right;
        Glide
                .with(this)
                .load(resid).into(right);

        final ImageView imageView=findViewById(R.id.votehand);
        final ImageView imageView1=findViewById(R.id.votemike);

        resid=R.drawable.votehand;
        Glide
                .with(this)
                .load(resid).into(imageView);
        resid=R.drawable.votemike;
        Glide
                .with(this)
                .load(resid).into(imageView1);

        final Animation translate_up = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        final Animation translate_down = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        final Animation hands=AnimationUtils.loadAnimation(this,R.anim.entry_hands);
        final Animation handsback=AnimationUtils.loadAnimation(this,R.anim.entry_hands_retract);
        final Animation mike=AnimationUtils.loadAnimation(this,R.anim.entry_mike);
        final Animation mikeback=AnimationUtils.loadAnimation(this,R.anim.entry_mike_retract);
        final TextView dot1,dot2,dot3;
        dot1=findViewById(R.id.dot1);
        dot2=findViewById(R.id.dot2);
        dot3=findViewById(R.id.dot3);

        hands.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(handsback);
                imageView.setVisibility(View.VISIBLE);
                mike.setAnimationListener(new Animation.AnimationListener() {
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

        class myRunnable implements Runnable{
            @Override
            public void run() {
                try {
                    for(int i=0;i>=0&&!threadStop;i++) {
                        Log.d("Wait Thread","i="+i);
                        if(terminate==true)
                            terminate();
                        switch (i % 4) {
                            case 0:
                                dotHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dot1.setVisibility(View.INVISIBLE);
                                        dot2.setVisibility(View.INVISIBLE);
                                        dot3.setVisibility(View.INVISIBLE);
                                        left.startAnimation(translate_up);
                                        right.startAnimation(translate_down);
                                    }
                                });
                                break;
                            case 1:
                                dotHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dot1.setVisibility(View.VISIBLE);
                                        right.startAnimation(translate_up);
                                        left.startAnimation(translate_down);
                                    }
                                });
                                break;
                            case 2:
                                dotHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dot2.setVisibility(View.VISIBLE);
                                        left.startAnimation(translate_up);
                                        right.startAnimation(translate_down);

                                    }
                                });
                                break;
                            case 3:
                                dotHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dot3.setVisibility(View.VISIBLE);
                                        right.startAnimation(translate_up);
                                        left.startAnimation(translate_down);
                                    }
                                });
                                break;
                        }
                        Thread.sleep(1000);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        myRunnable thread=new myRunnable();
        threadStop=false;
        new Thread(thread).start();
    }

    @Override
    public void onBackPressed() {
        //LOCKING BACK BUTTON
    }

    @Override
    public void finish() {
        threadStop=true;
        super.finish();

    }
    protected void terminate()
    {
        threadStop=true;
        super.finish();

    }
}
