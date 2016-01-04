package com.telesoftas.retrofitimpl.data.entities;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.telesoftas.retrofitimpl.data.repositories.RepositoryEntity;
import com.telesoftas.retrofitimpl.utils.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2015-10-29.
 */
public class Survey extends RepositoryEntity implements BaseColumns {

    public static final int TYPE_ACTIVE = 0x00;
    public static final int TYPE_COMPLETED = 0x01;
    public static final int STATUS_NEW = 0x00;
    public static final int STATUS_NOT_SENT = 0x01;
    public static final int STATUS_SUBMITTED = 0x02;
    public static final int STATUS_SENT = 0x03;
    public static final String TABLE_NAME = "surveys";
    public static final String COLUMN_TITLE = "title";
    public int status;
    private String title;
    private String description;
    private int type;
    private long date;
    private long userId;
    private List<Question> questions;

    public Survey() {
        this.date = System.currentTimeMillis();
    }

    public Survey(String title, String description) {
        this();
        this.title = title;
        this.description = description;
        this.type = TYPE_ACTIVE;
    }

    private static Survey extractSurvey(JsonElement jsonElement) {
        Survey survey = new Survey();
        JsonObject jsonObject = (JsonObject) jsonElement;
        survey.setTitle(jsonObject.get("title").getAsString());
        survey.setDescription(jsonObject.get("description").getAsString());
        JsonElement answered = jsonObject.get("already_answered_by_user");
        if (answered != null) {
            survey.setType(answered.getAsBoolean() ? Survey.TYPE_COMPLETED : TYPE_ACTIVE);
        } else {
            survey.setType(Survey.TYPE_ACTIVE);
        }
        survey.setStatus(STATUS_NEW);
        JsonElement questionsElement = jsonObject.get("questions");
        if (questionsElement instanceof JsonArray) {
            JsonArray questionsArray = questionsElement.getAsJsonArray();
            if (questionsArray.size() > 0) {
                List<Question> questionList = new ArrayList<>();
                for (JsonElement questionElement : questionsArray) {
                    Question question = extractQuestion(questionElement);
                    questionList.add(question);
                }
                survey.setQuestions(questionList);
            }
        }
        return survey;
    }

    private static Question extractQuestion(JsonElement jsonElement) {
        Question question = new Question();
        JsonObject questionObject = (JsonObject) jsonElement;
        question.setText(questionObject.get("question_text").getAsString());
        question.setType(questionObject.get("question_type").getAsString());
        JsonElement allowedAnswers = questionObject.get("number_of_allowed_answers");
        if (allowedAnswers != null) {
            question.setAllowedAnswers(allowedAnswers.getAsInt());
        }
        if (!question.getType().equals(Question.TYPE_TEXT)) {
            JsonArray choicesArray = questionObject.get("choices").getAsJsonArray();
            if (choicesArray.size() > 0) {
                List<Choice> choiceList = new ArrayList<>();
                for (JsonElement choiceElement : choicesArray) {
                    Choice choice = extractChoice(choiceElement);
                    choiceList.add(choice);
                }
                question.setChoices(choiceList);
            }
        }
        return question;
    }

    private static Choice extractChoice(JsonElement jsonElement) {
        Choice choice = new Choice();
        JsonObject choiceObject = (JsonObject) jsonElement;
        choice.setText(choiceObject.get("choice_text").getAsString());
        return choice;
    }

    @Override
    protected ContentValues extractContentValues() {
        return null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        this.date = System.currentTimeMillis();
    }

    public long getDate() {
        return date;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public static class SurveyListDeserializer implements JsonDeserializer<List<Survey>> {

        @Override
        public List<Survey> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Logger.d("SurveyTAG", "Deserializing");
            if (json instanceof JsonArray) {
                JsonArray jsonArray = (JsonArray) json;
                if (jsonArray.size() > 0) {
                    List<Survey> list = new ArrayList<>();
                    for (JsonElement jsonElement : jsonArray) {
                        Survey survey = extractSurvey(jsonElement);
                        list.add(survey);
                    }
                    return list;
                }
            }
            return null;
        }
    }

    public static class SurveyDeserializer implements JsonDeserializer {
        @Override
        public Survey deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return extractSurvey(json);
        }
    }
}
