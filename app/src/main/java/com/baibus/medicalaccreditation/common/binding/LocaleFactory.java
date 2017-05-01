package com.baibus.medicalaccreditation.common.binding;

import java.util.Locale;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 14.04.2017
 * Time: 18:43
 * To change this template use File | settings | File Templates.
 */
public class LocaleFactory {

    public static Locale create(String lang, String country) {
        return new Locale(lang, country);
    }
}
