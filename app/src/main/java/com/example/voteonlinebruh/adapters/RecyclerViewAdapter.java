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
import com.example.voteonlinebruh.models.RecyclerViewItem;
import com.example.voteonlinebruh.utility.ThemeManager;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.itemViewHolder> {
  private final ArrayList<RecyclerViewItem> recyclerViewItemArrayList;
  private OnItemClickListener mListener;
  private final Context context;

  public RecyclerViewAdapter(ArrayList<RecyclerViewItem> recyclerViewItem_list, Context context) {
    recyclerViewItemArrayList = recyclerViewItem_list;
    this.context = context;
  }

  public interface OnItemClickListener {
    void onItemClick(int position);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    mListener = listener;
  }

  static class itemViewHolder extends RecyclerView.ViewHolder {
    final ImageView imageView;
    final TextView textView1;
    final TextView textView2;
    final ImageView indicator;
    final ImageView candImage;
    final ProgressBar progress1;
      final ProgressBar progress2;

    itemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
      super(itemView);
      imageView = itemView.findViewById(R.id.party_sym);
      candImage = itemView.findViewById(R.id.cand_image);
      textView1 = itemView.findViewById(R.id.party_name);
      textView2 = itemView.findViewById(R.id.cand_name);
      indicator = itemView.findViewById(R.id.indicator);
      progress1 = itemView.findViewById(R.id.votingImageProgress);
      progress2 = itemView.findViewById(R.id.votingImageProgress2);
      itemView.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) listener.onItemClick(position);
              }
            }
          });
    }
  }

  @NonNull
  @Override
  public itemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v =
        LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.voting_info_card, viewGroup, false);
      return new itemViewHolder(v, mListener);
  }

  @Override
  public void onBindViewHolder(@NonNull final itemViewHolder itemViewHolder, int i) {
    RecyclerViewItem currentRecyclerViewItem = recyclerViewItemArrayList.get(i);
    String resUrl = currentRecyclerViewItem.getSymbol();
    final int themeId = ThemeManager.getThemeId();
    Glide.with(itemViewHolder.imageView.getContext())
        .load(resUrl)
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
                itemViewHolder.progress1.setVisibility(View.GONE);
                itemViewHolder.imageView.setPadding(25, 25, 25, 25);
                return false;
              }

              @Override
              public boolean onResourceReady(
                  Drawable resource,
                  Object model,
                  Target<Drawable> target,
                  DataSource dataSource,
                  boolean isFirstResource) {
                itemViewHolder.progress1.setVisibility(View.GONE);
                return false;
              }
            })
        .into(itemViewHolder.imageView);
    resUrl = currentRecyclerViewItem.getImage();
    Glide.with(itemViewHolder.imageView.getContext())
        .load(resUrl)
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
                itemViewHolder.progress2.setVisibility(View.GONE);
                itemViewHolder.candImage.setPadding(25, 25, 25, 25);
                return false;
              }

              @Override
              public boolean onResourceReady(
                  Drawable resource,
                  Object model,
                  Target<Drawable> target,
                  DataSource dataSource,
                  boolean isFirstResource) {
                itemViewHolder.progress2.setVisibility(View.GONE);
                return false;
              }
            })
        .into(itemViewHolder.candImage);
    itemViewHolder.textView1.setText(currentRecyclerViewItem.getPartyName());
    itemViewHolder.textView2.setText(currentRecyclerViewItem.getCandidateName());
    int resid = currentRecyclerViewItem.getIndicator();
    Glide.with(context).load(resid).into(itemViewHolder.indicator);
  }

  @Override
  public int getItemCount() {
    return recyclerViewItemArrayList.size();
  }
}
