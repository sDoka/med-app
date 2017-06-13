package com.baibus.medicalaccreditation.result;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.BR;
import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.base.Fragment;
import com.baibus.medicalaccreditation.databinding.FragmentResultBinding;
import com.baibus.medicalaccreditation.main.MainActivity;

import java.lang.ref.WeakReference;


/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 01.05.2017
 * Time: 8:27
 * To change this template use File | settings | File Templates.
 */
public class ResultFragment extends Fragment<ResultFragment, FragmentResultBinding, ResultVM, ResultVM.FactoryVM> {
    public final static String TAG = ResultFragment.class.getName();

    public static ResultFragment newInstance() {

        Bundle args = new Bundle();

        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_result;
    }

    @NonNull
    @Override
    protected ResultVM.FactoryVM getFactory() {
        return new ResultVM.FactoryVM();
    }

    @NonNull
    @Override
    protected ResultFragment getThis() {
        return this;
    }

    @Override
    protected void onCreatedVM() {

    }

    @Override
    protected void onRestoredVM() {

    }

//    private void initPager() {
//        binding.viewPager.addOnPageChangeListener(new OnPageChangeListener(this));
//        binding.viewPager.setCurrentItem(viewModel.position.get(), false);
//    }

    void startTestAgain() {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).viewModel.startTestAgain();
        }
    }

    @Override
    public int getMenu() {
        return R.menu.fragment_result;
    }

//    private static class OnPageChangeListener implements ViewPager.OnPageChangeListener {
//        private final WeakReference<ResultFragment> mResultFragment;
//
//        OnPageChangeListener(ResultFragment resultFragment) {
//            mResultFragment = new WeakReference<>(resultFragment);
//        }
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            final ResultFragment resultFragment = mResultFragment.get();
//            if (resultFragment != null) {
//                final ResultVM testingVM = resultFragment.viewModel;
//                if (testingVM != null && testingVM.position.get() != position) {
//                    testingVM.setTitle(position);
//                }
//            }
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    }
}
