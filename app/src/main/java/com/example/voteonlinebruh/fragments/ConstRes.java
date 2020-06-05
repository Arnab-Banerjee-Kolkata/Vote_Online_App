package com.example.voteonlinebruh.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.activities.ResultsDetailed;
import com.example.voteonlinebruh.api.PublicAPICall;
import com.example.voteonlinebruh.utility.ThemeManager;

import java.util.ArrayList;
import java.util.HashSet;

public class ConstRes extends Fragment {

  private static ArrayList cons_name, cand_name, par_name, p_img, c_img, votes;
  private static HashSet<Integer> ties;
  private static String state_name, stateCode, type;
  private static int rows, electionId;
  private static ResultsDetailed context;
  private static ListView listView;
  private static SwipeRefreshLayout swipe;
  private static ConstraintLayout constraintLayout;
  private static ImageButton close;
  public static ImageView infoIcon;
  private static boolean oneTimeDataLoad = false;
  private static SwipeRefreshLayout.OnRefreshListener listener;

  public static ConstRes newInstance(Bundle args) {
    ConstRes constRes = new ConstRes();
    constRes.setArguments(args);
    return constRes;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    context = (ResultsDetailed) activity;
  }

  @Override
  public void setArguments(@Nullable Bundle args) {

    cons_name = args.getStringArrayList("CON_NAME");
    state_name = args.getString("STATE_NAME");
    cand_name = args.getStringArrayList("CAND_NAME");
    par_name = args.getStringArrayList("PAR_NAME");
    p_img = args.getStringArrayList("PAR_IMG");
    c_img = args.getStringArrayList("CAN_IMG");
    votes = args.getStringArrayList("VOTES");
    rows = args.getInt("ROWS");
    type = args.getString("type");
    stateCode = args.getString("stateCode");
    electionId = args.getInt("ID");
    int prev = 0;
    ties = new HashSet<>();
    for (int i = 1; i < cons_name.size(); i++) {
      if (cons_name.get(prev).toString().equalsIgnoreCase(cons_name.get(i).toString())) {
        ties.add(prev);
        ties.add(i);
      }
      prev = i;
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_const_res, container, false);
    listener =
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            PublicAPICall publicAPICall = new PublicAPICall();
            publicAPICall.getOverallResult(type, electionId, stateCode, getContext(), context);
          }
        };
    if (ties.size() > 0) {
      infoIcon = view.findViewById(R.id.imageView);
      if (ThemeManager.getThemeId() == R.style.AppTheme_Light)
        infoIcon.setImageResource(R.drawable.ic_info_black_24dp);
      else infoIcon.setImageResource(R.drawable.ic_info_white_24dp);
      constraintLayout = view.findViewById(R.id.infoContainer);
      constraintLayout.setVisibility(View.VISIBLE);
      close = view.findViewById(R.id.closediag2);
      close.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              constraintLayout.setVisibility(View.GONE);
            }
          });
    }
    swipe = view.findViewById(R.id.swipeRefreshConstFrag);
    swipe.setOnRefreshListener(listener);
    listView = view.findViewById(R.id.list3);
    class MyAdapter extends ArrayAdapter<ArrayList> {
      ArrayList list;
      Context context;

      MyAdapter(ArrayList list, Context context) {
        super(context, R.layout.const_list_card, list);
        this.list = list;
        this.context = context;
      }

      public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater =
            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout v =
            (RelativeLayout) layoutInflater.inflate(R.layout.const_list_card, parent, false);
        TextView con = v.findViewById(R.id.conName),
            sta = v.findViewById(R.id.stateName),
            can = v.findViewById(R.id.candName),
            par = v.findViewById(R.id.partyName),
            vot = v.findViewById(R.id.votes);
        final ImageView partImg = v.findViewById(R.id.partySymbol),
            candImg = v.findViewById(R.id.candImage);
        final ProgressBar progress = v.findViewById(R.id.candImageProgress);
        con.setText((String) cons_name.get(position));
        sta.setText(state_name);
        can.setText((String) cand_name.get(position));
        par.setText((String) par_name.get(position));
        RequestListener listener =
            new RequestListener<Drawable>() {
              @Override
              public boolean onLoadFailed(
                  @Nullable GlideException e,
                  Object model,
                  Target<Drawable> target,
                  boolean isFirstResource) {
                progress.setVisibility(View.GONE);
                candImg.setPadding(25, 25, 25, 25);
                return false;
              }

              @Override
              public boolean onResourceReady(
                  Drawable resource,
                  Object model,
                  Target<Drawable> target,
                  DataSource dataSource,
                  boolean isFirstResource) {
                progress.setVisibility(View.GONE);
                return false;
              }
            };
        Glide.with(context)
            .load((String) p_img.get(position))
            .error(
                ThemeManager.getThemeId() == R.style.AppTheme_Light
                    ? R.drawable.ic_error_outline_black_24dp
                    : R.drawable.ic_error_outline_white_24dp)
            .listener(listener)
            .into(partImg);
        Glide.with(context)
            .load((String) c_img.get(position))
            .error(
                ThemeManager.getThemeId() == R.style.AppTheme_Light
                    ? R.drawable.ic_error_outline_black_24dp
                    : R.drawable.ic_error_outline_white_24dp)
            .into(candImg);
        vot.setText("Votes : " + votes.get(position));
        if (ties.contains(position))
          v.setBackground(getResources().getDrawable(R.drawable.highlighter2));
        if (ThemeManager.getThemeId() == R.style.AppTheme_Dark)
          v.findViewById(R.id.consContainer)
              .setBackground(getResources().getDrawable(android.R.drawable.dialog_holo_dark_frame));
        return v;
      }
    }
    final MyAdapter arrayAdapter = new MyAdapter(cons_name, view.getContext());
    listView.setAdapter(arrayAdapter);
    swipe.setRefreshing(false);
    oneTimeDataLoad = false;
    return view;
  }

  @Override
  public void onStart() {
    if (oneTimeDataLoad)
      swipe.post(
          new Runnable() {
            @Override
            public void run() {
              swipe.setRefreshing(true);
              listener.onRefresh();
            }
          });
    super.onStart();
    oneTimeDataLoad = true;
  }
}
