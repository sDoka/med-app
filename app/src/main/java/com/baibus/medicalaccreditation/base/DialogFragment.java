package com.baibus.medicalaccreditation.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;


public abstract class DialogFragment<C extends DialogFragment,
        B extends ViewDataBinding, VM extends DialogFragmentVM<C>,
        VMFactory extends DialogFragmentVMFactory<VM, C>>
        extends android.support.v4.app.DialogFragment implements DialogInterface.OnShowListener {

    private final CompositeSubscription mCompositeSubscription = Subscriptions.from();
    @Nullable
    public B binding;
    public VM viewModel;
    public final Handler mainHandler = new Handler();

    public abstract @IdRes int getVariable();
    public abstract @LayoutRes int getLayoutId();
    protected abstract @NonNull VMFactory getFactory();
    protected abstract @NonNull C getThis();
    protected abstract void onCreatedVM();
    protected abstract void onRestoredVM();

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public final Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            viewModel = onCreate();
            onCreatedVM();
        } else {
            viewModel = onRestore(savedInstanceState);
            onRestoredVM();
        }
        if (getLayoutId() != 0 && getVariable() != 0) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            binding = DataBindingUtil.inflate(inflater, getLayoutId(), null, false);
            if (binding != null) {
                binding.setVariable(getVariable(), viewModel);
                binding.executePendingBindings();
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (binding != null) builder.setView(binding.getRoot());
        Dialog dialog = viewModel.onBuildDialog(builder, savedInstanceState);
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public final void onShow(DialogInterface dialog) {
        onShowDialog((AlertDialog) dialog);
        if (viewModel != null) viewModel.onShow((AlertDialog) dialog);
    }

    protected void onShowDialog(AlertDialog dialog) {

    }

    private VM onCreate() {
        return getFactory().create(getThis());
    }

    private VM onRestore(@NonNull Bundle savedInstanceState) {
        return getFactory().restore(getThis(), savedInstanceState);
    }

//    change this implementation if want use resetViewModel
//    public final void resetViewModel() {
//        viewModel = onCreate();
//        onCreatedVM();
//        binding.setVariable(getVariable(), viewModel);
//    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (viewModel != null) viewModel.onActivityCreated(savedInstanceState);
    }

    @Override
    public final void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (viewModel != null) viewModel.onViewCreated();
    }

    @Override
    public final void onStart() {
        super.onStart();
        if (viewModel != null) viewModel.onStart();
    }

    @Override
    public final void onResume() {
        super.onResume();
        if (viewModel != null) viewModel.onResume();
    }

    @Override
    public final void onPause() {
        if (viewModel != null) viewModel.onPause();
        super.onPause();
    }

    @Override
    public final void onStop() {
        if (viewModel != null) viewModel.onStop();
        super.onStop();
    }

    @Override
    public final void onDestroyView() {
        if (binding != null) binding.unbind();
        binding = null;
        if (viewModel != null) {
            viewModel.onDestroyView();
        }
        super.onDestroyView();
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.clear();
        mainHandler.removeCallbacksAndMessages(null);
        if (viewModel != null) {
            viewModel.onDestroy();
        }
    }

    @Override
    public final void onSaveInstanceState(Bundle outState) {
        if (viewModel != null) {
            viewModel.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public final void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (viewModel != null) {
            viewModel.onViewStateRestored(savedInstanceState);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (viewModel != null) {
            viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public final void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (viewModel != null) {
            viewModel.onActivityResult(requestCode, resultCode, data);
        }
    }

    public final void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    @Override
    public final void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (viewModel != null) {
            viewModel.onDismiss(dialog);
        }
    }
}
