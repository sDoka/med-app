package com.baibus.medicalaccreditation.common.network.entities;

import com.baibus.medicalaccreditation.common.db.entities.Answer;
import com.baibus.medicalaccreditation.common.db.entities.Question;

import java.util.List;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 19.05.2017
 * Time: 0:21
 * To change this template use File | settings | File Templates.
 */
public class QuestionResponse {

    public long questionId;
    public long questionIndex;
    public long questionSpecializationId;
    public String questionText;
    public String questionFilePath;
    public List<Answer> answers;

    public Question getQuestion() {
        return Question.newInstance(questionId, questionIndex, questionSpecializationId,
                questionText, questionFilePath);
    }
}
