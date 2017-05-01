package com.baibus.medicalaccreditation.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.baibus.medicalaccreditation.BR;
import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.base.Activity;
import com.baibus.medicalaccreditation.base.Fragment;
import com.baibus.medicalaccreditation.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment<LoginFragment, FragmentLoginBinding, LoginVM, LoginVM.FactoryVM> {
    final static String TAG = LoginFragment.class.getName();

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @NonNull
    @Override
    protected LoginVM.FactoryVM getFactory() {
        return new LoginVM.FactoryVM();
    }

    @NonNull
    @Override
    protected LoginFragment getThis() {
        return this;
    }

    @Override
    protected void onCreatedVM() {

    }

    @Override
    protected void onRestoredVM() {

    }

    void showSnackBar(@NonNull String text,
                      @Nullable String action,
                      @Nullable View.OnClickListener listener) {
        CoordinatorLayout coordinatorLayout = ((Activity) getActivity())
                .coordinatorLayout();
        if (coordinatorLayout != null) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG);
            if (listener != null && !TextUtils.isEmpty(action)) {
                snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(action, listener);
            }
            snackbar.show();
        }
    }

    @Override
    public int getMenu() {
        return super.getMenu();
    }
}
