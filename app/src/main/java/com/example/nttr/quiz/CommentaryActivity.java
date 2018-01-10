package com.example.nttr.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class CommentaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentary);

        Intent intent = getIntent();
        ArrayList<String> commentaryList = intent.getStringArrayListExtra("解説リスト");

        //ListViewのセット
//        ListView listView = new ListView(this);
        ListView listView = (ListView) findViewById(R.id.listView);
//        setContentView(listView);

        //データの追加
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.my_list_item);

        for (String str : commentaryList) {
            adapter.add(str);
        }
        listView.setAdapter(adapter);

        Button retryButton = (Button) findViewById(R.id.retry_button);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentaryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
