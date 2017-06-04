package com.baibus.medicalaccreditation.auth;

import android.databinding.ObservableBoolean;
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
import android.widget.TextView;

import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.base.Activity;
import com.baibus.medicalaccreditation.base.ActivityVM;
import com.baibus.medicalaccreditation.base.FragmentVM;
import com.baibus.medicalaccreditation.base.FragmentVMFactory;
import com.baibus.medicalaccreditation.base.RxLoader;
import com.baibus.medicalaccreditation.common.binding.ObservableString;
import com.baibus.medicalaccreditation.common.network.ApiError;
import com.baibus.medicalaccreditation.common.provider.ApiModule;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

import static com.baibus.medicalaccreditation.auth.LoginVM.isEmailValid;
import static com.baibus.medicalaccreditation.auth.LoginVM.isNameValid;
import static com.baibus.medicalaccreditation.auth.LoginVM.isPasswordConfirmValid;
import static com.baibus.medicalaccreditation.auth.LoginVM.isPasswordValid;
import static com.baibus.medicalaccreditation.base.ActivityVM.isMainLooper;


public class RegistrationVM extends FragmentVM<RegistrationFragment>
        implements TextView.OnEditorActionListener {
    private final static String TAG = RegistrationVM.class.getSimpleName();

    private final static String BUNDLE_EMAIL = "email";
    private final static String BUNDLE_EMAIL_ERROR = "emailError";
    private final static String BUNDLE_NAME = "name";
    private final static String BUNDLE_NAME_ERROR = "nameError";
    private final static String BUNDLE_PASSWORD = "password";
    private final static String BUNDLE_PASSWORD_ERROR = "passwordError";
    private final static String BUNDLE_PASSWORD_CONFIRM = "passwordConfirm";
    private final static String BUNDLE_PASSWORD_CONFIRM_ERROR = "passwordConfirmError";
    private final static String BUNDLE_FEMALE = "female";
    private final static String BUNDLE_STATUS = "mStatus";

    private final static int LOADER_REGISTRATION_USER = 0;

    public final ObservableString email = new ObservableString();
    public final ObservableString emailError = new ObservableString();
    public final ObservableString name = new ObservableString();
    public final ObservableString nameError = new ObservableString();
    public final ObservableString password = new ObservableString();
    public final ObservableString passwordError = new ObservableString();
    public final ObservableString passwordConfirm = new ObservableString();
    public final ObservableString passwordConfirmError = new ObservableString();
    public final ObservableBoolean female = new ObservableBoolean();
    private final ObservableInt mStatus = new ObservableInt();
    private final OnPropertyChangedCallback mStatusChanged = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(android.databinding.Observable observable, int i) {
            isShowProgress.set(mStatus.get() != NOTHING);
        }
    };

    private RegistrationVM(RegistrationFragment fragment, @NonNull Bundle savedInstanceState) {
        super(fragment, savedInstanceState);

        setTitle(fragment.getString(R.string.action_registration));

        email.set(savedInstanceState.getString(BUNDLE_EMAIL));
        emailError.set(savedInstanceState.getString(BUNDLE_EMAIL_ERROR));
        name.set(savedInstanceState.getString(BUNDLE_NAME));
        nameError.set(savedInstanceState.getString(BUNDLE_NAME_ERROR));
        password.set(savedInstanceState.getString(BUNDLE_PASSWORD));
        passwordError.set(savedInstanceState.getString(BUNDLE_PASSWORD_ERROR));
        passwordConfirm.set(savedInstanceState.getString(BUNDLE_PASSWORD_CONFIRM));
        passwordConfirmError.set(savedInstanceState.getString(BUNDLE_PASSWORD_CONFIRM_ERROR));
        female.set(savedInstanceState.getBoolean(BUNDLE_FEMALE));
        mStatus.set(savedInstanceState.getInt(BUNDLE_STATUS));

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
        outState.putString(BUNDLE_NAME, name.get());
        outState.putString(BUNDLE_NAME_ERROR, nameError.get());
        outState.putString(BUNDLE_PASSWORD, password.get());
        outState.putString(BUNDLE_PASSWORD_ERROR, passwordError.get());
        outState.putString(BUNDLE_PASSWORD_CONFIRM, passwordConfirm.get());
        outState.putString(BUNDLE_PASSWORD_CONFIRM_ERROR, passwordConfirmError.get());
        outState.putBoolean(BUNDLE_FEMALE, female.get());
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
        return false;
    }


    public void attemptRegistration() {
        if (this.mStatus.get() != NOTHING) {
            return;
        }
        // Reset errors.
        this.emailError.set(null);
        this.nameError.set(null);
        this.passwordError.set(null);
        this.passwordConfirmError.set(null);

        // Store values at the time of the login attempt.
        String email = this.email.get();
        String name = this.name.get();
        String password = this.password.get();
        String passwordConfirm = this.passwordConfirm.get();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password confirm, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordConfirmValid(password, passwordConfirm)) {
            this.passwordError.set(fragment.getString(R.string.error_invalid_password_confirm));
            focusView = fragment.binding.editPasswordConfirm;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            this.passwordError.set(fragment.getString(R.string.error_invalid_password));
            focusView = fragment.binding.editPassword;
            cancel = true;
        }

        // Check for a valid name field.
        if (TextUtils.isEmpty(name)) {
            this.nameError.set(fragment.getString(R.string.name_field_required));
            focusView = fragment.binding.textName;
            cancel = true;
        } else if (!isNameValid(name)) {
            this.nameError.set(fragment.getString(R.string.error_invalid_name));
            focusView = fragment.binding.textName;
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
            loadRegistration(email, name, password, female.get(), true);
        }
    }

    private void resumeLoaders(@Status int status) {
        switch (status) {
            case REGISTRATION:
                loadRegistration(email.get(), name.get(), password.get(), female.get(), false);
                break;
            case NOTHING:
                break;
        }
    }

    private void restartLoaders(@Status int status) {
        switch (status) {
            case REGISTRATION:
                loadRegistration(email.get(), name.get(), password.get(), female.get(), true);
                break;
            case NOTHING:
                break;
        }
    }

    private void success(Boolean result) {
        mStatus.set(NOTHING);
        fragment.mainHandler.post(() -> {
            ActivityVM<?> activityVM = ((Activity) fragment.getActivity()).viewModel;
            activityVM.removeCurrentFragment();
        });
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
        throw Exceptions.propagate(throwable);
    }

    private void loadRegistration(String email, String name, String password, boolean female, boolean restart) {
        fragment.addSubscription(
                getObservableRegistration(email, name, password, female)
                        .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                        .observeOn(AndroidSchedulers.from(fragment.mainHandler.getLooper()))
                        .compose(RxLoader.from(this, LOADER_REGISTRATION_USER, restart))
                        .doOnSubscribe(() -> mStatus.set(REGISTRATION))
                        .subscribe(this::success, throwable -> onError(throwable, REGISTRATION))
        );
    }


    private static Observable<Boolean> getObservableRegistration(String email, String name, String password, boolean female) {
        return Observable
                .defer(() -> {
                    Log.d(TAG, "INNER LOADER_REGISTRATION_USER" + isMainLooper());
                    return ApiModule
                            .getAuthApi()
                            .registration(email, name, password, female ? "true" : "false", ApiModule.getDeviceKey())
                            .map(registrationResponse -> true);
                });
    }

    static class FactoryVM implements FragmentVMFactory<RegistrationVM, RegistrationFragment> {
        @NonNull
        @Override
        public RegistrationVM create(RegistrationFragment view) {
            return new RegistrationVM(view, view.getArguments());
        }

        @NonNull
        @Override
        public RegistrationVM restore(RegistrationFragment view, @NonNull Bundle savedInstanceState) {
            return new RegistrationVM(view, savedInstanceState);
        }
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NOTHING, REGISTRATION})
    private @interface Status{}
    private static final int NOTHING = 0;
    private static final int REGISTRATION = 1;

    private static @Status int getStatusFromInt(int from) {
        switch (from) {
            case NOTHING:
            case REGISTRATION:
                return from;
            default:
                return NOTHING;
        }
    }
}
