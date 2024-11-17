package com.gngsn.ch08.crm.v1;

/**
 * chapter08 - v1
 */
public class UserController {
    private final Database database;
    private final IMessageBus messageBus;

    public UserController(Database database, IMessageBus messageBus) {
        this.database = database == null ? new Database() : database;
        this.messageBus = messageBus == null ? new MessageBus() : messageBus;
    }

    public String changeEmail(int userId, String newEmail) {
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

        for (EmailChangedEvent ev : user.getEmailChangedEvents()) {
            messageBus.sendEmailChangedMessage(ev.getUserId(), ev.getNewEmail());
        }

        return "OK";
    }
}
