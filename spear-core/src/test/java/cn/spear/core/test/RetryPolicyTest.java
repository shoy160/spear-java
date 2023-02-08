package cn.spear.core.test;

import cn.spear.core.policy.attempt.Attempt;
import cn.spear.core.policy.RetryListener;
import cn.spear.core.policy.RetryPolicy;
import cn.spear.core.policy.RetryPolicyBuilder;
import cn.spear.core.policy.strategy.AttemptTimeLimiters;
import cn.spear.core.policy.strategy.StopStrategies;
import cn.spear.core.policy.strategy.WaitStrategies;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author luoyong
 * @date 2023/2/8
 */
@Slf4j
public class RetryPolicyTest {
    @Test
    public void retryTest() {
        RetryPolicy<Boolean> retryPolicy = RetryPolicyBuilder.<Boolean>newBuilder()
//                .retryIfResult(t -> Objects.equals(false, t))
                .retryIfException()
//                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                .withWaitStrategy(WaitStrategies.randomWait(10, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(5))
                .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(10, TimeUnit.SECONDS))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        long attemptNumber = attempt.getAttemptNumber();
                        log.info("第 {} 次重试执行", attemptNumber);
                        if (attempt.hasException()) {
                            attempt.getExceptionCause().printStackTrace();
                        }
                    }
                })
                .build();
        try {
            retryPolicy.call(() -> {
                log.info("执行操作");
                Thread.sleep(5000L);
                throw new Exception("cclss");
//                return false;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
