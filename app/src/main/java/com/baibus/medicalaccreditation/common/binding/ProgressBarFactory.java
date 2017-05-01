package com.baibus.medicalaccreditation.common.binding;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

import com.facebook.drawee.drawable.ProgressBarDrawable;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 30.04.2017
 * Time: 22:58
 * To change this template use File | settings | File Templates.
 */
public class ProgressBarFactory {

    public static Drawable newInstance(@ColorInt int color) {
        ProgressBarDrawable drawable = new ProgressBarDrawable();
        drawable.setColor(color);
        return drawable;
    }
}
