package com.baibus.medicalaccreditation.common.db.resolvers;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Attempt;
import com.baibus.medicalaccreditation.common.db.tables.AttemptTable;
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
public class AttemptPutResolver extends DefaultPutResolver<Attempt> {

    @Override
    @NonNull
    protected InsertQuery mapToInsertQuery(@NonNull Attempt object) {
        return InsertQuery.builder()
                .table(AttemptTable.TABLE)
                .build();
    }

    @Override
    @NonNull
    protected UpdateQuery mapToUpdateQuery(@NonNull Attempt object) {
        return UpdateQuery.builder()
                .table(AttemptTable.TABLE)
                .where("id = ?")
                .whereArgs(object.getId())
                .build();
    }

    @Override
    @NonNull
    public ContentValues mapToContentValues(@NonNull Attempt object) {
        ContentValues contentValues = new ContentValues(3);

        contentValues.put(AttemptTable.COLUMN_ID, object.getId());
        contentValues.put(AttemptTable.COLUMN_QUESTION_ID, object.getQuestionId());
        contentValues.put(AttemptTable.COLUMN_ANSWER_ID, object.getAnswerId());
        contentValues.put(AttemptTable.COLUMN_TIME, object.getTime());

        return contentValues;
    }
}
