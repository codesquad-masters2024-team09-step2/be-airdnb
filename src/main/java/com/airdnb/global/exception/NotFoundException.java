package com.airdnb.global.exception;

public class NotFoundException extends BizException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
