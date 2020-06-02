package com.example.voteonlinebruh.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.adapters.ListViewForResultListAdapter;
import com.example.voteonlinebruh.apiCalls.ServerCall;
import com.example.voteonlinebruh.models.ResultListItem;
import com.example.voteonlinebruh.utility.ThemeManager;

import java.util.ArrayList;

public class ResultList extends AppCompatActivity {

  private ListView listView;
  private Context context;
  private Toolbar toolbar;
  private ArrayList<ResultListItem> resultlist;
  private ListViewForResultListAdapter arrayAdapter;
  RelativeLayout listContainer;
  private int themeId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_election_result);
    toolbar = findViewById(R.id.toolbarres);
    listContainer = findViewById(R.id.listContainer2);
    if (themeId == R.style.AppTheme_Light) {
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
      listContainer.setBackground(getDrawable(android.R.drawable.dialog_holo_light_frame));
    } else {
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
      listContainer.setBackground(getDrawable(android.R.drawable.dialog_holo_dark_frame));
    }
    toolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onBackPressed();
          }
        });
    context = this;
    Intent intent = getIntent();
    resultlist = (ArrayList<ResultListItem>) intent.getSerializableExtra("list");
    listView = findViewById(R.id.list2);
    arrayAdapter = new ListViewForResultListAdapter(resultlist, this.getBaseContext());
    listView.setAdapter(arrayAdapter);
    listView.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listView.setEnabled(false);
            ServerCall serverCall = new ServerCall();
            String type = resultlist.get(position).getType();
            if (type.equalsIgnoreCase("VIDHAN SABHA")) {
              Intent intent = new Intent(getBaseContext(), ResultsDetailed.class);
              intent.putExtra("electionId", resultlist.get(position).getElectionId());
              intent.putExtra("type", type);
              intent.putExtra("stateName", resultlist.get(position).getStateName());
              startActivity(intent);
            } else {
              serverCall.getOverallResult(
                  type,
                  resultlist.get(position).getElectionId(),
                  getApplicationContext(),
                  null,
                  false);
              Intent intent = new Intent(getBaseContext(), WaitScreen.class);
              intent.putExtra("LABEL", "Hold on");
              startActivity(intent);
              overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
          }
        });
  }

  @Override
  protected void onResume() {
    listView.setEnabled(true);
    super.onResume();
  }
}
