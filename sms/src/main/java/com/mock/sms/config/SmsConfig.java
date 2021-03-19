package com.mock.sms.config;

import com.alipay.api.internal.mapping.ApiField;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * https://opendocs.alipay.com/pre-open/01odaz
 * description: SmsConfig <br>
 * date: 2021/3/17 16:26 <br>
 * author: cn_yaojin <br>
 * version: 1.0 <br>
 */
@Configuration
@ConfigurationProperties(prefix = "sms", ignoreUnknownFields = true)
@Setter
@Getter
public class SmsConfig {

    /**
     * 获取accessToken的参数开始
     **/
    @ApiField(value = "app_id")
    private String app_id;

    @ApiField(value = "private_key")
    private String private_key;

    @ApiField(value = "alipay_public_key")
    private String alipay_public_key;
    /**
     * 获取accessToken的参数结束
     **/

    @ApiField(value = "目标用户 String(64) 支付宝用户 PID 不可空")
    private String target_id;

    @ApiField(value = "租户应 用ID String(64) 支付宝分配 不可空")
    private String tenant_app_id;

    @ApiField(value = "医院在支付宝的小 程序id")
    private String tiny_app_id;

    @ApiField(value = "医院在支付宝平台 id(小程序 appId 对 应的 pid)")
    private String partner_id;


    @ApiField(value = "是否启用消息发送：是-true")
    private boolean smsSend = false;

}
