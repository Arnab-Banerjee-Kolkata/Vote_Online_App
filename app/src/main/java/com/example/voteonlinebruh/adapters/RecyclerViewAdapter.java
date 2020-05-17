package com.example.voteonlinebruh.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.voteonlinebruh.activities.MainActivity;
import com.example.voteonlinebruh.models.RecyclerViewItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.InputStream;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.itemViewHolder> {
    private ArrayList<RecyclerViewItem> recyclerViewItemArrayList;
    private OnItemClickListener mListener;
    private Context context;
    private ImageLoader imageLoader;

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
        ImageView imageView;
        TextView textView1;
        TextView textView2;
        ImageView indicator;
        ProgressBar progress;

        itemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.party_sym);
            textView1 = itemView.findViewById(R.id.party_name);
            textView2 = itemView.findViewById(R.id.cand_name);
            indicator = itemView.findViewById(R.id.selector);
            progress=itemView.findViewById(R.id.votingImageProgress);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onItemClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.voing_info_card, viewGroup, false);
        itemViewHolder ivh = new itemViewHolder(v, mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull final itemViewHolder itemViewHolder, int i) {
        RecyclerViewItem currentRecyclerViewItem = recyclerViewItemArrayList.get(i);
        String resUrl = currentRecyclerViewItem.getImgUrl();
        final int themeId= MainActivity.TM.getThemeId();
        Glide
                .with(itemViewHolder.imageView.getContext())
                .load(resUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        itemViewHolder.progress.setVisibility(View.GONE);
                        if (themeId == R.style.AppTheme_Light) {
                            target.onLoadFailed(context.getDrawable(R.drawable.ic_error_outline_black_24dp));
                        } else {
                            target.onLoadFailed(context.getDrawable(R.drawable.ic_error_outline_white_24dp));
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        itemViewHolder.progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(itemViewHolder.imageView);

        itemViewHolder.textView1.setText(currentRecyclerViewItem.getPname());
        itemViewHolder.textView2.setText(currentRecyclerViewItem.getCname());
        int resid = currentRecyclerViewItem.getIndicator();
        Glide
                .with(context)
                .load(resid).into(itemViewHolder.indicator);
    }

    @Override
    public int getItemCount() {
        return recyclerViewItemArrayList.size();
    }
}
