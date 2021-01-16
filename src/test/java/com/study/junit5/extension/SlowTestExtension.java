/*
 *  Life Cycle Callback 을 이용해서 실행하는데 오래걸리는 테스트를 찾아서 메시지를 출력해주는 extension
 */

package com.study.junit5.extension;

import com.study.junit5.annotation.SlowTest;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

public class SlowTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private final long THRESHOLD;

    public SlowTestExtension(long THRESHOLD) {
        this.THRESHOLD = THRESHOLD;
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = getStore(context);
        // 스토어에 시작 시간을 저장
        store.put("START_TIME", System.currentTimeMillis());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Method requiredTestMethod = context.getRequiredTestMethod();
        SlowTest annotation = requiredTestMethod.getAnnotation(SlowTest.class);
        String testMethodName = context.getRequiredTestMethod().getName();
        ExtensionContext.Store store = getStore(context);
        // 스토어에서 시작 시간을 가져오고 삭제
        long startTime = store.remove("START_TIME", long.class);
        long duration = System.currentTimeMillis() - startTime;
        if (duration > THRESHOLD && annotation == null) {
            System.out.printf("Please consider mark method [%s] with @LongTimeTest.\n", testMethodName);
        }
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        // 테스트 클래스 이름과 테스트 메서드 이름 가져오기
        String testClassName = context.getRequiredTestClass().getName();
        String testMethodName = context.getRequiredTestMethod().getName();
        // ExtensionContext 는 값들을 저장할 수 있는 store 를 제공한다
        // 테스트 클래스 이름과 테스트 메서드 이름의 조함으로 스토어를 생성
        return context.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));
    }
}
