package com.baibus.medicalaccreditation.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.base.ActivityVM;
import com.baibus.medicalaccreditation.base.ActivityVMFactory;
import com.baibus.medicalaccreditation.testing.TestingFragment;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 23.04.2017
 * Time: 21:40
 * To change this template use File | settings | File Templates.
 */
public class MainVM extends ActivityVM<MainActivity> {
    private final static String TAG = MainVM.class.getSimpleName();

    private MainVM(MainActivity activity, @Nullable Bundle savedInstanceState) {
        super(activity, savedInstanceState);
        if (savedInstanceState == null) {
            removeAllAndReplaceFragments(TestingFragment.newInstance(), TestingFragment.TAG);
//            activity.binding.navViewMain.setCheckedItem(R.id.menu_calendar);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    static class FactoryVM implements ActivityVMFactory<MainVM, MainActivity> {
        @NonNull
        @Override
        public MainVM create(MainActivity view) {
            return new MainVM(view, null);
        }

        @NonNull
        @Override
        public MainVM restore(MainActivity view, @NonNull Bundle savedInstanceState) {
            return new MainVM(view, savedInstanceState);
        }
    }
}
