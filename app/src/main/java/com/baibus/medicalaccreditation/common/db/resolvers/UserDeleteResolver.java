package com.baibus.medicalaccreditation.common.db.resolvers;

import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.User;
import com.baibus.medicalaccreditation.common.db.tables.UserTable;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:39
 * To change this template use File | settings | File Templates.
 */
public class UserDeleteResolver extends DefaultDeleteResolver<User> {

    @Override
    @NonNull
    protected DeleteQuery mapToDeleteQuery(@NonNull User object) {
        return DeleteQuery.builder()
                .table(UserTable.TABLE)
                .where("id = ?")
                .whereArgs(object.getId())
                .build();
    }
}