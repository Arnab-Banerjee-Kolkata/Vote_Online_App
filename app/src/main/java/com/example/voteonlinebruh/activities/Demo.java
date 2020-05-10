package com.example.voteonlinebruh.activities;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.voteonlinebruh.R;

public class Demo extends AppCompatActivity {

    private Handler handler = new Handler();
    private boolean threadstop = false;
    private ImageView imageView, shadowcut, shadowbot;
    private Animation fadein, fadeout, scale_slow, scale_fast;
    private View shadowfull, party, cand, sym, indi, tap1, tap2, tap3, tap4;
    private TextView text;
    private ImageButton cl;
    private int resid1, resid2, resid3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        setFinishOnTouchOutside(false);
        imageView = findViewById(R.id.screen);
        resid1 = R.drawable.vote_demo_1;
        resid2 = R.drawable.vote_demo_2;
        resid3 = R.drawable.vote_demo_3;
        final Context context = getApplicationContext();

        class optimize implements Runnable {

            @Override
            public void run() {

                fadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                fadein.setDuration(1000);
                fadeout = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                fadeout.setDuration(1000);
                scale_slow = AnimationUtils.loadAnimation(context, R.anim.scaleslow);
                scale_fast = AnimationUtils.loadAnimation(context, R.anim.scalefast);
                text = findViewById(R.id.text);
                shadowfull = findViewById(R.id.shadowfull);
                party = findViewById(R.id.party_high);
                cand = findViewById(R.id.cand_high);
                sym = findViewById(R.id.sym_high);
                indi = findViewById(R.id.ind_high);
                shadowcut = findViewById(R.id.shadow_cut);
                tap1 = findViewById(R.id.tap1);
                tap2 = findViewById(R.id.tap2);
                tap3 = findViewById(R.id.tap3);
                tap4 = findViewById(R.id.tap4);
                shadowbot = findViewById(R.id.shadow_bot);
            }
        }
        optimize o = new optimize();
        new Thread(o).start();

        class myRunnable implements Runnable {
            @Override
            public void run() {
                try {
                    for (int i = 0; i >= 0 && !threadstop; i++) {
                        Log.d("Animation Thread", "i=" + i);
                        switch (i % 20) {
                            case 0:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide
                                                .with(context)
                                                .load(resid1).into(imageView);
                                        text.setText("This is the screen you will be seeing in the voting area.");
                                        shadowfull.startAnimation(fadein);
                                        text.startAnimation(fadein);
                                        shadowfull.setVisibility(View.VISIBLE);
                                        text.setVisibility(View.VISIBLE);
                                    }
                                });
                                Thread.sleep(2000);
                                break;
                            case 1:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageView.startAnimation(fadein);
                                        imageView.setVisibility(View.VISIBLE);
                                        shadowfull.startAnimation(fadeout);
                                        text.startAnimation(fadeout);
                                        shadowfull.setVisibility(View.GONE);
                                        text.setVisibility(View.GONE);
                                    }
                                });
                                Thread.sleep(1500);
                                break;
                            case 2:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        text.setText("This line will have the name of the party.");
                                        shadowcut.startAnimation(fadein);
                                        party.startAnimation(fadein);
                                        text.startAnimation(fadein);
                                        shadowcut.setVisibility(View.VISIBLE);
                                        party.setVisibility(View.VISIBLE);
                                        text.setVisibility(View.VISIBLE);
                                    }
                                });
                                Thread.sleep(1500);
                                break;
                            case 3:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        party.startAnimation(fadeout);
                                        text.startAnimation(fadeout);
                                        party.setVisibility(View.GONE);
                                    }
                                });
                                break;
                            case 4:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        text.setText("This line will have the name of the candidate.");
                                        cand.startAnimation(fadein);
                                        text.startAnimation(fadein);
                                        cand.setVisibility(View.VISIBLE);
                                    }
                                });
                                Thread.sleep(1500);
                                break;
                            case 5:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        cand.startAnimation(fadeout);
                                        text.startAnimation(fadeout);
                                        cand.setVisibility(View.GONE);
                                    }
                                });
                                break;
                            case 6:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        text.setText("This area will have the symbol of the corresponding party.");
                                        text.startAnimation(fadein);
                                        sym.startAnimation(fadein);
                                        sym.setVisibility(View.VISIBLE);
                                    }
                                });
                                Thread.sleep(1500);
                                break;
                            case 7:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        text.startAnimation(fadeout);
                                        sym.startAnimation(fadeout);
                                        sym.setVisibility(View.GONE);
                                    }
                                });
                                break;
                            case 8:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        text.setText("This indicator will show you the candidate you are voting for.");
                                        text.startAnimation(fadein);
                                        indi.startAnimation(fadein);
                                        indi.setVisibility(View.VISIBLE);
                                    }
                                });
                                Thread.sleep(1500);
                                break;
                            case 9:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        indi.startAnimation(fadeout);
                                        text.startAnimation(fadeout);
                                        indi.setVisibility(View.GONE);
                                        text.setVisibility(View.GONE);
                                    }
                                });
                                break;
                            case 11:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        text.setText("Tap on your desired candidate/party to cast your vote.");
                                        text.setVisibility(View.VISIBLE);
                                        text.startAnimation(fadein);
                                        shadowcut.startAnimation(fadeout);
                                        shadowcut.setVisibility(View.GONE);
                                        shadowfull.startAnimation(fadein);
                                        shadowfull.setVisibility(View.VISIBLE);
                                    }
                                });
                                Thread.sleep(1500);
                                break;
                            case 12:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        shadowfull.startAnimation(fadeout);
                                        shadowfull.setVisibility(View.GONE);
                                        text.startAnimation(fadeout);
                                        text.setVisibility(View.GONE);
                                    }
                                });
                                Thread.sleep(500);
                                break;
                            case 13:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tap1.startAnimation(scale_slow);
                                    }
                                });
                                Thread.sleep(300);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tap2.startAnimation(scale_fast);
                                    }
                                });
                                Thread.sleep(350);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide
                                                .with(context)
                                                .load(resid2).placeholder(resid1).into(imageView);
                                    }
                                });
                                break;
                            case 14:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        shadowfull.startAnimation(fadein);
                                        shadowfull.setVisibility(View.VISIBLE);
                                        text.setText("You can change your choice if you wish to.");
                                        text.startAnimation(fadein);
                                        text.setVisibility(View.VISIBLE);
                                    }
                                });
                                Thread.sleep(1500);
                                break;
                            case 15:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        shadowfull.startAnimation(fadeout);
                                        shadowfull.setVisibility(View.GONE);
                                        text.startAnimation(fadeout);
                                        text.setVisibility(View.GONE);
                                    }
                                });
                                Thread.sleep(501);
                                break;
                            case 16:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tap3.startAnimation(scale_slow);
                                    }
                                });
                                Thread.sleep(300);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tap4.startAnimation(scale_fast);
                                    }
                                });
                                Thread.sleep(350);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide
                                                .with(context)
                                                .load(resid3).placeholder(resid2).into(imageView);
                                    }
                                });
                                break;
                            case 17:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        shadowbot.startAnimation(fadein);
                                        shadowbot.setVisibility(View.VISIBLE);
                                        text.setText("Tap on the 'Done' button to submit your vote.");
                                        text.startAnimation(fadein);
                                        text.setVisibility(View.VISIBLE);
                                    }
                                });
                                Thread.sleep(1500);
                                break;
                            case 18:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        shadowbot.startAnimation(fadeout);
                                        shadowbot.setVisibility(View.GONE);
                                        imageView.startAnimation(fadeout);
                                        imageView.setVisibility(View.GONE);
                                        text.startAnimation(fadeout);
                                        text.setVisibility(View.GONE);
                                    }
                                });
                        }
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        myRunnable thread = new myRunnable();
        new Thread(thread).start();

        cl = findViewById(R.id.closediag);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        threadstop = true;
        super.finish();
    }
}
