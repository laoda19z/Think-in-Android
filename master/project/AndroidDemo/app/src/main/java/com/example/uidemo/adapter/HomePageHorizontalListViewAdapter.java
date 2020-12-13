package com.example.uidemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.uidemo.R;
import com.example.uidemo.beans.Data;

import java.util.List;

public class HomePageHorizontalListViewAdapter extends BaseAdapter {

    private final Context context;
    private final List<Data> datas;
    private final int items;

    public HomePageHorizontalListViewAdapter(Context applicationContext, List<Data> datas, int res) {
        this.context = applicationContext;
        this.datas = datas;
        this.items = res;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(items, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //holder.imageView.setImageResource(datas.get(position).getImgid());
        //holder.imageView.setBackgroundResource(datas.get(position).getImgid());
        holder.textView.setText(datas.get(position).getText());


        //设置图片圆角角度
        RoundedCorners roundedCorners= new RoundedCorners(30);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options=RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(context)
                .load(datas.get(position).getImgid())
                .centerCrop()
                .override(600,300)
                .apply(options)
                .into(holder.imageView);


        return convertView;
    }

    static class ViewHolder {
        private ImageView imageView;
        private TextView textView;
        ViewHolder(View view) {
            imageView=view.findViewById(R.id.img);
            textView=view.findViewById(R.id.text1);
        }
    }
}



