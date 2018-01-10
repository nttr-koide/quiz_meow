package com.example.nttr.quiz;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nttr on 2017/12/13.
 */

public class CsvReader {
    List<ListData> objects = new ArrayList<>();
    public void reader(Context context) {
        AssetManager assetManager = context.getResources().getAssets();
        try {
            // CSVファイルの読み込み
            InputStream inputStream = assetManager.open("quiz.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferReader.readLine()) != null) {

                //カンマ区切りで１つづつ配列に入れる
                ListData quiz = new ListData();
                String[] RowData = line.split(",");

                //CSVの左([0]番目)から順番にセット
                quiz.setContent(RowData[0]);
                quiz.setOption1(RowData[1]);
                quiz.setOption2(RowData[2]);
                quiz.setOption3(RowData[3]);
                quiz.setAnswer(RowData[4]);
                quiz.setCommentaryAnswer(RowData[5]);
                quiz.setCommentary(RowData[6]);

                //ocjectsリストにクイズを追加
                objects.add(quiz);
            }
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}