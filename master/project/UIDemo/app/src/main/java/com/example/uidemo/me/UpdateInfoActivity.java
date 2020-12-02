package com.example.uidemo.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.uidemo.R;
public class UpdateInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_info_activity);

        ImageView ivUpInfoHeader = findViewById(R.id.iv_upInfoHeader);
        Button btnReturn = findViewById(R.id.btn_updateInfoRet);

        Glide.with(this)
                .load(R.drawable.girl)
                .circleCrop()
                .into(ivUpInfoHeader);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateInfoActivity.this.finish();
            }
        });

    }
}
