package com.baibus.medicalaccreditation.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import rx.Emitter;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 20.04.2017
 * Time: 17:03
 * To change this template use File | settings | File Templates.
 */
public class RxLoader {
    private static final String TAG = RxLoader.class.getSimpleName();

    /** This must be called IN or AFTER {@link FragmentVM#onActivityCreated(Bundle)}
     * @see <a href="https://issuetracker.google.com/issues/37139856">LoaderManager doStart called twice in Fragment.onStart</a>
     * and after release new builds sdk check this*/
    public static <T> Observable.Transformer<T, T> from(FragmentVM viewModel, int id, boolean forceReload) {
        if (viewModel.isActivityCreated()) {
            return from(viewModel.fragment.getContext(), viewModel.fragment.getLoaderManager(), id, forceReload);
        }
        throw new RuntimeException("Loader must be called IN or AFTER FragmentVM.onActivityCreated()");
    }

    public static <T> Observable.Transformer<T, T> from(DialogFragmentVM viewModel, int id, boolean forceReload) {
        if (viewModel.isActivityCreated()) {
            return from(viewModel.fragment.getContext(), viewModel.fragment.getLoaderManager(), id, forceReload);
        }
        throw new RuntimeException("Loader must be called IN or AFTER DialogFragmentVM.onActivityCreated()");
    }

    public static <T> Observable.Transformer<T, T> from(ActivityVM viewModel, int id, boolean forceReload) {
        return from(viewModel.activity, viewModel.activity.getSupportLoaderManager(), id, forceReload);
    }

    private static <T> Observable.Transformer<T, T> from(Context context, LoaderManager manager,
                                                         int id, boolean forceReload) {
        return tObservable -> Observable.create(
                new LoaderCallbackAsyncEmitter<>(tObservable, context, manager, id, forceReload),
                Emitter.BackpressureMode.DROP
        );
    }

    private static String isMainLooper() {
        return Looper.getMainLooper() == Looper.myLooper() ? " IS MAIN THREAD " : " IN BACKGROUND ";
    }

    private static class ObservableLoader<T> extends Loader<T> {

        private final Observable<T> mObservable;
        private Throwable mError;

        ObservableLoader(Context context, Observable<T> observable) {
            super(context);
            mObservable = observable;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Override
        protected void onForceLoad() {
            super.onForceLoad();
            mObservable.subscribe(this::deliverResult, this::deliverThrowable);
        }

        private void deliverThrowable(Throwable throwable) {
            mError = throwable;
            deliverResult(null);
        }

        @Override
        public void deliverResult(T data) {
            super.deliverResult(data);
        }
    }

    private static class LoaderCallbackAsyncEmitter<T> implements Action1<Emitter<T>> {
        private final Observable<T> mObservable;
        private final Context mContext;
        private final LoaderManager mLoaderManager;
        private final int mId;
        private final boolean mForceReload;

        LoaderCallbackAsyncEmitter(Observable<T> observable, Context context,
                                   LoaderManager loaderManager, int id, boolean forceReload) {
            mObservable = observable;
            mContext = context;
            mLoaderManager = loaderManager;
            mId = id;
            mForceReload = forceReload;
        }

        @Override
        public void call(Emitter<T> emitter) {
            LoaderManager.LoaderCallbacks<T> callbacks = new LoaderManager.LoaderCallbacks<T>() {
                @Override
                public Loader<T> onCreateLoader(int id, Bundle args) {
                    Log.d(TAG, "onCreateLoader:" + isMainLooper());
                    return new ObservableLoader<>(mContext, mObservable);
                }

                @Override
                public void onLoadFinished(Loader<T> loader, T data) {
                    Log.d(TAG, "onLoadFinished:" + isMainLooper());
                    Throwable error = ((ObservableLoader) loader).mError;
                    if (error != null) {
                        emitter.onError(error);
                    } else {
                        emitter.onNext(data);
                        emitter.onCompleted();
                    }
                }

                @Override
                public void onLoaderReset(Loader<T> loader) {

                }
            };
            if (mForceReload) {
                mLoaderManager.restartLoader(mId, null, callbacks);
            } else {
                mLoaderManager.initLoader(mId, null, callbacks);
            }
        }
    }

}
