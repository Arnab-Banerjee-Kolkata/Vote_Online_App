package com.example.voteonlinebruh.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.models.ElectionListItem;
import com.example.voteonlinebruh.adapters.ListViewForElectionListAdapter;

import java.util.ArrayList;

public class PublicElectionList extends AppCompatActivity {

    private ListView list;
    private Toolbar toolbar;
    private Context context;
    private ImageView imageView1, imageView2;
    private ArrayList<ElectionListItem> electionlist;
    private ListViewForElectionListAdapter arrayAdapter;
    int themeId = MainActivity.TM.getThemeId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(themeId);
        setContentView(R.layout.activity_public_list);
        context = this;
        toolbar = findViewById(R.id.toolbarlist);
        if (themeId == R.style.AppTheme_Light)
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        else
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imageView1 = findViewById(R.id.listbg1);
        int resid = R.drawable.wait_bg_left;
        Glide
                .with(this)
                .load(resid).into(imageView1);
        imageView2 = findViewById(R.id.listbg2);
        resid = R.drawable.wait_bg_right;
        Glide
                .with(this)
                .load(resid).into(imageView2);
        Intent intent = getIntent();
        electionlist = (ArrayList<ElectionListItem>) intent.getSerializableExtra("list");
        list = findViewById(R.id.list);
        arrayAdapter = new ListViewForElectionListAdapter(electionlist, this.getBaseContext());
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (arrayAdapter.getItem(position).getStatus()) {
                    case 0:
                        Toast.makeText(context, "Please stay tuned.", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        list.setEnabled(false);
                        Intent intent = new Intent(getApplicationContext(), PublicElectionEntryPoint.class);
                        intent.putExtra("NAME", arrayAdapter.getItem(position).getName());
                        intent.putExtra("electionId", arrayAdapter.getItem(position).getElectionId());
                        intent.putExtra("electionType", arrayAdapter.getItem(position).getType());
                        startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(context, "Please stay tuned until the result is declared.", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(context, "Result is declared, check results section.", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        Toast.makeText(context, "Election has been cancelled!", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        list.setEnabled(true);
        super.onResume();
    }
}
