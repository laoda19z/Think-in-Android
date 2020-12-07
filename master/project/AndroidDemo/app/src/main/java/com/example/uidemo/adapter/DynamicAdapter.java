package com.example.uidemo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.beans.Comment;
import com.example.uidemo.beans.Dynamic;
import com.example.uidemo.beans.User;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DynamicAdapter extends BaseAdapter {
    private Context context;//上下文环境
    private List<Dynamic> lists;
    private int layout;
    private OkHttpClient okHttpClient;
    private Handler handler;
    private User user = new User();
    private ViewHolder holder;
    private List<User> users = new ArrayList<>();
    private Dynamic currentdynamic;
    public DynamicAdapter(Context context, List<Dynamic> lists, int layout) {
        this.context = context;
        this.lists = lists;
        this.layout = layout;
    }
    public void setDynamic(List<Dynamic> lists){
        this.lists = lists;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        currentdynamic = lists.get(i);
        okHttpClient = new OkHttpClient();
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
//                        holder.tv_name.setText(user.getUsername());
//                        Log.e("mll",ConfigUtil.SERVER_ADDR+user.getHeadImg());
//                        Glide.with(context).load(ConfigUtil.SERVER_ADDR+user.getHeadImg()).override(50, 50).circleCrop().into(holder.iv_head);
                        break;
                    case 2:
                        Log.e("mll","OK");
                        break;
                }
            }
        };
        if (null == view){
            view = LayoutInflater.from(context).inflate(layout,null);
            holder = new ViewHolder();
            holder.iv_head = view.findViewById(R.id.trend_item_head);
            holder.iv_img = view.findViewById(R.id.trend_item_img);
            holder.trend_comment_list = view.findViewById(R.id.trend_comment_list);
            holder.tv_name = view.findViewById(R.id.trend_item_user);
            holder.tv_time = view.findViewById(R.id.trend_item_time);
            holder.tv_content = view.findViewById(R.id.trend_item_content);
            holder.tv_clickToComment = view.findViewById(R.id.btn_input_comment);
            holder.tv_position = view.findViewById(R.id.trend_item_position);
            holder.tv_clickToComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //让弹出框位于底部
                    final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.setContentView(R.layout.trends_input_comment);
                    dialog.findViewById(R.id.input_comment_container).getLocationOnScreen(new int[2]);
                    dialog.show();
                    dialog.findViewById(R.id.input_comment_dialog_container).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    //获取输入框控件
                    final TextView tvComm = dialog.findViewById(R.id.input_comment);
                    //点击发表
                    final TextView btn = (TextView) dialog.findViewById(R.id.btn_publish_comment);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(tvComm.getText().toString()!="" && tvComm.getText().toString()!=null){
                                TextView textView = new TextView(context);
                                Comment comment = new Comment();
                                comment.setContent(tvComm.getText().toString());
                                comment.setDynamicId(currentdynamic.getDynamicId());
                                comment.setReceiverId(currentdynamic.getUserId());
                                comment.setPublisherId(2);
                                Gson gson = new Gson();
                                String requComm = gson.toJson(comment);
                                sendComment(requComm);
//                                textView.setText(comment.getPublisherId()+"发布了"+comment.getContent()+"接收者为"+comment.getReceiverId());
                                textView.setText("当前用户："+tvComm.getText().toString());
                                holder.trend_comment_list.addView(textView);
                                notifyDataSetChanged();
                            }
                            dialog.dismiss();
                        }
                    });
                }
            });
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        for(int j = 0;j < users.size();++j){
            if(users.get(j).getUserId() == currentdynamic.getUserId()){
                user = users.get(j);
            }
        }
        List<Comment> list = currentdynamic.getComment();
        holder.trend_comment_list.removeAllViews();
        for(int j = 0;j < list.size();++j){
            Comment comment = list.get(j);
            TextView textView = new TextView(context);
            textView.setText(comment.getPublisherId()+"："+comment.getContent());
            holder.trend_comment_list.addView(textView);
            notifyDataSetChanged();
        }
        Log.e("mll","当前时间为："+currentdynamic.getTime().toString());
        SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.tv_time.setText(fo.format(currentdynamic.getTime()));
        holder.tv_name.setText(user.getUsername());
        Glide.with(context).load(ConfigUtil.SERVER_ADDR+user.getHeadImg()).override(50, 50).circleCrop().into(holder.iv_head);
        holder.tv_content.setText(currentdynamic.getContent());
        String position = currentdynamic.getLocation();
        holder.tv_position.setText(position);
        if(!currentdynamic.getImg().equals("dynamics/null")){
            holder.iv_img.setVisibility(View.VISIBLE);
            Glide.with(context).load(ConfigUtil.SERVER_ADDR+currentdynamic.getImg()).override(600, 400).into(holder.iv_img);
        }


        return view;
    }

    /**
     * 发送评论
     */
    private void sendComment(String str) {
        //创建Request对象
        //获取待传输数据的MIME类型
        MediaType type = MediaType.parse("text/plain;charset=utf-8");
        //创建RequestBody对象
        RequestBody requestBody = RequestBody.create(str,type);
        Request request = new Request.Builder().url(ConfigUtil.SERVER_ADDR+"AddCommentServlet").post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //如果是同步的话会导致线程阻塞接收响应，异步则不会产生线程阻塞，而是当响应返回时调用两个回调方法
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("mll","请求失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string();
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        });
    }

    static class ViewHolder {
        private ImageView iv_head;//用户头像
        private TextView tv_name;//用户名
        private TextView tv_content;//内容
        private TextView tv_time;//发布时间
        private ImageView iv_img;//图片
        private LinearLayout trend_comment_list;//评论列表
        private Button tv_clickToComment;
        private TextView tv_position;

    }
}
