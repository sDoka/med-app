package com.baibus.medicalaccreditation.testing;

import com.baibus.medicalaccreditation.common.db.entities.Answer;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 01.05.2017
 * Time: 8:11
 * To change this template use File | settings | File Templates.
 */
public interface OnAnswerAttemptedListener {
    void onAnswerAttempted(Answer answer);
}
