package com.example.uidemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.beans.User;

import java.util.List;

public class ContactAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<User> userList;
    private ViewHolder viewHolder;

    public ContactAdapter(Context context, int layout, List<User> userList) {
        this.context = context;
        this.layout = layout;
        this.userList = userList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(null == view){
            view = LayoutInflater.from(context).inflate(layout,null);
            viewHolder = new ViewHolder();
            viewHolder.ivhead = view.findViewById(R.id.chat_item_contact_head);
            viewHolder.tvUser = view.findViewById(R.id.chat_item_contact_name);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        User user = userList.get(i);
        if(user!=null){
            viewHolder.tvUser.setText(user.getUsername());
            Glide.with(context).load(ConfigUtil.SERVER_ADDR+user.getHeadImg()).circleCrop().into(viewHolder.ivhead);
        }

        return view;
    }
    static class ViewHolder{
        private ImageView ivhead;
        private TextView tvUser;
    }
}
