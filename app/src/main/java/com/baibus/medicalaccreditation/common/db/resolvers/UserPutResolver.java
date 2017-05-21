package com.baibus.medicalaccreditation.common.db.resolvers;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.User;
import com.baibus.medicalaccreditation.common.db.tables.UserTable;
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
public class UserPutResolver extends DefaultPutResolver<User> {

    @Override
    @NonNull
    protected InsertQuery mapToInsertQuery(@NonNull User object) {
        return InsertQuery.builder()
                .table(UserTable.TABLE)
                .build();
    }

    @Override
    @NonNull
    protected UpdateQuery mapToUpdateQuery(@NonNull User object) {
        return UpdateQuery.builder()
                .table(UserTable.TABLE)
                .where(UserTable.COLUMN_ID + " = ?")
                .whereArgs(object.getId())
                .build();
    }

    @Override
    @NonNull
    public ContentValues mapToContentValues(@NonNull User object) {
        ContentValues contentValues = new ContentValues(3);

        contentValues.put(UserTable.COLUMN_ID, object.getId());
        contentValues.put(UserTable.COLUMN_NAME, object.getName());
        contentValues.put(UserTable.COLUMN_EMAIL, object.getEmail());
        contentValues.put(UserTable.COLUMN_LAST_SYNCHRONIZATION, object.getLastSynchronization().getTime());
        contentValues.put(UserTable.COLUMN_DEVICE_ID, object.getDeviceId());

        return contentValues;
    }
}
