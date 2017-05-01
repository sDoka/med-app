package com.baibus.medicalaccreditation.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.baibus.medicalaccreditation.base.ActivityVM;
import com.baibus.medicalaccreditation.base.ActivityVMFactory;

public class AuthVM extends ActivityVM<AuthActivity> {
    private final static String TAG = AuthVM.class.getSimpleName();

    private final FragmentManager.OnBackStackChangedListener mBackStackChangedListener = activity::setupActionBar;

    private AuthVM(AuthActivity activity, @Nullable Bundle savedInstanceState) {
        super(activity, savedInstanceState);
        if (savedInstanceState == null) {
            removeAllAndReplaceFragments(LoginFragment.newInstance(), LoginFragment.TAG);
        }
        activity.getSupportFragmentManager().addOnBackStackChangedListener(mBackStackChangedListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity.getSupportFragmentManager().removeOnBackStackChangedListener(mBackStackChangedListener);
    }

    @Override
    public void removeCurrentFragment() {
        super.removeCurrentFragment();
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(LoginFragment.TAG);
        if (fragment instanceof LoginFragment) {
            ((LoginFragment) fragment).viewModel.showMessageSuccessRegistration();
        }
    }

    static class FactoryVM implements ActivityVMFactory<AuthVM, AuthActivity> {
        @NonNull
        @Override
        public AuthVM create(AuthActivity view) {
            return new AuthVM(view, null);
        }

        @NonNull
        @Override
        public AuthVM restore(AuthActivity view, @NonNull Bundle savedInstanceState) {
            return new AuthVM(view, savedInstanceState);
        }
    }
}
