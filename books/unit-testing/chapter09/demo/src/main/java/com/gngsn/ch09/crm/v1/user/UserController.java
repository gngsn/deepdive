package com.gngsn.ch09.crm.v1.user;

import com.gngsn.ch09.crm.v1.Database;
import com.gngsn.ch09.crm.v1.IMessageBus;
import com.gngsn.ch09.crm.v1.company.Company;
import com.gngsn.ch09.crm.v1.company.CompanyFactory;
import com.gngsn.ch09.crm.v1.logger.EventDispatcher;
import com.gngsn.ch09.crm.v1.logger.IDomainLogger;

public class UserController {
    private final Database database;
    private final EventDispatcher eventDispatcher;

    public UserController(Database database, IMessageBus messageBus, IDomainLogger domainLogger)  {
        this.database = database;
        this.eventDispatcher = new EventDispatcher(messageBus, domainLogger);
    }

    public String ChangeEmail(int userId, String newEmail) {
        Object[] userData = database.getUserById(userId);
        User user = UserFactory.create(userData);

        String error = user.canChangeEmail();
        if (error != null)
            return error;

        Object[] companyData = database.getCompany();
        Company company = CompanyFactory.create(companyData);

        user.changeEmail(newEmail, company);

        database.saveCompany(company);
        database.saveUser(user);
        eventDispatcher.dispatch(user.domainEvents);

        return "OK";
    }
}