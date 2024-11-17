package com.gngsn.cms.v4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;


class UserTest {

    @Test
    void changing_email_from_non_corporate_to_corporate() {
        Company company = new Company("mycorp.com", 1);
        User sut = new User(1, "user@gmail.com", UserType.CUSTOMER);
        sut.changeEmail("new@mycorp.com", company);

        assertEquals(2, company.getNumberOfEmployees());
        assertEquals("new@mycorp.com", sut.getEmail());
        assertEquals(UserType.EMPLOYEE, sut.getType());
    }

    @ParameterizedTest
    @CsvSource({
            "mycorp.com, email@mycorp.com, true",
            "mycorp.com, email@gmail.com, false"
    })
    public void differentiatesCorporateEmailFromNonCorporate(String domain, String email, boolean expectedResult) {
        Company sut = new Company(domain, 0);

        boolean isEmailCorporate = sut.isEmailCorporate(email);

        assertEquals(expectedResult, isEmailCorporate);
    }
}