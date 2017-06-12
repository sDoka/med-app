package com.baibus.medicalaccreditation.base;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.baibus.medicalaccreditation.MedApplication;
import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.baibus.medicalaccreditation.common.db.entities.User;

public abstract class FragmentVM<F extends Fragment> extends BaseObservable {

    private final static String BUNDLE_IS_SHOW_PROGRESS = "isShowProgress";

    private boolean mActivityCreated = false;

    protected F fragment;

    protected CharSequence title;

    public final ObservableBoolean isShowProgress = new ObservableBoolean();

    public ObservableField<Specialization> specialization = new ObservableField<>();
    public ObservableField<User> user = new ObservableField<>();


    public FragmentVM(F fragment, @Nullable Bundle savedInstanceState) {
        this.fragment = fragment;
        if (savedInstanceState != null) {
            isShowProgress.set(savedInstanceState.getBoolean(BUNDLE_IS_SHOW_PROGRESS));
        }
        fragment.addSubscription(MedApplication.getInstance()
                .subscribeOnSpecialization(specialization1 -> specialization.set(specialization1)));
        fragment.addSubscription(MedApplication.getInstance()
                .subscribeOnUser(user1 -> user.set(user1)));
    }

    protected void setTitle(CharSequence title) {
        this.title = title;
        fragment.setTitle(this);
    }


    public void onViewCreated() {

    }

    public boolean isActivityCreated() {
        return mActivityCreated;
    }

    /**
     * Fragment lifecycle
     */
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mActivityCreated = true;
    }

    public void onStart() {

    }

    public void onStop() {

    }

    public void onDestroy() {

    }

    public void onPause() {

    }

    public void onResume() {

    }

    public void onDestroyView() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(BUNDLE_IS_SHOW_PROGRESS, isShowProgress.get());
    }

    public void onViewStateRestored(Bundle savedInstanceState) {

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public void onPrepareOptionsMenu(Menu menu) {

    }

}
