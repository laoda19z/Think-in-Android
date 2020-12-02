package com.example.uidemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uidemo.R;
import com.example.uidemo.beans.Data;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapterTwo extends RecyclerView.Adapter<BannerAdapterTwo.BannerViewHolder>{
    private Context context;
    private List<Data> datas=new ArrayList<>();
    private int items;

    public BannerAdapterTwo(Context context, List<Data> datas, int res){
        this.context=context;
        this.datas=datas;
        this.items=res;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner2, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        ImageView imageView = holder.imageView;
        TextView textView=holder.textView;
        textView.setText(datas.get(position).getText());
        imageView.setBackgroundResource(datas.get(position).getImgid());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class BannerViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;//轮播条的 item 项
        TextView textView;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            textView=itemView.findViewById(R.id.text1);
        }
    }
}

