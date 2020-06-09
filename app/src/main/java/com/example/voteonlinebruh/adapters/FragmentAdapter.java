package com.example.voteonlinebruh.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.voteonlinebruh.fragments.ConstRes;
import com.example.voteonlinebruh.fragments.OverallRes;

public class FragmentAdapter extends FragmentPagerAdapter {

  private Context context;
  private Bundle args, args2;
  private Fragment firstFrag, secondFrag;

  public FragmentAdapter(Context context, FragmentManager fm, Bundle args, Bundle args2) {
    super(fm);
    this.context = context;
    this.args = args;
    this.args2 = args2;
  }

  @Override
  public Fragment getItem(int i) {
    if (i == 0) {
      firstFrag = OverallRes.newInstance(args);
      firstFrag.onResume();
      return firstFrag;
    } else {
      secondFrag = ConstRes.newInstance(args2);
      return secondFrag;
    }
  }

  @Override
  public int getCount() {
    return 2;
  }
}
