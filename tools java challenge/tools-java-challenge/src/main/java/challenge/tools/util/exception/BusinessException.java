package challenge.tools.util.exception;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class BusinessException extends RuntimeException {

    private String code;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public static Supplier<BusinessException> from(String messageCode, String message) {
        return () -> new BusinessException(messageCode, message);
    }

    public static Supplier<BusinessException> from(String messageCode, String message, Throwable cause) {
        return () -> new BusinessException(messageCode, message, cause);
    }

}