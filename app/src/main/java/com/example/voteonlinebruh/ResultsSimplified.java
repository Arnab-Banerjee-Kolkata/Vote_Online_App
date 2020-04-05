package com.example.voteonlinebruh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

public class ResultsSimplified extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int themeId = MainActivity.TM.getThemeId();
        setTheme(themeId);
        setContentView(R.layout.activity_results_simplified);

        Toolbar toolbar = findViewById(R.id.toolbarResSim);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final String ELECTION_NAME = getIntent().getStringExtra("NAME");
        Button button = findViewById(R.id.detailBut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResultsDetailed.class);
                intent.putExtra("NAME", ELECTION_NAME);
                startActivity(intent);
            }
        });

        PieChart chart = findViewById(R.id.chartoverall);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setCenterTextTypeface(ResourcesCompat.getFont(chart.getContext(), R.font.azo));
        chart.setDrawHoleEnabled(true);
        chart.setRotationEnabled(false);
        chart.setHoleColor(Color.TRANSPARENT);
        chart.setTransparentCircleAlpha(000);
        chart.setHoleRadius(45f);
        chart.setMaxAngle(360f); // FULL CHART
        chart.setRotationAngle(180f);
        chart.setDrawEntryLabels(false);
        chart.setUsePercentValues(false);
        ArrayList<PieEntry> values = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            values.add(new PieEntry(5, "ab"));
        }
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
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + (int) value;
            }
        });
        chart.invalidate();
        chart.setData(data);
        chart.setCenterText((int) data.getYValueSum() + "/" + (int) data.getYValueSum());
        chart.setCenterTextTypeface(ResourcesCompat.getFont(chart.getContext(), R.font.azo));
        chart.setCenterTextSize(20f);
        chart.getLegend().setEnabled(true);
        chart.setExtraOffsets(0.f, 5.f, 0.f, 5.f);
        Legend leg=chart.getLegend();
        leg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        LegendEntry[] legend = leg.getEntries();

        LayoutInflater layoutInflater = (LayoutInflater) this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableLayout tableLayout = findViewById(R.id.tableoverall);
        if (themeId == R.style.AppTheme_Light) {
            dataSet.setValueLineColor(Color.BLACK);
            chart.setCenterTextColor(Color.BLACK);
            data.setValueTextColor(Color.BLACK);
            leg.setTextColor(Color.BLACK);
            tableLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
        } else{
            dataSet.setValueLineColor(Color.WHITE);
            chart.setCenterTextColor(Color.WHITE);
            data.setValueTextColor(Color.WHITE);
            leg.setTextColor(Color.WHITE);
            tableLayout.setBackgroundResource(android.R.drawable.dialog_holo_dark_frame);
        }
        for (int i = 0; i < 5; i++) {
            View view = layoutInflater.inflate(R.layout.row, tableLayout, false);
            TableRow row = view.findViewById(R.id.rowwwww);
            if (i % 2 == 1)
                row.setBackgroundColor(getResources().getColor(R.color.shade));
            View color = view.findViewById(R.id.color);
            TextView names = view.findViewById(R.id.partynum),
                    seats = view.findViewById(R.id.seatnum);
            ImageView syms = view.findViewById(R.id.imnum);
            names.setText("ab");
            syms.setImageResource(R.drawable.ic_launcher_background);
            seats.setText("5");
            color.setBackgroundColor(legend[i].formColor);
            tableLayout.addView(row);
        }
    }
}
