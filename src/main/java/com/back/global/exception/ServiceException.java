package com.back.global.exception;

import com.back.global.rsData.RsData;

/**
 * 비즈니스 로직에서 발생하는 예외를 표현하는 커스텀 예외 클래스.
 * 에러 코드와 메시지를 포함하며, 실제 예외 내용은 throw 시점에 결정된다.
 */
public class ServiceException extends RuntimeException {

    private String msg;
    private String resultCode;

    public ServiceException(String resultCode, String msg) {
        super(msg);
        this.msg = msg;
        this.resultCode = resultCode;
    }

    public RsData<Void> getRsData() {
        return new RsData<>(
                msg,
                resultCode
        );
    }
}
