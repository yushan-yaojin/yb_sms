package com.mock.sms.vo;

import com.alipay.api.internal.mapping.ApiField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * description: 订单消息 <br>
 * date: 2021/3/17 16:57 <br>
 * author: cn_yaojin <br>
 * version: 1.0 <br>
 */
@Setter
@Getter
@Accessors(chain = true)
public class OrderSmsRequest extends SmsBase {


    @ApiField(value = "诊室编号")
    private String dept_num = "";

    @ApiField(value = "科室位置")
    private String dept_loc = "";

    @ApiField(value = "导航地址")
    private String navigation = "";

    @ApiField(value = "医生名称")
    private String doctor = "";

    @ApiField(value = "医生职级")
    private String doctor_rank = "";

    @ApiField(value = "医生 id")
    private String doctor_id = "";

    @ApiField(value = "医生头像 url")
    private String doctor_avatar = "";


    @ApiField(value = "预约时间")
    private String scheduled_time = "";

    @ApiField(value = "取号入口URL")
    private String take_num_url = "";

    @ApiField(value = "取号密码")
    private String take_num_password = "";

    @ApiField(value = "叫号进度入口")
    private String call_num_url = "";


}
