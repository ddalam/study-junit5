/*
 *  Extension 을 사용하는 방법
 *  1. 테스트 클래스에 @ExtendWith() 애노테이션을 사용해서 어떤 extension 을 사용할 것인지 선언
 *      여러개를 쓰는 것도 OK
 *  2. 필드에 extension 을 정의하는 방법 → @RegisterExtension
 *      1번 방법을 사용하면 extension 인스턴스를 기본 생성자로 만들기 때문에 커스터마이징 할 수 없다
 *      @RegisterExtension 을 사용할 때는 static 으로 정의해야 된다
 *  3. 자동으로 등록하는 방법 → dependency 를 등록 후 ServiceLoader 를 사용
 *      기본적으로 이 설정은 꺼져있고, 설정을 원할 때는 junit-platform.properties 에
 *      junit.jupiter.extensions.autodetection.enabled = true
 *      위 라인을 추가하면 된다
 */

package com.study.junit5.extension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

//@ExtendWith(SlowTestExtension.class)
class SlowTestExtensionTest {

    @RegisterExtension
    static SlowTestExtension slowTestExtension = new SlowTestExtension(1000L);

    @Test
    void longTimeTest() throws InterruptedException {
        Thread.sleep(1010L);
    }
}