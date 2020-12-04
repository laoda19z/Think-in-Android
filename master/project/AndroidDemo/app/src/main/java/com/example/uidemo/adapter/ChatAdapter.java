package com.example.uidemo.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uidemo.ConfigUtil;
import com.example.uidemo.LoginActivity;
import com.example.uidemo.R;
import com.example.uidemo.beans.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{
    //    上下文
    private Context context;
    private String receiverHead;
    //    对话列表
    private List<Chat> mlist;//创建集合mList，用来存储聊天数据

    //构造函数，因为在实例化一个类的时候 会执行构造函数里面的代码
    public ChatAdapter(Context context, List<Chat> list) {
        this.context = context;
        this.mlist = list;
    }

    public void setReceiverHead(String receiverHead) {
        this.receiverHead = receiverHead;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//加载基本布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//这里主要用于数据、事件绑定
        Chat chat = mlist.get(position);
        if (chat.getType() == Chat.TYPE_RECEIVED) {
//           如果收的的数据是左边，就显示左边的消息布局，将右边的消息布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            Glide.with(context).load(ConfigUtil.SERVER_ADDR+receiverHead).circleCrop().into(holder.chatReceiver);
            holder.leftChat.setText(chat.getText());//接收到的文本信息
            if (!"".equals(chat.getTime())){
                holder.Time.setVisibility(View .VISIBLE);
                holder.Time.setText(chat.getTime());//接收到消息时的时间
            }else {
                holder.Time.setVisibility(View .GONE);
            }

//
        } else if (chat.getType() == chat.TYPE_SENT) {
//           如果发出的消息是右边，就显示右边的消息布局，将左边的消息布局隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            Glide.with(context).load(ConfigUtil.SERVER_ADDR+ LoginActivity.currentUserHead).circleCrop().into(holder.chatPublisher);
            holder.rightChat.setText(chat.getText());//发送的文本信息
            if (!"".equals(chat.getTime())){
                holder.Time.setVisibility(View.VISIBLE);
                holder.Time.setText(chat.getTime());//发送消息的时间
            }else {
                holder.Time.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftChat;
        TextView rightChat;
        TextView Time;
        ImageView chatReceiver;
        ImageView chatPublisher;

        public ViewHolder(View itemView) {
            super(itemView);
            leftLayout = itemView.findViewById(R.id.left_layout);
            rightLayout = itemView.findViewById(R.id.right_layout);
            leftChat = itemView.findViewById(R.id.tv_left_text);
            rightChat = itemView.findViewById(R.id.tv_right_text);
            Time=itemView.findViewById(R.id.timer);//消息的时间
            chatPublisher = itemView.findViewById(R.id.chat_item_publisher);
            chatReceiver = itemView.findViewById(R.id.chat_item_receiver);
        }
    }
}
