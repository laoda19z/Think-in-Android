package com.example.uidemo.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.example.uidemo.R;
public class UpdatePhoneActivity extends AppCompatActivity {
    private Button btnReturn;
    private Button btnFinish;
    private EditText etNewPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_phone_layout);

        findViews();

        //返回键
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePhoneActivity.this.finish();
            }
        });

        //修改成功，返回上一个页面，并向服务器提交数据
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交数据
                String newPhone = etNewPhone.getText().toString().trim();
                /**
                 * 根据用户id，修改电话号码
                 * */

                UpdatePhoneActivity.this.finish();//返回上一个页面
            }
        });
    }

    private void findViews() {
        btnReturn = findViewById(R.id.btn_phRet);
        btnFinish = findViewById(R.id.btn_finishPhone);
        etNewPhone = findViewById(R.id.et_newPhone);
    }
}
