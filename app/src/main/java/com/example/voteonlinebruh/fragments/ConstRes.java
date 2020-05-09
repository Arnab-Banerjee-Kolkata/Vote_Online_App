package com.example.voteonlinebruh.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.utility.CaseConverter;

import java.util.ArrayList;

public class ConstRes extends Fragment {

    private ArrayList cons_name, cand_name, par_name, votes;
    private String state_name;
    private int rows;

    public static ConstRes newInstance(Bundle args){
        ConstRes constRes=new ConstRes();
        constRes.setArguments(args);
        return constRes;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {

        this.cons_name = args.getStringArrayList("CON_NAME");
        this.state_name = args.getString("STATE_NAME");
        this.cand_name = args.getStringArrayList("CAND_NAME");
        this.par_name = args.getStringArrayList("PAR_NAME");
        this.votes = args.getStringArrayList("VOTES");
        this.rows = args.getInt("ROWS");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_const_res, container, false);
        ListView listView = view.findViewById(R.id.list3);
        class MyAdapter extends ArrayAdapter<ArrayList> {
            ArrayList list;
            Context context;

            MyAdapter(ArrayList list, Context context) {
                super(context, R.layout.const_list_card, list);
                this.list = list;
                this.context = context;
            }

            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                CardView v = (CardView) layoutInflater.inflate(R.layout.const_list_card, parent, false);
                TextView con = v.findViewById(R.id.conName),
                        sta = v.findViewById(R.id.stateName),
                        can = v.findViewById(R.id.candName),
                        par = v.findViewById(R.id.partyName),
                        vot = v.findViewById(R.id.votes);
                con.setText((String) cons_name.get(position));
                sta.setText(state_name);
                can.setText((String) cand_name.get(position));
                par.setText((String) par_name.get(position));
                vot.setText("Votes : " + (String) votes.get(position));
                return v;
            }
        }
        final MyAdapter arrayAdapter = new MyAdapter(cons_name, view.getContext());
        listView.setAdapter(arrayAdapter);
        return view;
    }
}
