package com.example.nttr.quiz;

/**
 * Created by nttr on 2017/12/13.
 */

public class ListData {
    String content;
    String option1;
    String option2;
    String option3;
    String answer;
    String commentaryAnswer;
    String commentary;

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }

    public void setOption1 (String option1){
        this.option1 = option1;
    }

    public String getOption1(){
        return this.option1;
    }

    public void setOption2(String option2){
        this.option2 = option2;
    }

    public  String getOption2(){
        return this.option2;
    }

    public void setOption3(String option3){
        this.option3 = option3;
    }

    public String getOption3(){
        return this.option3;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public String getAnswer(){
        return this.answer;
    }

    public void setCommentaryAnswer(String commentaryAnswer){
        this.commentaryAnswer = commentaryAnswer;
    }

    public String getCommentaryAnswer(){
        return this.commentaryAnswer;
    }

    public void setCommentary(String commentary){
        this.commentary = commentary;
    }

    public String getCommentary(){
        return this.commentary;
    }
}
