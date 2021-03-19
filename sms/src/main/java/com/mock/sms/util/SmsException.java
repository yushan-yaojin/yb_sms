package com.mock.sms.util;

/**
 * description: SmsException <br>
 * date: 2021/3/18 14:09 <br>
 * author: cn_yaojin <br>
 * version: 1.0 <br>
 */
public class SmsException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected Integer code = 500;

    public SmsException() {
        super();
    }

    public SmsException(String message) {
        super(message);
    }

    public SmsException(String message, Integer code) {
        super(message);
        this.code = code;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
