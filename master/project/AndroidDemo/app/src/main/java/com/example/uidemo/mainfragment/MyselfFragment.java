package com.example.uidemo.mainfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.uidemo.R;
import com.example.uidemo.adapter.HorizontalListViewAdapter;
import com.example.uidemo.beans.Child;
import com.example.uidemo.me.CodeActivity;
import com.example.uidemo.me.HorizontalListView;
import com.example.uidemo.me.PersonInfoActivity;
import com.example.uidemo.me.SecurityActivity;

import java.util.ArrayList;
import java.util.List;

public class MyselfFragment extends FragmentActivity {
    private View view;
    private HorizontalListView HListView;
    private HorizontalListViewAdapter HAdapter;
    private Button btnCode;
    private Button btnInfo;
    private Button btnSeCenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull final Context context, @NonNull AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.myself_fragment,null);
        ImageView ivHeader = view.findViewById(R.id.iv_header);
        btnCode = view.findViewById(R.id.btn_code);
        //Button btnAddKid = findViewById(R.id.btn_addKid);
        btnInfo= view.findViewById(R.id.btn_info);
        btnSeCenter = view.findViewById(R.id.btn_seCenter);


        Glide.with(view.getContext())
                .load(R.drawable.girl)
                .circleCrop()
                .into(ivHeader);



        //连接服务端，接收遍历出来的child信息，并显示在宝宝列表中
        List<Child> children = new ArrayList<>();
        Child c1 = new Child();
        c1.setChildName("小鸟游六花");
        children.add(c1);
        Child c2 = new Child();
        c2.setChildName("小天");
        children.add(c2);

        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("mll","二维码");
                Intent intent = new Intent();
                intent.setClass(context, CodeActivity.class);
                context.startActivity(intent);
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("mll","个人信息");
                Intent intent = new Intent();
                intent.setClass(context, PersonInfoActivity.class);
                context.startActivity(intent);
            }
        });

        btnSeCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("mll","安全中心");
                Intent intent = new Intent();
                intent.setClass(context, SecurityActivity.class);
                context.startActivity(intent);
            }
        });
        HAdapter = new HorizontalListViewAdapter(view.getContext(),children);
        Log.e("ok","zzz");
        HListView = view.findViewById(R.id.horizon_listview);
        HListView.setAdapter(HAdapter);
        return view;
    }


}
