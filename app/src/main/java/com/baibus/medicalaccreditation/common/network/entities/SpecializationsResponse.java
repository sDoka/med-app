package com.baibus.medicalaccreditation.common.network.entities;

import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 19.05.2017
 * Time: 0:21
 * To change this template use File | settings | File Templates.
 */
public class SpecializationsResponse {

    @SerializedName("specializations")
    public List<Specialization> data;

}
