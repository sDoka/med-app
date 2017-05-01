package com.baibus.medicalaccreditation.common.provider;

import android.content.Context;

import com.baibus.medicalaccreditation.MedApplication;
import com.baibus.medicalaccreditation.common.db.DbOpenHelper;
import com.baibus.medicalaccreditation.common.db.entities.*;
import com.baibus.medicalaccreditation.common.db.resolvers.*;
import com.baibus.medicalaccreditation.common.db.tables.SpecializationTable;
import com.baibus.medicalaccreditation.common.db.tables.UserTable;
import com.pushtorefresh.storio.sqlite.Changes;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import rx.functions.Action1;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:06
 * To change this template use File | settings | File Templates.
 */
class StorIOProvider {
    private StorIOProvider() {
        throw new AssertionError();
    }

    private static volatile StorIOSQLite instance;

    private final static Set<String> tables;

    private final static Action1<Changes> observerChanges = changes -> {
        if (changes.affectedTables().contains(UserTable.TABLE)) {
            MedApplication.getInstance().reloadUser();
        }
        if (changes.affectedTables().contains(SpecializationTable.TABLE)) {
            MedApplication.getInstance().reloadSpecialization();
        }
    };

    static {
        HashSet<String> strings = new HashSet<>();
        strings.add(SpecializationTable.TABLE);
        strings.add(UserTable.TABLE);
        tables = Collections.unmodifiableSet(strings);
    }

    static StorIOSQLite getStorIO(Context context) {
        if (instance == null) {
            synchronized (StorIOProvider.class) {
                if (instance == null) {
                    instance = DefaultStorIOSQLite.builder()
                            .sqliteOpenHelper(new DbOpenHelper(context))
                            .addTypeMapping(Specialization.class, SQLiteTypeMapping.<Specialization>builder()
                                    .putResolver(new SpecializationPutResolver())
                                    .getResolver(new SpecializationGetResolver())
                                    .deleteResolver(new SpecializationDeleteResolver())
                                    .build())
                            .addTypeMapping(Question.class, SQLiteTypeMapping.<Question>builder()
                                    .putResolver(new QuestionPutResolver())
                                    .getResolver(new QuestionGetResolver())
                                    .deleteResolver(new QuestionDeleteResolver())
                                    .build())
                            .addTypeMapping(Answer.class, SQLiteTypeMapping.<Answer>builder()
                                    .putResolver(new AnswerPutResolver())
                                    .getResolver(new AnswerGetResolver())
                                    .deleteResolver(new AnswerDeleteResolver())
                                    .build())
                            .addTypeMapping(User.class, SQLiteTypeMapping.<User>builder()
                                    .putResolver(new UserPutResolver())
                                    .getResolver(new UserGetResolver())
                                    .deleteResolver(new UserDeleteResolver())
                                    .build())
                            .addTypeMapping(Attempt.class, SQLiteTypeMapping.<Attempt>builder()
                                    .putResolver(new AttemptPutResolver())
                                    .getResolver(new AttemptGetResolver())
                                    .deleteResolver(new AttemptDeleteResolver())
                                    .build())
                            .build();
                    instance.observeChangesInTables(tables).subscribe(observerChanges);
                    observerChanges.call(Changes.newInstance(tables));
                }
            }
        }

        return instance;
    }
}
