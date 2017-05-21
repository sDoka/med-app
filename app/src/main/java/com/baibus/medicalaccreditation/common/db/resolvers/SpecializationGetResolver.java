package com.baibus.medicalaccreditation.common.db.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.baibus.medicalaccreditation.common.db.tables.SpecializationTable;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:39
 * To change this template use File | settings | File Templates.
 */
public class SpecializationGetResolver extends DefaultGetResolver<Specialization> {

    @Override
    @NonNull
    public Specialization mapFromCursor(@NonNull Cursor cursor) {

        long id = cursor.getLong(cursor.getColumnIndexOrThrow(SpecializationTable.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(SpecializationTable.COLUMN_NAME));
        String filePath = cursor.getString(cursor.getColumnIndexOrThrow(SpecializationTable.COLUMN_FILE_PATH));

        return Specialization.newInstance(id, name, filePath);
    }
}
