package com.example.voteonlinebruh.activities;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.adapters.FragmentAdapter;
import com.example.voteonlinebruh.utility.ScreenControl;
import com.example.voteonlinebruh.api.PublicAPICall;
import com.example.voteonlinebruh.models.ConstituencyWiseResultList;
import com.example.voteonlinebruh.models.PartywiseResultList;
import com.example.voteonlinebruh.models.StateListItem;
import com.example.voteonlinebruh.utility.ThemeManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class ResultsDetailed extends FragmentActivity {
  private RelativeLayout rel;
  private FrameLayout swipeContainer;
  private TabLayout tabLayout;
  private String stateName, stateCode = "";
  private ViewPager viewPager;
  private final ScreenControl screenControl = new ScreenControl();
  private int electionId;
  private ArrayList<StateListItem> list;
  private SwipeRefreshLayout swipe;
  private static boolean stopLoad = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int themeid = ThemeManager.getThemeId();
    setTheme(themeid);
    setContentView(R.layout.activity_results_detailed);
    Toolbar toolbar = findViewById(R.id.toolbarvres);
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
    RelativeLayout container = findViewById(R.id.spinnerContainer);
    swipe = findViewById(R.id.swipeRefreshDetailedPage);
    swipeContainer = findViewById(R.id.swipeContainer);
    Intent intent = getIntent();
    electionId = intent.getIntExtra("electionId", 0);
    final String type = intent.getStringExtra("type");
    Spinner spinner = findViewById(R.id.spinner);
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
      PublicAPICall publicAPICall = new PublicAPICall();
      publicAPICall.getOverallResult(
          type, electionId, "", getApplicationContext(), ResultsDetailed.this);
    } else {
      //noinspection unchecked
      list = (ArrayList<StateListItem>) intent.getSerializableExtra("list");
      String[] states = new String[list.size()];
      for (int i = 0; i < list.size(); i++) states[i] = list.get(i).getStateName();
      ArrayAdapter<String> arrayAdapter =
          new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, states);
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
              PublicAPICall publicAPICall = new PublicAPICall();
              publicAPICall.getOverallResult(
                  type, electionId, stateCode, getApplicationContext(), ResultsDetailed.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
          });
      SwipeRefreshLayout.OnRefreshListener listener =
          new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              PublicAPICall publicAPICall = new PublicAPICall();
              publicAPICall.getOverallResult(
                  type, electionId, stateCode, getApplicationContext(), ResultsDetailed.this);
            }
          };
      swipe.setOnRefreshListener(listener);
    }
  }

  @SuppressWarnings({"unused", "ConstantConditions"})
  public void release(
      ArrayList<PartywiseResultList> partyresultlist,
      ArrayList<ConstituencyWiseResultList> constresultlist,
      HashMap<String, Integer> allianceMap,
      int status,
      int totalSeats,
      int tieCount,
      int stateElectionId,
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
      Bundle args = new Bundle();
      Bundle args2 = new Bundle();
      args.putStringArrayList("NAMES", name);
      args.putStringArrayList("SEATS", seat);
      args.putIntegerArrayList("SYMS", sym);
      args.putInt("ROWS", partyresultlist.size());
      args.putInt("totalSeats", totalSeats);
      args.putString("type", type);
      args.putString("stateCode", stateCode);
      args.putInt("ID", electionId);
      args.putInt("tieCount", tieCount);
      args.putSerializable("map", allianceMap);
      args2.putStringArrayList("CON_NAME", con_name);
      args2.putStringArrayList("CAND_NAME", can_name);
      args2.putStringArrayList("PAR_NAME", p_name);
      args2.putStringArrayList("PAR_IMG", p_img);
      args2.putStringArrayList("CAN_IMG", c_img);
      args2.putStringArrayList("VOTES", votes);
      args2.putString("STATE_NAME", stateName);
      args2.putString("type", type);
      args2.putInt("ID", electionId);
      args2.putInt("stateElectionId", stateElectionId);
      args2.putString("stateCode", stateCode);
      args2.putInt("ROWS", constresultlist.size());
      removeFragments();
      FragmentAdapter fragmentAdapter =
          new FragmentAdapter(getSupportFragmentManager(), args, args2);
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
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
          });
    }
    screenControl.makeWindowResponsive(ResultsDetailed.this.getWindow());
    rel.setVisibility(View.GONE);
    swipe.setRefreshing(false);
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
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
