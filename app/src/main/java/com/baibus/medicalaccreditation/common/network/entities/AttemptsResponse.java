package com.baibus.medicalaccreditation.common.network.entities;

import com.baibus.medicalaccreditation.common.db.entities.Attempt;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 05.06.2017
 * Time: 2:51
 * To change this template use File | settings | File Templates.
 */
public class AttemptsResponse {

    @SerializedName("testUnitAttempts")
    public List<UnitAttempts> data;

    public static class UnitAttempts {

        @SerializedName("testUnitAttemptId")
        public long id;
        @SerializedName("testUnitId")
        public long questionId;
        @SerializedName("testUnitAnswerId")
        public long answerId;
        @SerializedName("testUnitAttemptSubmitTime")
        public Date time;

        public Attempt getAttempt() {
            return Attempt.newInstance(id, questionId, answerId, time.getTime());
        }
    }
}
