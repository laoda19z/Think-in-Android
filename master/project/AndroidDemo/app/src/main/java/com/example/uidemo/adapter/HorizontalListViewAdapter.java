package com.example.uidemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uidemo.beans.Child;

import java.util.ArrayList;
import java.util.List;
import com.example.uidemo.R;

import static com.example.uidemo.LoginActivity.currentChildAge;
import static com.example.uidemo.LoginActivity.currentChildGrade;
import static com.example.uidemo.LoginActivity.currentChildName;
import static com.example.uidemo.LoginActivity.currentChildId;
import static com.example.uidemo.LoginActivity.currentChildSex;

public class HorizontalListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Child> children = new ArrayList<>();
    private int selectIndex = -1;

    public HorizontalListViewAdapter(Context mContext, List<Child> children) {
        this.mContext = mContext;
        this.children = children;
    }

    @Override
    public int getCount() {
        if(null != children){
            return children.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(null != children){
            return children.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void refresh(List<Child> children) {
        this.children = children;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.horizontal_item_layout,null);

        TextView tvName = convertView.findViewById(R.id.tv_hItem_name);
        Button btnCurrentChild = convertView.findViewById(R.id.btn_hItem_header);
        String name = children.get(position).getChildName();
        Log.e("列表中存在的孩子有",name);

        btnCurrentChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentChildId = children.get(position).getChildId();
                currentChildName = children.get(position).getChildName();
                currentChildAge = children.get(position).getChildAge();
                currentChildGrade = children.get(position).getChildGrade();
                currentChildSex = children.get(position).getChildSex();
                //btnCurrentChild.setBackgroundResource(R.drawable.select);
                Toast.makeText(view.getContext(),"您现在选中的孩子是："+children.get(position).getChildName(),Toast.LENGTH_SHORT).show();
                Log.i("当前孩子是",currentChildName+"，id是："+currentChildId);
            }
        });

        if(position == selectIndex){
            convertView.setSelected(true);
            Log.i("当前已被选中",selectIndex+"");
        }else{
            convertView.setSelected(false);
        }

        //设置item对应的孩子名称
        tvName.setText(name);

        return convertView;
    }

    //设置当前listView的选中item
    public void setSelectIndex(int i){
        selectIndex = i;
    }

}
