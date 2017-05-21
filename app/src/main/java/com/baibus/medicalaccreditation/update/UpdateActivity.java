package com.baibus.medicalaccreditation.update;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;

import com.baibus.medicalaccreditation.BR;
import com.baibus.medicalaccreditation.MedApplication;
import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.base.Activity;
import com.baibus.medicalaccreditation.databinding.ActivityUpdateBinding;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 21.05.2017
 * Time: 22:26
 * To change this template use File | settings | File Templates.
 */
public class UpdateActivity extends Activity<UpdateActivity, ActivityUpdateBinding, UpdateVM, UpdateVM.FactoryVM> {

    public static Intent getStartIntent() {
        return new Intent(MedApplication.getInstance(), UpdateActivity.class);
    }

    @Override
    protected int getVariable() {
        return BR.viewModel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update;
    }

    @NonNull
    @Override
    protected UpdateVM.FactoryVM getFactory() {
        return new UpdateVM.FactoryVM();
    }

    @NonNull
    @Override
    protected UpdateActivity getThis() {
        return this;
    }

    @Override
    protected void onCreatedVM() {
        setupActionBar();
    }

    @Override
    protected void onRestoredVM() {
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public CoordinatorLayout coordinatorLayout() {
        return binding.coordinatorLayout;
    }
}
