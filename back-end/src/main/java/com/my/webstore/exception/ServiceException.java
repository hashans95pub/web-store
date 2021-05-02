package com.my.webstore.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A custom exception which handles business layer exceptions extending existing exception details with an error type
 * and arguments for the validation error messages which may needed when get messages from the message source.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends Exception {
    public static final short PROCESSING_FAILED = 1;
    public static final short VALIDATION_FAILED = 2;

    private short code;
    private String[] messageArgs;

    public ServiceException(short code, String message, String... messageArgs) {
        super(message);
        this.code = code;
        this.messageArgs = messageArgs;
    }

    public ServiceException(short code, String message, Throwable cause, String... messageArgs) {
        super(message, cause);
        this.code = code;
        this.messageArgs = messageArgs;
    }

    public ServiceException(short code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
