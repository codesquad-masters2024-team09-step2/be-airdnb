package com.airdnb.global.exception;

public class ForbiddenException extends BizException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
