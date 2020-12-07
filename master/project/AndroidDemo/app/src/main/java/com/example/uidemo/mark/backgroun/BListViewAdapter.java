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
import com.example.uidemo.mark.MarkSuccess.BUserMark;

import java.util.List;

public class BListViewAdapter extends BaseAdapter {
    //上下文
    private Context context;
    //参数(传入的是列表中的图片的名字数组)
    private List<ImageEntity> picNameList;
    //布局
    private int layout;
    private Holder holder;
    public BListViewAdapter(Context context, List<ImageEntity> imageEntityList, int layout) {
        this.context = context;
        this.picNameList = imageEntityList;
        Log.e("BLIstView",picNameList.toString());
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview2_item,null);
            //初始化
            holder=new Holder();
            //创建item布局
            holder.imageView=convertView.findViewById(R.id.choiceimg);
            //在ConvertVIew缓存holder对象
            convertView.setTag(holder);
        }else{
            holder= (Holder) convertView.getTag();
        }

        final ImageEntity image=picNameList.get(position);
        //通过名字获得id
        int id = context.getResources().getIdentifier(image.getImagename(),"drawable", context.getPackageName());
        //通过id显示
        Glide.with(context).load(id).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对应的跳转
                //跳转到打卡详细信息界面
                Intent intent=new Intent();
                intent.setClass(context, BUserMark.class);
                //传递的参数为传递过来的图片名
                intent.putExtra("name",image.getImagename());
                intent.putExtra("json",image.getJson());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
