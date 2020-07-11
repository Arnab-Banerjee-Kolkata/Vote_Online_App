package com.example.voteonlinebruh.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.adapters.RecyclerViewAdapter;
import com.example.voteonlinebruh.models.RecyclerViewItem;
import com.example.voteonlinebruh.utility.ThemeManager;

import java.util.ArrayList;

public class Demo extends AppCompatActivity {

  private final Handler handler = new Handler();
  private boolean threadstop = false;
  private Animation fadein, fadeout, scale_slow, scale_fast;
  private TextView message, message2;
    private RecyclerView recyclerView;
    private ConstraintLayout constraintLayout;
  private RelativeLayout rel1, rel2;
  private ImageView tap1inner, tap1outer, tap2inner, tap2outer;
    private CardView view;
  private ConstraintLayout constraintLayout1;
    private ConstraintLayout constraintLayout2;
    private ConstraintLayout candImg;
  private TextView pname;
    private TextView cname;
  private RelativeLayout pimg;
  private ImageView indicator;
    private int resid;
    private int resid2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int tapinnerRes, tapouterRes;
      int themeId = ThemeManager.getThemeId();
    if (themeId == R.style.AppTheme_Light) {
      setTheme(R.style.DialogTheme_Light);
      getWindow().setBackgroundDrawableResource(android.R.color.white);
      resid = R.drawable.bulboff_black;
      resid2 = R.drawable.bulbon_black;
      tapinnerRes = R.drawable.tap_white;
      tapouterRes = R.drawable.tap_black;
    } else {
      setTheme(R.style.DialogTheme_Dark);
      getWindow().setBackgroundDrawableResource(R.color.darkBg);
      resid = R.drawable.bulboff_white;
      resid2 = R.drawable.bulbon_white;
      tapinnerRes = R.drawable.tap_black;
      tapouterRes = R.drawable.tap_white;
    }
    setContentView(R.layout.activity_demo);
    ArrayList<RecyclerViewItem> recyclerViewItem_list = new ArrayList<>();
    for (int i = 1; i < 7; i++)
      recyclerViewItem_list.add(
          new RecyclerViewItem(
              "",
              themeId == R.style.AppTheme_Light
                  ? "https://drive.google.com/uc?id=1-MdJtSoclXeSthHJccTAJvsyJCqgBuY-"
                  : "https://drive.google.com/uc?id=1ePbZVRIGkLZ5VzCzw1HqUzEEg8jwHW2-",
              "Party " + i,
              "Candidate " + i,
              resid));
    recyclerView = findViewById(R.id.rec2);
    recyclerView.setHasFixedSize(true);
      LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
          @Override
          public boolean canScrollVertically() {
              return false;
          }
      };
      Context context = getApplicationContext();
      RecyclerViewAdapter adapter = new RecyclerViewAdapter(recyclerViewItem_list, context);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
    setFinishOnTouchOutside(false);
    constraintLayout = findViewById(R.id.demoContainer);
    message = findViewById(R.id.messageDemo);
    message2 = findViewById(R.id.messageDemo2);
    tap1inner = findViewById(R.id.tap1inner);
    tap1outer = findViewById(R.id.tap1outer);
    tap2inner = findViewById(R.id.tap2inner);
    tap2outer = findViewById(R.id.tap2outer);
    tap1inner.setImageResource(tapinnerRes);
    tap1outer.setImageResource(tapouterRes);
    tap2inner.setImageResource(tapinnerRes);
    tap2outer.setImageResource(tapouterRes);
    rel1 = findViewById(R.id.rel1);
    rel2 = findViewById(R.id.rel2);
    fadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
    fadein.setDuration(1000);
    fadeout = AnimationUtils.loadAnimation(context, R.anim.fade_out);
    fadeout.setDuration(1000);
    scale_slow = AnimationUtils.loadAnimation(context, R.anim.scaleslow);
    scale_fast = AnimationUtils.loadAnimation(context, R.anim.scalefast);
      ImageButton cl = findViewById(R.id.closediag);
    cl.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            finish();
          }
        });
    final Drawable[] layer = new Drawable[2];
    layer[0] =
        new Drawable() {
          @Override
          public void draw(@NonNull Canvas canvas) {}

          @Override
          public void setAlpha(int alpha) {}

          @Override
          public void setColorFilter(@Nullable ColorFilter colorFilter) {}

          @Override
          public int getOpacity() {
            return PixelFormat.TRANSPARENT;
          }
        };
    layer[1] = getDrawable(R.drawable.highlighter);
    final TransitionDrawable drawable = new TransitionDrawable(layer);

    @SuppressWarnings("DuplicateBranchesInSwitch")
    class AnimationHandler implements Runnable {

      @Override
      public void run() {
        try {
          for (int i = 0; !threadstop; i++) {
            switch (i % 20) {
              case 0:
                handler.post(
                    new Runnable() {
                      @SuppressLint("SetTextI18n")
                      @Override
                      public void run() {
                        message.setText("This is the screen you will see in the voting area.");
                        message.startAnimation(fadein);
                        message.setVisibility(View.VISIBLE);
                      }
                    });
                Thread.sleep(500);
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        constraintLayout.startAnimation(fadein);
                        constraintLayout.setVisibility(View.VISIBLE);
                      }
                    });
                Thread.sleep(4000);
                break;
              case 1:
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        message.startAnimation(fadeout);
                      }
                    });
                break;
              case 2:
                handler.post(
                    new Runnable() {
                      @SuppressLint("SetTextI18n")
                      @Override
                      public void run() {
                        message.setText(
                            "This demo will guide you through the different items and the process of voting.");
                        message.startAnimation(fadein);
                      }
                    });
                Thread.sleep(4000);
                break;
              case 3:
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        message.startAnimation(fadeout);
                      }
                    });
                break;
              case 4:
                view = (CardView) recyclerView.getChildAt(1);
                constraintLayout1 = (ConstraintLayout) view.getChildAt(0);
                constraintLayout2 = (ConstraintLayout) constraintLayout1.getChildAt(1);
                pname = (TextView) constraintLayout2.getViewById(R.id.party_name);
                cname = (TextView) constraintLayout2.getViewById(R.id.cand_name);
                pimg = (RelativeLayout) constraintLayout2.getViewById(R.id.relativeLayout);
                indicator = (ImageView) constraintLayout2.getViewById(R.id.indicator);
                candImg = (ConstraintLayout) constraintLayout2.getViewById(R.id.relativeLayout2);
                handler.post(
                    new Runnable() {
                      @SuppressLint("SetTextI18n")
                      @Override
                      public void run() {
                        candImg.setBackground(drawable);
                        drawable.startTransition(1000);
                        message.setText("This area will have the image of the candidate.");
                        message.startAnimation(fadein);
                      }
                    });
                Thread.sleep(2000);
                break;
              case 5:
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        drawable.reverseTransition(1000);
                        message.startAnimation(fadeout);
                      }
                    });
                Thread.sleep(1000);
                handler.post(
                    new Runnable() {
                      @SuppressLint("SetTextI18n")
                      @Override
                      public void run() {
                        candImg.setBackgroundColor(
                            getResources().getColor(android.R.color.transparent));
                        cname.setBackground(drawable);
                        drawable.startTransition(1000);
                        message.setText("This line will contain the name of the candidate.");
                        message.startAnimation(fadein);
                      }
                    });
                Thread.sleep(2000);
                break;
              case 6:
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        drawable.reverseTransition(1000);
                        message.startAnimation(fadeout);
                      }
                    });
                break;
              case 7:
                handler.post(
                    new Runnable() {
                      @SuppressLint("SetTextI18n")
                      @Override
                      public void run() {
                        cname.setBackgroundColor(
                            getResources().getColor(android.R.color.transparent));
                        pname.setBackground(drawable);
                        drawable.startTransition(1000);
                        message.setText("This line will contain the name of the party.");
                        message.startAnimation(fadein);
                      }
                    });
                Thread.sleep(2000);
                break;
              case 8:
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        drawable.reverseTransition(1000);
                        message.startAnimation(fadeout);
                      }
                    });
                break;
              case 9:
                handler.post(
                    new Runnable() {
                      @SuppressLint("SetTextI18n")
                      @Override
                      public void run() {
                        pname.setBackgroundColor(
                            getResources().getColor(android.R.color.transparent));
                        pimg.setBackground(drawable);
                        drawable.startTransition(1000);
                        message.setText("This area will have the symbol of the party.");
                        message.startAnimation(fadein);
                      }
                    });
                Thread.sleep(2000);
                break;
              case 10:
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        drawable.reverseTransition(1000);
                        message.startAnimation(fadeout);
                      }
                    });
                break;
              case 11:
                handler.post(
                    new Runnable() {
                      @SuppressLint("SetTextI18n")
                      @Override
                      public void run() {
                        pimg.setBackgroundColor(
                            getResources().getColor(android.R.color.transparent));
                        indicator.setBackground(drawable);
                        drawable.startTransition(1000);
                        message.setText("This will indicate the choice that you have made.");
                        message.startAnimation(fadein);
                      }
                    });
                Thread.sleep(2000);
                break;
              case 12:
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        drawable.reverseTransition(1000);
                        message.startAnimation(fadeout);
                      }
                    });
                break;
              case 13:
                handler.post(
                    new Runnable() {
                      @SuppressLint("SetTextI18n")
                      @Override
                      public void run() {
                        indicator.setBackgroundColor(
                            getResources().getColor(android.R.color.transparent));
                        message.setText("Tap on one of the options to cast your vote.");
                        message.startAnimation(fadein);
                      }
                    });
                break;
              case 14:
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        tap1outer.startAnimation(scale_slow);
                      }
                    });
                Thread.sleep(300);
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        tap1inner.startAnimation(scale_fast);
                      }
                    });
                Thread.sleep(350);
                view = (CardView) recyclerView.getChildAt(0);
                constraintLayout1 = (ConstraintLayout) view.getChildAt(0);
                constraintLayout2 = (ConstraintLayout) constraintLayout1.getChildAt(1);
                indicator = (ImageView) constraintLayout2.getViewById(R.id.indicator);
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        indicator.setImageResource(resid2);
                      }
                    });
                break;
              case 15:
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        message.startAnimation(fadeout);
                      }
                    });
                Thread.sleep(1000);
                handler.post(
                    new Runnable() {
                      @SuppressLint("SetTextI18n")
                      @Override
                      public void run() {
                        message.setText("You can also change you choice if you wish to.");
                        message.startAnimation(fadein);
                      }
                    });
                break;
              case 16:
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        tap2outer.startAnimation(scale_slow);
                      }
                    });
                Thread.sleep(300);
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        tap2inner.startAnimation(scale_fast);
                      }
                    });
                Thread.sleep(350);
                view = (CardView) recyclerView.getChildAt(1);
                constraintLayout1 = (ConstraintLayout) view.getChildAt(0);
                constraintLayout2 = (ConstraintLayout) constraintLayout1.getChildAt(1);
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        indicator.setImageResource(resid);
                        indicator = (ImageView) constraintLayout2.getViewById(R.id.indicator);
                        indicator.setImageResource(resid2);
                      }
                    });
                break;
              case 17:
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        rel1.startAnimation(fadeout);
                      }
                    });
                Thread.sleep(1000);
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        rel1.setVisibility(View.INVISIBLE);
                        message.setVisibility(View.INVISIBLE);
                        rel2.startAnimation(fadein);
                        rel2.setVisibility(View.VISIBLE);
                      }
                    });
                Thread.sleep(4000);
                break;
              case 18:
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        indicator.setImageResource(resid);
                        constraintLayout.startAnimation(fadeout);
                        message2.startAnimation(fadeout);
                      }
                    });
                break;
              case 19:
                handler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        rel2.setVisibility(View.INVISIBLE);
                        constraintLayout.setVisibility(View.INVISIBLE);
                        rel1.setVisibility(View.VISIBLE);
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

    AnimationHandler animationHandler = new AnimationHandler();
    new Thread(animationHandler).start();
  }

  @Override
  public void finish() {
    threadstop = true;
    super.finish();
  }
}
