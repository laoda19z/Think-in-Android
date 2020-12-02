package com.example.uidemo.familyactivity.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.uidemo.R;
import com.example.uidemo.familyactivity.activities.DetailsActivity;
import com.example.uidemo.familyactivity.beans.ParentChildActivities;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ActivitiesAdapter extends BaseAdapter {
    private List<ParentChildActivities> activities;
    private LayoutInflater inflater;
    private int flag = 0;

    public ActivitiesAdapter(Context mContext, List<ParentChildActivities> activities){
        this.activities = activities;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return activities.size();
    }

    @Override
    public Object getItem(int position) {
        return activities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater
                    .inflate(R.layout.item_parentchildlist, parent, false);
        }

        ImageView parentChildIv = ViewHolder.get(convertView, R.id.img_parentChild);
        TextView nameTv = ViewHolder.get(convertView, R.id.tv_name);
        final ImageView heartIv = ViewHolder.get(convertView,R.id.iv_heart);
        TextView contextTimeTv = ViewHolder.get(convertView,R.id.tv_contextTime);
        TextView contextDistrictTv = ViewHolder.get(convertView,R.id.tv_context);


        ParentChildActivities parentChildActivities = activities.get(position);
        parentChildIv.setImageBitmap(parentChildActivities.getActivityImg());
        nameTv.setText(parentChildActivities.getActivityName());
        contextTimeTv.setText(parentChildActivities.getActivityTime());
        contextDistrictTv.setText(parentChildActivities.getActivityDistrict());

        heartIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == 0){
                    heartIv.setImageResource(R.drawable.heartafter);
                    flag = 1;
                }else{
                    heartIv.setImageResource(R.drawable.heartbefore);
                    flag = 0;
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                //添加数据传到DetilsActivity
                Bundle bundle = new Bundle();
                //!!!用bundle实现传double数据
                bundle.putString("name",activities.get(position).getActivityName());
                bundle.putString ("time",activities.get(position).getActivityTime());
                bundle.putString("district",activities.get(position).getActivityDistrict());
                bundle.putString("content",activities.get(position).getActivityContent());
                //将Bitmap写进字节流，传过去一个字节流，再转为Bitmap
                Bitmap imgBp = activities.get(position).getActivityImg();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imgBp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes=baos.toByteArray();
                bundle.putByteArray("bitmapImg", bytes);

                intent.putExtras(bundle);

                v.getContext().startActivity(intent);
            }
        });

        return convertView;

    }
}
