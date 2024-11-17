package com.gngsn.ch09.crm.v1.user;

import com.gngsn.ch09.crm.v1.Database;
import com.gngsn.ch09.crm.v1.IMessageBus;
import com.gngsn.ch09.crm.v1.company.Company;
import com.gngsn.ch09.crm.v1.company.CompanyFactory;
import com.gngsn.ch09.crm.v1.logger.IDomainLogger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    // [Fact]
    @Test
    public void Changing_email_from_corporate_to_non_corporate() {
        // Arrange
        var db = new Database(/* ConnectionString */);
        User user = createUser("user@mycorp.com", UserType.EMPLOYEE, db);
        CreateCompany("mycorp.com", 1, db);

        IMessageBus messageBusMock = Mockito.mock(IMessageBus.class); // Mock 설정
        IDomainLogger loggerMock = Mockito.mock(IDomainLogger.class); // Mock 설정
        var sut = new UserController(db, messageBusMock, loggerMock);

        // Act
        String result = sut.ChangeEmail(user.getUserId(), "new@gmail.com");

        // Assert
        assertEquals("OK", result);

        Object[] userData = db.getUserById(user.getUserId());
        User userFromDb = UserFactory.create(userData);
        assertEquals("new@gmail.com", userFromDb.getEmail());
        assertEquals(UserType.CUSTOMER, userFromDb.getType());

        Object[] companyData = db.getCompany();
        Company companyFromDb = CompanyFactory.create(companyData);
        assertEquals(0, companyFromDb.NumberOfEmployees);

//        messageBusMock.Verify(x => x.SendEmailChangedMessage(user.UserId, "new@gmail.com"), Times.Once);
//        loggerMock.Verify(x => x.UserTypeHasChanged(user.getUserId(), UserType.EMPLOYEE, UserType.CUSTOMER), Times.Once);
    }

    private void CreateCompany(String s, int i, Database db) {
        // ...db.insert
    }

    private User createUser(String email, UserType userType, Database db) {
        int userId = 23; // get from db
        return new User(userId, email, userType);
    }

}