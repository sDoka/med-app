package com.baibus.medicalaccreditation.common.db.tables;

import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Account;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 21.04.2017
 * Time: 16:13
 * To change this template use File | settings | File Templates.
 */
public class AccountTable {
    private AccountTable() {
        throw new AssertionError();
    }

    public static final String TABLE = "account";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_ACTIVATION = "activationDate";
    public static final String COLUMN_EXPIRATION = "expirationDate";

    @NonNull
    public static DeleteQuery deleteQuery(@NonNull Account account) {
        return DeleteQuery.builder()
                .table(TABLE)
                .where("user_id != ?")
                .whereArgs(account.getUserId())
                .build();
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_USER_ID + " INTEGER NOT NULL, "
                + COLUMN_TYPE + " INTEGER NOT NULL,"
                + COLUMN_ACTIVATION + " INTEGER NOT NULL,"
                + COLUMN_EXPIRATION + " INTEGER NOT NULL,"
                + "PRIMARY KEY (" + COLUMN_USER_ID + ", " + COLUMN_TYPE + ")"
                + ");";
    }
}
