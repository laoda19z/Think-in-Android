package com.example.uidemo.familyactivity.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.R;


public class DetailsActivity extends AppCompatActivity {
    private ImageView imgBackground;
    private TextView titleTv;
    private TextView timeTv;
    private TextView locationTv;
    private TextView contentTv;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        findViews();

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        byte[] bytes=bundle.getByteArray("bitmapImg");
        imgBackground.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
        titleTv.setText(bundle.getString("name"));
        timeTv.setText(bundle.getString("time"));
        locationTv.setText(bundle.getString("district"));
        contentTv.setText(bundle.getString("content"));
        back.bringToFront();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DetailsActivity.this,FamilyActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void findViews() {
        imgBackground = findViewById(R.id.img_background);
        titleTv = findViewById(R.id.activity_details_title);
        timeTv = findViewById(R.id.activity_details_time);
        locationTv = findViewById(R.id.activity_details_location);
        contentTv = findViewById(R.id.activity_details_content);
        back = findViewById(R.id.back);
    }
}
