package cn.spear.core.exception;

/**
 * @author shay
 * @date 2020/9/15
 */
public class BusiException extends RuntimeException {
    private Integer code;

    public BusiException() {
        super();
    }

    public BusiException(String message, int code) {
        super((message));
        this.code = code;
    }
}
