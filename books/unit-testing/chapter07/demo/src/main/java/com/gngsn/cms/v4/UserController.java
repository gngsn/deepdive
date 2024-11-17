package com.gngsn.cms.v4;

/**
 * Version 3. UserFactory 추가
 * - User 객체를 직접 인스턴스화 했던 의존성 완화
 */
public class UserController {
    private final Database database = new Database();       // ① 프로세스 외부 의존성(Database와 MessageBus)이 직접 인스턴스화
    private final MessageBus messageBus = new MessageBus();

    public void changeEmail(int userId, String newEmail) throws Exception {
        User user = UserFactory.Create(database.getUserById(userId));

        Object[] companyData = database.getCompany();
        Company company = CompanyFactory.create(companyData);

        user.changeEmail(newEmail, company);

        // ④ 새로운 이메일이 전과 다른지 여부와 관계없이, 무조건 데이터를 수정해서 저장하고 메시지 버스에 알림을 보냄
        database.saveCompany(company);
        database.saveUser(user);

        messageBus.sendEmailChangedMessage(userId, newEmail);
    }
}
