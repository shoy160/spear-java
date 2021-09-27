package cn.spear.core.exception;

import cn.spear.core.SpearCode;
import lombok.Getter;

/**
 * @author shay
 * @date 2020/9/15
 */
@Getter
public class SpearException extends RuntimeException {
    private final Integer code;
    private final String message;

    public SpearException(SpearCode code) {
        this(code.getCode(), code.getMessage());
    }

    public SpearException(SpearCode code, String message) {
        this(code.getCode(), message);
    }

    public SpearException(String message) {
        this(SpearCode.FAILURE, message);
    }

    public SpearException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
