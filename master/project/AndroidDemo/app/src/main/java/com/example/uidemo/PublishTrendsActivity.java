package com.example.uidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.telephony.CarrierConfigManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.uidemo.beans.Dynamic;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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
    private File file;
    private String imgUrl;
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
                    publishTrend();
//                    publishD();
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

    private void publishD() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在发表中，请稍后...");
        mDialog.show();
        //1、创建OKHTTPClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2、创建请求对象
        //获取待上传的pdf文件
        if(imgUrl!=null){
            String content = etContent.getText().toString();
            String name = "用户";
            String location = tvLocation.getText().toString();
            if ("获取当前位置".equals(location)) {
                location = "暂无";
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String time = formatter.format(date);
            String img = file.getName();
            file = new File(imgUrl);
            //获取MIME类型
            MediaType type = MediaType.parse("application/x-jpg");
            MediaType type2 = MediaType.parse("text/plain;charset=utf-8");
            //创建RequestBody对象
            RequestBody requestBody = RequestBody.create(file, type);
            RequestBody requestBody2 = RequestBody.create("ss",type2);
            Request request = new Request.Builder().url(ConfigUtil.SERVER_ADDR + "PublishTrendsServlet?username=" + name + "&content=" + content + "&location=" + location + "&time=" + time + "&img=" + img).post(requestBody).build();
            //3、创建CALL对象
            Call call = okHttpClient.newCall(request);
            //4、异步方式提交请求并获取响应
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("mll", "请求失败");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String str = response.body().string();
                    //推荐使用该方法，不用创建许多的Message
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    msg.obj = str;
                    handler.sendMessage(msg);
                }
            });
        }

    }

    /**
     * 发布动态
     */
    private void publishTrend() {
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
                    String name = "用户";
                    String location = tvLocation.getText().toString();
                    if ("获取当前位置".equals(location)) {
                        location = "暂无";
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    String time = formatter.format(date);
                    String img = null;
                    if(imgUrl!=null){
                        img = file.getName();
                    }
                    Log.e("mll", img);
//                    URL url = new URL(ConfigUtil.SERVER_ADDR + "PublishTrendsServlet");
                    URL url = new URL(ConfigUtil.SERVER_ADDR + "PublishTrendsServlet?username=" + name + "&content=" + content + "&location=" + location + "&time=" + time + "&img=" + img);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    MediaType type = MediaType.parse("application/x-jpg");

                    OutputStream out = conn.getOutputStream();
                    Log.e("mll","外");
                    if (imgUrl != null) {
                        Log.e("mll","内");
                        InputStream filein = new FileInputStream(imgUrl);
                        int n = -1;
                        while ((filein.read()) != -1) {
                            out.write(n);
                        }
                    }
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                    String str = reader.readLine();
                    if ("OK".equals(str)) {
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                    inputStream.close();
                    out.close();
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
        eventBus.unregister(PublishTrendsActivity.this);
    }

    /**
     * 打开系统相册
     */                                                                                                                     //PictureConfig.CHOOSE_REQUEST
    public void openSystemPic() {
        Log.e("mll", "打开");
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("mll", "回来了");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    break;
                case 1:
                    Uri imguri = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(imguri, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String imagePath = c.getString(columnIndex);
                    c.close();
                    Uri uri = getImageContentUri(PublishTrendsActivity.this, imagePath);
                    showImage(uri);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * uri转为file
     *
     * @param uri
     * @return
     */
    private File uri2File(Uri uri) {
        String img_path;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = managedQuery(uri, proj, null,
                null, null);
        if (actualimagecursor == null) {
            img_path = uri.getPath();
        } else {
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            img_path = actualimagecursor
                    .getString(actual_image_column_index);
        }
        File file = new File(img_path);
        return file;
    }

    /**
     * 预览图片
     *
     * @param
     */
    private void showImage(final Uri path) {
        Glide.with(PublishTrendsActivity.this).load(path).into(trendImg);
        new Thread() {
            @Override
            public void run() {
                file = uri2File(path);
                Log.e("mll", file.getName());
                imgUrl = getFilesDir().getAbsolutePath() + "/" + file.getName();
                try {
                    InputStream in = new FileInputStream(file);
                    OutputStream out = new FileOutputStream(imgUrl);
                    int len = -1;
                    while (-1 != (len = in.read())) {
                        out.write(len);
                        out.flush();
                    }
                    in.close();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public static Uri getImageContentUri(Context context, String path) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            // 如果图片不在手机的共享图片数据库，就先把它插入。
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}