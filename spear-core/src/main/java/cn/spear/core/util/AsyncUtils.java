package cn.spear.core.util;

import cn.spear.core.lang.CallableWithThrows;
import cn.spear.core.lang.RunnableWithThrows;

import java.util.concurrent.*;

/**
 * @author luoyong
 * @date 2023/2/8
 */
public final class AsyncUtils {

    public static void executeWithTimeout(
            RunnableWithThrows runnable, long timeout, TimeUnit timeUnit
    ) throws Throwable {
        executeWithTimeout(() -> {
            runnable.run();
            return true;
        }, timeout, timeUnit);
    }

    public static <T> T executeWithTimeout(
            CallableWithThrows<T> callable, long timeout, TimeUnit timeUnit
    ) throws Throwable {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            Future<T> future = executorService.submit(() -> {
                try {
                    return callable.run();
                } catch (Throwable e) {
                    throw new ExecutionException(e);
                }
            });
            return future.get(timeout, timeUnit);
        } catch (Throwable ex) {
            Throwable throwable = ex;
            if (throwable instanceof InterruptedException || throwable instanceof ExecutionException) {
                throwable = throwable.getCause();
            }
            throw throwable;
        }
    }
}
