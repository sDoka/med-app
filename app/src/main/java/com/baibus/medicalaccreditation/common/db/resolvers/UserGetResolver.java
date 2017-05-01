package com.baibus.medicalaccreditation.common.db.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.User;
import com.baibus.medicalaccreditation.common.db.tables.UserTable;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

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

        return User.newUser(id, name, email);
    }
}
