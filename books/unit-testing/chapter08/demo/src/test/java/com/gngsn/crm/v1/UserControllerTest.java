package com.gngsn.crm.v1;

import com.gngsn.ch08.crm.v1.*;
import com.gngsn.ch08.crm.v1.UserController;
import org.junit.jupiter.api.Test;
import com.gngsn.ch08.crm.v1.Database;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserControllerTest {

    @Test
    public void changingEmailFromCorporateToNonCorporate() {
        // Arrange
        Database db = new Database(/* ConnectionString */);
        User user = createUser("user@mycorp.com", UserType.EMPLOYEE, db);
        createCompany("mycorp.com", 1, db);

        IMessageBus messageBusMock = mock(IMessageBus.class);
        UserController sut = new UserController(db, messageBusMock);

        // Act
        String result = sut.changeEmail(user.getUserId(), "new@gmail.com");

        // Assert
        assertEquals("OK", result);

        Object[] userData = db.getUserById(user.getUserId());

        // Assuming UserFactory has a create method which accepts Object array.
        User userFromDb = UserFactory.create(userData);
        assertEquals("new@gmail.com", userFromDb.getEmail());
        assertEquals(UserType.CUSTOMER, userFromDb.getType());

        Object[] companyData = db.getCompany();

        // Assuming CompanyFactory has a create method which accepts Object array.
        Company companyFromDb = CompanyFactory.create(companyData);
        assertEquals(0, companyFromDb.getNumberOfEmployees());

        verify(messageBusMock).sendEmailChangedMessage(user.getUserId(), "new@gmail.com");
    }

    private void createCompany(String s, int i, Database db) {
        // Implementation depends on your application.
    }

    private User createUser(String s, UserType employee, Database db) {
        // Implementation depends on your application.
        return null;
    }
}