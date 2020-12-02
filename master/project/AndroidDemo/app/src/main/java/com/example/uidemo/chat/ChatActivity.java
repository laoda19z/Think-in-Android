package com.example.uidemo.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uidemo.R;
import com.example.uidemo.adapter.ChatAdapter;
import com.example.uidemo.beans.Chat;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ChatActivity extends AppCompatActivity implements EMMessageListener {
    public static ChatActivity activityInstance;
    //    private EaseChatFragment chatFragment;
// 聊天信息输入框
    private EditText mInputEdit;
    // 发送按钮
    private Button mSendBtn;
    //语音
    private Button sayBtn;
    //显示内容的LinearLayout
    private LinearLayout chatLayout;
    //显示历史记录的LinearLayout
    private LinearLayout historyLayout;
    // 显示内容的 TextView
    private TextView mContentText;
    // 消息监听器
    private EMMessageListener mMessageListener;
    // 当前聊天的 ID
    private String mChatId;
    // 当前会话对象
    private EMConversation mConversation;
    //刷新控件
    private SmartRefreshLayout srl;
    private int historyFlag = 0;//刷新记录
    //定义录制音频的MediaRecorder属性
    private MediaRecorder recorder;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Chat> list = new ArrayList<>();
    /**
     * 自定义实现Handler，主要用于刷新UI操作
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    Log.i("mll", "接收到发送的信息");
                    // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                    // 将新的消息内容和时间加入到下边
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                    String time = dateFormat.format(new Date(message.getMsgTime()));
                    Chat chat = new Chat(body.getMessage(), Chat.TYPE_RECEIVED, time);
                    list.add(chat);
                    chatAdapter.notifyItemInserted(list.size() - 1);
                    recyclerView.scrollToPosition(list.size() - 1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /**
         * 集成EaseUI的界面
         */
        activityInstance = this;
        //这里直接使用EaseUI封装好的聊天界面
//        chatFragment = new EaseChatFragment();
        //将参数传递给聊天界面
//        chatFragment.se
//        chatFragment.setChatFragmentHelper(getIntent().getExtras());
//        chatFragment.setArguments(getIntent().getExtras());
        //加载EaseUI封装的聊天界面Fragment
//        getSupportFragmentManager().beginTransaction().add(R.id.container,chatFragment).commit();

        // 获取当前会话的username(如果是群聊就是群id)
        mChatId = getIntent().getStringExtra("ec_chat_id");
        mMessageListener = this;

        initView();
        initConversation();
        /**
         * 下拉加载历史记录
         */
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //当历史记录没有加载过的时候才加载
                //加载历史记录
                loadHistoryMessage();
                srl.finishRefresh();
            }
        });
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mInputEdit = findViewById(R.id.ec_edit_message_input);
        mSendBtn = findViewById(R.id.ec_btn_send);
        recyclerView = findViewById(R.id.recyler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(this, list);//将集合数据填充到适配器中
        recyclerView.setAdapter(chatAdapter);
        srl = findViewById(R.id.srl);
        // 设置发送按钮的点击事件
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mInputEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    mInputEdit.setText("");
                    // 创建一条新消息，第一个参数为消息内容，第二个为接受者username
                    EMMessage message = EMMessage.createTxtSendMessage(content, mChatId);
                    // 将新的消息内容和时间加入到下边
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                    String time = dateFormat.format(new Date(message.getMsgTime()));
                    Chat chat = new Chat(content, Chat.TYPE_SENT, time);
                    list.add(chat);
                    chatAdapter.notifyItemInserted(list.size() - 1);
                    recyclerView.scrollToPosition(list.size() - 1);
                    // 调用发送消息的方法
                    EMClient.getInstance().chatManager().sendMessage(message);
                    // 为消息设置回调
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            // 消息发送成功
                            Log.i("mll", "发送成功");
                        }

                        @Override
                        public void onError(int i, String s) {
                            // 消息发送失败
                            Log.i("mll", "发送失败错误代码 " + i + " - " + s);
                        }

                        @Override
                        public void onProgress(int i, String s) {
                            // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
                        }
                    });
                }
            }
        });
//        sayBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //初始化MediaRecorder对象
//                recorder = new MediaRecorder();
//                //设置录制音频来源
//                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                //设置音频类型
//                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//                //设置音频编码方式（必须先设置类型（音频格式），后设置编码方式）
//                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//                //设置音频输入路径及音频名称
//                recorder.setOutputFile(getFilesDir()+"/sounds.3gp");
//                //准备录制
//                try {
//                    recorder.prepare();
//                    //开始录制
//                    recorder.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                recorder.stop();
//                recorder.release();
////                long length = recorder.
//                //voiceUri为语音文件本地资源标志符，length为录音时间(秒)
////                EMMessage message = EMMessage.createVoiceSendMessage(getFilesDir()+"/sounds.3gp",, mChatId);
////                EMClient.getInstance().chatManager().sendMessage(message);
//            }
//        });
    }

    /**
     * 加载历史记录
     */
    private void loadHistoryMessage() {
        if (mConversation.getAllMessages().size() > 0 && historyFlag == 0) {
            historyFlag = 1;
            List<EMMessage> messges = mConversation.getAllMessages();
            recyclerView.scrollToPosition(0);
            int i = 0;
            for (EMMessage msg : messges) {
                EMTextMessageBody body1 = (EMTextMessageBody) msg.getBody();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                String time = dateFormat.format(new Date(msg.getMsgTime()));
                Chat chat;
                if (mChatId.equals(msg.getFrom())) {
                    chat = new Chat(body1.getMessage(), Chat.TYPE_RECEIVED, time);
                } else {
                    chat = new Chat(body1.getMessage(), Chat.TYPE_SENT, time);
                }
                list.add(chat);
                chatAdapter.notifyItemInserted(i);
                recyclerView.scrollToPosition(i++);
            }
            recyclerView.scrollToPosition(list.size() - 1);
        }
    }

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    private void initConversation() {

        /**
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是会话类型可以为空
         * 第三个表示如果会话不存在是否创建
         */
        mConversation = EMClient.getInstance().chatManager().getConversation(mChatId, null, true);
        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();
        int count = mConversation.getAllMessages().size();
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            mConversation.loadMoreMsgFromDB(msgId, 20 - count);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }
/**
 * --------------------------------- Message Listener -------------------------------------
 * 环信消息监听主要方法
 */
    /**
     * 收到新消息
     *
     * @param list 收到的新消息集合
     */
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        // 循环遍历当前收到的消息
        for (EMMessage message : list) {
            if (message.getFrom().equals(mChatId)) {
                // 设置消息为已读
                mConversation.markMessageAsRead(message.getMsgId());
                Log.i("mll", "这里是消息监听器");
                // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            } else {
                // TODO 如果消息不是当前会话的消息发送通知栏通知
            }
        }
    }

    /**
     * 收到新的 CMD 消息
     */
    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        for (int i = 0; i < list.size(); i++) {
            // 透传消息
            EMMessage cmdMessage = list.get(i);
            EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
            Log.i("lzan13", "收到 CMD 透传消息" + body.action());
        }
    }

    /**
     * 收到新的已读回执
     *
     * @param list 收到消息已读回执
     */
    @Override
    public void onMessageRead(List<EMMessage> list) {
    }

    /**
     * 收到新的发送回执
     * TODO 无效 暂时有bug
     *
     * @param list 收到发送回执的消息集合
     */
    @Override
    public void onMessageDelivered(List<EMMessage> list) {
    }

    /**
     * 消息撤回回调
     *
     * @param list 撤回的消息列表
     */
    @Override
    public void onMessageRecalled(List<EMMessage> list) {
    }

    /**
     * 消息的状态改变
     *
     * @param message 发生改变的消息
     * @param object  包含改变的消息
     */
    @Override
    public void onMessageChanged(EMMessage message, Object object) {
    }
}