package com.baibus.medicalaccreditation.common.db.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Account;
import com.baibus.medicalaccreditation.common.db.tables.AccountTable;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:39
 * To change this template use File | settings | File Templates.
 */
public class AccountGetResolver extends DefaultGetResolver<Account> {

    @Override
    @NonNull
    public Account mapFromCursor(@NonNull Cursor cursor) {

        long user_id = cursor.getLong(cursor.getColumnIndexOrThrow(AccountTable.COLUMN_USER_ID));
        long type = cursor.getLong(cursor.getColumnIndexOrThrow(AccountTable.COLUMN_TYPE));
        long activationDate = cursor.getLong(cursor.getColumnIndexOrThrow(AccountTable.COLUMN_ACTIVATION));
        long expirationDate = cursor.getLong(cursor.getColumnIndexOrThrow(AccountTable.COLUMN_EXPIRATION));

        return Account.newInstance(user_id, type, activationDate, expirationDate);
    }
}
