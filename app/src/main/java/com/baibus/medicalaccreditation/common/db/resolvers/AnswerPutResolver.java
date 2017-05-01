package com.baibus.medicalaccreditation.common.db.resolvers;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Answer;
import com.baibus.medicalaccreditation.common.db.tables.AnswerTable;
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
public class AnswerPutResolver extends DefaultPutResolver<Answer> {

    @Override
    @NonNull
    protected InsertQuery mapToInsertQuery(@NonNull Answer object) {
        return InsertQuery.builder()
                .table(AnswerTable.TABLE)
                .build();
    }

    @Override
    @NonNull
    protected UpdateQuery mapToUpdateQuery(@NonNull Answer object) {
        return UpdateQuery.builder()
                .table(AnswerTable.TABLE)
                .where("id = ?")
                .whereArgs(object.getId())
                .build();
    }

    @Override
    @NonNull
    public ContentValues mapToContentValues(@NonNull Answer object) {
        ContentValues contentValues = new ContentValues(3);

        contentValues.put(AnswerTable.COLUMN_ID, object.getId());
        contentValues.put(AnswerTable.COLUMN_QUESTION_ID, object.getQuestionId());
        contentValues.put(AnswerTable.COLUMN_TEXT, object.getText());
        contentValues.put(AnswerTable.COLUMN_FILE_PATH, object.getFilePath());
        contentValues.put(AnswerTable.COLUMN_IS_CORRECT, object.isCorrect() ? 1 : 0);

        return contentValues;
    }
}
