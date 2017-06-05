package com.baibus.medicalaccreditation.common.db.resolvers;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Question;
import com.baibus.medicalaccreditation.common.db.tables.QuestionTable;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:26
 * To change this template use File | settings | File Templates.
 */
public class QuestionPutResolver extends DefaultPutResolver<Question> {

    @Override
    @NonNull
    protected InsertQuery mapToInsertQuery(@NonNull Question object) {
        return InsertQuery.builder()
                .table(QuestionTable.TABLE)
                .build();
    }

    @Override
    @NonNull
    protected UpdateQuery mapToUpdateQuery(@NonNull Question object) {
        return UpdateQuery.builder()
                .table(QuestionTable.TABLE)
                .where(QuestionTable.COLUMN_ID + " = ?")
                .whereArgs(object.getId())
                .build();
    }

    @Override
    @NonNull
    public ContentValues mapToContentValues(@NonNull Question object) {
        ContentValues contentValues = new ContentValues(3);

        contentValues.put(QuestionTable.COLUMN_ID, object.getId());
        contentValues.put(QuestionTable.COLUMN_INDEX, object.getIndex());
        contentValues.put(QuestionTable.COLUMN_SPECIALIZATION_ID, object.getSpecializationId());
        contentValues.put(QuestionTable.COLUMN_TEXT, object.getText());
        contentValues.put(QuestionTable.COLUMN_CODE, object.getCode());
        contentValues.put(QuestionTable.COLUMN_FILE_PATH, object.getFilePath());
        contentValues.put(QuestionTable.COLUMN_TYPE, object.getType());
        contentValues.put(QuestionTable.COLUMN_CREATED, object.getCreateDate());
        contentValues.put(QuestionTable.COLUMN_MODIFIED, object.getModifiedDate());

        return contentValues;
    }
}
