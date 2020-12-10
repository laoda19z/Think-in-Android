package com.example.uidemo.mark.calendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.chat.AddContactActivity;
import com.example.uidemo.mark.Entity.MarkPicEntity;
import com.example.uidemo.mark.Entity.ReturnMarkPic;
import com.google.gson.Gson;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;

public class PictureInfo extends AppCompatActivity {
    private ImageView ivmark;
    //查询数据
    private String username;
    private String date;
    private int child;
    //
    private Bitmap bitmap;
    //
    private Button btnPicBack;
    //获得需要输入的控件名称
    private TextView typename;
    private TextView dateformat;
    private TextView sporttime;
    private TextView impression;
    //
    private String type;
    private int time;
    private String stringimpression;
    private String testImgpath;
    //两图片
    private ImageView backmark;
    private ImageView shared;

    //分享
    private static final String TAG = "AddContactActivity";
    private static final String APP_ID = "1111276030";//官方获取的APPID
    private Tencent mTencent;
    private ShareUiListener mIUiListener;
    private Bundle params;
    private String imgPath;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1://表示数据完成
                    //图片显示在图片控件中
                    Glide.with(getApplicationContext()).load(new File(msg.obj + "")).into(ivmark);
                    typename.setText(type);
                    dateformat.setText(date);
                    sporttime.setText(time + "分钟");
                    if (!stringimpression.equals("nulls")) {
                        impression.setText(stringimpression);
                    } else {
                        impression.setText("当天未填写感想");
                    }
                    shared.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //分享的方法:
                            if(imgPath!=""){
                                sharedWitdQQ(imgPath);
//                                sharedWitdQQ("/data/user/0/com.example.uidemo/files/imgs/scene2.jpg");
                            }

                        }
                    });
                    break;
                case 2:
                    typename.setText("当天没有运动");
                    dateformat.setText("未打卡");
                    sporttime.setText("当前运动0分钟，今天努力吧!");
                    impression.setText("当天未填写感想");
                    Glide.with(getApplicationContext()).load(R.drawable.nomark).into(ivmark);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mark_picture_info);

        ivmark = findViewById(R.id.markpic);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        date = intent.getStringExtra("date");
        child = Integer.parseInt(intent.getStringExtra("child"));
        btnPicBack = findViewById(R.id.markPicBack);

        typename = findViewById(R.id.tv_typename);
        dateformat = findViewById(R.id.tv_dateformat);
        sporttime = findViewById(R.id.tv_sporttime);
        impression = findViewById(R.id.tv_impression_total);
        shared=findViewById(R.id.shared);
        backmark=findViewById(R.id.backmark);

        //先将ImageView加载gif图
        Glide.with(this).asGif().load(R.mipmap.loading).into(ivmark);
        typename.setText("加载中");
        dateformat.setText("加载中");
        sporttime.setText("加载中");
        impression.setText("加载中");

        //通过Intent传递过来的username和孩子id和日期，向服务器端查询图片，并显示
        new Thread() {
            @Override
            public void run() {
                try {
                    URL picpath = new URL(ConfigUtil.SERVER_ADDR + "markpic");
                    HttpURLConnection conn = (HttpURLConnection) picpath.openConnection();
                    //设置网络请求的方式为POST
                    conn.setRequestMethod("POST");
                    OutputStream out = conn.getOutputStream();
                    //将需要传输的数据变为Json串
                    //创建对象
                    MarkPicEntity pic = new MarkPicEntity(Integer.parseInt(username), date, 1);
                    Gson gson = new Gson();
                    String sql = gson.toJson(pic);
                    out.write(sql.getBytes());
                    InputStream in = conn.getInputStream();
                    InputStreamReader instr = new InputStreamReader(in, "UTF-8");
                    StringBuffer buffer = new StringBuffer();
                    //如果当月打卡无记录则传输过来的为null
                    int len = 0;
                    char[] chars = new char[1024];
                    while ((len = instr.read(chars)) != -1) {
                        buffer.append(new String(chars, 0, len));
                    }
                    //通过这个下载网络图片并显示
                    String total1 = URLDecoder.decode(buffer.toString(), "utf-8");
                    Log.e("total1", total1);
                    if (total1.equals("null") == false) {
                        showDate(total1);
                    } else {
                        showNoAarkDate();
                    }
                } catch (MalformedURLException | ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        //点击事件
        btnPicBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        backmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void sharedWitdQQ(String s) {
        mTencent = Tencent.createInstance(APP_ID, PictureInfo.this.getApplicationContext());
        mIUiListener = new ShareUiListener();
//        shareToQQ(s);
        shareToQQImage(s);
    }
    //内部类
    class ShareUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            //分享成功
            Log.e("mll","分享成功");
        }

        @Override
        public void onError(UiError uiError) {
            //分享失败
            Log.e("mll","分享失败");
        }

        @Override
        public void onCancel() {
            //分享取消
            Log.e("mll","分享取消");
        }

        @Override
        public void onWarning(int i) {
            Log.e("mll","授权警告");
        }
    }

    /**
     * 默认分享——图文并存
     */
    private void shareToQQ(String s) {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "打卡分享");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "欢迎大家一起加入乐动云记录孩子成长");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,s);// 内容地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,s);// 网络图片地址　　params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "应用名称");// 应用名称
        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
        Log.e("mll","QQ分享"+s);
        mTencent.shareToQQ(PictureInfo.this, params, mIUiListener);
    }
    /**
     * 分享图片
     *
     */
    private void shareToQQImage(String file){
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);// 设置分享类型为纯图片分享
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, file);
        String path = getFilesDir().getAbsolutePath();
        //需要填写file
        Log.e("mll",file);
        File file1 = new File(file);
        if(file1.exists()){
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, file);// 需要分享的本地图片URL
            mTencent.shareToQQ(PictureInfo.this, params, mIUiListener);
        }
        Log.e("mll","file为："+file);

    }


    //没有打卡记录
    private void showNoAarkDate() {
        Message msg = handler.obtainMessage();
        msg.what = 2;
        msg.obj = "no";
        handler.sendMessage(msg);
    }

    //获取数据，下载图片
    private void showDate(String total1) {
        //数据转对象
        Gson gson = new Gson();
        ReturnMarkPic returnmark = gson.fromJson(total1, ReturnMarkPic.class);
        //获取数据
        String path = returnmark.getBackground();
        type = returnmark.getSporttype();
        time = returnmark.getSporttime();
        stringimpression = returnmark.getImpression();
        showImages(path);
    }

    private void showImages(String path) {
        URL url = null;
        try {
            String paths = ConfigUtil.SERVER_ADDR + path + ".jpg";
            imgPath = paths;
            url = new URL(paths);
            InputStream in1 = url.openStream();
            String files = getBaseContext().getFilesDir().getAbsolutePath();
            String imgs = files + "/imgs";
            //files/imgs
            //判断imgs目录是否存在
            File dirImgs = new File(imgs);
            if (!dirImgs.exists()) {
                dirImgs.mkdir();
            }
            //修改图片地址为本地地址
            String[] array = path.split("/");
            String imgPath = imgs + "/" + array[1] + ".jpg";
            File file = new File(imgPath);
            Log.e("保存地址", imgPath);
            if (file.exists()) {
                Log.e("文件已经存在", "aLreadyExists");
            } else {
                Log.e("文件不存在", "需要下载");
                OutputStream out = new FileOutputStream(imgPath);
                int b = -1;
                while ((b = in1.read()) != -1) {
                    out.write(b);
                }
            }
            testImgpath = imgPath;
            Log.e("mll","imgpath"+imgPath);
//            shareToQQImage(imgPath);
            //files/imgs/123.1.2020-11-30.jpg
            //将输入流解析成Bitmap对象
            Message msg = handler.obtainMessage();
            msg.what = 1;
            msg.obj = imgPath;
            handler.sendMessage(msg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
