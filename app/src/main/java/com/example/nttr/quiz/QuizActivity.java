package com.example.nttr.quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    // ① 変数の宣言
    TextView countTextView;
    TextView contentTextView;
    TextView tv;
    // Buttonの配列
    // ボタンを3つセットで管理したいので、配列で宣言します。
    Button[] optionButtons;
    // 現在の問題番号
    int questionNumber;
    // 獲得したポイント数
    int point;
    // Listという、配列よりもデータを追加しやすい型を使います。
    List<Quiz> quizList;
    // 間違えた問題の解説を入れるリスト
    ArrayList<String> commentaryList;
    //アニメーション
//    private TranslateAnimation translateAnimation;
    private ScaleAnimation scaleAnimation;
    //タイマー
    CountDownTimer countDownTimer;
    //csvから読み出したクイズオブジェクト（一行分）を格納するため
    Quiz quiz;
    //効果音
    SoundPool mSoundPool;
    int mSoundResId;
    int mSoundResId_fight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        // ② 初期化処理
        // Viewの関連付けを行います
        countTextView = findViewById(R.id.countTextView);
        contentTextView = findViewById(R.id.contentTextView);
        tv = findViewById(R.id.tv);
        // 配列を初期化します
        // 今回、ボタンは3つなので、3の大きさの配列を用意します
        optionButtons = new Button[3];
        optionButtons[0] = findViewById(R.id.optionButton1);
        optionButtons[1] = findViewById(R.id.optionButton2);
        optionButtons[2] = findViewById(R.id.optionButton3);
        for(Button button : optionButtons) {
            // レイアウトではなくコードからonClickを設定します
            // setOnClickListenerに、thisを入れて呼び出します
            button.setOnClickListener(this);
        }
//        thisは
//        new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//              〜
//          }
//        }

        // Quizを1から始めるのでリセットする
        resetQuiz();
    }

    // ③ 出題するクイズのリストを作成します
    void createQuizList() {
        CsvReader parser = new CsvReader();
        parser.reader(getApplicationContext());
        quizList = new ArrayList<>();
        commentaryList = new ArrayList<>();
        for (ListData quiz: parser.objects) {
            quizList.add(new Quiz(quiz.getContent(), quiz.getOption1(), quiz.getOption2(), quiz.getOption3(), quiz.getAnswer(), quiz.getCommentaryAnswer(), quiz.getCommentary()));
        }

        // Listの中身をシャッフルします
        Collections.shuffle(quizList);
    }

    // ④ クイズを表示します
    void showQuiz() {
        countTextView.setText("Ｑ" + (questionNumber + 1));
        //問題番号表示のアニメーション
        fadein();
        // 表示する問題をリストから取得します。
        // 配列では[番号]でしたが、リストでは get(番号)で取得します。配列と同じく0からスタートです。
        quiz = quizList.get(questionNumber);
        contentTextView.setText(quiz.content);
        optionButtons[0].setText(quiz.option1);
        optionButtons[1].setText(quiz.option2);
        optionButtons[2].setText(quiz.option3);

        //タイマー
        countDownTimer = new CountDownTimer(31000, 100) {
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this).setCancelable(false);//ここに置かないとthisでエラー出た 書き方？
            @Override
            public void onTick(long millisUntilFinished) {
                int time = (int) millisUntilFinished / 1000;
                ((TextView) findViewById(R.id.tv)).setText("残り"+ String.valueOf(time) + "秒");
            }
            @Override
            public void onFinish() {
                if((commentaryList.size() + point) != quizList.size()) { //CountDownTimer.cancel()からフィニッシュ！のあとにタイムアップ出てしまうからこれにした..
                    // 時間切れのため、結果を表示します。
                    // 結果表示にはDialogを使います。
                    //                countDownTimer.cancel();
                    builder.setTitle("タイムアップ！");
                    builder.setMessage("正解数：" + point);
                    builder.setPositiveButton("リトライ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // もう一度クイズをやり直す
                            //                    resetQuiz();Main
                            //クイズ画面遷移のためのintent
                            Intent intent = new Intent(QuizActivity.this, QuizActivity.class);
                            // クイズ画面へ遷移
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("解説", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //解説画面遷移のためのintent
                            Intent intent = new Intent(QuizActivity.this, CommentaryActivity.class);
                            // commentaryListをintentに格納
                            intent.putStringArrayListExtra("解説リスト", commentaryList);
                            // 解説画面へ遷移
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }
            }
        };
        if(questionNumber == 0) {
            countDownTimer.start();
        }
    }

    // ⑤ クイズのリセット
    void resetQuiz() {
        questionNumber = 0;
        point = 0;
        createQuizList();
        showQuiz();
    }

    // ⑥ クイズのアップデート
    void updateQuiz() {
        questionNumber++;
        if (questionNumber < quizList.size()) {
            showQuiz();
        } else {
            // 全ての問題を解いてしまったので、結果を表示します。
            // 結果表示にはDialogを使います。
            //カウントダウンタイマーキャンセル
            countDownTimer.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(false);
            builder.setTitle("フィニッシュ！");
            builder.setMessage("お疲れさまでした。" + "\n" + "正解数：" + point);
            builder.setPositiveButton("リトライ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // もう一度クイズをやり直す
//                    resetQuiz();
                    //クイズ画面遷移のためのintent
                    Intent intent = new Intent(QuizActivity.this, QuizActivity.class);
                    // クイズ画面へ遷移
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("解説", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //解説画面遷移のためのintent
                    Intent intent = new Intent(QuizActivity.this, CommentaryActivity.class);
                    // commentaryListをintentに格納
                    intent.putStringArrayListExtra("解説リスト", commentaryList);
                    // 解説画面へ遷移
                    startActivity(intent);
                }
            });
            builder.show();
        }
    }

    // ⑦ setOnClickListerを呼び出したViewをクリックした時に呼び出されるメソッド
    @Override
    public void onClick(View view) {
        //音量調整
        AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        int maxVolume  = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        float volumeRate = (float)currentVolume / maxVolume;
        // 引数のViewには、押されたViewが入っています。
        // Buttonが押された時しかよばれないので、キャストといって型を示してあげます。
        Button clickedButton = (Button) view;
        quiz = quizList.get(questionNumber);
        // ボタンの文字と、答えが同じかチェックします。
        if (TextUtils.equals(clickedButton.getText(), quiz.answer)) {
            //効果音再生
            mSoundPool.play(mSoundResId, volumeRate * 0.2f, volumeRate * 0.2f, 0, 0, 1.0f);
            // 正解の場合だけ1ポイント加算します。
            point++;
            Toast.makeText(this, "正解！", Toast.LENGTH_SHORT).show();
            //第一引数には表示したいアクティビティのオブジェクト
        } else {
            //効果音再生
            mSoundPool.play(mSoundResId_fight, volumeRate * 0.2f, volumeRate * 0.2f, 0, 0, 1.0f);

            //不正解問題の問題文と解説リストに格納
            commentaryList.add("第" + (questionNumber + 1) + "問. " + quiz.commentaryAnswer + "\n" + quiz.commentary);
            if(TextUtils.equals(quiz.option1, quiz.answer)){
                Toast ts = Toast.makeText(this, "はずれ！" + "\n" + "正解は、" + quiz.option1 , Toast.LENGTH_SHORT);
                ts.setGravity(Gravity.BOTTOM, 0, 80);
                ts.show();
            }
            if(TextUtils.equals(quiz.option2, quiz.answer)){
                Toast ts = Toast.makeText(this, "はずれ！" + "\n" + "正解は、" + quiz.option2, Toast.LENGTH_SHORT);
                ts.setGravity(Gravity.BOTTOM, 0, 80);
                ts.show();
            }
            if(TextUtils.equals(quiz.option3, quiz.answer)){
                Toast ts = Toast.makeText(this, "はずれ！" + "\n" + "正解は、" + quiz.option3, Toast.LENGTH_SHORT);
                ts.setGravity(Gravity.BOTTOM, 0, 80);
                ts.show();
            }
        }
        // 次の問題にアップデートします。
        updateQuiz();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //noinspection deprecation
            mSoundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
        } else {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        mSoundResId = mSoundPool.load(this, R.raw.cat, 1);
        mSoundResId_fight = mSoundPool.load(this, R.raw.cat_fight, 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundPool.release();
    }

//    private void fadeout(){
//        // 透明度を1から0に変化
//        AlphaAnimation alphaFadeout = new AlphaAnimation(1.0f, 0.0f);
//        // animation時間 msec
//        alphaFadeout.setDuration(2000);
//        // animationが終わったそのまま表示にする
//        alphaFadeout.setFillAfter(true);
//
//        countTextView.startAnimation(alphaFadeout);
//    }
//
    private void fadein(){
        // 透明度を0から1に変化
        AlphaAnimation alphaFadeIn = new AlphaAnimation(0.0f, 1.0f);
        // animation時間 msec
        alphaFadeIn.setDuration(500);
        // animationが終わったそのまま表示にする
        alphaFadeIn.setFillAfter(true);

        countTextView.startAnimation(alphaFadeIn);
    }
//
//    private void startTranslate(){
//        // 設定を切り替え可能
//        int type = 0;
//        if(type == 0){
//            // TranslateAnimation(int fromXType, float fromXValue, int toXType, float toXValue, int fromYType, float fromYValue, int toYType, float toYValue)
//            translateAnimation = new TranslateAnimation(
//                    Animation.ABSOLUTE, 0.0f,
//                    Animation.ABSOLUTE, 500.0f,
//                    Animation.ABSOLUTE, 0.0f,
//                    Animation.ABSOLUTE, 1200.0f);
//        }
//        else if(type == 1){
//            translateAnimation = new TranslateAnimation(
//                    Animation.RELATIVE_TO_SELF, 0.0f,
//                    Animation.RELATIVE_TO_SELF, 0.9f,
//                    Animation.RELATIVE_TO_SELF, 0.0f,
//                    Animation.RELATIVE_TO_SELF, 1.8f);
//        }
//        else if(type ==2){
//            translateAnimation = new TranslateAnimation(
//                    Animation.RELATIVE_TO_PARENT, 0.0f,
//                    Animation.RELATIVE_TO_PARENT, 0.4f,
//                    Animation.RELATIVE_TO_PARENT, 0.0f,
//                    Animation.RELATIVE_TO_PARENT, 0.6f);
//        }
//
//        // animation時間 msec
//        translateAnimation.setDuration(2000);
//        // 繰り返し回数
//        translateAnimation.setRepeatCount(0);
//        // animationが終わったそのまま表示にする
//        translateAnimation.setFillAfter(true);
//        //アニメーションの開始
//        countTextView.startAnimation(translateAnimation);
//    }
//
//    private void startScaling(){
//        // ScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
//        scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f,2.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        // animation時間 msec
//        scaleAnimation.setDuration(100);
//        // 繰り返し回数
//        scaleAnimation.setRepeatCount(0);
//        // animationが終わったそのまま表示にする
//        scaleAnimation.setFillAfter(true);
//        //アニメーションの開始
//        tv.startAnimation(scaleAnimation);
//    }
//
//    private void endScaling(){
//        // ScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
//        scaleAnimation = new ScaleAnimation(2.0f, 1.0f, 2.0f,1.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        // animation時間 msec
//        scaleAnimation.setDuration(1000);
//        // 繰り返し回数
//        scaleAnimation.setRepeatCount(0);
//        // animationが終わったそのまま表示にする
//        scaleAnimation.setFillAfter(true);
//        //アニメーションの開始
//        tv.startAnimation(scaleAnimation);
//    }

    @Override
    public void onBackPressed() {
    }
}

