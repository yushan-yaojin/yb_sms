package com.mock.sms.vo;

import com.alipay.api.internal.mapping.ApiField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * description: SmsBase <br>
 * date: 2021/3/17 16:59 <br>
 * author: cn_yaojin <br>
 * version: 1.0 <br>
 */
@Setter
@Getter
@Accessors(chain = true)
public class SmsBase {

    @ApiField(value = "该字段不用管，测试用的")
    private String access_token;

    @ApiField(value = "医院名称")
    private String hospital = "";

    @ApiField(value = "医院登记号")
    private String hospital_register_id = "";

    @ApiField(value = "医院预约单订单号")
    private String out_biz_no;

    @ApiField(value = "支付宝用户的唯一userId，已经在获取token 接口中返回")
    private String buyer_id;

    @ApiField(value = "订单创建时间")
    private String order_create_time;

    @ApiField(value = "订单修改时间")
    private String order_modified_time;

    @ApiField(value = "订单金额")
    private String amount;

    @ApiField(value = "支付金额")
    private String pay_amount;

    @ApiField(value = "支付宝交易号")
    private String trade_no;

    @ApiField(value = "商品数量")
    private String quantity = "1";

    @ApiField(value = "商品 skuId")
    private String sku_id = "";

    @ApiField(value = "商品单价")
    private String unit_price = "";

    @ApiField(value = "就诊科室")
    private String department = "";

    @ApiField(value = "就诊单id")
    private String medical_order_id = "";

    @ApiField(value = "就诊/检 查序号")
    private String medical_num = "";

    @ApiField(value = "订单链接")
    private String merchant_order_link_page = "";

    @ApiField(value = "就诊人 必须与 buy_id 对 应的姓名一致")
    private String patient = "";

}
