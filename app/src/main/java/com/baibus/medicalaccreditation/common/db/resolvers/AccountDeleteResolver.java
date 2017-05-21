package com.baibus.medicalaccreditation.common.db.resolvers;

import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Account;
import com.baibus.medicalaccreditation.common.db.tables.AccountTable;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:39
 * To change this template use File | settings | File Templates.
 */
public class AccountDeleteResolver extends DefaultDeleteResolver<Account> {

    @Override
    @NonNull
    protected DeleteQuery mapToDeleteQuery(@NonNull Account object) {
        return DeleteQuery.builder()
                .table(AccountTable.TABLE)
                .where(AccountTable.COLUMN_USER_ID + " = ? AND ? = " + AccountTable.COLUMN_TYPE)
                .whereArgs(object.getUserId(), object.getType())
                .build();
    }
}