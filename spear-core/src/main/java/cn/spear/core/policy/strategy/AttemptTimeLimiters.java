package cn.spear.core.policy.strategy;

import java.util.concurrent.*;

/**
 * @author luoyong
 * @date 2023/2/8
 */
public final class AttemptTimeLimiters {
    private AttemptTimeLimiters() {
    }

    public static <V> AttemptTimeLimiter<V> noTimeLimit() {
        return new AttemptTimeLimiters.NoAttemptTimeLimit<>();
    }

    public static <V> AttemptTimeLimiter<V> fixedTimeLimit(long duration, TimeUnit timeUnit) {
        return new AttemptTimeLimiters.FixedAttemptTimeLimit<>(duration, timeUnit);
    }

    public static <V> AttemptTimeLimiter<V> fixedTimeLimit(long duration, TimeUnit timeUnit, ExecutorService executorService) {
        return new AttemptTimeLimiters.FixedAttemptTimeLimit<>(executorService, duration, timeUnit);
    }

    private static final class FixedAttemptTimeLimit<V> implements AttemptTimeLimiter<V> {
        private final ExecutorService executorService;
        private final long duration;
        private final TimeUnit timeUnit;

        public FixedAttemptTimeLimit(long duration, TimeUnit timeUnit) {
            this(Executors.newFixedThreadPool(1), duration, timeUnit);
        }

        private FixedAttemptTimeLimit(ExecutorService executorService, long duration, TimeUnit timeUnit) {
            this.executorService = executorService;
            this.duration = duration;
            this.timeUnit = timeUnit;
        }

        @Override
        public V call(Callable<V> callable) throws Exception {
            Future<V> future = executorService.submit(callable);
            return future.get(this.duration, this.timeUnit);
        }
    }

    private static final class NoAttemptTimeLimit<V> implements AttemptTimeLimiter<V> {
        private NoAttemptTimeLimit() {
        }

        @Override
        public V call(Callable<V> callable) throws Exception {
            return callable.call();
        }
    }
}
