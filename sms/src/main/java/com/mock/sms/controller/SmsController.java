package com.mock.sms.controller;

import com.alibaba.fastjson.JSON;
import com.mock.sms.service.SmsService;
import com.mock.sms.vo.AuthToken;
import com.mock.sms.vo.OrderSmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * description: SmsController <br>
 * date: 2021/3/18 9:18 <br>
 * author: cn_yaojin <br>
 * version: 1.0 <br>
 */
@RestController
@RequestMapping(value = "/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Resource
    private HttpServletRequest request;

    @RequestMapping(value = "/loginNotify", method = RequestMethod.GET)
    @ResponseBody
    protected String loginNotify(String appId, String ticket, String alipayUid, String accessToken) {
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(accessToken).setTicket(ticket).setAppId(appId).setAlipayUid(alipayUid);
        System.out.println(JSON.toJSONString(authToken));
        this.smsService.saveToken(authToken);
        System.out.println("消息授权身份信息保存成功");
        return "true";
    }

    @PostMapping(value = "sendSms")
    public Object sendSms(
            @RequestBody OrderSmsRequest smsRequest
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            this.smsService.sendSmsOrder0(smsRequest, smsRequest.getBuyer_id());
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

    @GetMapping(value = "findNum")
    public Object findNum() {
        return 10;
    }

    @GetMapping(value = "demo")
    public Object demo() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        this.smsService.demo();
        return result;
    }


}
