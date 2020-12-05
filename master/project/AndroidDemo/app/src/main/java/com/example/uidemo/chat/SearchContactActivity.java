package com.example.uidemo.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.uidemo.R;

public class SearchContactActivity extends AppCompatActivity {

    private Button btnSearch;
    private EditText etName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contact);

        findViews();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void findViews() {
        btnSearch = findViewById(R.id.search_contact_btn);
        etName = findViewById(R.id.search_contact_et);
    }
}