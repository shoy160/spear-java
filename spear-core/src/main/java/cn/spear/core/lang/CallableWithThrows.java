package cn.spear.core.lang;

/**
 * @author luoyong
 * @date 2023/2/8
 */
@FunctionalInterface
public interface CallableWithThrows<T> {
    /**
     * 执行
     *
     * @return T
     * @throws Throwable
     */
    T run() throws Throwable;
}
