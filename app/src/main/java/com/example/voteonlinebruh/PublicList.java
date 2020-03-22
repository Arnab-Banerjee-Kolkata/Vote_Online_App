package com.example.voteonlinebruh;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PublicList extends AppCompatActivity {

    ListView list;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MainActivity.TM.getThemeId());
        setContentView(R.layout.activity_public_list);

        final int x=MainActivity.TM.getThemeId();

        context=this;
        Toolbar toolbar = findViewById(R.id.toolbarlist);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView imageView1 = findViewById(R.id.listbg);
        int resid = R.drawable.wait_bg_right;
        Glide
                .with(this)
                .load(resid).into(imageView1);

        ArrayList<ListItem> electionlist = new ArrayList<ListItem>();
        electionlist.add(new ListItem("LS Remote", 1));
        electionlist.add(new ListItem("VS Remote", 0));
        class MyAdapter extends ArrayAdapter<ListItem> {
            ArrayList<ListItem> list;
            Context context;

            MyAdapter(ArrayList<ListItem> list, Context context) {
                super(context, R.layout.election_card, list);
                this.list = list;
                this.context=context;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ConstraintLayout row = (ConstraintLayout) layoutInflater.inflate(R.layout.election_card, parent, false);
                TextView name, state;
                ImageView indicator;
                indicator = row.findViewById(R.id.imageView3);
                name = row.findViewById(R.id.textView15);
                state = row.findViewById(R.id.textView14);
                name.setText(list.get(position).getName());
                if (list.get(position).getState() == 0) {
                    state.setText("Upcoming");
                    indicator.setImageResource(R.drawable.yellow);
                } else {
                    state.setText("Ongoing");
                    indicator.setImageResource(R.drawable.green);
                }
                return row;
            }
        }
        list = findViewById(R.id.list);
        final MyAdapter arrayAdapter=new MyAdapter(electionlist,this.getBaseContext());
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), PublicElection.class);
                intent.putExtra("NAME", arrayAdapter.getItem(position).getName());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        list.setEnabled(true);
        super.onResume();
    }
}
