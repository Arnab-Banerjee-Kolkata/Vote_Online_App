package com.example.voteonlinebruh.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.adapters.ListViewForElectionListAdapter;
import com.example.voteonlinebruh.adapters.ListViewForResultListAdapter;
import com.example.voteonlinebruh.apiCalls.ServerCall;
import com.example.voteonlinebruh.models.ResultListItem;
import java.util.ArrayList;

public class ResultList extends AppCompatActivity {

    ListView listView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MainActivity.TM.getThemeId());
        setContentView(R.layout.activity_election_result);
        Toolbar toolbar = findViewById(R.id.toolbarres);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        context=this;

        Intent intent = getIntent();
        final ArrayList<ResultListItem> resultlist = (ArrayList<ResultListItem>) intent.getSerializableExtra("list");


        listView = findViewById(R.id.list2);
        final ListViewForResultListAdapter arrayAdapter = new ListViewForResultListAdapter(resultlist, this.getBaseContext());
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setEnabled(false);
                ServerCall serverCall = new ServerCall();
                serverCall.getOverallResult(resultlist.get(position).getType(),resultlist.get(position).getElectionId(),getApplicationContext());
                Intent intent = new Intent(getBaseContext(), WaitScreen.class);
                intent.putExtra("LABEL", "Hold on");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        listView.setEnabled(true);
        super.onResume();
    }
}
