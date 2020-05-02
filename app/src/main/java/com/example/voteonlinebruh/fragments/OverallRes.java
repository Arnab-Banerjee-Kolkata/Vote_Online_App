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

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.activities.MainActivity;
import com.github.mikephil.charting.charts.PieChart;
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
    private int rows;

    @Override
    public void setArguments(@Nullable Bundle args) {

        this.name = args.getStringArrayList("NAMES");
        this.sym = args.getIntegerArrayList("SYMS");
        this.seat = args.getStringArrayList("SEATS");
        this.rows = args.getInt("ROWS");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overall_res, container, false);
        int themeId = MainActivity.TM.getThemeId();
        //CHART CODE
        PieChart chart = v.findViewById(R.id.chart);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setCenterTextTypeface(ResourcesCompat.getFont(chart.getContext(), R.font.azo));
        chart.setDrawHoleEnabled(true);
        chart.setRotationEnabled(false);
        chart.setHoleColor(Color.TRANSPARENT);
        chart.setTransparentCircleAlpha(000);
        chart.setHoleRadius(40f);
        chart.setMaxAngle(180f); // HALF CHART
        chart.setRotationAngle(180f);
        chart.setDrawEntryLabels(false);
        chart.setUsePercentValues(false);
        ArrayList<PieEntry> values = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            values.add(new PieEntry(Float.parseFloat((String) seat.get(i)), (String) name.get(i)));
        }
        PieDataSet dataSet = new PieDataSet(values, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(6f);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        PieData data = new PieData(dataSet);
        chart.animateY(1000);
        data.setValueTextSize(15f);
        data.setValueTypeface(ResourcesCompat.getFont(chart.getContext(), R.font.azo));
        data.setValueTextColor(Color.BLACK);
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
        chart.setCenterTextSize(15f);
        chart.setCenterTextOffset(0, -10);
        chart.getLegend().setEnabled(false);
        LegendEntry[] legend = chart.getLegend().getEntries();

        //TABLE CODE
        TableLayout tableLayout = v.findViewById(R.id.table);
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
            //syms.setImageResource((Integer) sym.get(i));
            seats.setText((String) seat.get(i));
            color.setBackgroundColor(legend[i].formColor);
            tableLayout.addView(row);
        }
        if (themeId == R.style.AppTheme_Light) {
            chart.setCenterTextColor(Color.BLACK);
            tableLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
        } else {
            chart.setCenterTextColor(Color.WHITE);
            tableLayout.setBackgroundResource(android.R.drawable.dialog_holo_dark_frame);
        }
        return v;
    }
}
