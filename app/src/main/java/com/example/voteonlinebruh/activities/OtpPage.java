package com.example.voteonlinebruh.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.apiCalls.ServerCall;

public class OtpPage extends AppCompatActivity {
    Context mContext;
    Button login;

    int themeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MainActivity.TM.getThemeId());
        if (MainActivity.TM.getThemeId() == R.style.AppTheme_Light)
            themeId = R.style.ConfirmTheme_Light;
        else
            themeId = R.style.ConfirmTheme_Dark;
        setContentView(R.layout.activity_otp_page);

        mContext = getApplicationContext();
        ImageView imageView = findViewById(R.id.otpbg);
        int resid = R.drawable.log_bot;
        Glide
                .with(this)
                .load(resid).into(imageView);
        //ET CODE

        final EditText[] ets = new EditText[4];
        ets[0] = findViewById(R.id.otp1);
        ets[0].setTransformationMethod(null);
        ets[1] = findViewById(R.id.otp2);
        ets[1].setTransformationMethod(null);
        ets[2] = findViewById(R.id.otp3);
        ets[2].setTransformationMethod(null);
        ets[3] = findViewById(R.id.otp4);
        ets[3].setTransformationMethod(null);

        class textFocus implements TextWatcher {
            private View view;

            private textFocus(View view) {
                this.view = view;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub
                String text = editable.toString();

                switch (view.getId()) {

                    case R.id.otp1:
                        if (text.length() == 1)
                            ets[1].requestFocus();
                        break;
                    case R.id.otp2:
                        if (text.length() == 1)
                            ets[2].requestFocus();
                        break;
                    case R.id.otp3:
                        if (text.length() == 1)
                            ets[3].requestFocus();
                        break;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        }
        class listener implements View.OnKeyListener {
            private int index;

            public listener(int index) {
                this.index = index;
            }

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (ets[index].getText().toString().isEmpty() && index != 0) {
                        ets[index - 1].requestFocus();
                        ets[index - 1].setText("");
                    }
                }
                return false;
            }
        }

        ets[0].addTextChangedListener(new textFocus(ets[0]));
        ets[1].addTextChangedListener(new textFocus(ets[1]));
        ets[2].addTextChangedListener(new textFocus(ets[2]));
        ets[3].addTextChangedListener(new textFocus(ets[3]));

        ets[0].setOnKeyListener(new listener(0));
        ets[1].setOnKeyListener(new listener(1));
        ets[2].setOnKeyListener(new listener(2));
        ets[3].setOnKeyListener(new listener(3));

        //VALIDATION CODE

        login = findViewById(R.id.verifybut);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String otp;
                    WaitScreen.terminate = false;
                    otp = "";
                    for (EditText x : ets)
                        otp += x.getText().toString();
                    if (otp.length() != 4) {
                        otp = "";
                        throw new Exception("Please enter OTP !");
                    }

                    login.setEnabled(false);
                    SharedPreferences sharedPreferences = getSharedPreferences("VoterDetails", Context.MODE_PRIVATE);

                    String OTP = otp;
                    String boothId = sharedPreferences.getString("boothId", "0");
                    String aadhaarNo = sharedPreferences.getString("aadhaarNo", "");

                    ServerCall serverCall = new ServerCall();
                    serverCall.validateOtp(aadhaarNo, boothId, OTP, mContext, OtpPage.this);

                    Intent intent = new Intent(getApplicationContext(), WaitScreen.class);
                    intent.putExtra("LABEL", "Authenticating");
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, themeId).setTitle("Confirm Log Out")
                .setMessage("\nYou cannot go back ! You will be logged out.\nAre you sure you want to log out ?\n")
                .setPositiveButton("Yes, Logout !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("No !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onResume() {
        login.setEnabled(true);
        super.onResume();
    }

    void logout() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }
}