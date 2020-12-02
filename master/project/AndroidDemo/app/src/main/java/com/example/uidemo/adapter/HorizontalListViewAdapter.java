package com.example.uidemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.uidemo.beans.Child;


import java.util.ArrayList;
import java.util.List;
import com.example.uidemo.R;
public class HorizontalListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Child> children = new ArrayList<>();

    public HorizontalListViewAdapter(Context mContext, List<Child> children) {
        this.mContext = mContext;
        this.children = children;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.horizontal_item_layout,null);

        TextView tvName = convertView.findViewById(R.id.tv_hItem_name);
        String name = children.get(position).getChildName();
        Log.e("text",name);

        tvName.setText(name);

        return convertView;
    }
}
