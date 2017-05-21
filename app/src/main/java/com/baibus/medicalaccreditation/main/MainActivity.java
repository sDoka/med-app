package com.baibus.medicalaccreditation.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarDrawerToggle;

import com.baibus.medicalaccreditation.BR;
import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.base.Activity;
import com.baibus.medicalaccreditation.MedApplication;
import com.baibus.medicalaccreditation.databinding.ActivityMainBinding;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 23.04.2017
 * Time: 21:38
 * To change this template use File | settings | File Templates.
 */
public class MainActivity extends Activity<MainActivity, ActivityMainBinding, MainVM, MainVM.FactoryVM> {

    public static Intent getStartIntent() {
        return new Intent(MedApplication.getInstance(), MainActivity.class);
    }

    @Override
    protected int getVariable() {
        return BR.viewModel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @NonNull
    @Override
    protected MainVM.FactoryVM getFactory() {
        return new MainVM.FactoryVM();
    }

    @NonNull
    @Override
    protected MainActivity getThis() {
        return this;
    }

    @IdRes
    @Override
    public int getFragmentContainerId() {
        return R.id.mainFrame;
    }

    @Override
    protected void onCreatedVM() {
        setupActionBar();
        setupDrawerLayout();

    }

    @Override
    protected void onRestoredVM() {
        setupActionBar();
        setupDrawerLayout();
    }

    @Override
    public int getMenu() {
        return R.menu.activity_main;
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void setupActionBar() {
        // Show the Up button in the action bar.
        setSupportActionBar(binding.appBar.toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

//    @IdRes
//    @Override
//    public int getFragmentContainerId() {
//        return R.id.mainFrame;
//    }

    private void setupDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout, binding.appBar.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top);
    }
}
