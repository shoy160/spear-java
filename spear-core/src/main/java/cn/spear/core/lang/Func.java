package cn.spear.core.lang;

/**
 * todo
 *
 * @author shay
 * @date 2020/8/15
 */
public interface Func<R, T> {
    /**
     * 执行方法
     *
     * @param source 参数
     * @return R
     */
    R invoke(T source);
}
