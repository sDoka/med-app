package com.baibus.medicalaccreditation;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.StrictMode;

import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.baibus.medicalaccreditation.common.db.entities.User;
import com.baibus.medicalaccreditation.common.db.tables.SpecializationTable;
import com.baibus.medicalaccreditation.common.db.tables.UserTable;
import com.baibus.medicalaccreditation.common.provider.ApiModule;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 21.04.2017
 * Time: 15:21
 * To change this template use File | settings | File Templates.
 */
public class MedApplication extends Application {

    private final static String APP_PREFERENCES = "APP_PREFERENCES";
    private final static String APP_PREFERENCES_SPECIALIZATION_ID = "specializationId";

    private static MedApplication instance;
    public static MedApplication getInstance() {
        return instance;
    }

    private BehaviorSubject<User> user;
    private BehaviorSubject<Specialization> specialization;
    private BehaviorSubject<List<Specialization>> specializations;
    private BehaviorSubject<Long> specializationId;

    private SharedPreferences mPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Timber.plant(new Timber.DebugTree());
        Fresco.initialize(this);

        mPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        Stetho.initializeWithDefaults(this);

        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
        }

        user = BehaviorSubject.create();
        specialization = BehaviorSubject.create();
        specializationId = BehaviorSubject.create(mPreferences.getLong(APP_PREFERENCES_SPECIALIZATION_ID, -1L));
        specializations = BehaviorSubject.create(new ArrayList<>());

    }

    public void setSpecializationId(long specializationId) {
        if (specializationId != this.specializationId.getValue()) {
            this.specializationId.onNext(specializationId);
            this.reloadSpecialization();
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putLong(APP_PREFERENCES_SPECIALIZATION_ID, specializationId);
            editor.apply();
        }
    }

    public void reloadUser() {
        reload(User.class,
                UserTable.QUERY,
                user);
    }

    public void reloadSpecializations() {
        reloadList(Specialization.class,
                SpecializationTable.QUERY_ALL,
                specializations);
    }

    public void reloadSpecialization() {
        reload(Specialization.class,
                SpecializationTable.queryId(specializationId.getValue()),
                specialization);
    }

    private <T> void reload(Class<T> clazz, Query query, BehaviorSubject<T> subject) {
        ApiModule
                .getStoreIOSQLite()
                .get()
                .object(clazz)
                .withQuery(query)
                .prepare()
                .asRxObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(throwable -> null)
                .subscribe(subject::onNext);
    }

    private <T> void reloadList(Class<T> clazz, Query query, BehaviorSubject<List<T>> subject) {
        ApiModule
                .getStoreIOSQLite()
                .get()
                .listOfObjects(clazz)
                .withQuery(query)
                .prepare()
                .asRxObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(throwable -> null)
                .subscribe(subject::onNext);
    }

    public Subscription subscribeOnSpecialization(Action1<Specialization> onNext) {
        return specialization.subscribe(onNext);
    }

    public Subscription subscribeOnSpecializations(Action1<List<Specialization>> onNext) {
        return specializations.subscribe(onNext);
    }

    public Subscription subscribeOnUser(Action1<User> onNext) {
        return user.subscribe(onNext);
    }

}
