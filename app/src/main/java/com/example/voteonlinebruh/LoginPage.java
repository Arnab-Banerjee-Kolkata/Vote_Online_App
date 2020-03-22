package com.example.voteonlinebruh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class LoginPage extends AppCompatActivity{
    Button login;
    Context mContext;


    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MainActivity.TM.getThemeId());
        setContentView(R.layout.activity_login_page);

        mContext = this.getApplicationContext();

        //UI CODE STARTS HERE

        Toolbar toolbar = findViewById(R.id.toolbarlogin);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView imageView = findViewById(R.id.loginbg);
        int resid = R.drawable.log_bot;
        Glide
                .with(this)
                .load(resid).into(imageView);

        //ET CODE

        final EditText ets[] = new EditText[12];
        ets[0] = findViewById(R.id.et1);
        ets[0].setTransformationMethod(null);
        ets[1] = findViewById(R.id.et2);
        ets[1].setTransformationMethod(null);
        ets[2] = findViewById(R.id.et3);
        ets[2].setTransformationMethod(null);
        ets[3] = findViewById(R.id.et4);
        ets[3].setTransformationMethod(null);
        ets[4] = findViewById(R.id.et5);
        ets[4].setTransformationMethod(null);
        ets[5] = findViewById(R.id.et6);
        ets[5].setTransformationMethod(null);
        ets[6] = findViewById(R.id.et7);
        ets[6].setTransformationMethod(null);
        ets[7] = findViewById(R.id.et8);
        ets[7].setTransformationMethod(null);
        ets[8] = findViewById(R.id.et9);
        ets[8].setTransformationMethod(null);
        ets[9] = findViewById(R.id.et10);
        ets[9].setTransformationMethod(null);
        ets[10] = findViewById(R.id.et11);
        ets[10].setTransformationMethod(null);
        ets[11] = findViewById(R.id.et12);
        ets[11].setTransformationMethod(null);

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

                    case R.id.et1:
                        if (text.length() == 1)
                            ets[1].requestFocus();
                        break;
                    case R.id.et2:
                        if (text.length() == 1)
                            ets[2].requestFocus();
                        break;
                    case R.id.et3:
                        if (text.length() == 1)
                            ets[3].requestFocus();
                        break;
                    case R.id.et4:
                        if (text.length() == 1)
                            ets[4].requestFocus();
                        break;
                    case R.id.et5:
                        if (text.length() == 1)
                            ets[5].requestFocus();
                        break;
                    case R.id.et6:
                        if (text.length() == 1)
                            ets[6].requestFocus();
                        break;
                    case R.id.et7:
                        if (text.length() == 1)
                            ets[7].requestFocus();
                        break;
                    case R.id.et8:
                        if (text.length() == 1)
                            ets[8].requestFocus();
                        break;
                    case R.id.et9:
                        if (text.length() == 1)
                            ets[9].requestFocus();
                        break;
                    case R.id.et10:
                        if (text.length() == 1)
                            ets[10].requestFocus();
                        break;
                    case R.id.et11:
                        if (text.length() == 1)
                            ets[11].requestFocus();
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
                if ((keyCode == KeyEvent.KEYCODE_DEL || keyCode == KeyEvent.KEYCODE_BACK ) && event.getAction() == KeyEvent.ACTION_DOWN) {
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
        ets[6].addTextChangedListener(new textFocus(ets[6]));
        ets[7].addTextChangedListener(new textFocus(ets[7]));
        ets[8].addTextChangedListener(new textFocus(ets[8]));
        ets[9].addTextChangedListener(new textFocus(ets[9]));
        ets[10].addTextChangedListener(new textFocus(ets[10]));
        ets[11].addTextChangedListener(new textFocus(ets[11]));

        ets[0].setOnKeyListener(new listener(0));
        ets[1].setOnKeyListener(new listener(1));
        ets[2].setOnKeyListener(new listener(2));
        ets[3].setOnKeyListener(new listener(3));
        ets[4].setOnKeyListener(new listener(4));
        ets[5].setOnKeyListener(new listener(5));
        ets[6].setOnKeyListener(new listener(6));
        ets[7].setOnKeyListener(new listener(7));
        ets[8].setOnKeyListener(new listener(8));
        ets[9].setOnKeyListener(new listener(9));
        ets[10].setOnKeyListener(new listener(10));
        ets[11].setOnKeyListener(new listener(11));

        //UI CODE ENDS AND VALIDATION CODE STARTS HERE

        final EditText boothid = findViewById(R.id.boothid);

        login = findViewById(R.id.loginbut);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = "";
                try {
                    WaitScreen.terminate = false;
                    uid = "";
                    for (EditText x : ets)
                        uid += x.getText().toString();
                    if (uid.length() != 12) {
                        uid = "";
                        throw new Exception("Please fill out Aadhaar details !");
                    }
                    if (boothid.getText().toString().isEmpty()) {
                        throw new Exception("Please enter the booth ID !");
                    }

                    login.setEnabled(false);

                    //VALUES TO WORK WITH ->
                    String AADHAARID = uid;
                    String BOOTHID = boothid.getText().toString();


                    SharedPreferences p = getSharedPreferences("VoterDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = p.edit();
                    editor.putString("aadhaarNo", AADHAARID);
                    editor.putString("boothId", BOOTHID);
                    editor.apply();


                    ServerCall serverCall = new ServerCall();
                    serverCall.validateDetails(AADHAARID, BOOTHID, mContext, LoginPage.this);


                    //ACTUAL WAIT SCREEN CALL
                    Intent intent = new Intent(getApplicationContext(), WaitScreen.class);
                    intent.putExtra("LABEL", "Validating");
                    startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onResume() {
        login.setEnabled(true);
        super.onResume();
    }
}
