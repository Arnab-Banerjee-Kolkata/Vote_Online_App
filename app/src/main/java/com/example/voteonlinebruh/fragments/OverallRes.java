package com.example.voteonlinebruh.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.activities.MainActivity;
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

public class OverallRes extends Fragment {

    private ArrayList name, sym, seat;
    private int rows, totalSeats;
    private String type;
    private PieChart chart;
    private ArrayList<PieEntry> values;
    private Legend leg;
    private PieDataSet dataSet;
    private PieData data;
    private View v;
    private TableLayout tableLayout;
    private TextView textView;

    public static OverallRes newInstance(Bundle args) {
        OverallRes overallRes = new OverallRes();
        overallRes.setArguments(args);
        return overallRes;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {

        this.name = args.getStringArrayList("NAMES");
        this.sym = args.getIntegerArrayList("SYMS");
        this.seat = args.getStringArrayList("SEATS");
        this.rows = args.getInt("ROWS");
        this.type = args.getString("type");
        this.totalSeats = args.getInt("totalSeats");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_overall_res, container, false);
        textView = v.findViewById(R.id.textView23);
        int themeId = MainActivity.TM.getThemeId();
        //CHART CODE
        chart = v.findViewById(R.id.chart);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setCenterTextTypeface(ResourcesCompat.getFont(chart.getContext(), R.font.azo));
        chart.setDrawHoleEnabled(true);
        chart.setRotationEnabled(false);
        chart.setHoleColor(Color.TRANSPARENT);
        chart.setTransparentCircleAlpha(0);
        chart.setRotationAngle(180f);
        chart.setDrawEntryLabels(false);
        chart.setUsePercentValues(false);
        values = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            values.add(new PieEntry(Float.parseFloat((String) seat.get(i)), (String) name.get(i)));
        }
        dataSet = new PieDataSet(values, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(6f);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data = new PieData(dataSet);
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
        chart.setCenterText((int) data.getYValueSum() + "/" + totalSeats);
        chart.setCenterTextTypeface(ResourcesCompat.getFont(chart.getContext(), R.font.azo));
        chart.setCenterTextSize(15f);
        leg = chart.getLegend();
        leg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        if (type.equalsIgnoreCase("VIDHAN SABHA")) {
            textView.setPadding(0, 275, 0, 0);
            chart.setHoleRadius(45f);
            chart.setMaxAngle(360f);
            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setValueLinePart1OffsetPercentage(100f);
            dataSet.setValueLinePart1Length(0.6f);
            dataSet.setValueLinePart2Length(0.6f);
            chart.setExtraOffsets(0.f, 5.f, 0.f, 5.f);
            if (themeId == R.style.AppTheme_Light) {
                dataSet.setValueLineColor(Color.BLACK);
                chart.setCenterTextColor(Color.BLACK);
                data.setValueTextColor(Color.BLACK);
            } else {
                dataSet.setValueLineColor(Color.WHITE);
                chart.setCenterTextColor(Color.WHITE);
                data.setValueTextColor(Color.WHITE);
            }
        } else {
            leg.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
            leg.setYOffset(40);
            chart.setHoleRadius(40f);
            chart.setMaxAngle(180f);
            data.setValueTextColor(Color.BLACK);
            chart.setCenterTextOffset(0, -10);
        }
        LegendEntry[] legend = chart.getLegend().getEntries();

        //TABLE CODE
        tableLayout = v.findViewById(R.id.table);
        for (int i = 0; i < rows; i++) {
            View view = inflater.inflate(R.layout.row, container, false);
            TableRow row = view.findViewById(R.id.rowwwww);
            if (i % 2 == 1)
                row.setBackgroundColor(getResources().getColor(R.color.shade));
            View color = view.findViewById(R.id.color);
            TextView names = view.findViewById(R.id.partynum),
                    seats = view.findViewById(R.id.seatnum);
            ImageView syms = view.findViewById(R.id.imnum);
            names.setText((String) name.get(i));
            Glide.with(this)
                    .load(sym.get(i))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(syms);
            seats.setText((String) seat.get(i));
            color.setBackgroundColor(legend[i].formColor);
            tableLayout.addView(row);
        }
        if (themeId == R.style.AppTheme_Light) {
            leg.setTextColor(Color.BLACK);
            tableLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
        } else {
            leg.setTextColor(Color.WHITE);
            tableLayout.setBackgroundResource(android.R.drawable.dialog_holo_dark_frame);
        }
        return v;
    }
}
