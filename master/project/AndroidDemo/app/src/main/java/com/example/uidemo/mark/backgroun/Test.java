package com.example.uidemo.mark.backgroun;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.R;
import com.example.uidemo.mark.Entity.ImageEntity;

import java.util.ArrayList;
import java.util.List;

public class Test extends AppCompatActivity {
    private ListView listView2;
    //获得的父图片类名字
    private String name;
    //获得的josn串
    private String json;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_back_listview2);

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        json=intent.getStringExtra("json");
        listView2=findViewById(R.id.listview2);

        //通过自己循环，拼接数字，得到图片的名字
        List<ImageEntity> imageslist2=new ArrayList<>();
        for(int i=1;i<=4;++i){
            ImageEntity image1=new ImageEntity(name+i,json);
            imageslist2.add(image1);
        }
        BListViewAdapter bListViewAdapter=new BListViewAdapter(this,imageslist2,R.layout.listview2_item);
        listView2.setAdapter(bListViewAdapter);
    }
}
