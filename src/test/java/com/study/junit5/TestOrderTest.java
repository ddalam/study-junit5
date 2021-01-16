/*
 *	내부적으로 정해진 테스트 순서가 있다. 하지만 이 순서는 JUnit 내부 구조에 따라 언제든 바뀔 수 있기 때문에 이 순서에 의존하면 안된다
 * 	실행 순서를 드러내지 않은 의도는 각각의 테스트가 잘 작성된 단위 테스트라면 다른 단위 테스트와 독립적으로 실행이 가능해야 하기 때문
 *
 *  하지만, 경우에 따라 원하는 순서대로 테스트를 실행하고 싶은 경우에 @TestMethodOrder 애노테이션을 사용하면 된다
 *
 *  테스트 간 의존성이 있고, 상태 값을 서로 공유하도록 하고싶은 경우
 *  → @TestInstance 를 사용해 테스트 인스턴스를 하나만 만들어 공유하고, @TestMethodOrder 를 함께 사용하는 방법이 있다
 *
 *  @TestMethodOrder 에 MethodOrderer 의 구현체를 넣어준다
 *  MethodOrderer 의 기본 구현체 3개
 *  1. Alphanumeric
 *  2. OderAnnotation : JUnit 이 제공하는 @Order 애노테이션으로 순서를 지정, 낮은 값일수록 더 높은 우선순위
 *  3. Random
 */

package com.study.junit5.testInstance;

import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestOrderTest {

    @Order(2)
    @DisplayName("로그인")
    void login() {}

    @Order(1)
    @DisplayName("회원가입")
    void createNewUser() {}
}
