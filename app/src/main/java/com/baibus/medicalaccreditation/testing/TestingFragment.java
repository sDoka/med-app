package com.baibus.medicalaccreditation.testing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.baibus.medicalaccreditation.BR;
import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.base.Fragment;
import com.baibus.medicalaccreditation.databinding.FragmentTestingBinding;

import java.lang.ref.WeakReference;


/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 01.05.2017
 * Time: 8:27
 * To change this template use File | settings | File Templates.
 */
public class TestingFragment extends Fragment<TestingFragment, FragmentTestingBinding, TestingVM, TestingVM.FactoryVM> {
    public final static String TAG = TestingFragment.class.getName();

    public static TestingFragment newInstance() {

        Bundle args = new Bundle();

        TestingFragment fragment = new TestingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_testing;
    }

    @NonNull
    @Override
    protected TestingVM.FactoryVM getFactory() {
        return new TestingVM.FactoryVM();
    }

    @NonNull
    @Override
    protected TestingFragment getThis() {
        return this;
    }

    @Override
    protected void onCreatedVM() {
        initPager();
    }

    @Override
    protected void onRestoredVM() {
        initPager();
    }

    private void initPager() {
        binding.viewPager.addOnPageChangeListener(new OnPageChangeListener(this));
        setCurrentItem(viewModel.position.get(), false);
    }

    void showResult() {
        //TODO finish testing
        //Log.d(TAG, question.toString() + ";position=" + position);
    }

    void setCurrentItem(int position, boolean smoothScroll) {
        binding.viewPager.setCurrentItem(position, smoothScroll);
    }

    @Override
    public int getMenu() {
        return super.getMenu();
    }

    private static class OnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<TestingFragment> mTestingFragment;

        OnPageChangeListener(TestingFragment testingFragment) {
            mTestingFragment = new WeakReference<>(testingFragment);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            final TestingFragment testingFragment = mTestingFragment.get();
            if (testingFragment != null) {
                final TestingVM testingVM = testingFragment.viewModel;
                if (testingVM != null && testingVM.position.get() != position) {
                    testingVM.setTitle(position);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
