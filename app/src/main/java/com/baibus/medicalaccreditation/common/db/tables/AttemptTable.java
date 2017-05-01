package com.baibus.medicalaccreditation.common.db.tables;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.queries.Query;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 21.04.2017
 * Time: 16:13
 * To change this template use File | settings | File Templates.
 */
public class AttemptTable {
    private AttemptTable() {
        throw new AssertionError();
    }

    public static final String TABLE = "attempt";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_QUESTION_ID = "questionId";
    public static final String COLUMN_ANSWER_ID = "answerId";
    public static final String COLUMN_TIME = "submitTime";

    // Yep, with StorIO you can safely store queries as objects and reuse them, they are immutable
    @NonNull
    public static final Query QUERY_ALL = Query.builder()
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
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_QUESTION_ID + " INTEGER NOT NULL, "
                + COLUMN_ANSWER_ID + " INTEGER NOT NULL, "
                + COLUMN_TIME + " INTEGER NOT NULL"
                + ");";
    }
}