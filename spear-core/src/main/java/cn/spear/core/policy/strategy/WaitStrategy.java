package cn.spear.core.policy.strategy;

import cn.spear.core.policy.attempt.Attempt;

/**
 * @author luoyong
 * @date 2023/2/8
 */
public interface WaitStrategy {
    long computeSleepTime(Attempt<?> var1);
}
