/*
 * 테스트 메서드 마다 테스트 클래스의 인스턴스가 생성된다
 * 이유 ? 테스트 간 의존성을 없애기 위해. 서로 의존하는 값들이 바뀌면 테스트가 실행되는 순서에 따라 테스트가 불안정하게 됨
 * 테스트 순서 != 메서드 선언 순서 가 아니다
 * JUnit5 부터는 기본적으로 선언 순서대로 실행되지만, 절대 그렇지는 않다고 생각하는게 좋다
 * 따라서, 테스트 클래스 전역에 선언된 변수를 각 테스트 메서드에서 아무리 변경을 한다해도 다른 메서드에 영향을 주지 않는다
 *
 * JUnit5 에서는 테스트 클래스 인스턴스를 클래스 당 1개만 만들어서 공유하는 방법이 추가되었다
 * @TestInstance(TestInstance.Lifecycle.PER_CLASS)
 */

package com.study.junit5;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS)
public class TestInstanceTest {

	// @BeforeAll, @AfterAll 는 반드시 static 클래스여야 한다
	// 테스트 메서드 마다 인스턴스를 만들게 되면, 여러 인스턴스에서 메서드를 사용할 수 있게 static 이었어야 했다
	// 하지만, 테스트 인스턴스를 클래스 마다 만들게 되면 @BeforeAll, @AfterAll 메서드들이 static 일 필요가 없다
	@BeforeAll
	void beforeAll() {}

	@AfterAll
	void afterAll() {}

}
