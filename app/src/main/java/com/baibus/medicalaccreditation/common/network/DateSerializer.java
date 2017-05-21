package com.baibus.medicalaccreditation.common.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 18.05.2017
 * Time: 20:03
 * To change this template use File | settings | File Templates.
 */
public class DateSerializer implements JsonSerializer<Date> {
    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return src == null ? null : new JsonPrimitive(src.getTime());
    }
}
