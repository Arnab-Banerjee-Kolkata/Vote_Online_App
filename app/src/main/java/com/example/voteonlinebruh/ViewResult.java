package com.example.voteonlinebruh;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MainActivity.TM.getThemeId());
        setContentView(R.layout.activity_view_result);

        String ELECTION_NAME = getIntent().getStringExtra("NAME");

        Toolbar toolbar = findViewById(R.id.toolbarvres);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TabLayout tabLayout = findViewById(R.id.tab);
        final ViewPager viewPager = findViewById(R.id.pager);
        if (MainActivity.TM.getThemeId() == R.style.AppTheme_Light)
            tabLayout.setBackgroundColor(getResources().getColor(R.color.lightBg));
        else
            tabLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
        //DUMMY VALUES
        String state = "West Bengal";
        ArrayList name = new ArrayList(),
                sym = new ArrayList(),
                seat = new ArrayList();
        ArrayList con_name = new ArrayList(),
                can_name = new ArrayList(),
                p_name = new ArrayList(),
                votes = new ArrayList();
        name.add("abc1");
        name.add("abc2");
        name.add("abc3");
        name.add("abc4");
        name.add("abc5");
        sym.add(R.mipmap.ic_launcher);
        sym.add(R.mipmap.ic_launcher);
        sym.add(R.mipmap.ic_launcher);
        sym.add(R.mipmap.ic_launcher);
        sym.add(R.mipmap.ic_launcher);
        seat.add("5");
        seat.add("4");
        seat.add("3");
        seat.add("2");
        seat.add("1");
        con_name.add("Constituency1");
        con_name.add("Constituency2");
        con_name.add("Constituency3");
        con_name.add("Constituency4");
        con_name.add("Constituency5");
        can_name.add("Candidate 1");
        can_name.add("Candidate 2");
        can_name.add("Candidate 3");
        can_name.add("Candidate 4");
        can_name.add("Candidate 5");
        p_name.add("Party 1");
        p_name.add("Party 2");
        p_name.add("Party 3");
        p_name.add("Party 4");
        p_name.add("Party 5");
        votes.add("5555");
        votes.add("5555");
        votes.add("5555");
        votes.add("5555");
        votes.add("5555");

        TextView textView = findViewById(R.id.state_name);
        textView.setText(state);
        Bundle args = new Bundle(),
                args2 = new Bundle();
        args.putStringArrayList("NAMES", name);
        args.putStringArrayList("SEATS", seat);
        args.putIntegerArrayList("SYMS", sym);
        args.putInt("ROWS", 5);
        args2.putStringArrayList("CON_NAME", con_name);
        args2.putStringArrayList("CAND_NAME", can_name);
        args2.putStringArrayList("PAR_NAME", p_name);
        args2.putStringArrayList("VOTES", votes);
        args2.putString("STATE_NAME", state);
        args2.putInt("ROWS", 5);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(this, getSupportFragmentManager(), args, args2);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
