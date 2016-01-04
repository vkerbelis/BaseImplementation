package com.telesoftas.retrofitimpl.data.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2015-11-02.
 */
public class Question {

    public static final String TYPE_TEXT = "text";
    public static final String TYPE_SINGLE_CHOICE = "radio";
    public static final String TYPE_MULTI_CHOICE = "multiple_choice";

    private String type;
    private String text;
    private int allowedAnswers;
    private long surveyId;
    private List<Choice> choices;

    public Question() {
        this((Long) null);
    }

    public Question(Long surveyId) {
        this.surveyId = surveyId == null ? 0 : surveyId;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        if (this.allowedAnswers == 0) {
            this.allowedAnswers = 1;
        }
        if (TYPE_TEXT.equals(type)) {
            choices = new ArrayList<>();
            choices.add(new Choice());
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAllowedAnswers() {
        return allowedAnswers;
    }

    public void setAllowedAnswers(int allowedAnswers) {
        this.allowedAnswers = allowedAnswers;
    }

    public long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(long surveyId) {
        this.surveyId = surveyId;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

}
