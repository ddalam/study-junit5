/*
 * UserService 를 생성하려면 UserRepository 의 구현체가 필요한데 현재 UserRepository 인터페이스만 있고, 구현체가 만들어지지 않은 상태라면
 * 없는 것들을 mocking...
 *
 * Mock 객체를 생성하는 방법
 *  1. Mockito.mock() 메서드 사용
 *      UserRepository userRepository = mock(UserRepository.class);
 *  2. @Mock 애노테이션을 사용
 *      - @Mock 을 사용하려면 MockitoExtension 사용을 선언해야 한다
 *      - 클래스 필드 or 메서드 매개변수에 모두 사용할 수 있다
 *      - @Mock UserRepository userRepository
 *
 * Mock 객체 Stubbing ( Mock 객체의 행동을 조작 )
 *  1. Mock 객체의 기본 행동
 *      - 객체를 return 할 때는 null 을 리턴 ( Optional 타입은 Optional.empty 를 리턴 )
 *      - void 메서드는 어떤 일도 발생하지 않음
 *      - 콜렉션 타입은 비어있는 콜렉션
 *      - Primitive 타입은 기본값을 가짐
 * 2. Mock 객체가 기본 행동이 아닌 다른 행동을 하도록 할 때
 *      - 특정 매개변수를 받은 경우 특정한 값을 리턴하거나 예외를 던지도록
 *          when(userRepository.findById(1L)).thenReturn(Optional.of(user)); → 매개변수로 1L 을 받았을 때
 *          when(userRepository.findById(1L)).thenReturn(Optional.of(user)); → 매개변수로 아무 값이나 받았을 때
 */

package com.study.junit5.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
//        assertNotNull(userService);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");

        // Stubbing : when() 메서드의 매겨변수가 호출이 되면 → 그러면 user 를 리턴해라
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // findUser 메서드에서 사용하는 userRepository 는 mock 객체이고, findById 는 1L 로 호출이 되면 Optional 로 감싸진 user 를 리턴하도록 정해놓았기 때문에 ( stubbing )
        // findUser 메서드를 호출했을 때 userRepository.findById 가 리턴한 값은 user 가 된다
        User foundUser = userService.findUser(1L);

        assertEquals(user.getEmail(), foundUser.getEmail());
    }
}