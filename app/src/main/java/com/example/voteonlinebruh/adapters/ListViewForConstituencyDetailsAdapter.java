package com.example.voteonlinebruh.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.models.ConstituencyDetailResult;
import com.example.voteonlinebruh.utility.ThemeManager;

import java.util.ArrayList;

public class ListViewForConstituencyDetailsAdapter extends ArrayAdapter<ConstituencyDetailResult> {
  private ArrayList<ConstituencyDetailResult> list;
  private ImageView partySym;
  private Context context;

  public ListViewForConstituencyDetailsAdapter(
      ArrayList<ConstituencyDetailResult> list, ImageView partySym, Context context) {
    super(context, R.layout.const_detail_card, list);
    this.list = list;
    this.partySym = partySym;
    this.context = context;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    LayoutInflater layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    ConstraintLayout row =
        (ConstraintLayout) layoutInflater.inflate(R.layout.const_detail_card, parent, false);
    TextView rank, candName, partyName, votes;
    ImageView candImage, partyImage;
    final ProgressBar candProgress, partyProgress;
    candImage = row.findViewById(R.id.candImage2);
    partyImage = row.findViewById(R.id.partySymbol2);
    rank = row.findViewById(R.id.rank);
    candName = row.findViewById(R.id.candName2);
    votes = row.findViewById(R.id.votes2);
    partyName = row.findViewById(R.id.partyName2);
    candProgress = row.findViewById(R.id.candImageProgress2);
    partyProgress = row.findViewById(R.id.partyImageProgress2);
    candName.setText(list.get(position).getName());
    rank.setText((position + 1) + ".");
    candName.setText(list.get(position).getName());
    partyName.setText(list.get(position).getPartyName());
    votes.setText(Integer.toString(list.get(position).getVotes()));
    final int themeId = ThemeManager.getThemeId();
    Glide.with(context)
        .load(list.get(position).getImage())
        .listener(
            new RequestListener<Drawable>() {
              @Override
              public boolean onLoadFailed(
                  @Nullable GlideException e,
                  Object model,
                  Target<Drawable> target,
                  boolean isFirstResource) {
                candProgress.setVisibility(View.GONE);
                if (themeId == R.style.AppTheme_Light) {
                  target.onLoadFailed(context.getDrawable(R.drawable.ic_error_outline_black_24dp));
                } else {
                  target.onLoadFailed(context.getDrawable(R.drawable.ic_error_outline_white_24dp));
                }
                return false;
              }

              @Override
              public boolean onResourceReady(
                  Drawable resource,
                  Object model,
                  Target<Drawable> target,
                  DataSource dataSource,
                  boolean isFirstResource) {
                candProgress.setVisibility(View.GONE);
                return false;
              }
            })
        .into(candImage);
    Glide.with(context)
        .load(list.get(position).getPartySymbol())
        .listener(
            new RequestListener<Drawable>() {
              @Override
              public boolean onLoadFailed(
                  @Nullable GlideException e,
                  Object model,
                  Target<Drawable> target,
                  boolean isFirstResource) {
                partyProgress.setVisibility(View.GONE);
                if (themeId == R.style.AppTheme_Light) {
                  target.onLoadFailed(context.getDrawable(R.drawable.ic_error_outline_black_24dp));
                } else {
                  target.onLoadFailed(context.getDrawable(R.drawable.ic_error_outline_white_24dp));
                }
                return false;
              }

              @Override
              public boolean onResourceReady(
                  Drawable resource,
                  Object model,
                  Target<Drawable> target,
                  DataSource dataSource,
                  boolean isFirstResource) {
                partyProgress.setVisibility(View.GONE);
                return false;
              }
            })
        .into(partyImage);
    if (position == 0) Glide.with(context).load(list.get(0).getPartySymbol()).into(partySym);
    return row;
  }
}
