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
public class RemovedQuestionResponse {

    @SerializedName("removedTestUnitIds")
    public List<RemovedQuestion> data;

    public static class RemovedQuestion {
        @SerializedName("testUnitId")
        public Long id;
    }
}
