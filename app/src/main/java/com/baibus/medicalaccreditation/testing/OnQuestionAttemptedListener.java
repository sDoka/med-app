package com.baibus.medicalaccreditation.testing;

import com.baibus.medicalaccreditation.common.db.entities.Question;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 01.05.2017
 * Time: 8:30
 * To change this template use File | settings | File Templates.
 */
public interface OnQuestionAttemptedListener {
    void onQuestionAttempted(Question question, int position);
}
