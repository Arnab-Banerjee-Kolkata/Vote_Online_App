package com.example.voteonlinebruh.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.apiCalls.ServerCall;
import com.example.voteonlinebruh.utility.ThemeManager;

public class MainActivity extends AppCompatActivity {

    private static boolean scheduledRestart = false;
    public static ThemeManager TM = new ThemeManager();
    private Toolbar toolbar;
    private ImageButton button;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ConstraintLayout pri, man, pub, res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final SharedPreferences pref = getSharedPreferences("Voter.Online.Theme", MODE_PRIVATE);
        final SharedPreferences.Editor edit = pref.edit();
        if (!pref.contains("themeLight")) {
            edit.putBoolean("themeLight", true);
            TM.change(1);
            edit.apply();
        } else {
            if (pref.getBoolean("themeLight", true))
                TM.change(1);
            else
                TM.change(0);
        }
        setTheme(TM.getThemeId());


        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        button = findViewById(R.id.themeToggle);
        if (TM.getThemeId() == R.style.AppTheme_Light) {
            button.setImageDrawable(getDrawable(R.drawable.ic_moon_black_24dp));
            toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
            collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.lightBg));
        } else {
            button.setImageDrawable(getDrawable(R.drawable.ic_wb_sunny_black_24dp));
            toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
            collapsingToolbarLayout.setContentScrimColor(getResources().getColor(android.R.color.black));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TM.getThemeId() == R.style.AppTheme_Light) {
                    TM.change(0);
                    edit.putBoolean("themeLight", false);
                    scheduledRestart = true;
                } else {
                    TM.change(1);
                    edit.putBoolean("themeLight", true);
                    scheduledRestart = true;
                }
                edit.apply();
                onResume();
            }
        });

        pub = findViewById(R.id.public_button);
        pri = findViewById(R.id.private_button);
        man = findViewById(R.id.manage);
        res = findViewById(R.id.result);
        pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pub.setEnabled(false);
                ServerCall serverCall = new ServerCall();
                serverCall.getPublicElectionList(getApplicationContext());
                Intent intent = new Intent(getBaseContext(), WaitScreen.class);
                intent.putExtra("LABEL", "Refreshing");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        /*pri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pri.setEnabled(false);
                Intent intent=new Intent(getApplicationContext(),PrivateElection.class);
                startActivity(intent);
            }
        });

        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                man.setEnabled(false);
                Intent intent=new Intent(getApplicationContext(),ManageElection.class);
                startActivity(intent);
            }
        });*/

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res.setEnabled(false);
                ServerCall serverCall = new ServerCall();
                serverCall.getPublicResultList(getApplicationContext());
                Intent intent = new Intent(getBaseContext(), WaitScreen.class);
                intent.putExtra("LABEL", "Refreshing");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        /*UI CODE END*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (scheduledRestart) {
            scheduledRestart = false;
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        pub.setEnabled(true);
        pri.setEnabled(true);
        man.setEnabled(true);
        res.setEnabled(true);
    }
}