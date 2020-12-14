package com.example.uidemo.familyactivity.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.familyactivity.activities.DetailsActivity;
import com.example.uidemo.familyactivity.beans.ParentChildActivities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ActivitiesAdapter extends BaseAdapter {
    private List<ParentChildActivities> activities;
    private LayoutInflater inflater;
    private int flag = 0;
    private String info;

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
                    info = String.valueOf(activities.get(position).getActivityId());
                    //传一个ID到服务端
                    reToServer();
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

    private void reToServer() {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(ConfigUtil.SERVER_ADDR + "/MyLovesInsertServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = null;
                    out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(out, "utf-8"));

                    writer.write(info);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    Log.i("result",result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
