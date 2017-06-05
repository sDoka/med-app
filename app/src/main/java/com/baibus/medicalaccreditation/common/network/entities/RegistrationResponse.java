package com.baibus.medicalaccreditation.common.network.entities;

import com.baibus.medicalaccreditation.common.db.entities.Account;
import com.baibus.medicalaccreditation.common.db.entities.User;

import java.util.Date;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 18.05.2017
 * Time: 23:48
 * To change this template use File | settings | File Templates.
 */
public class RegistrationResponse {

    public long userId;
    public String userName;
    public String email;
    public long actualDeviceId;
    public Date lastSynchronizationDate;
    public long userAccountTypeId;

    public User getUser() {
        return User.newInstance(userId, userName, email, lastSynchronizationDate, actualDeviceId);
    }

    public Account getAccount() {
        //TODO
        return Account.newInstance(userId, userAccountTypeId, 0, 0);
    }

}
