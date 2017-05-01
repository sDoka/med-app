package com.baibus.medicalaccreditation;

import android.app.Application;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.baibus.medicalaccreditation.common.db.entities.User;
import com.baibus.medicalaccreditation.common.db.tables.SpecializationTable;
import com.baibus.medicalaccreditation.common.db.tables.UserTable;
import com.baibus.medicalaccreditation.common.provider.ApiModule;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

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

    private static MedApplication instance;
    public static MedApplication getInstance() {
        return instance;
    }

    private BehaviorSubject<User> user;
    private BehaviorSubject<Specialization> specialization;
    private BehaviorSubject<Integer> specializationId;

    // Monitors Memory Leaks, because why not!
    // You can play with sample app and Rx subscriptions
    // To see how it can leak memory if you won't unsubscribe.
    private RefWatcher refWatcher;


    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        // Normal app init code...
        refWatcher = LeakCanary.install(this);
        Timber.plant(new Timber.DebugTree());
        instance = this;
        Fresco.initialize(this);

        user = BehaviorSubject.create();
        specialization = BehaviorSubject.create();
        specializationId = BehaviorSubject.create(-1);
    }

    @NonNull
    public RefWatcher refWatcher() {
        return refWatcher;
    }

    public void setSpecializationId(int specializationId) {
        if (specializationId != this.specializationId.getValue()) {
            this.specializationId.onNext(specializationId);
            this.reloadSpecialization();
        }
    }

    public void reloadUser() {
        reload(User.class,
                UserTable.QUERY,
                user);
    }

    public void reloadSpecialization() {
        reload(Specialization.class,
                SpecializationTable.queryId(specializationId.getValue()),
                specialization);
    }

    private <T, Q extends Query> void reload(Class<T> clazz, Q query, BehaviorSubject<T> subject) {
        ApiModule
                .getStoreIOSQLite(MedApplication.getInstance())
                .get()
                .object(clazz)
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

    public Subscription subscribeOnUser(Action1<User> onNext) {
        return user.subscribe(onNext);
    }

}
