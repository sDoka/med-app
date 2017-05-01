package com.baibus.medicalaccreditation.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.tables.*;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 21.04.2017
 * Time: 16:10
 * To change this template use File | settings | File Templates.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "med.db";

    public DbOpenHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(SpecializationTable.getCreateTableQuery());
        db.execSQL(QuestionTable.getCreateTableQuery());
        db.execSQL(AnswerTable.getCreateTableQuery());
        db.execSQL(UserTable.getCreateTableQuery());
        db.execSQL(AttemptTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // no impl
    }
}
