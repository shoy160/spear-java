package cn.spear.core.lang;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author shay
 * @date 2020/9/8
 */
public abstract class TypeReference<T> implements Type {
    private final Type type;

    protected TypeReference() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        } else {
            this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        }
    }

    public Type getType() {
        return this.type;
    }
}