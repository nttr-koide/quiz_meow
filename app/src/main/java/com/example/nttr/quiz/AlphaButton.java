package com.example.nttr.quiz;

/**
 * Created by RikimaruKOIDE on 2017/12/16.
 */

import android.content.Context;
import android.util.AttributeSet;

/*** 半透明になるエフェクトのボタン ***/
public class AlphaButton extends android.support.v7.widget.AppCompatButton {
    public AlphaButton(Context context) {
        super(context);
    }

    public AlphaButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setPressed(boolean pressed) {
        if(pressed){
            this.setAlpha(0.75f);
        }else{
            this.setAlpha(1.0f);
        }
        super.setPressed(pressed);
    }

}
