package com.example.uidemo.mark.backgroun;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.uidemo.R;
import com.example.uidemo.mark.MarkSuccess.UserMark;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import jp.wasabeef.glide.transformations.CropTransformation;

public class BackgroundChoice extends AppCompatActivity {
    private ImageView ivSelect;
    private ImageView mIvImage;
    private LinearLayout lin1;
    private String json;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background_choice);

        Intent intent=getIntent();
        json=intent.getStringExtra("json");


        ivSelect=findViewById(R.id.iv_select);
        mIvImage=findViewById(R.id.iv_test);
        lin1=findViewById(R.id.lin1);
        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(BackgroundChoice.this,PictureSelector.SELECT_REQUEST_CODE).selectPicture();
            }
        });



    }

    //图片裁切的话，显示使用path，不裁切使用uri，因为上面onClick方法中，设置的为selectPicture(),所有默认为不裁切，使用uri即可
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                final String uri=pictureBean.getUri()+"";
                final String path=pictureBean.getPath()+"";
                Log.e("path",pictureBean.getPath());
                Log.e("uri",pictureBean.getUri()+"");

                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,20,0,0);
                params.width=984;
                params.height=340;
                mIvImage.setLayoutParams(params);

                Glide.with(this)
                        .load(pictureBean.getUri())
                        .apply(RequestOptions.bitmapTransform(new CropTransformation(984 ,340, CropTransformation.CropType.CENTER)))
                        .into(mIvImage);

                mIvImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent();
                        intent.setClass(BackgroundChoice.this, UserMark.class);
                        intent.putExtra("uri",uri);
                        intent.putExtra("path",path);
                        intent.putExtra("json",json);
                        startActivity(intent);
                    }
                });

            }
        }
    }
}
