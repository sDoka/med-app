package com.baibus.medicalaccreditation.common.network.entities;

import com.baibus.medicalaccreditation.common.db.entities.Answer;
import com.baibus.medicalaccreditation.common.db.entities.Question;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 19.05.2017
 * Time: 0:21
 * To change this template use File | settings | File Templates.
 */
public class QuestionResponse {

    @SerializedName("modifiedTestUnits")
    public List<ModifiedQuestion> data;

    public static class ModifiedQuestion {

        @SerializedName("testUnitId")
        public long questionId;
        @SerializedName("testUnitNumber")
        public long questionIndex;
        @SerializedName("testUnitCode")
        public String code;
        @SerializedName("testUnitSpecializationId")
        public long questionSpecializationId;
        @SerializedName("testUnitText")
        public String questionText;
        @SerializedName("testUnitDLFileEntryId")
        public String questionFilePath;
        @SerializedName("testUnitType")
        public int type;
        @SerializedName("testUnitCreateDate")
        public Date createDate;
        @SerializedName("testUnitModifiedDate")
        public Date modifiedDate;
        @SerializedName("testUnitAnswers")
        public List<Answer> answers;

        public Question getQuestion() {
            return Question.newInstance(questionId, questionIndex, questionSpecializationId, code,
                    questionText, questionFilePath, type, createDate.getTime(), modifiedDate.getTime());
        }
    }
}
