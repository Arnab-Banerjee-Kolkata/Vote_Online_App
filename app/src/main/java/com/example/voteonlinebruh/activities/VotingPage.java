package com.example.voteonlinebruh.activities;

import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.api.PublicAPICall;
import com.example.voteonlinebruh.adapters.RecyclerViewAdapter;
import com.example.voteonlinebruh.models.RecyclerViewItem;
import com.example.voteonlinebruh.models.PublicCandidate;
import com.example.voteonlinebruh.utility.PostingService;
import com.example.voteonlinebruh.utility.ScreenControl;
import com.example.voteonlinebruh.utility.ThemeManager;

import java.util.ArrayList;

public class VotingPage extends AppCompatActivity {
  private ArrayList<RecyclerViewItem> recyclerViewItem_list;
  private int selected = -1;
  private Button done;
  private Context mContext;
  private ArrayList<PublicCandidate> publicCandidates;
  private RecyclerView recyclerView;
  private RecyclerViewAdapter adapter;
  private LinearLayoutManager layoutManager;
  private ScreenControl screenControl = new ScreenControl();
  private RelativeLayout waitrel;
  private Intent intent;
  private int themeId, themeId2, resid;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    if (themeId == R.style.AppTheme_Light) {
      themeId2 = R.style.ConfirmTheme_Light;
      resid = R.drawable.bulboff_black;
    } else {
      themeId2 = R.style.ConfirmTheme_Dark;
      resid = R.drawable.bulboff_white;
    }
    setContentView(R.layout.activity_voting_page);
    mContext = getApplicationContext();
    waitrel = findViewById(R.id.waitRel3);
    final String boothId = getIntent().getStringExtra("boothId");
    intent = new Intent(this, PostingService.class);
    intent.putExtra("boothId", boothId);
    intent.putExtra("action", PostingService.ACTION_START_SERVICE);
    PublicAPICall publicAPICall = new PublicAPICall();
    publicAPICall.getRandomKey(boothId, mContext, VotingPage.this);
    waitrel.setVisibility(View.VISIBLE);
    screenControl.makeScreenUnresponsive(VotingPage.this.getWindow());
    // dialogbox
    final AlertDialog.Builder builder =
        new AlertDialog.Builder(this, themeId2)
            .setTitle("Confirm Submission\n\n")
            .setMessage(
                "\nPlease confirm your vote for your selected candidate. Your vote will be registered on confirmation.\n")
            .setPositiveButton(
                "Yes, I Confirm !",
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    String candidateId = publicCandidates.get(selected).getId();
                    PublicAPICall publicAPICall = new PublicAPICall();
                    publicAPICall.storeVote(boothId, candidateId, mContext, false);
                    Intent intent = new Intent(mContext, Thanks.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    VotingPage.this.finish();
                  }
                })
            .setNegativeButton(
                "No, I Want To Change !",
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    done.setEnabled(true);
                    dialog.dismiss();
                  }
                });
    done = findViewById(R.id.donebutton);
    publicCandidates = (ArrayList<PublicCandidate>) getIntent().getSerializableExtra("list");
    int len = publicCandidates.size();

    recyclerViewItem_list = new ArrayList<>();

    for (int i = 0; i < len; i++) {
      PublicCandidate candidate = publicCandidates.get(i);
      recyclerViewItem_list.add(
          new RecyclerViewItem(
              candidate.getSymbol(),
              candidate.getImage(),
              candidate.getPartyName(),
              candidate.getName(),
              resid));
    }
    recyclerView = findViewById(R.id.rec);
    recyclerView.setHasFixedSize(true);
    layoutManager = new LinearLayoutManager(this);
    adapter = new RecyclerViewAdapter(recyclerViewItem_list, getApplicationContext());
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
    adapter.setOnItemClickListener(
        new RecyclerViewAdapter.OnItemClickListener() {
          @Override
          public void onItemClick(int position) {
            if (position != selected) {
              if (selected != -1) {
                adapter.notifyItemChanged(selected);
              }
              CardView view = (CardView) layoutManager.findViewByPosition(position);
              ConstraintLayout constraintLayout = (ConstraintLayout) view.getChildAt(0);
              ConstraintLayout constraintLayout1 =
                  (ConstraintLayout) constraintLayout.getChildAt(1);
              ImageView indicator = (ImageView) constraintLayout1.getViewById(R.id.indicator);
              if (resid == R.drawable.bulboff_white)
                indicator.setImageResource(R.drawable.bulbon_white);
              else indicator.setImageResource(R.drawable.bulbon_black);
              selected = position;
            }
          }
        });

    done.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (selected >= 0) {
              done.setEnabled(false);
              AlertDialog dialog = builder.create();
              dialog.setCanceledOnTouchOutside(false);
              dialog.setCancelable(false);
              dialog.show();
            } else
              Toast.makeText(
                      getApplicationContext(), "Please select your candidate !", Toast.LENGTH_SHORT)
                  .show();
          }
        });
    VotingInstructions.instance.finish();
    PublicElectionEntryPoint.instance.finish();
  }

  public void release() {
    waitrel.setVisibility(View.GONE);
    screenControl.makeWindowResponsive(VotingPage.this.getWindow());
  }

  @Override
  public void onBackPressed() {}

  @Override
  protected void onUserLeaveHint() {
    Log.d("Home", "pressed");
    startService(intent);
    finish();
    super.onUserLeaveHint();
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    View decorView = getWindow().getDecorView();
    decorView.setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
  }

  void logout() {
    super.onBackPressed();
  }

  @Override
  public void finish() {
    super.finish();
  }
}
