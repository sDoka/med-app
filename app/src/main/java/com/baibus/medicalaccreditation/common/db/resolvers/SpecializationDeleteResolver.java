package com.baibus.medicalaccreditation.common.db.resolvers;

import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.baibus.medicalaccreditation.common.db.tables.SpecializationTable;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:39
 * To change this template use File | settings | File Templates.
 */
public class SpecializationDeleteResolver extends DefaultDeleteResolver<Specialization> {

    @Override
    @NonNull
    protected DeleteQuery mapToDeleteQuery(@NonNull Specialization object) {
        return DeleteQuery.builder()
                .table(SpecializationTable.TABLE)
                .where("id = ?")
                .whereArgs(object.getId())
                .build();
    }
}