package com.telesoftas.retrofitimpl.data.entities;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2015-11-02.
 */
public class Choice {

    private String text;
    private boolean checked;
    private long questionId;

    public Choice() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

}
