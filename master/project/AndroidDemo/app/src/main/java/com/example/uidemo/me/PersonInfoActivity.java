package com.example.uidemo.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;

import static com.example.uidemo.LoginActivity.currentUserEmail;
import static com.example.uidemo.LoginActivity.currentUserHead;
import static com.example.uidemo.LoginActivity.currentUserName;
import static com.example.uidemo.LoginActivity.currentUserPhoneNum;
import static com.example.uidemo.LoginActivity.currentUserRealName;
import static com.example.uidemo.LoginActivity.currentUserSex;

public class PersonInfoActivity extends AppCompatActivity {
    private ImageView ivHeader;
    private Button btnReturn;
    private Button btnUpdateName;
    private Button btnUpdateInfo;
    private TextView tvPerInfoName;
    private TextView tvPersonInfoName;
    private TextView tvPersonInfoSex;
    private TextView tvPersonInfoPhone;
    private TextView tvPersonInfoEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info_layout);

        findViews();

        //设置信息
        tvPerInfoName.setText(currentUserName);
        tvPersonInfoName.setText(currentUserRealName);
        tvPersonInfoSex.setText(currentUserSex);
        tvPersonInfoPhone.setText(currentUserPhoneNum);
        tvPersonInfoEmail.setText(currentUserEmail);


        //设置头像
        if(currentUserHead.isEmpty()){
            Glide.with(this)
                    .load(R.mipmap.tx)
                    .circleCrop()
                    .into(ivHeader);
        }else{
            Glide.with(this)
                    .load(ConfigUtil.SERVER_ADDR+currentUserHead+"")
                    .circleCrop()
                    .into(ivHeader);
        }

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonInfoActivity.this.finish();
            }
        });

        btnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PersonInfoActivity.this,UpdateNameActivity.class);
                startActivity(intent);
            }
        });

        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PersonInfoActivity.this,UpdateInfoActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    private void findViews() {
        ivHeader = findViewById(R.id.iv_perInfoHeader);
        btnReturn = findViewById(R.id.btn_perInfoRet);
        btnUpdateName = findViewById(R.id.btn_upName);
        btnUpdateInfo = findViewById(R.id.btn_updateInfo);
        tvPersonInfoName = findViewById(R.id.tv_personInfoName);
        tvPersonInfoSex = findViewById(R.id.tv_personInfoSex);
        tvPersonInfoPhone = findViewById(R.id.tv_personInfoPhone);
        tvPersonInfoEmail = findViewById(R.id.tv_personInfoEmail);
        tvPerInfoName = findViewById(R.id.tv_perInfoName);
    }

    /**
     *重写onActivityResult方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2){
            if(requestCode == 1){
                Bundle bundle = data.getExtras();
                String newName = bundle.getString("newName");
                String newSex = bundle.getString("newSex");
                String newPhone = bundle.getString("newPhone");
                String newEmail = bundle.getString("newEmail");
                //更新数据
                tvPersonInfoName.setText(newName);
                tvPersonInfoSex.setText(newSex);
                tvPersonInfoPhone.setText(newPhone);
                tvPersonInfoEmail.setText(newEmail);

            }
        }
    }
}
