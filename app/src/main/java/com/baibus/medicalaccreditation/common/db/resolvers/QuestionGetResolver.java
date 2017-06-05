package com.baibus.medicalaccreditation.common.db.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Question;
import com.baibus.medicalaccreditation.common.db.tables.QuestionTable;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:39
 * To change this template use File | settings | File Templates.
 */
public class QuestionGetResolver extends DefaultGetResolver<Question> {

    @Override
    @NonNull
    public Question mapFromCursor(@NonNull Cursor cursor) {

        long id = cursor.getLong(cursor.getColumnIndexOrThrow(QuestionTable.COLUMN_ID));
        long index = cursor.getLong(cursor.getColumnIndexOrThrow(QuestionTable.COLUMN_INDEX));
        long specializationId = cursor.getLong(cursor.getColumnIndexOrThrow(QuestionTable.COLUMN_SPECIALIZATION_ID));
        String text = cursor.getString(cursor.getColumnIndexOrThrow(QuestionTable.COLUMN_TEXT));
        String code = cursor.getString(cursor.getColumnIndexOrThrow(QuestionTable.COLUMN_CODE));
        int column = cursor.getColumnIndexOrThrow(QuestionTable.COLUMN_FILE_PATH);
        String filePath = cursor.isNull(column) ? null : cursor.getString(column);
        int type = cursor.getColumnIndexOrThrow(QuestionTable.COLUMN_TYPE);
        long createDate = cursor.getLong(cursor.getColumnIndexOrThrow(QuestionTable.COLUMN_CREATED));
        long modifiedDate = cursor.getLong(cursor.getColumnIndexOrThrow(QuestionTable.COLUMN_MODIFIED));

        return Question.newInstance(id, index, specializationId, code, text, filePath, type, createDate, modifiedDate);
    }
}
