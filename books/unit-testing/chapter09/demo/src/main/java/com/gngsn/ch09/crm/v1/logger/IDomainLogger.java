package com.gngsn.ch09.crm.v1.logger;

import com.gngsn.ch09.crm.v1.user.UserType;
public interface IDomainLogger {

    void userTypeHasChanged(int userId, UserType oldType, UserType newType);
}
