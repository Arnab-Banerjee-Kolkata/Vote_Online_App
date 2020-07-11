package com.example.voteonlinebruh.activities;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.api.PublicAPICall;
import com.example.voteonlinebruh.utility.ThemeManager;

public class OtpPage extends AppCompatActivity {
  private Context mContext;
  private Button login;
  private EditText[] ets;
  private EditText boothid;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_otp_page);
    TextView disclaimer = findViewById(R.id.textView4);
    Toolbar toolbar = findViewById(R.id.toolbarotp);
    if (themeId == R.style.AppTheme_Light) {
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
      disclaimer.setBackgroundResource(R.drawable.spinnerbglight);
    } else {
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
      disclaimer.setBackgroundResource(R.drawable.spinnerbgdark);
    }
    toolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onBackPressed();
          }
        });
    mContext = getApplicationContext();
    ImageView imageView = findViewById(R.id.otpbg);
    int resid = R.drawable.log_bot;
    Glide.with(this).load(resid).into(imageView);
    // ET CODE

    ets = new EditText[6];
    ets[0] = findViewById(R.id.otp1);
    ets[0].setTransformationMethod(null);
    ets[1] = findViewById(R.id.otp2);
    ets[1].setTransformationMethod(null);
    ets[2] = findViewById(R.id.otp3);
    ets[2].setTransformationMethod(null);
    ets[3] = findViewById(R.id.otp4);
    ets[3].setTransformationMethod(null);
    ets[4] = findViewById(R.id.otp5);
    ets[4].setTransformationMethod(null);
    ets[5] = findViewById(R.id.otp6);
    ets[5].setTransformationMethod(null);

    boothid = findViewById(R.id.boothid);

    class textFocus implements TextWatcher {
      private final View view;

      private textFocus(View view) {
        this.view = view;
      }

      @Override
      public void afterTextChanged(Editable editable) {
        // TODO Auto-generated method stub
        String text = editable.toString();

        switch (view.getId()) {
          case R.id.otp1:
            if (text.length() == 1) ets[1].requestFocus();
            break;
          case R.id.otp2:
            if (text.length() == 1) ets[2].requestFocus();
            break;
          case R.id.otp3:
            if (text.length() == 1) ets[3].requestFocus();
            break;
          case R.id.otp4:
            if (text.length() == 1) ets[4].requestFocus();
            break;
          case R.id.otp5:
            if (text.length() == 1) ets[5].requestFocus();
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
      private final int index;

      listener(int index) {
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
    ets[4].addTextChangedListener(new textFocus(ets[4]));
    ets[5].addTextChangedListener(new textFocus(ets[5]));

    ets[0].setOnKeyListener(new listener(0));
    ets[1].setOnKeyListener(new listener(1));
    ets[2].setOnKeyListener(new listener(2));
    ets[3].setOnKeyListener(new listener(3));
    ets[4].setOnKeyListener(new listener(4));
    ets[5].setOnKeyListener(new listener(5));

    login = findViewById(R.id.verifybut);
    login.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            try {
              StringBuilder otp;
              WaitScreen.terminate = false;
              otp = new StringBuilder();
              for (EditText x : ets) otp.append(x.getText().toString());
              if (boothid.getText().toString().isEmpty()) {
                throw new Exception("Please enter the booth ID !");
              }
              if (otp.length() != 6) {
                throw new Exception("Please enter OTP !");
              }
              String boothId = boothid.getText().toString();
              if (!validInput(boothId)) {
                throw new Exception("Invalid Booth ID !");
              }
              if (!validInput(otp.toString())) {
                throw new Exception("Invalid OTP !");
              }
              login.setEnabled(false);
              String OTP = otp.toString();
              PublicAPICall publicAPICall = new PublicAPICall();
              publicAPICall.validateBoothOtp(boothId, OTP, mContext, OtpPage.this);

              Intent intent = new Intent(getApplicationContext(), WaitScreen.class);
              intent.putExtra("LABEL", "Authenticating");
              startActivity(intent);
              overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
              OtpPage.this.finish();

            } catch (Exception e) {
              Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean validInput(String s) {
    char c;
    boolean retval = true;
    for (int i = 0; i < s.length(); i++) {
      c = s.charAt(i);
      if (!Character.isDigit(c) && !Character.isLetter(c)) {
        retval = false;
        break;
      }
    }
    return retval;
  }

  @Override
  protected void onResume() {
    login.setEnabled(true);
    super.onResume();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.prev_act_from_left_to_right, R.anim.curr_act_go_to_right);
  }
}
