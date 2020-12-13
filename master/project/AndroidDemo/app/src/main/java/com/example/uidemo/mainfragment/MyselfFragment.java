package com.example.uidemo.mainfragment;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.adapter.HorizontalListViewAdapter;
import com.example.uidemo.me.AddKidActivity;
import com.example.uidemo.me.CodeActivity;
import com.example.uidemo.me.HorizontalListView;
import com.example.uidemo.me.PersonInfoActivity;
import com.example.uidemo.me.SecurityActivity;
import com.example.uidemo.test.ReportListActivity;

import org.greenrobot.eventbus.EventBus;

import static com.example.uidemo.LoginActivity.currentChildId;
import static com.example.uidemo.LoginActivity.currentUserHead;
import static com.example.uidemo.LoginActivity.currentUserKids;
import static com.example.uidemo.LoginActivity.currentUserName;

public class MyselfFragment extends FragmentActivity {
    private View view;
    private HorizontalListView HListView;
    private HorizontalListViewAdapter HAdapter;
    private Button btnCode;
    private Button btnInfo;
    private Button btnSeCenter;
    private Button btnAddKid;
    private ImageView ivHeader;
    private TextView tvName;
    private Button btnNew;
    private EventBus eventBus;
    private Button btnTest;
    public static int index=-1;
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull final Context context, @NonNull AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.myself_fragment,null);
        eventBus = EventBus.getDefault();
        ivHeader = view.findViewById(R.id.iv_header);
        btnCode = view.findViewById(R.id.btn_code);
        btnAddKid = view.findViewById(R.id.btn_addKid);
        btnInfo= view.findViewById(R.id.btn_info);
        btnSeCenter = view.findViewById(R.id.btn_seCenter);
        tvName = view.findViewById(R.id.tv_name);
        btnNew = view.findViewById(R.id.btn_fl);
        btnTest = view.findViewById(R.id.btn_test);
        Log.e("mll","name"+currentUserName);
        tvName.setText(currentUserName);

        //设置头像
        if(currentUserHead.isEmpty()){
            Glide.with(view.getContext())
                    .load(R.mipmap.tx)
                    .circleCrop()
                    .into(ivHeader);
        }else{
            Glide.with(view.getContext())
                    .load(ConfigUtil.SERVER_ADDR+currentUserHead+"")
                    .circleCrop()
                    .into(ivHeader);
        }

        //二维码按钮
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("me","跳转二维码");
                Intent intent = new Intent();
                intent.setClass(context, CodeActivity.class);
                context.startActivity(intent);
            }
        });

        //详细信息按钮
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("me","跳转个人信息");
                Intent intent = new Intent();
                intent.setClass(context, PersonInfoActivity.class);
                context.startActivity(intent);
            }
        });

        //安全信息按钮
        btnSeCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("me","跳转安全中心");
                Intent intent = new Intent();
                intent.setClass(context, SecurityActivity.class);
                context.startActivity(intent);
            }
        });

        //添加孩子按钮
        btnAddKid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, AddKidActivity.class);
                context.startActivity(intent);
            }
        });

        //我的测评接口
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if (currentChildId == 0){
                    Toast.makeText(context, "请选择孩子", Toast.LENGTH_SHORT).show();
                }else {
                    intent.putExtra("id", currentChildId + "");
                    intent.setClass(context, ReportListActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        //横向listView显示孩子列表
        HListView = view.findViewById(R.id.horizon_listView);
        HAdapter = new HorizontalListViewAdapter(view.getContext(),currentUserKids);
        Log.e("展示横向孩子列表","");
        HListView.setAdapter(HAdapter);
        HListView.setSelection(index);

        if(index!=-1){
            HAdapter.setSelectIndex(index);
            HAdapter.notifyDataSetChanged();
        }
        //设置选择孩子的点击事件
        HListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HAdapter.setSelectIndex(i);
                index=i;
                HAdapter.notifyDataSetChanged();
                eventBus.getDefault().postSticky(MyselfFragment.this);
            }
        });

        //刷新页面按钮
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvName.setText(currentUserName);
                Glide.with(view.getContext())
                        .load(ConfigUtil.SERVER_ADDR+currentUserHead+"")
                        .circleCrop()
                        .into(ivHeader);
                HAdapter.refresh(currentUserKids);
                Toast.makeText(view.getContext(),"页面刷新成功！",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
