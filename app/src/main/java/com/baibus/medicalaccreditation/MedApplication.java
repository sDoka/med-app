package com.baibus.medicalaccreditation;

import android.app.Application;
import android.os.StrictMode;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.baibus.medicalaccreditation.common.db.entities.User;
import com.baibus.medicalaccreditation.common.db.tables.SpecializationTable;
import com.baibus.medicalaccreditation.common.db.tables.UserTable;
import com.baibus.medicalaccreditation.common.provider.ApiModule;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.ExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

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

    private static MedApplication instance;
    public static MedApplication getInstance() {
        return instance;
    }

    private BehaviorSubject<User> user;
    private BehaviorSubject<Specialization> specialization;
    private BehaviorSubject<List<Specialization>> specializations;
    private BehaviorSubject<Long> specializationId;

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
        instance = this;
        // Normal app init code...
        installLeakCanary();
        Timber.plant(new Timber.DebugTree());
        Fresco.initialize(this);

        Stetho.initializeWithDefaults(this);

        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
        }

        user = BehaviorSubject.create();
        specialization = BehaviorSubject.create();
        specializationId = BehaviorSubject.create(-1L);
        specializations = BehaviorSubject.create(new ArrayList<>());

        //android.view.inputmethod.InputMethodManager;$ControlledInputConnectionWrapper
    }

    private void installLeakCanary() {
        ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults()
                .instanceField("android.support.design.widget.TextInputEditText", "mContext")
                .clazz("android.view.inputmethod.InputMethodManager$ControlledInputConnectionWrapper")
                .clazz("android.view.inputmethod.InputMethodManager")
                .build();
        refWatcher = LeakCanary.refWatcher(this)
                .excludedRefs(excludedRefs)
                .buildAndInstall();
//        refWatcher = LeakCanary.install(this);
    }

    @NonNull
    public RefWatcher refWatcher() {
        return refWatcher;
    }

    public void setSpecializationId(long specializationId) {
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
