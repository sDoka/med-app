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
public class AnswerTable {
    private AnswerTable() {
        throw new AssertionError();
    }

    public static final String TABLE = "answer";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_QUESTION_ID = "questionId";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_FILE_PATH = "filePath";
    public static final String COLUMN_IS_CORRECT = "isCorrect";

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

    public static @NonNull
    DeleteQuery deleteIds(String ids) {
        return DeleteQuery.builder()
                .table(TABLE)
                .where(COLUMN_QUESTION_ID + " IN (?)")
                .whereArgs(ids)
                .build();
    }

    public static @NonNull Query queryQuestionId(long id) {
        return Query.builder()
                .table(TABLE)
                .where(COLUMN_QUESTION_ID + " = ?")
                .whereArgs(id)
                .build();
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_QUESTION_ID + " INTEGER NOT NULL, "
                + COLUMN_TEXT + " TEXT NOT NULL, "
                + COLUMN_FILE_PATH + " VARCHAR (200) NULL, "
                + COLUMN_IS_CORRECT + " SMALLINT NOT NULL"
                + ");";
    }
}
