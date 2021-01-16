/*
 * UserService 를 생성하려면 UserRepository 의 구현체가 필요한데 현재 UserRepository 인터페이스만 있고, 구현체가 만들어지지 않은 상태라면
 * 없는 것들을 mocking...
 *
 * Mock 객체를 생성하는 방법
 * 1. Mockito.mock() 메서드 사용
 *      UserRepository userRepository = mock(UserRepository.class);
 * 2. @Mock 애노테이션을 사용
 *      - @Mock 을 사용하려면 MockitoExtension 사용을 선언해야 한다
 *      - 클래스 필드 or 메서드 매개변수에 모두 사용할 수 있다
 *      - @Mock UserRepository userRepository
 */

package com.study.junit5.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

// 필드에 @Mock 애노테이션을 사용할 수 있고
//    @Mock
//    UserRepository userRepository;

    @Test
    void createUserService(@Mock UserRepository userRepository) {

        // mock 메서드에 원하는 클래스를 넣어 가짜 객체를 생성할 수 있다
//        UserRepository userRepository = mock(UserRepository.class);

        UserService userService = new UserService(userRepository);

        assertNotNull(userService);
    }
}