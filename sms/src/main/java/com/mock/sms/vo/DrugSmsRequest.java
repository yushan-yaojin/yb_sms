package com.mock.sms.vo;

import com.alipay.api.internal.mapping.ApiField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * description: DrugSmsRequest <br>
 * date: 2021/3/18 9:05 <br>
 * author: cn_yaojin <br>
 * version: 1.0 <br>
 */
@Setter
@Getter
@Accessors(chain = true)
public class DrugSmsRequest extends SmsBase {

    @ApiField(value = "取药号码")
    private String take_medicine_num;

    @ApiField(value = "取药窗口")
    private String take_medicine_window;

    @ApiField(value = "取药地址")
    private String take_medicine_loc;

    @ApiField(value = "取药导航链接")
    private String take_medicine_url;

}
