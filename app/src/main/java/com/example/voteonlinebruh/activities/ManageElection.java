package com.example.voteonlinebruh.activities;

import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.utility.ThemeManager;

public class ManageElection extends AppCompatActivity {

    private Handler handler = new Handler();
    private Toolbar toolbar;
    private ConstraintLayout login, register;
    private Animation fadein, fadeout, slideup, slidedown;
    private TextView taptoreg, taptolog;
    private Button loginmanage, registermanage;
    private EditText userid, password, name, phone, email, pass1, pass2;
    private int themeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeId = ThemeManager.getThemeId();
        setTheme(themeId);
        setContentView(R.layout.activity_manage_election);
        toolbar = findViewById(R.id.toolbarman);
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

        //ON TAP ANIMATIONS

        login = findViewById(R.id.loginpart);
        register = findViewById(R.id.registerpart);
        fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        slideup = AnimationUtils.loadAnimation(this, R.anim.slideup);
        slidedown = AnimationUtils.loadAnimation(this, R.anim.slidedown);
        fadein.setDuration(750);
        fadeout.setDuration(500);

        taptoreg = findViewById(R.id.textView10);
        taptolog = findViewById(R.id.textView11);

        taptoreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.startAnimation(fadeout);
                login.setVisibility(View.GONE);
                register.startAnimation(slideup);
                register.setVisibility(View.VISIBLE);
            }
        });

        taptolog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 2; i++) {
                            if (i == 0)
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        register.startAnimation(slidedown);
                                        register.setVisibility(View.GONE);
                                    }
                                });
                            else
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        login.startAnimation(fadein);
                                        login.setVisibility(View.VISIBLE);
                                    }
                                });
                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });

        //ANIMATION CODE ENDS


        loginmanage = findViewById(R.id.loginmanage);
        registermanage = findViewById(R.id.registermanage);

        //LOGIN TO ACCOUNT

        userid = findViewById(R.id.userid);
        password = findViewById(R.id.pass);

        loginmanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (userid.getText().toString().isEmpty())
                        throw new Exception("Please enter your email/phone no. !");
                    if (password.getText().toString().isEmpty())
                        throw new Exception("Please enter your password !");
                    if (invalide(userid.getText().toString()) && invalidp(userid.getText().toString()))
                        throw new Exception("Please enter valid email/phone no. !");
                    if (password.getText().toString().length() < 8)
                        throw new Exception("Password must be at least 8 characters !");
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //REGISTER FOR ACCOUNT

        name = findViewById(R.id.user_name);
        phone = findViewById(R.id.user_phone);
        email = findViewById(R.id.user_email);
        pass1 = findViewById(R.id.password1);
        pass2 = findViewById(R.id.password2);

        registermanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (name.getText().toString().isEmpty())
                        throw new Exception("Please enter your name !");
                    if (email.getText().toString().isEmpty())
                        throw new Exception("Please enter your email !");
                    if (phone.getText().toString().isEmpty())
                        throw new Exception("Please enter your phone no. !");
                    if (pass1.getText().toString().isEmpty())
                        throw new Exception("Please enter a password !");
                    if (pass2.getText().toString().isEmpty())
                        throw new Exception("Please retype the password !");
                    if (invalide(email.getText().toString()))
                        throw new Exception("Please enter valid email address !");
                    if (invalidp(phone.getText().toString()))
                        throw new Exception("Please enter valid phone no. !");
                    if (pass1.getText().length() < 8)
                        throw new Exception("Password must be at least 8 character !");
                    if (!pass1.getText().toString().equals(pass2.getText().toString()))
                        throw new Exception("Both passwords do not match !");
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean invalide(String x) {
        boolean ret = true;
        int c = 0;

        if (x.endsWith(".com")) {
            for (int i = 1; i < x.length() - 4; i++) {
                if (x.charAt(i) == '@')
                    if (i != x.length() - 5)
                        c++;
                    else {
                        c = 2;
                        break;
                    }
            }
            if (c == 1)
                ret = false;
        }
        return ret;
    }

    boolean invalidp(String x) {
        boolean ret = true;
        if (x.length() == 10) {
            try {
                Long.parseLong(x);
                ret = false;
            } catch (Exception e) {
                ret = true;
            }
        }
        return ret;
    }
}
