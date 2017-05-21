package com.baibus.medicalaccreditation.auth;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;

import com.baibus.medicalaccreditation.BR;
import com.baibus.medicalaccreditation.MedApplication;
import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.base.Activity;
import com.baibus.medicalaccreditation.databinding.ActivityAuthBinding;

public class AuthActivity extends Activity<AuthActivity, ActivityAuthBinding, AuthVM, AuthVM.FactoryVM> {

    public static Intent getStartIntent() {
        return new Intent(MedApplication.getInstance(), AuthActivity.class);
    }

    @Override
    protected int getVariable() {
        return BR.viewModel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth;
    }

    @NonNull
    @Override
    protected AuthVM.FactoryVM getFactory() {
        return new AuthVM.FactoryVM();
    }

    @NonNull
    @Override
    protected AuthActivity getThis() {
        return this;
    }

    @Override
    public CoordinatorLayout coordinatorLayout() {
        return binding.coordinatorLayout;
    }

    @IdRes
    @Override
    public int getFragmentContainerId() {
        return R.id.mainFrame;
    }

    @Override
    protected void onCreatedVM() {
        setupActionBar();
    }

    @Override
    protected void onRestoredVM() {
        setupActionBar();
    }

    @Override
    public void onBackStackChanged() {
        super.onBackStackChanged();
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void setupActionBar() {
        // Show the Up button in the action bar.
        setSupportActionBar(binding.toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top);
    }
}
