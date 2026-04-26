package com.javaeasybank.account.exception;

import com.javaeasybank.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class TransferException extends BusinessException {
    private final String errorCode;

    public TransferException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
