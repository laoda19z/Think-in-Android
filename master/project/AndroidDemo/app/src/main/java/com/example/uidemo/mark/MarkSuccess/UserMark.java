package com.example.uidemo.mark.MarkSuccess;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.uidemo.ConfigUtil;
import com.example.uidemo.MainActivity;
import com.example.uidemo.R;
import com.example.uidemo.mark.Entity.Mark;
import com.example.uidemo.mark.Entity.TotalMark;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserMark extends AppCompatActivity {
    private ImageView background;
    private ImageView ivZxing;
    private Button remark;
    private String uri;
    private String path;
    //显示文字的控件
    private TextView writeday;
    private TextView writeotherdate;
    private TextView writeusername;
    private TextView writeka;
    private TextView writeimpression;
    //
    private String username;
    private String date;
    private String sport;
    private int minutes;
    private String impression;
    private int child;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_mark);

        background=findViewById(R.id.iv_backgrounds);
        ivZxing=findViewById(R.id.iv_zxing);
        remark=findViewById(R.id.btn_remark);
        writeday=findViewById(R.id.need_to_write_day);
        writeotherdate=findViewById(R.id.need_to_write_otherdate);
        writeusername=findViewById(R.id.need_to_write_username);
        writeka=findViewById(R.id.need_to_write_kaluli);
        writeimpression=findViewById(R.id.need_to_write_impression);

        Intent intent=getIntent();
        uri=intent.getStringExtra("uri");
        path=intent.getStringExtra("path");
        String json=intent.getStringExtra("json");
        //通过json变为对象，获得数据，并显示在控件中
        Gson gson=new Gson();
        Mark mark=gson.fromJson(json, Mark.class);
        //获得属性
        //用户名
        username=mark.getUsername();
        //时间
        date=mark.getDate();
        //运动项目
        sport=mark.getSporttype();
        //运动时间
        minutes=mark.getMinutes();
        //感想
        impression=mark.getImpression();
        //孩子id
        child=mark.getChild();
        //显示到控件中
        //对时间进行操作
        String[] array=date.split("-");
        writeday.setText(array[2]);
        writeotherdate.setText(array[0]+"."+array[1]);
        writeusername.setText(username);
        writeimpression.setText(impression);
        Uri uris= Uri.parse(uri);
        Glide.with(this).load(uris).into(background);

        //生成二维码
        createEwm();

        okHttpUploadImage();

        //设置点击事件
        remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里进行图片的上传和跳转
                //跳转回最初的页面
                Intent intent1=new Intent();
                intent1.setClass(UserMark.this, MainActivity.class);
                intent1.putExtra("flushstatus","需要刷新");
                startActivityForResult(intent1,1);
            }
        });
    }



    //这个方法是用来上传图片的
    private void okHttpUploadImage() {
        //将需要的数据封装为json串
        String trans=totalDataToJson();
        // 创建 OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder().build();
        // 要上传的文件
        File file = new File(path);
        MediaType mediaType = MediaType.parse("image/jpeg");
        // 把文件封装进请求体
        RequestBody fileBody = RequestBody.create(mediaType,file);
        // MultipartBody 上传文件专用的请求体
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM) // 表单类型(必填)
                .addFormDataPart("trans",trans)
                .addFormDataPart("image", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR+"upload")
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println(response.body().string());
                } else {
                    System.out.println(response.code());
                }
            }
        });
    }

    //数据变为JSON串
    private String totalDataToJson() {
        //创建对象
        TotalMark total=new TotalMark();
        total.setUsername(username);
        total.setDate(date);
        total.setSporttype(sport);
        total.setMinutes(minutes);
        total.setImpression(impression);
        total.setChild(child);
        String Picname=username+"."+child+"."+date;
        total.setPicname(Picname);
        Gson gson=new Gson();
        String json= null;
        try {
            String json1=gson.toJson(total);
            json = URLEncoder.encode(gson.toJson(total),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void createEwm(){
        /*生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败  */
        int width=600;
        int height=600;
        BitMatrix bitMatrix = null;
        try {
            bitMatrix =new MyQRCodeWriter().encode("asuna",BarcodeFormat.QR_CODE,width,height);
            /*转为一维数组*/
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int y = 0; y < width; y++) {
                    if (bitMatrix.get(y, i)) {
                        pixels[y * width + i] = 0xff000000;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            Glide.with(this).load(bitmap).into(ivZxing);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}
