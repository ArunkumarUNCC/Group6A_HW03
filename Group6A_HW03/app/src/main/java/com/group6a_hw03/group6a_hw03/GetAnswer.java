package com.group6a_hw03.group6a_hw03;

/**
 * Created by Arunkumar's on 9/24/2015.
 */
public class GetAnswer {
    int answer;
    String questionId;

    public GetAnswer(int answer, String questionId) {
        this.answer = answer;
        this.questionId = questionId;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
