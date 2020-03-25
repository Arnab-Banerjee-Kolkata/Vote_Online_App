package com.example.voteonlinebruh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.itemViewHolder> {
    private ArrayList<Item> itemArrayList;
    private OnItemClickListener mListener;
    private Context context;
    private ImageLoader imageLoader;

    public itemAdapter(ArrayList<Item> item_list, Context context) {
        itemArrayList=item_list;
        this.context=context;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public  void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public static class itemViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public ImageView indicator;
        public itemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.party_sym);
            textView1=itemView.findViewById(R.id.party_name);
            textView2=itemView.findViewById(R.id.cand_name);
            indicator=itemView.findViewById(R.id.selector);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION)
                            listener.onItemClick(position);
                    }
                }
            });
        }
    }
    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.voing_info_card,viewGroup,false);
        itemViewHolder ivh=new itemViewHolder(v,mListener);
        return  ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder itemViewHolder, int i) {
        Item currentItem=itemArrayList.get(i);
        String resUrl = context.getString(R.string.web_host)+currentItem.getImgUrl();

        if(resUrl.indexOf("https://")==0)
        {

            Glide
                    .with(itemViewHolder.imageView.getContext())
                    .load(resUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(itemViewHolder.imageView);
        }
        else
        {
            //resUrl="http://remote-voting.rf.gd/PartySymbols/resize.jpg";
            //new DownloadImageTask(itemViewHolder.imageView).execute(resUrl);

//            Picasso.with(context)
//                    .load(resUrl)
//                    .resize(itemViewHolder.imageView.getWidth(), itemViewHolder.imageView.getHeight()).into(itemViewHolder.imageView);
//
        }


        itemViewHolder.textView1.setText(currentItem.getPname());
        itemViewHolder.textView2.setText(currentItem.getCname());
        int resid = currentItem.getIndicator();
        Glide
                .with(context)
                .load(resid).into(itemViewHolder.indicator);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}