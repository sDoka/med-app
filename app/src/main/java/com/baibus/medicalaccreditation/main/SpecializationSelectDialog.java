package com.baibus.medicalaccreditation.main;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.base.DialogFragment;

import static com.baibus.medicalaccreditation.main.SpecializationSelectDialogVM.ARGUMENT_IS_NEED;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.05.2017
 * Time: 1:18
 * To change this template use File | settings | File Templates.
 */
public class SpecializationSelectDialog extends DialogFragment<SpecializationSelectDialog,
        ViewDataBinding, SpecializationSelectDialogVM, SpecializationSelectDialogVM.VMFactory> {

    public static final String TAG = SpecializationSelectDialog.class.getName();

    public static SpecializationSelectDialog newInstance(boolean need) {

        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_IS_NEED, need);
        SpecializationSelectDialog fragment = new SpecializationSelectDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @NonNull
    @Override
    protected SpecializationSelectDialogVM.VMFactory getFactory() {
        return new SpecializationSelectDialogVM.VMFactory();
    }

    @NonNull
    @Override
    protected SpecializationSelectDialog getThis() {
        return this;
    }

    @Override
    protected void onCreatedVM() {

    }

    @Override
    protected void onRestoredVM() {

    }
}
