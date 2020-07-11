package com.example.voteonlinebruh.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.api.PublicAPICall;
import com.example.voteonlinebruh.models.PartywiseResultList;
import com.example.voteonlinebruh.utility.ThemeManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultsSimplified extends AppCompatActivity {

  private PieChart chart;
  private Toolbar toolbar;
  private Button button;
  private TableLayout tableLayout;
  private SwipeRefreshLayout swipe;
  private SwipeRefreshLayout.OnRefreshListener listener;
  private int themeId;
  private int totalSeats;
  private int tieCount;
  private boolean oneTimeDataLoad = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_results_simplified);
    toolbar = findViewById(R.id.toolbarResSim);
    toolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onBackPressed();
          }
        });
    button = findViewById(R.id.detailBut);
    final Intent intent = getIntent();
    button.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            button.setEnabled(false);
            PublicAPICall publicAPICall = new PublicAPICall();
            publicAPICall.getStateList(
                intent.getIntExtra("electionId", 0),
                intent.getStringExtra("type"),
                getApplicationContext());
            Intent intent = new Intent(getBaseContext(), WaitScreen.class);
            intent.putExtra("LABEL", "Hold on");
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
          }
        });
    int status = intent.getIntExtra("status", 0);
    totalSeats = intent.getIntExtra("totalSeats", 0);
    tieCount = intent.getIntExtra("tieCount", 0);
    ImageView indicator = findViewById(R.id.indicator2);
    switch (status) {
      case 2:
        indicator.setImageResource(R.drawable.pend_res);
        break;
      case 3:
        indicator.setImageResource(R.drawable.complete);
        break;
    }
    @SuppressWarnings("unchecked")
    ArrayList<PartywiseResultList> resultlist =
        (ArrayList<PartywiseResultList>) intent.getSerializableExtra("list");
    @SuppressWarnings("unchecked")
    HashMap<String, Integer> alliance =
        (HashMap<String, Integer>) intent.getSerializableExtra("map");
    chart = findViewById(R.id.chartoverall);
    tableLayout = findViewById(R.id.tableoverall);
    swipe = findViewById(R.id.swipeRefreshSimplifiedPage);
    listener =
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            PublicAPICall publicAPICall = new PublicAPICall();
            publicAPICall.getOverallResult(
                intent.getStringExtra("type"),
                intent.getIntExtra("electionId", 0),
                getApplicationContext(),
                ResultsSimplified.this,
                true);
          }
        };
    swipe.setOnRefreshListener(listener);
    populate(resultlist, alliance);
  }

  @SuppressLint("SetTextI18n")
  public void populate(
      ArrayList<PartywiseResultList> resultlist, HashMap<String, Integer> alliance) {
    chart.setBackgroundColor(Color.TRANSPARENT);
    chart.setUsePercentValues(false);
    chart.getDescription().setEnabled(false);
    chart.setCenterTextTypeface(ResourcesCompat.getFont(chart.getContext(), R.font.azo));
    chart.setDrawHoleEnabled(true);
    chart.setRotationEnabled(false);
    chart.setHoleColor(Color.TRANSPARENT);
    chart.setTransparentCircleAlpha(0);
    chart.setHoleRadius(45f);
    chart.setMaxAngle(360f); // FULL CHART
    chart.setRotationAngle(180f);
    chart.setDrawEntryLabels(false);
    chart.setUsePercentValues(false);
    ArrayList<PieEntry> values = new ArrayList<>();
    for (String i : alliance.keySet()) {
      values.add(new PieEntry(alliance.get(i),i));
    }
    if (tieCount > 0) values.add(new PieEntry(tieCount, "Ties"));
    PieDataSet dataSet = new PieDataSet(values, "");
    dataSet.setSliceSpace(3f);
    dataSet.setSelectionShift(6f);
    dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
    dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
    dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
    dataSet.setValueLinePart1OffsetPercentage(100f);
    dataSet.setValueLinePart1Length(0.6f);
    dataSet.setValueLinePart2Length(0.6f);
    PieData data = new PieData(dataSet);
    chart.animateY(1000);
    data.setValueTextSize(15f);
    data.setValueTypeface(ResourcesCompat.getFont(chart.getContext(), R.font.azo));
    data.setValueFormatter(
        new IValueFormatter() {
          @Override
          public String getFormattedValue(
              float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return "" + (int) value;
          }
        });
    chart.invalidate();
    chart.setData(data);
    chart.setCenterText((int) data.getYValueSum() - tieCount + "/" + totalSeats);
    chart.setCenterTextTypeface(ResourcesCompat.getFont(chart.getContext(), R.font.azo));
    chart.setCenterTextSize(15f);
    chart.getLegend().setEnabled(true);
    chart.setExtraOffsets(0.f, 5.f, 0.f, 5.f);
    Legend leg = chart.getLegend();
    leg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
    LegendEntry[] legend = leg.getEntries();
    LayoutInflater layoutInflater =
        (LayoutInflater) this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (themeId == R.style.AppTheme_Light) {
      dataSet.setValueLineColor(Color.BLACK);
      chart.setCenterTextColor(Color.BLACK);
      data.setValueTextColor(Color.BLACK);
      leg.setTextColor(Color.BLACK);
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
      tableLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
    } else {
      dataSet.setValueLineColor(Color.WHITE);
      chart.setCenterTextColor(Color.WHITE);
      data.setValueTextColor(Color.WHITE);
      leg.setTextColor(Color.WHITE);
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
      tableLayout.setBackgroundResource(android.R.drawable.dialog_holo_dark_frame);
    }
    tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
    for (int i = 0; i <= resultlist.size(); i++) {
      View view = layoutInflater.inflate(R.layout.table_row, tableLayout, false);
      TableRow row = view.findViewById(R.id.rowwwww);
      if (i % 2 == 1) row.setBackgroundColor(getResources().getColor(R.color.shade));
      TextView names = view.findViewById(R.id.partynum), seats = view.findViewById(R.id.seatnum);
      final ImageView syms = view.findViewById(R.id.imnum);
      final ProgressBar progress = view.findViewById(R.id.tableImageProgress);
      String resUrl = "";
      if (i == resultlist.size()) {
        if (tieCount > 0) {
          names.setText("Tied Constituency");
          seats.setText(Integer.toString(tieCount));
        } else {
          break;
        }
      } else {
        resUrl = resultlist.get(i).getPartySymbol();
        names.setText(resultlist.get(i).getPartyname());
        seats.setText(Integer.toString(resultlist.get(i).getSeatsWon()));
      }
      Glide.with(this)
          .load(resUrl)
          .error(
              themeId == R.style.AppTheme_Light
                  ? R.drawable.ic_error_outline_black_24dp
                  : R.drawable.ic_error_outline_white_24dp)
          .listener(
              new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(
                    @Nullable GlideException e,
                    Object model,
                    Target<Drawable> target,
                    boolean isFirstResource) {
                  progress.setVisibility(View.GONE);
                  syms.setPadding(10, 20, 10, 10);
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
              })
          .into(syms);
      tableLayout.addView(row);
      swipe.setRefreshing(false);
    }
  }

  @Override
  protected void onResume() {
    button.setEnabled(true);
    if (oneTimeDataLoad)
      swipe.post(
          new Runnable() {
            @Override
            public void run() {
              swipe.setRefreshing(true);
              listener.onRefresh();
            }
          });
    super.onResume();
    oneTimeDataLoad = true;
  }
}
