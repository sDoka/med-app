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
public class SpecializationTable {
    private SpecializationTable() {
        throw new AssertionError();
    }

    public static final String TABLE = "specialization";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FILE_PATH = "filePath";

    // Yep, with StorIO you can safely store queries as objects and reuse them, they are immutable
    @NonNull
    public static final Query QUERY_ALL = Query.builder()
            .table(TABLE)
            .build();

    @NonNull
    public static final DeleteQuery DELETE_ALL = DeleteQuery.builder()
            .table(TABLE)
            .build();

    public static @NonNull Query queryId(long id) {
        return Query.builder()
                .table(TABLE)
                .where(COLUMN_ID + " = ?")
                .whereArgs(id)
                .build();
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_NAME + " VARCHAR (100) NOT NULL, "
                + COLUMN_FILE_PATH + " VARCHAR (200) NOT NULL"
                + ");";
    }
}
