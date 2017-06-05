package com.baibus.medicalaccreditation.common.db.resolvers;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Account;
import com.baibus.medicalaccreditation.common.db.tables.AccountTable;
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
public class AccountPutResolver extends DefaultPutResolver<Account> {

    @Override
    @NonNull
    protected InsertQuery mapToInsertQuery(@NonNull Account object) {
        return InsertQuery.builder()
                .table(AccountTable.TABLE)
                .build();
    }

    @Override
    @NonNull
    protected UpdateQuery mapToUpdateQuery(@NonNull Account object) {
        return UpdateQuery.builder()
                .table(AccountTable.TABLE)
                .where(AccountTable.COLUMN_USER_ID + " = ? AND ? = " + AccountTable.COLUMN_TYPE)
                .whereArgs(object.getUserId(), object.getType())
                .build();
    }

    @Override
    @NonNull
    public ContentValues mapToContentValues(@NonNull Account object) {
        ContentValues contentValues = new ContentValues(3);

        contentValues.put(AccountTable.COLUMN_USER_ID, object.getUserId());
        contentValues.put(AccountTable.COLUMN_TYPE, object.getType());
        contentValues.put(AccountTable.COLUMN_ACTIVATION, object.getActivationDate());
        contentValues.put(AccountTable.COLUMN_EXPIRATION, object.getExpirationDate());

        return contentValues;
    }
}
