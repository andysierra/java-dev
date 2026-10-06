package com.andressierra.model.exception;

import com.andressierra.model.messages.MessagesEnum;

public class OptimisticLockException extends CustomException {

    public OptimisticLockException() {
        super(
                MessagesEnum.CONCURRENT_MODIFICATION.getMessage(),
                MessagesEnum.CONCURRENT_MODIFICATION.getOperationCode(),
                MessagesEnum.CONCURRENT_MODIFICATION.getCode()
        );
    }
}
