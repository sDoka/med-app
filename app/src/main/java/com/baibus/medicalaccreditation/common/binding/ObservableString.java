package com.baibus.medicalaccreditation.common.binding;

import android.databinding.ObservableField;
import android.text.TextUtils;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 21.05.2017
 * Time: 22:08
 * To change this template use File | settings | File Templates.
 */
public class ObservableString extends ObservableField<String> {
    @Override
    public void set(String value) {
        if (!TextUtils.equals(value, get()))
            super.set(value);
    }
}
