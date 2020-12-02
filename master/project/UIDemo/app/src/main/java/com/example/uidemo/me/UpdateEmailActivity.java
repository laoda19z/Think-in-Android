package com.example.uidemo.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.example.uidemo.R;
public class UpdateEmailActivity extends AppCompatActivity {
    private Button btnReturn;
    private Button btnFinish;
    private EditText etNewEmail;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_email_layout);

        findViews();

        //返回键
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateEmailActivity.this.finish();
            }
        });

        //修改成功，返回上一个页面，并提交数据给服务器
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取新邮箱
                String newEmail = etNewEmail.getText().toString().trim();
                /**
                 * 传递到服务端
                 * 并根据用户id进行修改
                 * */


                UpdateEmailActivity.this.finish();//返回上一个页面
            }
        });
    }

    private void findViews() {
        btnReturn = findViewById(R.id.btn_upEmailRet);
        btnFinish = findViewById(R.id.btn_finishEmail);
        etNewEmail = findViewById(R.id.et_newEmail);
    }
}
