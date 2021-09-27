package cn.spear.core.message;

import cn.spear.core.SpearCode;
import cn.spear.core.exception.SpearException;
import cn.spear.core.message.model.impl.BaseMessage;
import lombok.NonNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author shay
 * @date 2020/9/18
 */
public class MessageFutureTask<T extends BaseMessage> implements Future<T> {
    private final Object messageLock;
    private final BaseMessage request;
    private boolean done;
    private T result;
    private Throwable throwable;

    public MessageFutureTask(BaseMessage request) {
        this.request = request;
        messageLock = new Object();
    }

    public void setResult(T result) {
        this.result = result;
        synchronized (messageLock) {
            this.done = true;
            messageLock.notifyAll();
        }
    }

    public void setException(Throwable throwable) {
        this.throwable = throwable;
        synchronized (messageLock) {
            this.done = true;
            messageLock.notifyAll();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return this.done;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        try {
            return get(-1, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T get(long timeout, @NonNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!this.done) {
            synchronized (messageLock) {
                if (timeout < 0) {
                    messageLock.wait();
                } else {
                    long timeoutMillis = (TimeUnit.MILLISECONDS == unit) ? timeout : TimeUnit.MILLISECONDS.convert(timeout, unit);
                    messageLock.wait(timeoutMillis);
                }
            }
        }
        if (!this.done) {
            throw new SpearException(SpearCode.TIME_OUT, String.format("RPC调用超时,requestId:%s", request.getId()));
        }
        if (null != this.throwable) {
            this.throwable.printStackTrace();
            throw new SpearException(SpearCode.INTERNAL_SERVER_ERROR, "RPC调用异常：" + this.throwable.getMessage());
        }
        return result;
    }
}
