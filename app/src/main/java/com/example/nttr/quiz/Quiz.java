package com.example.nttr.quiz;

/**
 * Created by nttr on 2017/12/07.
 */

public class Quiz {
    // 問題
    String content;
    // 選択肢
    String option1;
    String option2;
    String option3;
    // 答えの文字
    String answer;
    // 問題の答え(解説用)
    String commentaryAnswer;
    // 問題の解説
    String commentary;


    // コンストラクタ
    // Ctrl + Enterキーで、GeneratorからConstructorを生成します。
    public Quiz(String content, String option1, String option2, String option3, String answer, String commentaryAnswer, String commentary) {
        this.content = content;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answer = answer;
        this.commentaryAnswer = commentaryAnswer;
        this.commentary = commentary;
    }
}