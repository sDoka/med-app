package com.baibus.medicalaccreditation.common.db.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Attempt;
import com.baibus.medicalaccreditation.common.db.tables.AttemptTable;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;


/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:39
 * To change this template use File | settings | File Templates.
 */
public class AttemptGetResolver extends DefaultGetResolver<Attempt> {

    @Override
    @NonNull
    public Attempt mapFromCursor(@NonNull Cursor cursor) {

        long id = cursor.getLong(cursor.getColumnIndexOrThrow(AttemptTable.COLUMN_ID));
        long questionId = cursor.getLong(cursor.getColumnIndexOrThrow(AttemptTable.COLUMN_QUESTION_ID));
        long answerId = cursor.getLong(cursor.getColumnIndexOrThrow(AttemptTable.COLUMN_ANSWER_ID));
        long time = cursor.getLong(cursor.getColumnIndexOrThrow(AttemptTable.COLUMN_TIME));

        return Attempt.newInstance(id, questionId, answerId, time);
    }
}
