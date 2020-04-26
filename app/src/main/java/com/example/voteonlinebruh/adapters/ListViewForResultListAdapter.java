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
import com.example.voteonlinebruh.models.ResultListItem;

import java.util.ArrayList;

public class ListViewForResultListAdapter extends ArrayAdapter<ResultListItem> {
    ArrayList<ResultListItem> list;
    Context context;

    public ListViewForResultListAdapter(ArrayList<ResultListItem> list, Context context) {
        super(context, R.layout.res_list_card, list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout row = (ConstraintLayout) layoutInflater.inflate(R.layout.res_list_card, parent, false);
        TextView type, name, status;
        ImageView indicator;
        indicator = row.findViewById(R.id.imageView5);
        type = row.findViewById(R.id.electionType);
        name = row.findViewById(R.id.textView16);
        status = row.findViewById(R.id.textView17);
        name.setText(list.get(position).getName());
        type.setText(list.get(position).getType());
        int status_code = list.get(position).getStatus();
        switch (status_code) {
            case 2:
                status.setText("Partial Result");
                indicator.setImageResource(R.drawable.pend_res);
                break;
            case 3:
                status.setText("Published");
                indicator.setImageResource(R.drawable.complete);
                break;
        }
        return row;
    }
}
