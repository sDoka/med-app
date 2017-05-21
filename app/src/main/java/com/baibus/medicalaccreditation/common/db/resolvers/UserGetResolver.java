package com.baibus.medicalaccreditation.common.db.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.User;
import com.baibus.medicalaccreditation.common.db.tables.UserTable;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

import java.util.Date;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:39
 * To change this template use File | settings | File Templates.
 */
public class UserGetResolver extends DefaultGetResolver<User> {

    @Override
    @NonNull
    public User mapFromCursor(@NonNull Cursor cursor) {

        long id = cursor.getLong(cursor.getColumnIndexOrThrow(UserTable.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_NAME));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_EMAIL));
        Date lastSynchronization = new Date(cursor.getLong(
                cursor.getColumnIndexOrThrow(UserTable.COLUMN_LAST_SYNCHRONIZATION)));
        long deviceId = cursor.getLong(cursor.getColumnIndexOrThrow(UserTable.COLUMN_DEVICE_ID));

        return User.newInstance(id, name, email, lastSynchronization, deviceId);
    }
}
