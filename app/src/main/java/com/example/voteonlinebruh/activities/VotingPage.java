package com.example.voteonlinebruh.activities;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.apiCalls.ServerCall;
import com.example.voteonlinebruh.adapters.RecyclerViewAdapter;
import com.example.voteonlinebruh.models.RecyclerViewItem;
import com.example.voteonlinebruh.models.PublicCandidate;

import java.util.ArrayList;

public class VotingPage extends AppCompatActivity {
    ArrayList<RecyclerViewItem> recyclerViewItem_list;
    int selected = -1;

    Button done;

    String electionId, electionName, aadhaarNo, boothId;
    Context mContext, context = this;
    int themeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MainActivity.TM.getThemeId());
        if (MainActivity.TM.getThemeId() == R.style.AppTheme_Light)
            themeId = R.style.ConfirmTheme_Light;
        else
            themeId = R.style.ConfirmTheme_Dark;
        setContentView(R.layout.activity_voting_page);

        mContext = getApplicationContext();


        SharedPreferences pref1 = getSharedPreferences("VoterDetails", MODE_PRIVATE);
        aadhaarNo = pref1.getString("aadhaarNo", "");
        boothId = pref1.getString("boothId", "");

        SharedPreferences pref2 = getSharedPreferences("ElectionDetails", MODE_PRIVATE);
        electionId = pref2.getString("electionId", "");
        electionName = pref2.getString("electionName", "");

        //dialogbox
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, themeId).setTitle("Confirm Submission\n\n")
                .setMessage("\nPlease confirm your vote for your selected candidate. Your vote will be registered on confirmation.\n")
                .setPositiveButton("Yes, I Confirm !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ServerCall serverCall=new ServerCall();
                        //serverCall.storeVote()
                    }
                })
                .setNegativeButton("No, I Want To Change !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        //DUMMY CHECKING CODE

        done = findViewById(R.id.donebutton);
        final ArrayList<PublicCandidate> publicCandidates=(ArrayList<PublicCandidate>)getIntent().getSerializableExtra("list");
        int len = publicCandidates.size();

        recyclerViewItem_list = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            recyclerViewItem_list.add(new RecyclerViewItem(publicCandidates.get(i).getSymbol(), publicCandidates.get(i).getPartyName(), publicCandidates.get(i).getName(), R.drawable.bulboff));
        }

        final RecyclerView recyclerView = findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(recyclerViewItem_list, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //BULB CHANGE CODE

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position != selected) {
                    if (selected != -1) {
                        adapter.notifyItemChanged(selected);
                    }
                    CardView view = (CardView) layoutManager.findViewByPosition(position);
                    ConstraintLayout constraintLayout = (ConstraintLayout) view.getChildAt(0);
                    LinearLayout linearLayout = (LinearLayout) constraintLayout.getChildAt(1);
                    ImageView indicator = (ImageView) linearLayout.getChildAt(2);
                    indicator.setImageResource(R.drawable.bulbon);
                    selected = position;
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //VALUES TO WORK WITH->
                if (selected >= 0) {
                    done.setEnabled(false);
                    String PARTY = recyclerViewItem_list.get(selected).getPname();
                    String CANDIDATE = recyclerViewItem_list.get(selected).getCname();
                    int cadidateId=publicCandidates.get(selected).getId();
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.show();
                } else
                    Toast.makeText(getApplicationContext(), "Please select your candidate !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, themeId).setTitle("Confirm Log Out")
                .setMessage("\nYou cannot go back ! Your vote will be registered as 'NOTA'.\n\nAre you sure you want to log out ?\n")
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    void logout(){
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
