package com.example.voteonlinebruh.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.adapters.ListViewForResultListAdapter;
import com.example.voteonlinebruh.api.PublicAPICall;
import com.example.voteonlinebruh.models.ResultListItem;
import com.example.voteonlinebruh.utility.ThemeManager;

import java.util.ArrayList;

public class ResultList extends AppCompatActivity {

  private ListView listView;
  private ArrayList<ResultListItem> resultlist;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_election_result);
    Toolbar toolbar = findViewById(R.id.toolbarres);
    RelativeLayout listContainer = findViewById(R.id.listContainer2);
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
    Intent intent = getIntent();
    //noinspection unchecked
    resultlist = (ArrayList<ResultListItem>) intent.getSerializableExtra("list");
    listView = findViewById(R.id.list2);
    ListViewForResultListAdapter arrayAdapter = new ListViewForResultListAdapter(resultlist, this.getBaseContext());
    listView.setAdapter(arrayAdapter);
    listView.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listView.setEnabled(false);
            PublicAPICall publicAPICall = new PublicAPICall();
            String type = resultlist.get(position).getType();
            if (type.equalsIgnoreCase("VIDHAN SABHA")) {
              Intent intent = new Intent(getBaseContext(), ResultsDetailed.class);
              intent.putExtra("electionId", resultlist.get(position).getElectionId());
              intent.putExtra("type", type);
              intent.putExtra("stateName", resultlist.get(position).getStateName());
              startActivity(intent);
            } else {
              publicAPICall.getOverallResult(
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
