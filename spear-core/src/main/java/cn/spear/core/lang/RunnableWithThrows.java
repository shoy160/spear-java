package cn.spear.core.lang;

/**
 * @author luoyong
 * @date 2023/2/8
 */
@FunctionalInterface
public interface RunnableWithThrows {
    /**
     * 执行
     *
     * @throws Throwable
     */
    void run() throws Throwable;
}
