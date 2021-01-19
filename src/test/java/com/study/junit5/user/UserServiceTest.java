/*
 * UserService 를 생성하려면 UserRepository 의 구현체가 필요한데 현재 UserRepository 인터페이스만 있고, 구현체가 만들어지지 않은 상태라면
 * 없는 것들을 mocking...
 *
 * Mock 객체를 생성하는 방법 → testCreateMock() 참고
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
 *          - 메서드가 리턴하는게 있을 때 → testStubbingCase1() 참고
 *          - void 메서드의 경우 → testStubbingCase2() 참고
 *          - 메서드가 동일한 매개변수로 호출되더라도 호출되는 순서에 따라 다르게 행동하도록 → testStubbingCase3() 참고
 *
 * Mock 객체가 어떻게 사용이 됐는지 확인
 *  1. 특정 메서드가 특정 파라미터로 호출되었는지 확인할 때 - testVerifyMockCase1() 참고
 *  2. 어떤 순서로 메서드가 호출되었는지 확인할 때 - testVerifyMockCase2() 참고
 *  3. Mock 객체가 사용되지 않았는지 확인 - testVerifyMockCase3() 참고
 *
 * Mockito BDD
 * - BDD : Behavior-driven Development
 * - BDD 에 따라 테스트 행동을 정의할 때 행동에 대한 스팩
 *      1. Title : 행동의 이름
 *      2. Narrative : 행동에 대한 설명 구성 방법
 *          - As s : 역할로
 *          - I want : ~ 하고싶다면
 *          - so that : ~ 할 수 있다
 *      3. Acceptance criteria : 인수테스트 내용을 작성할 때
 *          - Given : 어떤 상황이 주어졌을 때
 *          - When : 어떤 행동을 하면
 *          - then : 어떻게 될것이다
 */

package com.study.junit5.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

// 필드에 @Mock 애노테이션을 사용할 수 있고
//    @Mock
//    UserRepository userRepository;

    @Test
    void testCreateMock(@Mock UserRepository userRepository) {

        // mock 메서드에 원하는 클래스를 넣어 가짜 객체를 생성할 수 있다
//        UserRepository userRepository = mock(UserRepository.class);

        UserService userService = new UserService(userRepository);
        assertNotNull(userService);
    }

    @Test
    void testStubbingCase1() {
        UserService userService = mock(UserService.class);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");

        // 매개변수로 1L 을 받았을 때 user 를 리턴
        when(userService.findUser(1L)).thenReturn(user);
        User foundUser = userService.findUser(1L);
        assertEquals(user.getEmail(), foundUser.getEmail());

        // 매개변수로 아무 값이나 받았을 때 user 를 리턴
        when(userService.findUser(any())).thenReturn(user);
        User foundUser2 = userService.findUser(2L);
        assertEquals(user.getEmail(), foundUser2.getEmail());

        // 매개변수로 1L 을 받았을 때 예외를 리턴
        when(userService.findUser(1L)).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> {
            userService.findUser(1L);
        });
    }

    @Test
    void testStubbingCase2() {
        UserService userService = mock(UserService.class);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");

        // 1L 일 때는 예외를 던지지만 2L 로 일 때는 예외가 발생하지 않음
        doThrow(new IllegalArgumentException()).when(userService).checkUser(1L);
        assertThrows(IllegalArgumentException.class, () -> {
            userService.checkUser(1L);
        });

        userService.checkUser(2L);
    }

    @Test
    void testStubbingCase3() {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");

        when(userRepository.findById(any()))    // 어떤 파라미터로 호출이 되던지
                .thenReturn(Optional.of(user))  // 처음 호출할 때는 user 를 리턴하고
                .thenThrow(new RuntimeException()) // 두번째 호출할 때는 예외를 리턴
                .thenReturn(Optional.empty());   // 세번째 호출할 때는 비어있는 값을 리턴

        // 처음 호출할 때
        Optional<User> foundUser = userRepository.findById(1L);
        assertEquals(user.getEmail(), foundUser.get().getEmail());

        // 두번째 호출할 때
        assertThrows(RuntimeException.class, () -> {
            userRepository.findById(2L);
        });

        // 세번째 호출할 때
        assertEquals(Optional.empty(), userRepository.findById(3L));
    }

    @Test
    void testVerifyMockCase1() {
        UserService userService = mock(UserService.class);

        userService.checkUser(1L);

        // checkUser 메서드가 1L 을 매개변수로 1번 호출이 되었어야 한다
        verify(userService, times(1)).checkUser(1L);
        // checkUser 메서드가 1L 을 매개변수로 1번 호출되어야 하는데, 2번 호출되었는지 확인해서 테스트 실패
//        verify(userService, times(2)).checkUser(1L);
        // findUser 메서드가 어떤 값을 매개변수로도 한번도 호출되지 않았어야 한다
        verify(userService, never()).findUser(any());
    }

    @Test
    void testVerifyMockCase2() {
        UserService userService = mock(UserService.class);

        userService.checkUser(1L);
        userService.findUser(1L);

        InOrder inOrder = inOrder(userService);
        // 아래 두 라인의 순서를 바꾸면 테스트 실패
        inOrder.verify(userService).checkUser(1L);
        inOrder.verify(userService).findUser(1L);
    }

    @Test
    void testVerifyMockCase3() {
        UserService userService = mock(UserService.class);
        UserRepository userRepository = mock(UserRepository.class);

        userRepository.findById(1L);

        verifyNoMoreInteractions(userService);
    }

    @Test
    void testMockitoBDD() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = mock(UserService.class);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");

        // given ( BDDMockito library )
        given(userService.findUser(1L)).willReturn(user);

        // when
        userService.checkUser(1L);

        // then ( BDDMockito library )
        then(userService).should(times(1)).checkUser(1L);
    }
}