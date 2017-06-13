package com.baibus.medicalaccreditation.common.binding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.databinding.adapters.ListenerUtil;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.baibus.medicalaccreditation.BR;
import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.databinding.PartialNavHeaderMainBinding;
import com.baibus.medicalaccreditation.main.MainVM;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.WeakReference;

public class BindingAdapters {
    private BindingAdapters() {
        throw new AssertionError();
    }

    @BindingAdapter("hasFixedSize")
    public static void bindHasFixedSize(RecyclerView view, boolean newValue) {
        view.setHasFixedSize(newValue);
    }


    @BindingAdapter("animatedVisibility")
    public static void setVisibility(View view, int visibility) {
        // Were we animating before? If so, what was the visibility?
        Integer endAnimVisibility =
                (Integer) view.getTag(R.id.finalVisibility);
        int oldVisibility = endAnimVisibility == null
                ? view.getVisibility()
                : endAnimVisibility;

        if (oldVisibility == visibility) {
            // just let it finish any current animation.
            return;
        }

        boolean isVisible = oldVisibility == View.VISIBLE;
        boolean willBeVisible = visibility == View.VISIBLE;

        view.setVisibility(View.VISIBLE);
        float startAlpha = isVisible ? 1f : 0f;
        if (endAnimVisibility != null) {
            startAlpha = view.getAlpha();
        }
        float endAlpha = willBeVisible ? 1f : 0f;

        // Now newInstance an animator
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, startAlpha, endAlpha);

        alpha.addListener(new VisibilityAnimatorListenerAdapter(view, visibility));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            alpha.setAutoCancel(true);
        } else {
            if (endAnimVisibility != null) {
                ObjectAnimator oldAlpha =
                        ListenerUtil.trackListener(view, alpha, R.id.visibilityObjectAnimator);
                if (oldAlpha != null) {
                    oldAlpha.cancel();
                }
            }
        }

        alpha.start();
    }

    @BindingAdapter({"progressBarImage"})
    public static void bindProgressBarImage(SimpleDraweeView view, Drawable progressBarImage) {
        view.getHierarchy().setProgressBarImage(progressBarImage);
    }

    @BindingAdapter(value = {"cornerRadius", "cornerTopLeft", "cornerTopRight", "cornerBottomRight", "cornerBottomLeft"}, requireAll = false)
    public static void bindRoundFresco(SimpleDraweeView view, float radius, boolean topLeft, boolean topRight, boolean bottomRight, boolean bottomLeft) {
        RoundingParams roundingParams = view.getHierarchy().getRoundingParams();
        if (roundingParams == null) roundingParams = RoundingParams.fromCornersRadius(radius);
        roundingParams.setCornersRadii(topLeft ? radius : 0f, topRight ? radius : 0f,
                bottomRight ? radius : 0f, bottomLeft ? radius : 0f);
        view.getHierarchy().setRoundingParams(roundingParams);
    }

    @BindingAdapter({"model"})
    public static void loadHeader(NavigationView view, MainVM model) {
        View viewHeaderView = view.getHeaderView(0);
        if (viewHeaderView != null) {
            ViewDataBinding binding = DataBindingUtil.bind(viewHeaderView);
            if (binding != null) {
                binding.setVariable(BR.viewModel, model);
                //binding.executePendingBindings();
            }
        }
    }

    @BindingAdapter({"colorProgress"})
    public static void setColorProgressBar(ProgressBar bar, @ColorRes int color) {
        bar.getProgressDrawable().setColorFilter(ContextCompat.getColor(bar.getContext(), color),
                PorterDuff.Mode.SRC_IN);
    }

    private static class VisibilityAnimatorListenerAdapter extends AnimatorListenerAdapter {
        private final WeakReference<View> mView;
        private final int mVisibility;
        private boolean mIsCanceled;

        private VisibilityAnimatorListenerAdapter(View view, int visibility) {
            mView = new WeakReference<>(view);
            mVisibility = visibility;
        }

        @Override
        public void onAnimationStart(Animator anim) {
            View view = mView.get();
            if (view != null) {
                view.setTag(R.id.finalVisibility, mVisibility);
            }
        }

        @Override
        public void onAnimationCancel(Animator anim) {
            mIsCanceled = true;
        }

        @Override
        public void onAnimationEnd(Animator anim) {
            View view = mView.get();
            if (view == null) return;
            view.setTag(R.id.finalVisibility, null);
            if (!mIsCanceled) {
                view.setAlpha(1f);
                view.setVisibility(mVisibility);
            }
        }
    }

    @BindingAdapter({"itemDivider"})
    public static void bindItemDecorationDivider(RecyclerView view,
                                                 RecyclerView.ItemDecoration newValue,
                                                 RecyclerView.ItemDecoration oldValue) {
        if (newValue == oldValue) return;
        if (oldValue != null) view.removeItemDecoration(oldValue);
        if (newValue != null) view.addItemDecoration(newValue);
    }

    @BindingAdapter({"startOffset"})
    public static void bindStartOffset(RecyclerView view,
                                               RecyclerView.ItemDecoration newValue,
                                               RecyclerView.ItemDecoration oldValue) {
        if (newValue == oldValue) return;
        if (oldValue != null) view.removeItemDecoration(oldValue);
        if (newValue != null) view.addItemDecoration(newValue);
    }

    @BindingAdapter({"endOffset"})
    public static void bindEndOffset(RecyclerView view,
                                             RecyclerView.ItemDecoration newValue,
                                             RecyclerView.ItemDecoration oldValue) {
        if (newValue == oldValue) return;
        if (oldValue != null) view.removeItemDecoration(oldValue);
        if (newValue != null) view.addItemDecoration(newValue);
    }

}