package com.example.uidemo.dynamic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.uidemo.ConfigUtil;
import com.example.uidemo.LoginActivity;
import com.example.uidemo.R;
import com.example.uidemo.beans.Dynamic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 发布动态
 */
public class PublishTrendsActivity extends AppCompatActivity {
    private Button btnFinish;
    private Button btnPublish;
    private Button btnAddImg;
    private Button btnLocation;
    private EditText etContent;
    private TextView tvLocation;
    private ProgressDialog mDialog;
    private EventBus eventBus;
    private ImageView trendImg;
    private LinearLayout linearLayout;
    private String path;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_trends);

        findViews();
        setListener();
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        mDialog.dismiss();
                        finish();
                        break;
                }
            }
        };
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        btnFinish.setOnClickListener(myListener);
        btnLocation.setOnClickListener(myListener);
        btnAddImg.setOnClickListener(myListener);
        btnPublish.setOnClickListener(myListener);
    }

    private void findViews() {
        btnFinish = findViewById(R.id.btn_publish_finish);
        btnPublish = findViewById(R.id.btn_publish_ok);
        btnAddImg = findViewById(R.id.btn_publish_addImg);
        btnLocation = findViewById(R.id.btn_publish_location);
        etContent = findViewById(R.id.edit_publish_trend);
        tvLocation = findViewById(R.id.tv_publish_location);
        trendImg = findViewById(R.id.publish_trend_img);
        linearLayout = findViewById(R.id.publish_center);
    }

    class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_publish_finish:
                    //返回
                    finish();
                    break;
                case R.id.btn_publish_ok:
                    //发布动态
                    if(path==null||path==""){
                        //没有图片
                        publishDynamicWithNoImg();
                    }else {//有图片
                        publishDynamic();
                    }
                    break;
                case R.id.btn_publish_addImg:
                    //打卡相册添加图片
                    openSystemPic();
                    break;
                case R.id.btn_publish_location:
                    //定位地址
                    startLocation();
                    break;
            }
        }
    }
    //这个方法是用来上传动态
    private void publishDynamic() {
        //展示dialog
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在发表中，请稍后...");
        mDialog.show();
        String content = etContent.getText().toString();
        String location = tvLocation.getText().toString();
        if ("获取当前位置".equals(location)) {
            location = "暂无";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long savetime = System.currentTimeMillis();
        Date date1 = new Date();
        String time = formatter.format(date1);
        Date date = null;
        try {
            date = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String img = null;
        Dynamic dynamic = new Dynamic();
        dynamic.setContent(content);
        dynamic.setLocation(location);
        dynamic.setTime(date);
        int userid = Integer.parseInt(LoginActivity.currentUserId);
        dynamic.setUserId(userid);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        // 创建 OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder().build();
        // 要上传的文件
        File file = new File(path);
        dynamic.setImg(userid+savetime+"");
        //将需要的数据封装为json串
        String trans = null;
        try {
            trans = URLEncoder.encode(gson.toJson(dynamic),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("image/jpeg");
//        MediaType mediaType2 = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        // 把文件封装进请求体
        RequestBody fileBody = RequestBody.create(mediaType,file);
//        RequestBody transBody = RequestBody.create(mediaType2,trans);
        // MultipartBody 上传文件专用的请求体
        RequestBody body;
        if(file!=null){
            body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM) // 表单类型(必填)
                    .addFormDataPart("trans",trans)
                    .addFormDataPart("image", file.getName(), fileBody)
                    .build();
        }else {
            body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM) // 表单类型(必填)
                    .addFormDataPart("trans",trans)
                    .build();
        }
        Request request = new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR+"PublishTrendsServlet")
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
                String s = response.body().string();
                if("OK".equals(s)){
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        });
    }
    /**
     * 定位并将定位后的地址显示在界面上
     */
    private void startLocation() {
        GPS gps = new GPS(getApplicationContext());
        eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(PublishTrendsActivity.this)) {
            eventBus.register(PublishTrendsActivity.this);
        }
        gps.startLocation();
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在定位中，请稍后...");
        mDialog.show();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(GPS gps) {
        mDialog.dismiss();
        gps.stopLocation();
        tvLocation.setText(gps.getMylocation());
        tvLocation.setTextSize(18);
        tvLocation.setTextColor(Color.RED);
        btnLocation.setBackgroundResource(R.mipmap.warter2);
        eventBus.unregister(PublishTrendsActivity.this);
    }

    /**
     * 打开系统相册
     */                                                                                                                     //PictureConfig.CHOOSE_REQUEST
    public void openSystemPic() {
        Log.e("mll", "打开");
        com.wildma.pictureselector.PictureSelector.create(PublishTrendsActivity.this, com.wildma.pictureselector.PictureSelector.SELECT_REQUEST_CODE).selectPicture();
    }
    private void publishDynamicWithNoImg() {
        //展示dialog
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在发表中，请稍后...");
        mDialog.show();
        //封装数据
        //内容
        new Thread() {
            @Override
            public void run() {
                try {
                    String content = etContent.getText().toString();
                    String name = LoginActivity.currentUserId;
                    String location = tvLocation.getText().toString();
                    if ("获取当前位置".equals(location)) {
                        location = "暂无";
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    String time = formatter.format(date);
                    String img = null;
                    URL url = new URL(ConfigUtil.SERVER_ADDR + "PublishTrendsWithNoImgServlet?username=" + name + "&content=" + content + "&location=" + location + "&time=" + time + "&img=" + img);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                    String str = reader.readLine();
                    if ("OK".equals(str)) {
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                    inputStream.close();
                    reader.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case com.wildma.pictureselector.PictureSelector.SELECT_REQUEST_CODE:
                    // 结果回调
                    if (data != null) {
                        PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                        path = pictureBean.getPath() + "";
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 20, 0, 0);
                        params.width = 200;
                        params.height = 200;
                        trendImg.setLayoutParams(params);

                        Glide.with(PublishTrendsActivity.this)
                                .load(pictureBean.getUri())
                                .apply(RequestOptions.bitmapTransform(new CropTransformation(200, 200, CropTransformation.CropType.CENTER)))
                                .into(trendImg);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}