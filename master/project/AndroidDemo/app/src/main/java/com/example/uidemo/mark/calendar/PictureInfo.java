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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.mark.Entity.MarkPicEntity;
import com.google.gson.Gson;

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
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1://表示数据完成
                    //图片显示在图片控件中
                    Log.e("路径",msg.obj+"");
                    Glide.with(getApplicationContext()).load(new File(msg.obj+"")).into(ivmark);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mark_picture_info);

        ivmark=findViewById(R.id.markpic);
        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        date=intent.getStringExtra("date");
        child= Integer.parseInt(intent.getStringExtra("child"));
        btnPicBack=findViewById(R.id.markPicBack);

        //通过Intent传递过来的username和孩子id和日期，向服务器端查询图片，并显示
        new Thread(){
            @Override
            public void run() {
                try {
                    URL picpath = new URL(ConfigUtil.SERVER_ADDR+"markpic");
                    HttpURLConnection conn = (HttpURLConnection)picpath.openConnection();
                    //设置网络请求的方式为POST
                    conn.setRequestMethod("POST");
                    OutputStream out=conn.getOutputStream();
                    //将需要传输的数据变为Json串
                    Log.e("child",child+"");
                    //创建对象
                    MarkPicEntity pic=new MarkPicEntity(username,date,child);
                    Gson gson=new Gson();
                    String sql=gson.toJson(pic);
                    out.write(sql.getBytes());
                    InputStream in = conn.getInputStream();
                    InputStreamReader instr=new InputStreamReader(in,"UTF-8");
                    StringBuffer buffer=new StringBuffer();
                    //如果当月打卡无记录则传输过来的为null
                    int len=0;
                    char[] chars=new char[1024];
                    while((len=instr.read(chars))!=-1){
                        buffer.append(new String(chars,0,len));
                    }
                    //通过这个下载网络图片并显示
                    Log.e("backpic",buffer.toString());
                    showImages(buffer.toString());
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

    }

    private void showImages(String path) {
        URL url= null;
        try {
            String paths=ConfigUtil.SERVER_ADDR+path+".jpg";
            url = new URL(paths);
            InputStream in1=url.openStream();
            String files=getBaseContext().getFilesDir().getAbsolutePath();
            Log.e("本地路径",files);
            String imgs=files+"/imgs";
            //files/imgs
            //判断imgs目录是否存在
            File dirImgs=new File(imgs);
            if(!dirImgs.exists()){
                dirImgs.mkdir();
            }
            //修改图片地址为本地地址
            String[] array=path.split("/");
            String imgPath=imgs+"/"+array[1]+".jpg";
            File file=new File(imgPath);
            if(file.exists()){
                Log.e("文件已经存在","aLreadyExists");
            }
            else{
                Log.e("文件不存在","需要下载");
                OutputStream out=new FileOutputStream(imgPath);
                int b= -1;
                while((b=in1.read())!=-1){
                    out.write(b);
                }
            }
            //files/imgs/123.1.2020-11-30.jpg
            //将输入流解析成Bitmap对象
            Message msg=handler.obtainMessage();
            msg.what=1;
            msg.obj=imgPath;
            handler.sendMessage(msg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
