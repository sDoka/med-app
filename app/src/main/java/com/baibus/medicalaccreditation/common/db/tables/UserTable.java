package com.baibus.medicalaccreditation.common.db.tables;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 21.04.2017
 * Time: 16:13
 * To change this template use File | settings | File Templates.
 */
public class UserTable {
    private UserTable() {
        throw new AssertionError();
    }

    public static final String TABLE = "user";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";

    @NonNull
    public static final Query QUERY = Query.builder()
            .table(TABLE)
            .limit(1)
            .build();

    @NonNull
    public static final DeleteQuery DELETE_ALL = DeleteQuery.builder()
            .table(UserTable.TABLE)
            .build();


    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_NAME + " VARCHAR (100) NOT NULL, "
                + COLUMN_EMAIL + " VARCHAR (200) NOT NULL"
                + ");";
    }
}
