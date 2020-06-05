package com.example.voteonlinebruh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.adapters.FragmentAdapter;
import com.example.voteonlinebruh.apiCalls.ServerCall;
import com.example.voteonlinebruh.models.ConstituencyWiseResultList;
import com.example.voteonlinebruh.models.PartywiseResultList;
import com.example.voteonlinebruh.models.StateListItem;
import com.example.voteonlinebruh.utility.ScreenControl;
import com.example.voteonlinebruh.utility.ThemeManager;

import java.util.ArrayList;
import java.util.List;

public class ResultsDetailed extends FragmentActivity {
    private RelativeLayout rel, container;
    private FrameLayout swipeContainer;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private String stateName, stateCode = "";
    private Spinner spinner;
    private ViewPager viewPager;
    private ArrayAdapter<String> arrayAdapter;
    private Bundle args, args2;
    private FragmentAdapter fragmentAdapter;
    private ScreenControl screenControl = new ScreenControl();
    private int themeid, electionId;
    private ArrayList<StateListItem> list;
    private SwipeRefreshLayout swipe;
    SwipeRefreshLayout.OnRefreshListener listener;
    private static boolean stopLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeid = ThemeManager.getThemeId();
        setTheme(themeid);
        setContentView(R.layout.activity_results_detailed);
        toolbar = findViewById(R.id.toolbarvres);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab);
        rel = findViewById(R.id.waitRel2);
        container = findViewById(R.id.spinnerContainer);
        swipe = findViewById(R.id.swipeRefreshDetailedPage);
        swipeContainer = findViewById(R.id.swipeContainer);
        Intent intent = getIntent();
        electionId = intent.getIntExtra("electionId", 0);
        final String type = intent.getStringExtra("type");
        spinner = findViewById(R.id.spinner);
        if (themeid == R.style.AppTheme_Light) {
            tabLayout.setBackgroundColor(getResources().getColor(R.color.lightBg));
            spinner.setPopupBackgroundResource(R.drawable.spinnerbglight);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        } else {
            tabLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
            spinner.setPopupBackgroundResource(R.drawable.spinnerbgdark);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        }
        if (type.equalsIgnoreCase("VIDHAN SABHA")) {
            stateName = intent.getStringExtra("stateName");
            container.setVisibility(View.GONE);
            viewPager.setPadding(0, 0, 0, 0);
            rel.setVisibility(View.VISIBLE);
            screenControl.makeScreenUnresponsive(ResultsDetailed.this.getWindow());
            ServerCall serverCall = new ServerCall();
            serverCall.getOverallResult(
                    type, electionId, "", getApplicationContext(), ResultsDetailed.this);
        } else {
            list = (ArrayList<StateListItem>) intent.getSerializableExtra("list");
            String[] states = new String[list.size()];
            for (int i = 0; i < list.size(); i++) states[i] = list.get(i).getStateName();
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            rel.setVisibility(View.VISIBLE);
                            screenControl.makeScreenUnresponsive(ResultsDetailed.this.getWindow());
                            stateName = list.get(position).getStateName();
                            stateCode = list.get(position).getStateCode();
                            ServerCall serverCall = new ServerCall();
                            serverCall.getOverallResult(
                                    type, electionId, stateCode, getApplicationContext(), ResultsDetailed.this);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
            listener =
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            ServerCall serverCall = new ServerCall();
                            serverCall.getOverallResult(
                                    type, electionId, stateCode, getApplicationContext(), ResultsDetailed.this);
                        }
                    };
            swipe.setOnRefreshListener(listener);
        }
    }

    public void release(
            ArrayList<PartywiseResultList> partyresultlist,
            ArrayList<ConstituencyWiseResultList> constresultlist,
            int status,
            int totalSeats,
            String type,
            boolean requestStatus) {
        if (!requestStatus) {
            swipeContainer.setVisibility(View.VISIBLE);
            viewPager.removeAllViews();
            viewPager.setVisibility(View.GONE);
        } else {
            swipeContainer.setVisibility(View.GONE);
            viewPager.removeAllViews();
            viewPager.setVisibility(View.VISIBLE);
            ArrayList name = new ArrayList(),
                    sym = new ArrayList(),
                    seat = new ArrayList(),
                    con_name = new ArrayList(),
                    can_name = new ArrayList(),
                    p_name = new ArrayList(),
                    p_img = new ArrayList(),
                    c_img = new ArrayList(),
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
                p_img.add(i.getPartySymbol());
                c_img.add(i.getCandidateImage());
                votes.add(Integer.toString(i.getVoteCount()));
            }
            args = new Bundle();
            args2 = new Bundle();
            args.putStringArrayList("NAMES", name);
            args.putStringArrayList("SEATS", seat);
            args.putIntegerArrayList("SYMS", sym);
            args.putInt("ROWS", partyresultlist.size());
            args.putInt("totalSeats", totalSeats);
            args.putString("type", type);
            args.putString("stateCode", stateCode);
            args.putInt("ID", electionId);
            args2.putStringArrayList("CON_NAME", con_name);
            args2.putStringArrayList("CAND_NAME", can_name);
            args2.putStringArrayList("PAR_NAME", p_name);
            args2.putStringArrayList("PAR_IMG", p_img);
            args2.putStringArrayList("CAN_IMG", c_img);
            args2.putStringArrayList("VOTES", votes);
            args2.putString("STATE_NAME", stateName);
            args2.putString("type", type);
            args2.putInt("ID", electionId);
            args2.putString("stateCode", stateCode);
            args2.putInt("ROWS", constresultlist.size());
            removeFragments();
            fragmentAdapter =
                    new FragmentAdapter(getBaseContext(), getSupportFragmentManager(), args, args2);
            viewPager.setAdapter(fragmentAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.getTabAt(0).select();
            tabLayout.addOnTabSelectedListener(
                    new TabLayout.OnTabSelectedListener() {
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
        screenControl.makeWindowResponsive(ResultsDetailed.this.getWindow());
        rel.setVisibility(View.GONE);
        swipe.setRefreshing(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        stopLoad = true;
    }

    private void removeFragments() {
        if (!getSupportFragmentManager().isDestroyed() && !stopLoad) {
            List<Fragment> list = getSupportFragmentManager().getFragments();
            for (int i = 0; i < list.size(); i++)
                getSupportFragmentManager().beginTransaction().remove(list.get(i)).commitNow();
        }
    }

    @Override
    protected void onResume() {
        stopLoad = false;
        super.onResume();
    }
}
