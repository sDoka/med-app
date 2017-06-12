package com.baibus.medicalaccreditation.main;

import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.baibus.medicalaccreditation.BR;
import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.auth.AuthActivity;
import com.baibus.medicalaccreditation.base.ActivityVM;
import com.baibus.medicalaccreditation.base.ActivityVMFactory;
import com.baibus.medicalaccreditation.common.db.tables.AttemptTable;
import com.baibus.medicalaccreditation.common.db.tables.QuestionTable;
import com.baibus.medicalaccreditation.common.provider.ApiModule;
import com.baibus.medicalaccreditation.result.ResultFragment;
import com.baibus.medicalaccreditation.testing.TestingFragment;

import static android.support.v4.view.GravityCompat.START;

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
                int notAttempted = ApiModule
                        .getStoreIOSQLite()
                        .get()
                        .numberOfResults()
                        .withQuery(QuestionTable.queryNotAttempted(specialization.get().getId()))
                        .prepare()
                        .executeAsBlocking();
                if (notAttempted > 0) startTest();
                else startShowResults();
            }
        }
    };

    private MainVM(MainActivity activity, @Nullable Bundle savedInstanceState) {
        super(activity, savedInstanceState);
        if (savedInstanceState == null) {
            if (specialization.get() == null) {
                changeSpecialization(true);
            } else {
                mSpecializationChangedCallback.onPropertyChanged(specialization, BR._all);
            }
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

    @Override
    public boolean onBackKeyPress() {
        if (activity.binding.drawerLayout.isDrawerOpen(START)) {
            activity.binding.drawerLayout.closeDrawer(START);
            return true;
        }
        return super.onBackKeyPress();
    }

    public void fabOnClick() {

    }

    public void changeSpecialization() {
        changeSpecialization(false);
    }

    private void changeSpecialization(boolean need) {
        SpecializationSelectDialog newFragment = SpecializationSelectDialog.newInstance(need);

        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(SpecializationSelectDialog.TAG);
        if (prev != null) {
            ft.remove(prev);
        }

        newFragment.show(ft, SpecializationSelectDialog.TAG);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_spec:
                activity.binding.drawerLayout.closeDrawer(START);
                changeSpecialization();
                return true;
            case R.id.logout:
                activity.startActivity(AuthActivity.getStartIntent());
                activity.finish();
                return true;
        }

        return false;
    }

    private void startTest() {
        removeAllAndReplaceFragments(TestingFragment.newInstance(), TestingFragment.TAG);
    }

    public void startTestAgain() {
        ApiModule
                .getStoreIOSQLite()
                .delete()
                .byQuery(AttemptTable.deleteSpecializationId(specialization.get().getId()))
                .prepare()
                .executeAsBlocking();

        startTest();
    }

    public void startShowResults() {
        removeAllAndReplaceFragments(ResultFragment.newInstance(), ResultFragment.TAG);
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
