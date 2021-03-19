package com.mock.sms.vo;

import com.alipay.api.internal.mapping.ApiField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * description: CheckSmsRequest <br>
 * date: 2021/3/18 8:36 <br>
 * author: cn_yaojin <br>
 * version: 1.0 <br>
 */
@Setter
@Getter
@Accessors(chain = true)
public class CheckSmsRequest extends OrderSmsRequest {

    @ApiField(value = "检查项目名称")
    private String check_item;

    @ApiField(value = "检查时间")
    private String check_time;

    @ApiField(value = "检查室编号")
    private String check_num;

    @ApiField(value = "检查室位置")
    private String check_loc;

    @ApiField(value = "检查注意事项")
    private String check_precautions;

    @ApiField(value = "查看报告")
    private String report_url;


}
