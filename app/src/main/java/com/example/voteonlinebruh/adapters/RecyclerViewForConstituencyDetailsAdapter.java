package com.example.voteonlinebruh.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class RecyclerViewForConstituencyDetailsAdapter
    extends RecyclerView.Adapter<RecyclerViewForConstituencyDetailsAdapter.ItemViewHolder> {
  private ArrayList<ConstituencyDetailResult> list;
  private ImageView partySym;
  private Context context;
  private boolean tie;

  public RecyclerViewForConstituencyDetailsAdapter(
      ArrayList<ConstituencyDetailResult> list, ImageView partySym, boolean tie, Context context) {
    this.list = list;
    this.partySym = partySym;
    this.context = context;
    this.tie = tie;
  }

  static class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView rank, candName, partyName, votes;
    ImageView candImage, partyImage;
    ProgressBar candProgress, partyProgress;

    ItemViewHolder(@NonNull View itemView) {
      super(itemView);
      candImage = itemView.findViewById(R.id.candImage2);
      partyImage = itemView.findViewById(R.id.partySymbol2);
      rank = itemView.findViewById(R.id.rank);
      candName = itemView.findViewById(R.id.candName2);
      votes = itemView.findViewById(R.id.votes2);
      partyName = itemView.findViewById(R.id.partyName2);
      candProgress = itemView.findViewById(R.id.candImageProgress2);
      partyProgress = itemView.findViewById(R.id.partyImageProgress2);
    }
  }

  @NonNull
  @Override
  public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v =
        LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.const_detail_card, viewGroup, false);
    ItemViewHolder ivh = new ItemViewHolder(v);
    return ivh;
  }

  @Override
  public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int i) {
    final int themeId = ThemeManager.getThemeId();
    if (tie) {
      if (i == 0 | i == 1) itemViewHolder.rank.setText(1 + ".");
      else itemViewHolder.rank.setText((i + 1) + ".");
      partySym.setImageResource(
          themeId == R.style.AppTheme_Light
              ? R.drawable.ic_error_outline_black_24dp
              : R.drawable.ic_error_outline_white_24dp);
      partySym.setPadding(50,50,50,50);
    } else {
      itemViewHolder.rank.setText((i + 1) + ".");
      if (i == 0) Glide.with(context).load(list.get(0).getPartySymbol()).into(partySym);
    }
    itemViewHolder.candName.setText(list.get(i).getName());
    itemViewHolder.candName.setText(list.get(i).getName());
    itemViewHolder.partyName.setText(list.get(i).getPartyName());
    itemViewHolder.votes.setText("Votes : " + list.get(i).getVotes());
    Glide.with(context)
        .load(list.get(i).getImage())
        .error(
            themeId == R.style.AppTheme_Light
                ? R.drawable.ic_error_outline_black_24dp
                : R.drawable.ic_error_outline_white_24dp)
        .listener(
            new RequestListener<Drawable>() {
              @Override
              public boolean onLoadFailed(
                  @Nullable GlideException e,
                  Object model,
                  Target<Drawable> target,
                  boolean isFirstResource) {
                itemViewHolder.candProgress.setVisibility(View.GONE);
                itemViewHolder.candImage.setPadding(40, 80, 40, 80);
                return false;
              }

              @Override
              public boolean onResourceReady(
                  Drawable resource,
                  Object model,
                  Target<Drawable> target,
                  DataSource dataSource,
                  boolean isFirstResource) {
                itemViewHolder.candProgress.setVisibility(View.GONE);
                return false;
              }
            })
        .into(itemViewHolder.candImage);
    Glide.with(context)
        .load(list.get(i).getPartySymbol())
        .error(
            themeId == R.style.AppTheme_Light
                ? R.drawable.ic_error_outline_black_24dp
                : R.drawable.ic_error_outline_white_24dp)
        .listener(
            new RequestListener<Drawable>() {
              @Override
              public boolean onLoadFailed(
                  @Nullable GlideException e,
                  Object model,
                  Target<Drawable> target,
                  boolean isFirstResource) {
                itemViewHolder.partyProgress.setVisibility(View.GONE);
                return false;
              }

              @Override
              public boolean onResourceReady(
                  Drawable resource,
                  Object model,
                  Target<Drawable> target,
                  DataSource dataSource,
                  boolean isFirstResource) {
                itemViewHolder.partyProgress.setVisibility(View.GONE);
                return false;
              }
            })
        .into(itemViewHolder.partyImage);
  }

  @Override
  public int getItemCount() {
    return list.size();
  }
}
