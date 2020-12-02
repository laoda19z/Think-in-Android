package com.example.uidemo.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.uidemo.R;

import androidx.appcompat.app.AppCompatActivity;

public class CodeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_code_layout);

        Button btnReturn = findViewById(R.id.btn_return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               CodeActivity.this.finish();
            }
        });
    }
}
