package com.baibus.medicalaccreditation.auth;

import android.databinding.ObservableInt;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.base.Activity;
import com.baibus.medicalaccreditation.base.ActivityVM;
import com.baibus.medicalaccreditation.base.FragmentVM;
import com.baibus.medicalaccreditation.base.FragmentVMFactory;
import com.baibus.medicalaccreditation.base.RxLoader;
import com.baibus.medicalaccreditation.common.binding.ObservableString;
import com.baibus.medicalaccreditation.common.db.tables.AccountTable;
import com.baibus.medicalaccreditation.common.network.ApiError;
import com.baibus.medicalaccreditation.common.network.entities.RegistrationResponse;
import com.baibus.medicalaccreditation.common.provider.ApiModule;
import com.baibus.medicalaccreditation.update.UpdateActivity;
import com.pushtorefresh.storio.StorIOException;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.baibus.medicalaccreditation.base.ActivityVM.isMainLooper;
import static com.baibus.medicalaccreditation.common.db.tables.UserTable.DELETE_ALL;

public class LoginVM extends FragmentVM<LoginFragment>
    implements TextView.OnEditorActionListener {
    private final static String TAG = LoginVM.class.getSimpleName();

    private final static String BUNDLE_EMAIL = "email";
    private final static String BUNDLE_EMAIL_ERROR = "emailError";
    private final static String BUNDLE_PASSWORD = "password";
    private final static String BUNDLE_PASSWORD_ERROR = "passwordError";
    private final static String BUNDLE_STATUS = "mStatus";

    private final static int LOADER_AUTHENTICATE_USER = 0;
    private final static int LOADER_CHECK_USER = 1;

    public final ObservableString email = new ObservableString("qwerty@qwerty.com");
    public final ObservableString emailError = new ObservableString();
    public final ObservableString password = new ObservableString("qwerty");
    public final ObservableString passwordError = new ObservableString();
    private final ObservableInt mStatus = new ObservableInt();
    private final OnPropertyChangedCallback mStatusChanged = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(android.databinding.Observable observable, int i) {
            isShowProgress.set(mStatus.get() != NOTHING);
        }
    };


    private LoginVM(LoginFragment fragment, @Nullable Bundle savedInstanceState) {
        super(fragment, savedInstanceState);

        setTitle(fragment.getString(R.string.action_sign_in));

        if (savedInstanceState != null) {
            email.set(savedInstanceState.getString(BUNDLE_EMAIL));
            emailError.set(savedInstanceState.getString(BUNDLE_EMAIL_ERROR));
            password.set(savedInstanceState.getString(BUNDLE_PASSWORD));
            passwordError.set(savedInstanceState.getString(BUNDLE_PASSWORD_ERROR));
            mStatus.set(savedInstanceState.getInt(BUNDLE_STATUS));
        }

        mStatus.addOnPropertyChangedCallback(mStatusChanged);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        resumeLoaders(getStatusFromInt(mStatus.get()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_EMAIL, email.get());
        outState.putString(BUNDLE_EMAIL_ERROR, emailError.get());
        outState.putString(BUNDLE_PASSWORD, password.get());
        outState.putString(BUNDLE_PASSWORD_ERROR, passwordError.get());
        outState.putInt(BUNDLE_STATUS, mStatus.get());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStatus.removeOnPropertyChangedCallback(mStatusChanged);
    }

    @Override
    public boolean onEditorAction(@NonNull TextView textView, int actionId,
                                  @Nullable KeyEvent keyEvent) {
        Log.d(TAG, "onEditorAction:" + actionId);
        if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
            attemptLogin();
            return true;
        }
        return false;
    }

    public void showRegistration() {
        ActivityVM<?> activityVM = ((Activity) fragment.getActivity()).viewModel;
        activityVM.addFragment(RegistrationFragment.newInstance(), RegistrationFragment.TAG);
    }

    public void attemptLogin() {
        if (this.mStatus.get() != NOTHING) {
            return;
        }
        // Reset errors.
        this.emailError.set(null);
        this.passwordError.set(null);

        // Store values at the time of the login attempt.
        String email = this.email.get();
        String password = this.password.get();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            this.passwordError.set(fragment.getString(R.string.error_invalid_password));
            focusView = fragment.binding.editPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            this.emailError.set(fragment.getString(R.string.error_field_required));
            focusView = fragment.binding.textEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            this.emailError.set(fragment.getString(R.string.error_invalid_email));
            focusView = fragment.binding.textEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            loadLogin(email, password, true);
        }
    }

    private void resumeLoaders(@Status int status) {
        switch (status) {
            case CHECK:
                loadCheck(false);
                break;
            case NOTHING:
                break;
            case LOGIN:
                loadLogin(email.get(), password.get(), false);
                break;
        }
    }

    private void restartLoaders(@Status int status) {
        switch (status) {
            case CHECK:
                loadCheck(true);
                break;
            case NOTHING:
                break;
            case LOGIN:
                loadLogin(email.get(), password.get(), true);
                break;
        }
    }

    void showMessageSuccessRegistration() {
        fragment.showSnackBar("Вы успешно зарегистрированы", null, null);
    }

    private void authSuccess(Boolean result) {
        mStatus.set(NOTHING);
        fragment.getActivity().startActivity(UpdateActivity.getStartIntent());
        fragment.getActivity().finish();
    }

    private void onError(Throwable throwable, @Status int from) {
        Log.d(TAG, "OUTER LOADER onError:" + isMainLooper());
        mStatus.set(NOTHING);
        if (throwable instanceof ApiError) {
            ApiError apiError = (ApiError) throwable;
            switch (apiError.getKind()) {
                case NETWORK:
                    fragment.showSnackBar("Ошибка соединения", "повторить",
                            view -> restartLoaders(from));
                    return;
                case UNAUTHENTICATED:
                case CLIENT:
                case SERVER:
                    fragment.showSnackBar(apiError.getMessage(), null, null);
                    return;
                case UNEXPECTED:
                    throw apiError;
            }
        }
        if (TextUtils.equals(throwable.getMessage(), "User not been saved")) {
            fragment.showSnackBar(throwable.getMessage(), "повторить",
                    view -> restartLoaders(from));
            return;
        }
        throw Exceptions.propagate(throwable);
    }

    private void loadCheck(boolean restart) {
        mStatus.set(NOTHING);
//        fragment.addSubscription(
//                getObservableCheck()
//                        .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
//                        .observeOn(AndroidSchedulers.from(fragment.mainHandler.getLooper()))
//                        .compose(RxLoader.from(this, LOADER_CHECK_USER, restart))
//                        .doOnSubscribe(() -> mStatus.set(CHECK))
//                        .subscribe(this::authSuccess, throwable -> onError(throwable, CHECK))
//        );
    }

    private void loadLogin(String email, String password, boolean restart) {
        fragment.addSubscription(
                getObservableUserLogin(email, password)
                        .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                        .observeOn(AndroidSchedulers.from(fragment.mainHandler.getLooper()))
                        .compose(RxLoader.from(this, LOADER_AUTHENTICATE_USER, restart))
                        .doOnSubscribe(() -> mStatus.set(LOGIN))
                        .subscribe(this::authSuccess, throwable -> onError(throwable, LOGIN))
        );
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    static boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }


    static boolean isNameValid(String name) {
        return name.length() > 3
                && name.matches(".*[a-zA-Z].*");
    }

    static boolean isPasswordValid(String password) {
        return password.length() > 3
                && password.matches(".*[a-zA-Z].*");
    }

    static boolean isPasswordConfirmValid(String password, String passwordConfirm) {
        return TextUtils.equals(password, passwordConfirm);
    }

    private static Observable<Boolean> getObservableCheck() {
        return Observable
                .defer(() -> {
                    Log.d(TAG, "INNER LOADER_CHECK_USER" + isMainLooper());
                    return ApiModule
                            .getAuthApi()
                            .check();
                })
                .map(new Save());
    }

    private static Observable<Boolean> getObservableUserLogin(String email, String password) {
        return Observable
                .defer(() -> {
                    Log.d(TAG, "INNER LOADER_AUTHENTICATE_USER" + isMainLooper());
                    return ApiModule
                            .getAuthApi()
                            .login(email, password, ApiModule.getDeviceKey());
                })
                .map(new Save());
    }

    private static class Save implements Func1<RegistrationResponse, Boolean>  {
        @Override
        public Boolean call(RegistrationResponse registrationResponse) {
            ApiModule
                    .getStoreIOSQLite()
                    .delete()
                    .byQuery(DELETE_ALL)
                    .prepare()
                    .executeAsBlocking();

            PutResult result = ApiModule
                    .getStoreIOSQLite()
                    .put()
                    .object(registrationResponse.getUser())
                    .prepare()
                    .executeAsBlocking();
            ApiModule
                    .getStoreIOSQLite()
                    .delete()
                    .byQuery(AccountTable.deleteQuery(registrationResponse.getAccount()))
                    .prepare()
                    .executeAsBlocking();
            ApiModule
                    .getStoreIOSQLite()
                    .put()
                    .object(registrationResponse.getAccount())
                    .prepare()
                    .executeAsBlocking();
            if (result.wasInserted() || result.wasUpdated()) return true;
            throw new StorIOException("User not been saved");
        }
    }

    static class FactoryVM implements FragmentVMFactory<LoginVM, LoginFragment> {
        @NonNull
        @Override
        public LoginVM create(LoginFragment view) {
            return new LoginVM(view, null);
        }

        @NonNull
        @Override
        public LoginVM restore(LoginFragment view, @NonNull Bundle savedInstanceState) {
            return new LoginVM(view, savedInstanceState);
        }
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CHECK, NOTHING, LOGIN})
    private @interface Status{}
    private static final int CHECK = 0;
    private static final int NOTHING = 1;
    private static final int LOGIN = 2;

    private static @Status int getStatusFromInt(int from) {
        switch (from) {
            case CHECK:
            case NOTHING:
            case LOGIN:
                return from;
            default:
                return NOTHING;
        }
    }
}

