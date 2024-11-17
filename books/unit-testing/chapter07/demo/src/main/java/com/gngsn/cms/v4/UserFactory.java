package com.gngsn.cms.v4;

/**
 * 유틸리티 코드의 예
 * - 다소 복잡하지만 도메인 유의성이 없음
 * - 즉, 사용자 이메일을 변경 하려는 클라이언트의 목표와 직접적인 관련이 없음
 */
public class UserFactory {
    public static User Create(Object[] data) {
        assert data.length >= 3;

        int id = (int)data[0];
        String email = (String)data[1];
        UserType type = (UserType)data[2];

        return new User(id, email, type);
    }
}
