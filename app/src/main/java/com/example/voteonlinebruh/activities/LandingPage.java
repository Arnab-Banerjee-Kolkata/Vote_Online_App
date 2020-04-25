package com.example.voteonlinebruh.activities;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.voteonlinebruh.R;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LandingPage extends AppCompatActivity {
    private Handler handler = new Handler();
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MainActivity.TM.getThemeId());
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = findViewById(R.id.timertool);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //ANIMATION CODES

        final ImageView clockmin, clockhour;
        clockmin = findViewById(R.id.clockmin);
        clockhour = findViewById(R.id.clockhour);
        final Animation rotatemin = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotatemin.setDuration(2000);
        final Animation rotatehour = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotatehour.setDuration(10000);
        class myRunnable implements Runnable {
            @Override
            public void run() {
                try {
                    for (int i = 0; i >= 0; i++) {
                        if (i % 2 == 0)
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    clockmin.startAnimation(rotatemin);
                                }
                            });
                        if (i % 10 == 0)
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    clockhour.startAnimation(rotatehour);
                                }
                            });
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        myRunnable thread = new myRunnable();
        new Thread(thread).start();

        String time = getIntent().getStringExtra("TIME");


        //TIMER CODE

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final TextView timer = findViewById(R.id.timer);
        final Date voteDate = (Date) date.clone();
        long duration;
        Date currDate = new Date();
        try {
            currDate = dateFormat.parse(dateFormat.format(currDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        duration = voteDate.getTime() - currDate.getTime();
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long sec, min, hour, day;
                sec = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                min = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                hour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                day = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                String timertext = day + ":";
                if (hour < 10)
                    timertext += "0" + hour + ":";
                else
                    timertext += hour + ":";
                if (min < 10)
                    timertext += "0" + min + ":";
                else
                    timertext += min + ":";
                if (sec < 10)
                    timertext += "0" + sec;
                else
                    timertext += sec;
                timer.setText(timertext);
            }

            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }

    @Override
    public void finish() {
        if (countDownTimer != null)
            countDownTimer.cancel();
        super.finish();
    }
}
