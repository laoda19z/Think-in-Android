package com.example.uidemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uidemo.R;
import com.example.uidemo.beans.Data;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter2 extends BaseAdapter {
    private Context context;
    private List<Data> datas=new ArrayList<>();
    private int items;

    public BannerAdapter2(Context context, List<Data> datas, int res){
        this.context=context;
        this.datas=datas;
        this.items=res;
    }
    @Override
    public int getCount() {
        if(null!=datas){
            return datas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(null!=datas){
            return datas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(null==convertView){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(items, null);
            //加载item控件的引用
            holder = new Holder();
            holder.imageView = convertView.findViewById(R.id.img);
            holder.textView=convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        }else{
            holder= (Holder) convertView.getTag();
        }
        holder.imageView.setImageResource(datas.get(position).getImgid());
        holder.textView.setText(datas.get(position).getText());
        return convertView;
    }
    static class Holder{
        private ImageView imageView;
        private TextView textView;
    }
}
