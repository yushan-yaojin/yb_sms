package com.mock.sms.util;

import com.mock.sms.vo.AuthToken;

import java.util.HashMap;
import java.util.Map;

/**
 * description: SmsUserSessiontUtil <br>
 * date: 2021/3/18 11:54 <br>
 * author: cn_yaojin <br>
 * version: 1.0 <br>
 */
public class SmsUserSessiontUtil {

    private static Map<String /*buserId*/, AuthToken> MAP = new HashMap<>();


    public static void add(AuthToken authToken) {
        MAP.put(authToken.getAlipayUid(), authToken);
    }

    public static AuthToken get(String buserId) {
        return null;
    }

}
