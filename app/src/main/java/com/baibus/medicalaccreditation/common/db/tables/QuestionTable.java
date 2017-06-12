package com.baibus.medicalaccreditation.common.db.tables;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import static com.baibus.medicalaccreditation.common.db.tables.AttemptTable.COLUMN_QUESTION_ID;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 21.04.2017
 * Time: 16:13
 * To change this template use File | settings | File Templates.
 */
public class QuestionTable {
    private QuestionTable() {
        throw new AssertionError();
    }

    public static final String TABLE = "question";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INDEX = "sequence";
    public static final String COLUMN_SPECIALIZATION_ID = "specializationId";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_FILE_PATH = "filePath";
    public static final String COLUMN_TYPE = "typeId";
    public static final String COLUMN_CREATED = "createDate";
    public static final String COLUMN_MODIFIED = "modifiedDate";
    public static final String COLUMN_CODE = "code";

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

    public static @NonNull DeleteQuery deleteIds(String ids) {
        return DeleteQuery.builder()
                .table(TABLE)
                .where(COLUMN_ID + " IN (?)")
                .whereArgs(ids)
                .build();
    }

    public static @NonNull Query querySpecializationId(long id) {
        return Query.builder()
                .table(TABLE)
                .where(COLUMN_SPECIALIZATION_ID + " = ?")
                .whereArgs(id)
                .build();
    }

    public static @NonNull Query queryNotAttempted(long id) {
        return Query.builder()
                .table(TABLE)
                .where(COLUMN_SPECIALIZATION_ID + " = ? AND " + COLUMN_ID + " NOT IN (SELECT " + COLUMN_QUESTION_ID + " FROM " + AttemptTable.TABLE + ")")
                .whereArgs(id)
                .build();
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_INDEX + " INTEGER NOT NULL, "
                + COLUMN_SPECIALIZATION_ID + " INTEGER NOT NULL, "
                + COLUMN_TEXT + " TEXT NOT NULL, "
                + COLUMN_TYPE + " INTEGER NOT NULL, "
                + COLUMN_CREATED + " INTEGER NOT NULL, "
                + COLUMN_MODIFIED + " INTEGER NOT NULL, "
                + COLUMN_FILE_PATH + " VARCHAR (200) NULL, "
                + COLUMN_CODE + " VARCHAR (200) NOT NULL"
                + ");";
    }
}
