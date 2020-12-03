package com.example.uidemo.mark.backgroun;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.uidemo.R;
import com.example.uidemo.mark.Entity.ImageEntity;

import java.util.List;

public class AListViewAdapter extends BaseAdapter {
    //上下文
    private Context context;
    //参数(传入的是列表中的图片的名字数组)
    private List<ImageEntity> picNameList;
    //布局
    private int layout;
    private Holder holder;
    public AListViewAdapter(Context context, List<ImageEntity> imageEntityList, int layout) {
        this.context = context;
        this.picNameList = imageEntityList;
        Log.e("picNameList",picNameList.size()+"");
        this.layout = layout;
    }

    class Holder{
        private ImageView imageView;
    }

    @Override
    public int getCount() {
        return picNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return picNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            //加载布局文件复制给convertView
            convertView= LayoutInflater.from(context).inflate(R.layout.listview1_item,null);
            //初始化
            holder=new Holder();
            //创建item布局
            holder.imageView=convertView.findViewById(R.id.typeimage);
            //在ConvertVIew缓存holder对象
            convertView.setTag(holder);
        }else{
            holder= (Holder) convertView.getTag();
        }

        final ImageEntity image=picNameList.get(position);
        Log.e("ALISTVIEW",image.getImagename());
        //获得id
        Log.e("AlistView",position+"");
        int id = context.getResources().getIdentifier(image.getImagename(),"drawable", context.getPackageName());
        Log.e("AlistViewAdapter页面",id+"");
        Glide.with(context).load(id).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对应的跳转
                //跳转到对应的页面，例如早安卡展示页面
                Intent intent=new Intent();
                intent.setClass(context,Test.class);
                //传递的参数为传递过来的图片名
                intent.putExtra("name",image.getImagename());
                intent.putExtra("json",image.getJson());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
