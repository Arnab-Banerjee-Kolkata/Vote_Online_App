package com.example.voteonlinebruh.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.adapters.FragmentAdapter;
import com.example.voteonlinebruh.fragments.OverallRes;
import com.example.voteonlinebruh.utility.ScreenControl;
import com.example.voteonlinebruh.apiCalls.ServerCall;
import com.example.voteonlinebruh.models.ConstituencyWiseResultList;
import com.example.voteonlinebruh.models.PartywiseResultList;
import com.example.voteonlinebruh.models.StateListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultsDetailed extends FragmentActivity {
    RelativeLayout rel;
    TabLayout tabLayout;
    String stateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int themeid = MainActivity.TM.getThemeId();
        setTheme(themeid);
        setContentView(R.layout.activity_results_detailed);
        Toolbar toolbar = findViewById(R.id.toolbarvres);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tabLayout = findViewById(R.id.tab);

        rel = findViewById(R.id.waitRel2);
        Intent intent = getIntent();
        final int electionId = intent.getIntExtra("electionId", 0);
        final ArrayList<StateListItem> list = (ArrayList<StateListItem>) intent.getSerializableExtra("list");
        final String type = intent.getStringExtra("type");

        Spinner spinner = findViewById(R.id.spinner);

        if (themeid == R.style.AppTheme_Light) {
            tabLayout.setBackgroundColor(getResources().getColor(R.color.lightBg));
            spinner.setPopupBackgroundResource(R.drawable.spinnerbglight);
        } else {
            tabLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
            spinner.setPopupBackgroundResource(R.drawable.spinnerbgdark);
        }
        String states[] = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            states[i] = list.get(i).getStateName();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rel.setVisibility(View.VISIBLE);
                ScreenControl screenControl = new ScreenControl();
                screenControl.makeScreenUnresponsive(ResultsDetailed.this.getWindow());
                stateName = list.get(position).getStateName();
                ServerCall serverCall = new ServerCall();
                serverCall.getOverallResult(type, electionId,
                        list.get(position).getStateCode(),
                        getApplicationContext(),
                        ResultsDetailed.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void release(ArrayList<PartywiseResultList> partyresultlist, ArrayList<ConstituencyWiseResultList> constresultlist, boolean status) {
        final ViewPager viewPager = findViewById(R.id.pager);
        TextView message = findViewById(R.id.errorMessage);
        if (!status) {
            message.setVisibility(View.VISIBLE);
            viewPager.removeAllViews();
            viewPager.setVisibility(View.GONE);
        } else {
            viewPager.removeAllViews();
            viewPager.setVisibility(View.VISIBLE);
            message.setVisibility(View.INVISIBLE);
            ArrayList name = new ArrayList(),
                    sym = new ArrayList(),
                    seat = new ArrayList(),
                    con_name = new ArrayList(),
                    can_name = new ArrayList(),
                    p_name = new ArrayList(),
                    votes = new ArrayList();
            for (PartywiseResultList i : partyresultlist) {
                name.add(i.getPartyname());
                seat.add(Integer.toString(i.getSeatsWon()));
                sym.add(i.getPartySymbol());
            }
            for (ConstituencyWiseResultList i : constresultlist) {
                con_name.add(i.getConstituencyName());
                can_name.add(i.getCandidateName());
                p_name.add(i.getPartyName());
                votes.add(Integer.toString(i.getVoteCount()));
            }
            Bundle args = new Bundle(),
                    args2 = new Bundle();
            args.putStringArrayList("NAMES", name);
            args.putStringArrayList("SEATS", seat);
            args.putIntegerArrayList("SYMS", sym);
            args.putInt("ROWS", partyresultlist.size());
            args2.putStringArrayList("CON_NAME", con_name);
            args2.putStringArrayList("CAND_NAME", can_name);
            args2.putStringArrayList("PAR_NAME", p_name);
            args2.putStringArrayList("VOTES", votes);
            args2.putString("STATE_NAME", stateName);
            args2.putInt("ROWS", constresultlist.size());
            List<Fragment> list=getSupportFragmentManager().getFragments();
            for (int i = 0; i < list.size(); i++)
                getSupportFragmentManager().beginTransaction().remove(list.get(i)).commitNow();
            FragmentAdapter fragmentAdapter = new FragmentAdapter(getBaseContext(), getSupportFragmentManager(), args, args2);
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
        ScreenControl screenControl = new ScreenControl();
        screenControl.makeWindowResponsive(ResultsDetailed.this.getWindow());
        rel.setVisibility(View.GONE);
    }
}
