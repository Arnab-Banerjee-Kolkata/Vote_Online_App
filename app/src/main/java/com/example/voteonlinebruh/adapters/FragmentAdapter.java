package com.example.voteonlinebruh.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.voteonlinebruh.fragments.ConstRes;
import com.example.voteonlinebruh.fragments.OverallRes;

@SuppressWarnings("deprecation")
public class FragmentAdapter extends FragmentPagerAdapter {

  private final Bundle args;
  private final Bundle args2;

  public FragmentAdapter(FragmentManager fm, Bundle args, Bundle args2) {
    super(fm);
    this.args = args;
    this.args2 = args2;
  }

  @NonNull
  @Override
  public Fragment getItem(int i) {
    if (i == 0) {
      Fragment firstFrag = OverallRes.newInstance(args);
      firstFrag.onResume();
      return firstFrag;
    } else {
      return ConstRes.newInstance(args2);
    }
  }

  @Override
  public int getCount() {
    return 2;
  }
}
