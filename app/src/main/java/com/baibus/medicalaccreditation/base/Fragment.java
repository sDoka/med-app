package com.baibus.medicalaccreditation.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public abstract class Fragment<C extends Fragment, B extends ViewDataBinding, VM extends FragmentVM<C>, VMFactory extends FragmentVMFactory<VM, C>>
        extends android.support.v4.app.Fragment {

    private final CompositeSubscription mCompositeSubscription = Subscriptions.from();
    public B binding;
    public VM viewModel;
    public final Handler mainHandler = new Handler();

    public abstract @IdRes int getVariable();
    public abstract @LayoutRes int getLayoutId();
    protected abstract @NonNull VMFactory getFactory();
    protected abstract @NonNull C getThis();
    protected abstract void onCreatedVM();
    protected abstract void onRestoredVM();

    @MenuRes
    public int getMenu() {
        return 0;
    }

    public final void resetViewModel() {
        viewModel.onPause();
        viewModel.onStop();
        viewModel.onDestroyView();
        viewModel.onDestroy();
        viewModel = onCreate();
        onCreatedVM();
        viewModel.onViewCreated();
        viewModel.onActivityCreated(null);
        viewModel.onStart();
        viewModel.onResume();
        binding.setVariable(getVariable(), viewModel);
        getActivity().supportInvalidateOptionsMenu();
    }
    
    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(getMenu() != 0);
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        if (savedInstanceState == null) {
            viewModel = onCreate();
            onCreatedVM();
        } else {
            viewModel = onRestore(savedInstanceState);
            onRestoredVM();
        }
        binding.setVariable(getVariable(), viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    public final void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.onViewCreated();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.onActivityCreated(savedInstanceState);
    }

    @Override
    public final void onStart() {
        super.onStart();
        viewModel.onStart();
    }


    @Override
    public final void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    public final void onPause() {
        viewModel.onPause();
        super.onPause();
    }

    @Override
    public final void onStop() {
        viewModel.onStop();
        super.onStop();
    }

    @Override
    public final void onDestroyView() {
        binding.unbind();
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
//        MedApplication.getInstance().refWatcher().watch(this);
//        if (viewModel != null) {
//            MedApplication.getInstance().refWatcher().watch(viewModel);
//        }
    }

    @Override
    public final void onSaveInstanceState(Bundle outState) {
        if (viewModel != null) {
            viewModel.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        @MenuRes int menuId = getMenu();
        if (menuId != 0) {
            inflater.inflate(menuId, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public final void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (viewModel != null) {
            viewModel.onPrepareOptionsMenu(menu);
        }
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        return viewModel.onOptionsItemSelected(item)
                || super.onOptionsItemSelected(item);
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


    void setTitle(FragmentVM fVM) {
        if (getActivity() instanceof Activity) {
            Activity smActivity = (Activity) getActivity();
            ActivityVM<?> aVM = smActivity.viewModel;
            aVM.title.set(fVM.title);
        }
    }

    protected final void onFragmentResume() {
        setTitle(viewModel);
    }

    private VM onCreate() {
        return getFactory().create(getThis());
    }

    private VM onRestore(@NonNull Bundle savedInstanceState) {
        return getFactory().restore(getThis(), savedInstanceState);
    }
}