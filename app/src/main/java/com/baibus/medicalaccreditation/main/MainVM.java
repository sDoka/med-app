package com.baibus.medicalaccreditation.main;

import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

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

    private final OnPropertyChangedCallback mSpecializationChangedCallback = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            if (specialization.get() != null) {
                removeAllAndReplaceFragments(TestingFragment.newInstance(), TestingFragment.TAG);
            }
        }
    };

    private MainVM(MainActivity activity, @Nullable Bundle savedInstanceState) {
        super(activity, savedInstanceState);
        if (savedInstanceState == null) {
            changeSpecialization();
//            removeAllAndReplaceFragments(TestingFragment.newInstance(), TestingFragment.TAG);
//            activity.binding.navViewMain.setCheckedItem(R.id.menu_calendar);
        }
        specialization.addOnPropertyChangedCallback(mSpecializationChangedCallback);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        specialization.removeOnPropertyChangedCallback(mSpecializationChangedCallback);
    }

    public void fabOnClick() {

    }

    public void changeSpecialization() {
        SpecializationSelectDialog newFragment = SpecializationSelectDialog.newInstance();

        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(SpecializationSelectDialog.TAG);
        if (prev != null) {
            ft.remove(prev);
        }

        newFragment.show(ft, SpecializationSelectDialog.TAG);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
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
