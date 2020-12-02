package com.example.uidemo.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.example.uidemo.R;
public class UpdateNameActivity extends AppCompatActivity {
    private Button btnReturn;
    private Button btnFinish;
    private EditText etNewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_name_layout);

        findViews();

        //返回键
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateNameActivity.this.finish();
            }
        });

        //修改完成按钮，并将数据传递给服务器
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 将新昵称传递给服务器
                 * 根据用户id修改
                 * */
                String newName = etNewName.getText().toString();

                UpdateNameActivity.this.finish();//返回上一个页面
            }
        });
    }

    private void findViews() {
        btnReturn = findViewById(R.id.btn_updateNameRet);
        btnFinish = findViewById(R.id.btn_finishName);
        etNewName = findViewById(R.id.et_newName);
    }
}
