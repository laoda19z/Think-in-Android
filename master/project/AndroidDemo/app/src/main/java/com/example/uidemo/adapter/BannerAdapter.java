package com.example.uidemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uidemo.R;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder>{

    private int[] urls;//图片地址
    private Context context;
    public BannerAdapter(Context context, int[] urls){
        this.urls = urls;
        this.context = context;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        ImageView imageView = holder.imageView;
        //XUI 的图片加载器
        //ImageLoader.get().loadImage(imageView, urls[position]);
        imageView.setImageResource(urls[position]);
    }

    @Override
    public int getItemCount() {
        return urls.length;
    }

    class BannerViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;//轮播条的 item 项
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_item);
        }
    }
}

