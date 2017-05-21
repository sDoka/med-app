package com.baibus.medicalaccreditation.common.db.resolvers;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.baibus.medicalaccreditation.common.db.tables.SpecializationTable;
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
public class SpecializationPutResolver extends DefaultPutResolver<Specialization> {

    @Override
    @NonNull
    protected InsertQuery mapToInsertQuery(@NonNull Specialization object) {
        return InsertQuery.builder()
                .table(SpecializationTable.TABLE)
                .build();
    }

    @Override
    @NonNull
    protected UpdateQuery mapToUpdateQuery(@NonNull Specialization object) {
        return UpdateQuery.builder()
                .table(SpecializationTable.TABLE)
                .where(SpecializationTable.COLUMN_ID + " = ?")
                .whereArgs(object.getId())
                .build();
    }

    @Override
    @NonNull
    public ContentValues mapToContentValues(@NonNull Specialization object) {
        ContentValues contentValues = new ContentValues(3);

        contentValues.put(SpecializationTable.COLUMN_ID, object.getId());
        contentValues.put(SpecializationTable.COLUMN_NAME, object.getName());
        contentValues.put(SpecializationTable.COLUMN_FILE_PATH, object.getFilePath());

        return contentValues;
    }
}
