package com.baibus.medicalaccreditation.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.baibus.medicalaccreditation.MedApplication;
import com.baibus.medicalaccreditation.R;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;


public abstract class Activity<C extends Activity, B extends ViewDataBinding, VM extends ActivityVM<C>, VMFactory extends ActivityVMFactory<VM, C>>
		extends AppCompatActivity
		implements FragmentManager.OnBackStackChangedListener {
	private final String TAG = getClass().getName();
	private final CompositeSubscription mCompositeSubscription = Subscriptions.from();
	public B binding;
	public VM viewModel;
	public final Handler mainHandler = new Handler();
	
	protected abstract @IdRes int getVariable();
	protected abstract @LayoutRes int getLayoutId();
	protected abstract @NonNull VMFactory getFactory();
	protected abstract @NonNull C getThis();
    protected abstract void onCreatedVM();
    protected abstract void onRestoredVM();

	@MenuRes
	public int getMenu() {
		return 0;
	}

	@IdRes
	public int getFragmentContainerId() {
		return 0;
	}

	public final void resetViewModel() {
		viewModel.onPause();
		viewModel.onStop();
		viewModel.onDestroy();
		viewModel = onCreate();
		onCreatedVM();
		viewModel.onPostCreate(null);
		viewModel.onStart();
		viewModel.onResume();
		binding.setVariable(getVariable(), viewModel);
		supportInvalidateOptionsMenu();
	}

	public CoordinatorLayout coordinatorLayout() {
		return null;
	}

	@Override
	protected final void onCreate(@Nullable Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		bind(savedInstanceState);
	}

	@Override
	public final boolean onCreateOptionsMenu(Menu menu) {
		return viewModel.onCreateOptionsMenu(menu, getMenu())
				|| super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return viewModel.onPrepareOptionsMenu(menu)
				|| super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Making notification bar transparent
	 */
	protected void transparentStatusBarColor() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
//			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		}
	}

	@Override
	protected final void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		viewModel.onPostCreate(savedInstanceState);
	}

	@Override
	protected final void onStart() {
		super.onStart();
		viewModel.onStart();
	}

	@Override
	protected final void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		viewModel.onResume();
	}

	@Override
	protected final void onPostResume() {
		super.onPostResume();
		viewModel.onPostResume();
	}

	@Override
	protected final void onPause() {
		Log.d(TAG, "onPause");
		viewModel.onPause();
		super.onPause();
	}

	@Override
	protected final void onStop() {
		viewModel.onStop();
		super.onStop();
	}

	@Override
	protected final void onRestart() {
		viewModel.onRestart();
		super.onRestart();
	}

	@Override
	protected final void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
		getSupportFragmentManager().removeOnBackStackChangedListener(this);
		mCompositeSubscription.clear();
		mainHandler.removeCallbacksAndMessages(null);
		if (viewModel != null) {
			viewModel.onDestroy();
		}
		binding.unbind();
		binding = null;
//		MedApplication.getInstance().refWatcher().watch(this);
//		if (viewModel != null) {
//			MedApplication.getInstance().refWatcher().watch(viewModel);
//		}
	}

	@Override
	protected final void onSaveInstanceState(Bundle outState) {
		viewModel.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected final void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		viewModel.onRestoreInstanceState(savedInstanceState);
	}


	@Override
	public final void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		viewModel.onConfigurationChanged(newConfig);
	}

	@Override
	public final void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		viewModel.onWindowFocusChanged(hasFocus);
	}

	@Override
	public final void onBackPressed() {
		Log.d(TAG, "onBackPressed");
		if (!viewModel.onBackKeyPress()) {
			super.onBackPressed();
			overridePendingTransitionExit();
		}
	}

	@Override
	public void onBackStackChanged() {
		android.support.v4.app.Fragment currFrag = getCurrentFragment();
		if (currFrag instanceof Fragment)
			((Fragment) currFrag).onFragmentResume();
	}

	protected android.support.v4.app.Fragment getCurrentFragment(){
		FragmentManager manager = getSupportFragmentManager();
		@IdRes int id = getFragmentContainerId();

		if (manager != null && id != 0) {
			return manager.findFragmentById(id);
		}
		return null;
	}

	@Override
	public final boolean onOptionsItemSelected(MenuItem item) {
		return viewModel.onOptionsItemSelected(item)
				|| super.onOptionsItemSelected(item);
	}

	@Override
	public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi (Build.VERSION_CODES.HONEYCOMB)
	protected void setupActionBar() {

	}

	@Override
	protected final void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		viewModel.onActivityResult(requestCode, resultCode, data);
	}

	public final void addSubscription(Subscription subscription) {
		mCompositeSubscription.add(subscription);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransitionEnter();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		overridePendingTransitionEnterResult();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransitionExit();
	}

	/**
	 * Overrides the pending Activity transition by performing the "Enter" animation.
	 */
	protected void overridePendingTransitionEnterResult() {
		overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
	}

	/**
	 * Overrides the pending Activity transition by performing the "Enter" animation.
	 */
	protected void overridePendingTransitionEnter() {
		overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
	}

	/**
	 * Overrides the pending Activity transition by performing the "Exit" animation.
	 */
	protected void overridePendingTransitionExit() {
		overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
	}


	private void bind(@Nullable Bundle savedInstanceState) {
		binding = DataBindingUtil.setContentView(this, getLayoutId());
		if (savedInstanceState == null) {
			viewModel = onCreate();
			onCreatedVM();
		} else {
			viewModel = onRestore(savedInstanceState);
			onRestoredVM();
		}
		binding.setVariable(getVariable(), viewModel);
		binding.executePendingBindings();
		getSupportFragmentManager().addOnBackStackChangedListener(this);
	}

	private VM onCreate() {
		return getFactory().create(getThis());
	}

	private VM onRestore(@NonNull Bundle savedInstanceState) {
		return getFactory().restore(getThis(), savedInstanceState);
	}
}
