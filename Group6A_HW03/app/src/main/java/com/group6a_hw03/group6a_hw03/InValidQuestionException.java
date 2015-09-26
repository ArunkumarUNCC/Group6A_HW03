package com.group6a_hw03.group6a_hw03;

/**
 * Created by Arunkumar's on 9/26/2015.
 */
public class InValidQuestionException extends Exception {
    String exceptionReason,exceptionQuestion;

    public InValidQuestionException(String exceptionReason,String exceptionQuestion) {
        this.exceptionReason = exceptionReason;
        this.exceptionQuestion = exceptionQuestion;
    }

    public String getExceptionReason() {
        return exceptionReason;
    }

    public void setExceptionReason(String exceptionReason) {
        this.exceptionReason = exceptionReason;
    }

    public String getExceptionQuestion() {
        return exceptionQuestion;
    }

    public void setExceptionQuestion(String exceptionQuestion) {
        this.exceptionQuestion = exceptionQuestion;
    }
}
