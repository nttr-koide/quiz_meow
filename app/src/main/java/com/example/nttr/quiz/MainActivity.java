package com.example.nttr.quiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mMediaPlayer;
//    //効果音
//    SoundPool mSoundPool;
//    int mSoundResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMediaPlayer = MediaPlayer.create(this, R.raw.getdown);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        Button button = (Button) findViewById(R.id.start_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mSoundPool.play(mSoundResId, 1.0f,1.0f, 0, 0, 1.0f);
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            //noinspection deprecation
//            mSoundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
//        } else {
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .build();
//            mSoundPool = new SoundPool.Builder()
//                    .setMaxStreams(1)
//                    .setAudioAttributes(audioAttributes)
//                    .build();
//        }
//        mSoundResId = mSoundPool.load(this, R.raw.cat, 1);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mSoundPool.release();
//    }

    @Override
    public void onBackPressed() {
    }
}
