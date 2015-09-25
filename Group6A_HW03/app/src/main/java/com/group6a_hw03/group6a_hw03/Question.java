package com.group6a_hw03.group6a_hw03;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Arunkumar's on 9/23/2015.
 */
public class Question {
    public String fQuestionId,fQuestions,fImageLinks;
    public LinkedList<String> fRadioGroupOptions;

    public Question(String fQuestionId, String fQuestions, LinkedList<String> fRadioGroupOptions, String fImageLinks) {
        this.fQuestionId = fQuestionId;
        this.fImageLinks = fImageLinks;
        this.fQuestions = fQuestions;
        this.fRadioGroupOptions = fRadioGroupOptions;
    }

    public String getfImageLinks() {
        return fImageLinks;
    }

    public void setfImageLinks(String fImageLinks) {
        this.fImageLinks = fImageLinks;
    }

    public String getfQuestionId() {
        return fQuestionId;
    }

    public void setfQuestionId(String fQuestionId) {
        this.fQuestionId = fQuestionId;
    }

    public String getfQuestions() {
        return fQuestions;
    }

    public void setfQuestions(String fQuestions) {
        this.fQuestions = fQuestions;
    }

    public LinkedList<String> getfRadioGroupOptions() {
        return fRadioGroupOptions;
    }

    public void setfRadioGroupOptions(LinkedList<String> fRadioGroupOptions) {
        this.fRadioGroupOptions = fRadioGroupOptions;
    }
}
