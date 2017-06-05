package com.baibus.medicalaccreditation.common.db.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Answer;
import com.baibus.medicalaccreditation.common.db.tables.AnswerTable;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:39
 * To change this template use File | settings | File Templates.
 */
public class AnswerGetResolver extends DefaultGetResolver<Answer> {

    @Override
    @NonNull
    public Answer mapFromCursor(@NonNull Cursor cursor) {

        long id = cursor.getLong(cursor.getColumnIndexOrThrow(AnswerTable.COLUMN_ID));
        long questionId = cursor.getLong(cursor.getColumnIndexOrThrow(AnswerTable.COLUMN_QUESTION_ID));
        String text = cursor.getString(cursor.getColumnIndexOrThrow(AnswerTable.COLUMN_TEXT));
        int column = cursor.getColumnIndexOrThrow(AnswerTable.COLUMN_FILE_PATH);
        int type = cursor.getColumnIndexOrThrow(AnswerTable.COLUMN_TYPE);
        String filePath = cursor.isNull(column) ? null : cursor.getString(column);
        int isCorrect = cursor.getInt(cursor.getColumnIndexOrThrow(AnswerTable.COLUMN_IS_CORRECT));

        return Answer.newInstance(id, questionId, text, type, filePath, isCorrect == 1);
    }
}
