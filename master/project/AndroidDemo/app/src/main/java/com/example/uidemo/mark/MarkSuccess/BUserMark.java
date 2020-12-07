package com.example.uidemo.mark.MarkSuccess;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
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
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.tencent.connect.UserInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BUserMark extends AppCompatActivity {
    private ImageView background;
    private ImageView ivZxing;
    private Button remark;
    //显示文字的控件
    private TextView writeday;
    private TextView writeotherdate;
    private TextView writeusername;
    private TextView writeka;
    private TextView writeimpression;
    //
    private int username;
    private String date;
    private String sport;
    private int minutes;
    private String impression;
    private int child;
    //上传图片的id
    private int id;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_mark);

        background = findViewById(R.id.iv_backgrounds);
        ivZxing = findViewById(R.id.iv_zxing);
        remark = findViewById(R.id.btn_remark);
        writeday = findViewById(R.id.need_to_write_day);
        writeotherdate = findViewById(R.id.need_to_write_otherdate);
        writeusername = findViewById(R.id.need_to_write_username);
        writeka = findViewById(R.id.need_to_write_kaluli);
        writeimpression = findViewById(R.id.need_to_write_impression);

        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        //获得图片名字
        name = intent.getStringExtra("name");

        //通过json变为对象，获得数据，并显示在控件中
        Gson gson = new Gson();
        Mark mark = gson.fromJson(json, Mark.class);
        //获得属性
        //用户名
        username = mark.getUsername();
        //时间
        date = mark.getDate();
        //运动项目
        sport = mark.getSporttype();
        //运动时间
        minutes = mark.getMinutes();
        if (minutes == 0) {
            writeka.setText("消耗卡路里 : 0K");
        } else {
            int ka = minutes * 9;
            writeka.setText("消耗卡路里 : " + minutes + "k");
        }
        //感想
        impression = mark.getImpression();
        if (impression.equals("nulls")) {

        } else {
            writeimpression.setText("感想 : " + impression);
        }
        //孩子id
        child = mark.getChild();
        //运动时间
        minutes = mark.getMinutes();
        Log.e("minutes", minutes + "");
        //显示到控件中
        //对时间进行操作
        String[] array = date.split("-");
        writeday.setText(array[2]);
        writeotherdate.setText(array[0] + "." + array[1]);
        writeusername.setText("用户名 : " + username);
        //通过图片名显示
        id = getResources().getIdentifier(name, "drawable", getPackageName());
        Glide.with(this).load(id).into(background);

        //生成二维码
        createEwm();


        //封装串
        String trans = totalDataToJson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/json"), trans);
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建Request.Builder
        Request request = new Request.Builder()
                .post(requestBody)
                .url(ConfigUtil.SERVER_ADDR + "uploadbackground")
                .build();
        //
        Call call = okHttpClient.newCall(request);
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

        //点击打卡
        remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //这里进行图片的上传和跳转
//                //跳转回最初的页面
                Intent intent1=new Intent();
                intent1.setClass(BUserMark.this, MainActivity.class);
                startActivityForResult(intent1,1);

            }
        });

    }


    //数据变为JSON串
    private String totalDataToJson() {
        //创建对象
        TotalMark total = new TotalMark();
        total.setUsername(username);
        total.setDate(date);
        total.setSporttype(sport);
        total.setMinutes(minutes);
        total.setImpression(impression);
        total.setChild(child);
        String Picname = name;
        total.setPicname(Picname);
        Gson gson = new Gson();
        String json = null;
        try {
            String json1 = gson.toJson(total);
            json = URLEncoder.encode(gson.toJson(total), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void createEwm() {
        /*生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败  */
        int width = 600;
        int height = 600;
        BitMatrix bitMatrix = null;
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.MARGIN, 0);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            String contexts = "用户名 : " + username + " 运动项目 : " + sport + " 打卡日期 : " + date + " 加油吧!";
            bitMatrix = new MyQRCodeWriter().encode(contexts, BarcodeFormat.QR_CODE, width, height, hints);
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
