package com.baibus.medicalaccreditation.common.binding;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import java.util.HashMap;

import rx.Observable;
import rx.Subscription;
import timber.log.Timber;

public class ReadOnlyField<T> extends ObservableField<T> {
    private final Observable<T> source;
    private final HashMap<OnPropertyChangedCallback, Subscription> subscriptions = new HashMap<>();

    public static <U> ReadOnlyField<U> create(@NonNull Observable<U> source) {
        return new ReadOnlyField<>(source);
    }

    private ReadOnlyField(@NonNull Observable<T> source) {
        super();
        this.source = source
                .doOnNext(ReadOnlyField.super::set)
                .doOnError(throwable -> Timber.e(throwable, "onError in source observable"))
                .onErrorResumeNext(Observable.empty())
                .share();
    }

    /**
     * @deprecated Setter of ReadOnlyField does nothing. Merge with the source Observable instead.
     */
    @Deprecated
    @Override
    public void set(T value) {
    }

    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.addOnPropertyChangedCallback(callback);
        subscriptions.put(callback, source.subscribe());
    }

    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.removeOnPropertyChangedCallback(callback);
        Subscription subscription = subscriptions.remove(callback);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
