package com.example.voteonlinebruh;

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

import java.util.ArrayList;

public class VotingPage extends AppCompatActivity {
    ArrayList<Item> item_list;
    int selected = -1;

    Button done;

    String electionId, electionName, aadhaarNo, boothId;
    Context mContext, context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MainActivity.TM.getThemeId());
        int themeId;
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


        ServerCall serverCall = new ServerCall();
        serverCall.getPublicCandidateList(aadhaarNo, electionId, electionName, mContext, VotingPage.this);


        Intent intent = new Intent(getApplicationContext(), WaitScreen.class);
        intent.putExtra("LABEL", "Fetching Panel");
        startActivity(intent);


        //dialogbox
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, themeId).setTitle("Confirm Submission\n\n")
                .setMessage("\nPlease confirm your vote for your selected candidate. Your vote will be registered on confirmation.\n")
                .setPositiveButton("Yes, I Confirm !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), Thanks.class);

                        //REGISTER VOTE & DESTROY ACTIVITY HERE


                        startActivity(intent);
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
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //VALUES TO WORK WITH->
                if (selected >= 0) {
                    done.setEnabled(false);
                    String PARTY = item_list.get(selected).getPname();
                    String CANDIDATE = item_list.get(selected).getCname();
                    Toast.makeText(context, item_list.get(selected).getPname() + "\n" + item_list.get(selected).getCname(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void finish() {
        super.finish();
    }

    public void showList(ArrayList<PublicCandidate> publicCandidates) {
        int len = publicCandidates.size();

        item_list = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            item_list.add(new Item(publicCandidates.get(i).symbol, publicCandidates.get(i).partyName, publicCandidates.get(i).name, R.drawable.bulboff));
        }

        final RecyclerView recyclerView = findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        final itemAdapter adapter = new itemAdapter(item_list, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //BULB CHANGE CODE

        adapter.setOnItemClickListener(new itemAdapter.OnItemClickListener() {
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

    }
}
