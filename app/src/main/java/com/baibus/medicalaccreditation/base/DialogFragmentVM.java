package com.baibus.medicalaccreditation.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;



public abstract class DialogFragmentVM<F extends DialogFragment> extends BaseObservable {

    public final static String BUNDLE_IS_SHOW_PROGRESS = "isShowProgress";

    private boolean mActivityCreated = false;

    protected F fragment;

    public final ObservableBoolean isShowProgress = new ObservableBoolean();


    public DialogFragmentVM(F fragment, @Nullable Bundle savedInstanceState) {
        this.fragment = fragment;
        if (savedInstanceState != null) {
            isShowProgress.set(savedInstanceState.getBoolean(BUNDLE_IS_SHOW_PROGRESS));
        }
    }

    public Fragment getParentFragment() {
        return fragment.getTargetFragment();
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

    public void onViewCreated() {

    }

    public Dialog onBuildDialog(AlertDialog.Builder builder, @Nullable Bundle savedInstanceState){
        return builder.create();
    }

    public void onShow(AlertDialog dialog) {

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

    public void onDismiss(DialogInterface dialog) {

    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(BUNDLE_IS_SHOW_PROGRESS, isShowProgress.get());
    }

    public void onViewStateRestored(Bundle savedInstanceState) {

    }

}
