package com.baibus.medicalaccreditation.common.network.entities;

import com.baibus.medicalaccreditation.common.db.entities.Account;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 05.06.2017
 * Time: 2:56
 * To change this template use File | settings | File Templates.
 */
public class AccountResponse {

    @SerializedName("userAccounts")
    public List<Account> data;
}
