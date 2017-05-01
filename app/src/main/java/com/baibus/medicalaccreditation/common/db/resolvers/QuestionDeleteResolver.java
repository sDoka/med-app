package com.baibus.medicalaccreditation.common.db.resolvers;

import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Question;
import com.baibus.medicalaccreditation.common.db.tables.QuestionTable;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:39
 * To change this template use File | settings | File Templates.
 */
public class QuestionDeleteResolver extends DefaultDeleteResolver<Question> {

    @Override
    @NonNull
    protected DeleteQuery mapToDeleteQuery(@NonNull Question object) {
        return DeleteQuery.builder()
                .table(QuestionTable.TABLE)
                .where("id = ?")
                .whereArgs(object.getId())
                .build();
    }
}