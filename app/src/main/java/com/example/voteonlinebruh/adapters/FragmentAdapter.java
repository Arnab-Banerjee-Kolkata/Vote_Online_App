package com.example.voteonlinebruh.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.voteonlinebruh.fragments.OverallRes;
import com.example.voteonlinebruh.fragments.ConstRes;

public class FragmentAdapter extends FragmentPagerAdapter {

    private Context context;
    private Bundle args,args2;

    public FragmentAdapter(Context context, FragmentManager fm, Bundle args, Bundle args2) {
        super(fm);
        this.context=context;
        this.args=args;
        this.args2 = args2;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            OverallRes overallRes = new OverallRes();
            overallRes.setArguments(args);
            return overallRes;
        } else {
            ConstRes constRes = new ConstRes();
            constRes.setArguments(args2);
            return constRes;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
