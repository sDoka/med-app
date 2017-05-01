package com.baibus.medicalaccreditation.auth;

import android.databinding.ObservableField;
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

import com.baibus.medicalaccreditation.MedApplication;
import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.base.Activity;
import com.baibus.medicalaccreditation.base.ActivityVM;
import com.baibus.medicalaccreditation.base.FragmentVM;
import com.baibus.medicalaccreditation.base.FragmentVMFactory;
import com.baibus.medicalaccreditation.base.RxLoader;
import com.baibus.medicalaccreditation.common.db.entities.User;
import com.baibus.medicalaccreditation.common.network.RetrofitException;
import com.baibus.medicalaccreditation.common.provider.ApiModule;
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
import static com.baibus.medicalaccreditation.common.provider.ApiModule.HTTP_CODE_BAD_REQUEST;
import static com.baibus.medicalaccreditation.common.provider.ApiModule.HTTP_CODE_UNAUTHORIZED;
import static com.baibus.medicalaccreditation.common.provider.ApiModule.HTTP_CODE_PAYMENT_REQUIRED;
import static com.baibus.medicalaccreditation.common.provider.ApiModule.HTTP_CODE_FORBIDDEN;
import static com.baibus.medicalaccreditation.common.provider.ApiModule.HTTP_CODE_NOT_FOUND;

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

    private final static int MESSAGE_SAVE_USER = -1;
    private final static int MESSAGE_NETWORK = -2;

    public final ObservableField<String> email = new ObservableField<>();
    public final ObservableField<String> emailError = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();
    public final ObservableField<String> passwordError = new ObservableField<>();
    private final ObservableInt mStatus = new ObservableInt();
    private final OnPropertyChangedCallback mStatusChanged = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(android.databinding.Observable observable, int i) {
            isShowProgress.set(mStatus.get() != NOTHING);
        }
    };


    private LoginVM(LoginFragment fragment, @NonNull Bundle savedInstanceState) {
        super(fragment, savedInstanceState);

        setTitle(fragment.getString(R.string.action_sign_in));

        email.set(savedInstanceState.getString(BUNDLE_EMAIL));
        emailError.set(savedInstanceState.getString(BUNDLE_EMAIL_ERROR));
        password.set(savedInstanceState.getString(BUNDLE_PASSWORD));
        passwordError.set(savedInstanceState.getString(BUNDLE_PASSWORD_ERROR));
        mStatus.set(savedInstanceState.getInt(BUNDLE_STATUS));

        mStatus.addOnPropertyChangedCallback(mStatusChanged);

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
        //TODO show message
    }

    private void authSuccess(User user) {
        mStatus.set(NOTHING);
        //TODO update bd
    }

    private void onError(Throwable throwable, @Status int from) {
        Log.d(TAG, "OUTER LOADER onError:" + isMainLooper());
        mStatus.set(NOTHING);
        if (throwable instanceof RetrofitException) {
            RetrofitException retrofitException = (RetrofitException) throwable;
            switch (retrofitException.getKind()) {
                case NETWORK:
                    retrofitException.getCause().printStackTrace();
                    showError(MESSAGE_NETWORK, from);
                    return;
                case HTTP:
                    showError(retrofitException.getResponse().code(), from);
                    return;
                case UNEXPECTED:
                    throw retrofitException;
            }
        }
        if (TextUtils.equals(throwable.getMessage(), "User not been saved")) {
            showError(MESSAGE_SAVE_USER, from);
            return;
        }
        throw Exceptions.propagate(throwable);
    }

    private void showError(int code, @Status int from) {
        //TODO strings
        switch (code) {
            case MESSAGE_NETWORK:
                fragment.showSnackBar("Инета нет",
                        "повторить",
                        view -> restartLoaders(from));
                break;
            case MESSAGE_SAVE_USER:
                fragment.showSnackBar("Не удалось сохранить",
                        "повторить",
                        view -> restartLoaders(from));
                break;
            case HTTP_CODE_BAD_REQUEST:
            case HTTP_CODE_UNAUTHORIZED:
            case HTTP_CODE_PAYMENT_REQUIRED:
            case HTTP_CODE_FORBIDDEN:
            case HTTP_CODE_NOT_FOUND:
                fragment.showSnackBar("Что-то пошло не так",
                        "повторить",
                        view -> restartLoaders(from));
                break;
            default:
                if (ApiModule.isServerError(code)) {
                    fragment.showSnackBar("Ошибка сервера, попробуйте повторить позже",
                            "повторить",
                            view -> restartLoaders(from));
                    return;
                }
                if (ApiModule.isClientError(code)) {
                    fragment.showSnackBar("ошибка передачи данных",
                            "повторить",
                            view -> restartLoaders(from));
                    return;
                }
                fragment.showSnackBar("Не обработанная ошибка", null, null);
                break;
        }
    }

    private void loadCheck(boolean restart) {
        getObservableCheck()
                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .observeOn(AndroidSchedulers.from(fragment.mainHandler.getLooper()))
                .compose(RxLoader.from(this, LOADER_CHECK_USER, restart))
                .doOnSubscribe(() -> mStatus.set(CHECK))
                .subscribe(this::authSuccess, throwable -> onError(throwable, CHECK));
    }

    private void loadLogin(String email, String password, boolean restart) {
        getObservableUserLogin(email, password)
                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .observeOn(AndroidSchedulers.from(fragment.mainHandler.getLooper()))
                .compose(RxLoader.from(this, LOADER_AUTHENTICATE_USER, restart))
                .doOnSubscribe(() -> mStatus.set(LOGIN))
                .subscribe(this::authSuccess, throwable -> onError(throwable, LOGIN));
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        //•	E-mail адрес (строка по паттерну);
        return email.contains("@");
    }

    static boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        //•	Имя (только латинские или русские буквы 4-8 символов);
        return name.length() > 3;
    }

    static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        //•	Пароль (4 – 16 символов русского или латинского алфавита, минимум одна цифра);
        return password.length() > 3;
    }

    static boolean isPasswordConfirmValid(String password, String passwordConfirm) {
        return TextUtils.equals(password, passwordConfirm);
    }

    private static Observable<User> getObservableCheck() {
        return Observable
                .defer(() -> {
                    Log.d(TAG, "INNER LOADER_CHECK_USER" + isMainLooper());
                    return ApiModule
                            .getAuthApi()
                            .check();
                })
                .map(new SaveUser());
    }

    private static Observable<User> getObservableUserLogin(String email, String password) {
        return Observable
                .defer(() -> {
                    Log.d(TAG, "INNER LOADER_AUTHENTICATE_USER" + isMainLooper());
                    return ApiModule
                            .getAuthApi()
                            .login(email, password);
                })
                .map(new SaveUser());
    }

    private static class SaveUser implements Func1<User, User>  {
        @Override
        public User call(User user) {
            ApiModule
                    .getStoreIOSQLite(MedApplication.getInstance())
                    .delete()
                    .byQuery(DELETE_ALL)
                    .prepare()
                    .executeAsBlocking();
            PutResult result = ApiModule
                    .getStoreIOSQLite(MedApplication.getInstance())
                    .put()
                    .object(user)
                    .prepare()
                    .executeAsBlocking();
            if (result.wasInserted()) return user;
            throw new StorIOException("User not been saved");
        }
    }

    static class FactoryVM implements FragmentVMFactory<LoginVM, LoginFragment> {
        @NonNull
        @Override
        public LoginVM create(LoginFragment view) {
            return new LoginVM(view, view.getArguments());
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

