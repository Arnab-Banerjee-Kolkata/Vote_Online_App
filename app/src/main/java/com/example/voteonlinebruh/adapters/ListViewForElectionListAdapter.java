package com.example.voteonlinebruh.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.models.ElectionListItem;

import java.util.ArrayList;

public class ListViewForElectionListAdapter extends ArrayAdapter<ElectionListItem> {
    ArrayList<ElectionListItem> list;
    Context context;

    public ListViewForElectionListAdapter(ArrayList<ElectionListItem> list, Context context) {
        super(context, R.layout.election_card, list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout row = (ConstraintLayout) layoutInflater.inflate(R.layout.election_card, parent, false);
        TextView type, name, year, status;
        ImageView indicator;
        indicator = row.findViewById(R.id.imageView3);
        type = row.findViewById(R.id.electionType2);
        name = row.findViewById(R.id.electionName);
        year = row.findViewById(R.id.electionYear2);
        status = row.findViewById(R.id.textView14);
        type.setText(list.get(position).getType());
        name.setText(list.get(position).getName());
        year.setText(Integer.toString(list.get(position).getYear()));
        int status_code = list.get(position).getStatus();
        switch (status_code) {
            case 0:
                status.setText("Upcoming");
                indicator.setImageResource(R.drawable.upcoming);
                break;
            case 1:
                status.setText("Ongoing");
                indicator.setImageResource(R.drawable.ongoing);
                break;
            case 2:
                status.setText("Pending Result");
                indicator.setImageResource(R.drawable.pend_res);
                break;
            case 3:
                status.setText("Result Published");
                indicator.setImageResource(R.drawable.complete);
                break;
            case 4:
                status.setText("Cancelled");
                indicator.setImageResource(R.drawable.canceld);
                break;
        }
        return row;
    }
}
