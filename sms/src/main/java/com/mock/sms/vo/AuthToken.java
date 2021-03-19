package com.mock.sms.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * description: AuthToken <br>
 * date: 2021/3/18 11:55 <br>
 * author: cn_yaojin <br>
 * version: 1.0 <br>
 */
@Setter
@Getter
@Accessors(chain = true)
public class AuthToken {

    private String appId;
    private String ticket;
    private String alipayUid;
    private String accessToken;
    private String cardNum;
    private String phone;


}
