package com.baibus.medicalaccreditation.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;


import com.baibus.medicalaccreditation.MedApplication;
import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.baibus.medicalaccreditation.common.db.entities.User;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public abstract class ActivityVM<A extends Activity> extends BaseObservable {

    private final static String BUNDLE_IS_SHOW_PROGRESS = "isShowProgress";

    protected A activity;

    public ObservableField<CharSequence> title = new ObservableField<>();

    public final ObservableBoolean isShowProgress = new ObservableBoolean();

    public ObservableField<Specialization> specialization = new ObservableField<>();
    public ObservableField<User> user = new ObservableField<>();

    public static String isMainLooper() {
        return Looper.getMainLooper() == Looper.myLooper() ? " IS MAIN THREAD " : " IN BACKGROUND ";
    }

    public ActivityVM(A activity, @Nullable Bundle savedInstanceState) {
        this.activity = activity;
        if (savedInstanceState != null) {
            isShowProgress.set(savedInstanceState.getBoolean(BUNDLE_IS_SHOW_PROGRESS));
        }
        activity.addSubscription(MedApplication.getInstance()
                .subscribeOnSpecialization(specialization1 -> specialization.set(specialization1)));
        activity.addSubscription(MedApplication.getInstance()
                .subscribeOnUser(user1 -> user.set(user1)));
    }

    /**
     * Activity lifecycle
     */
    protected boolean onBackKeyPress() {
        //Return false if need default handle
        return false;
    }

    protected void onStart() {

    }

    protected void onStop() {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    protected void onDestroy() {

    }

    protected void onPause() {

    }

    protected void onResume() {

    }

    protected void onPostResume() {

    }

    protected void onRestart() {

    }

    protected void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    protected void onPostCreate(Bundle savedInstanceState) {}

    protected boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!activity.getSupportFragmentManager().popBackStackImmediate()) {
                activity.finish();
            }
            return true;
        }
        return false;
    }

    protected void onConfigurationChanged(Configuration newConfig) {

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    protected void onSaveInstanceState(Bundle outState){
        outState.putBoolean(BUNDLE_IS_SHOW_PROGRESS, isShowProgress.get());
    }

    protected boolean onCreateOptionsMenu(Menu menu, @MenuRes int menuRes) {
        return false;
    }

    protected boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    protected void onWindowFocusChanged(boolean hasFocus) {

    }

    public void addFragment(@NonNull Fragment fragment,
                             @NonNull String fragmentTag) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.add(activity.getFragmentContainerId(), fragment, fragmentTag);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void removeCurrentFragment() {
        if (activity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            activity.getSupportFragmentManager().popBackStackImmediate();
        }
    }

    public void removeAllAndReplaceFragments(@NonNull Fragment fragment,
                                                @NonNull String fragmentTag) {
        activity.getSupportFragmentManager().popBackStackImmediate(0, POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(activity.getFragmentContainerId(), fragment, fragmentTag);
        ft.commit();
    }

}