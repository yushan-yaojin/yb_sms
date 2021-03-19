package com.mock.sms.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.CommerceAppUploadRequestContent;
import com.alipay.api.request.AlipayCommerceAppAuthUploadRequest;
import com.alipay.api.response.AlipayCommerceAppAuthUploadResponse;
import com.mock.sms.config.SmsConfig;
import com.mock.sms.mapper.ISmsTokenMapper;
import com.mock.sms.util.SmsException;
import com.mock.sms.vo.AuthToken;
import com.mock.sms.vo.CheckSmsRequest;
import com.mock.sms.vo.DrugSmsRequest;
import com.mock.sms.vo.OrderSmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * description: SmsService <br>
 * date: 2021/3/17 16:46 <br>
 * author: cn_yaojin <br>
 * version: 1.0 <br>
 */
@Service
public class SmsService {

    @Autowired
    private ISmsTokenMapper smsTokenMapper;

    @Autowired
    private SmsConfig smsConfig;

    private AlipayClient alipayClient;

    @PostConstruct
    private void start() {
        alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", smsConfig.getApp_id(), smsConfig.getPrivate_key(), "json", "GBK", smsConfig.getAlipay_public_key(), "RSA2");
    }

    public void demo() {
        List<AuthToken> list = this.smsTokenMapper.list("1", "1");
        this.smsTokenMapper.add("3", "3", "3", "3");
        System.out.println(JSON.toJSONString(list));
    }

    public void saveToken(AuthToken authToken) {
        List<AuthToken> list = this.smsTokenMapper.listByUserId(authToken.getAlipayUid());
        if (list == null || list.size() == 0) {
            //新增身份token
            this.smsTokenMapper.add(authToken.getAlipayUid(), authToken.getAppId(), authToken.getTicket(), authToken.getAccessToken());
        } else {
            //更新身份token
            this.smsTokenMapper.update(authToken.getAlipayUid(), authToken.getTicket(), authToken.getAccessToken());
        }
    }

    /**
     * 绑定身份证号
     */
    public void bindCardNum(AuthToken authToken) {
        if (authToken != null) {
            this.smsTokenMapper.updateCard(authToken.getCardNum(), authToken.getPhone(), authToken.getAlipayUid());
        }
    }

    public AuthToken findAccessToken(String buserId) {
        List<AuthToken> list = this.smsTokenMapper.listByUserId(buserId);
        if (list == null || list.size() == 0) {
            throw new SmsException("消息推送身份信息不存在");
        }
        return list.get(0);
    }

    public AuthToken findAccessTokenByCardNum(String cardNum) {
        List<AuthToken> list = this.smsTokenMapper.listByCardNum(cardNum);
        if (list == null || list.size() == 0) {
            throw new SmsException("消息推送身份信息不存在");
        }
        return list.get(0);
    }

    public AuthToken findAccessTokenByPhone(String phone) {
        List<AuthToken> list = this.smsTokenMapper.listByPhone(phone);
        if (list == null || list.size() == 0) {
            throw new SmsException("消息推送身份信息不存在");
        }
        return list.get(0);
    }


    private AlipayCommerceAppAuthUploadRequest getSmsRequst() {
        AlipayCommerceAppAuthUploadRequest request = new AlipayCommerceAppAuthUploadRequest();
        request.setServiceName("alipay.commerce.app.data");//应用服务名称 固定值 String(256) 不可空
        request.setTargetId(smsConfig.getTarget_id()); //目标用户 String(64) 支付宝用户 PID 不可空
        return request;
    }

    /**
     * 发送挂号单消息
     *
     * @param orderSmsRequest
     * @param buserId
     */
    public void sendSmsOrder0(OrderSmsRequest orderSmsRequest, String buserId) {
        AuthToken authToken = findAccessToken(buserId);
        sendSmsOrder(orderSmsRequest, authToken.getAccessToken());
    }

    /**
     * 发送检查单消息
     *
     * @param checkSmsRequest
     * @param buserId
     */
    public void sendSmsCheck0(CheckSmsRequest checkSmsRequest, String buserId) {
        AuthToken authToken = findAccessToken(buserId);
        hospitalCheck(authToken.getAccessToken(), checkSmsRequest);
    }

    /**
     * 发送医药消息
     *
     * @param drugSmsRequest
     * @param buserId
     */
    public void sendSmsDrug0(DrugSmsRequest drugSmsRequest, String buserId) {
        AuthToken authToken = findAccessToken(buserId);
        hospitalDrug(authToken.getAccessToken(), drugSmsRequest);
    }

    /**
     * 发送挂号单 消息
     *
     * @param orderSmsRequest
     * @param access_token
     */
    private void sendSmsOrder(OrderSmsRequest orderSmsRequest, String access_token) {
        AlipayCommerceAppAuthUploadRequest request = getSmsRequst();

        CommerceAppUploadRequestContent content = new CommerceAppUploadRequestContent(); //服务数据参数
        content.setTenantAppId(smsConfig.getTenant_app_id()); //租户应 用ID String(64) 支付宝分配 不可空
        content.setActivityId("upload_hospital_order"); //业务流程ID String(64) 不可空 此处固定为“upload_hospital_order”

        JSONObject json_body = new JSONObject(); //业务流程参数 String(6000) 业务流程请求参数说明
        json_body.put("out_biz_no", orderSmsRequest.getOut_biz_no());//医院预约单订单号 唯一不重复(同一 家 ISV 接入的所有 医院的挂号单、检 查号、医药单都不 可重复) String(128)
        json_body.put("partner_id", ""); //医院在支付宝平台 id(小程序 appId 对 应的 pid)，不传。 根据 out_biz_id 和 partner_id 做幂等 String(128)      06：可空
        json_body.put("buyer_id", orderSmsRequest.getBuyer_id());//就诊人 id，授权 人id 就诊人在支付宝平 台的 2088 开头 16 位id
        json_body.put("tiny_app_id", smsConfig.getTiny_app_id());//医院在支付宝的小 程序id
        json_body.put("order_create_time", orderSmsRequest.getOrder_create_time());//订单创建时间
        json_body.put("order_modified_time", orderSmsRequest.getOrder_modified_time());//订单修改时间
        json_body.put("amount", orderSmsRequest.getAmount());//订单金额
        json_body.put("pay_amount", orderSmsRequest.getPay_amount());//支付金额
        json_body.put("trade_no", orderSmsRequest.getTrade_no());//支付宝交易号
        json_body.put("order_type", ""); //订单类型 String(128) 可空 固定为 ZJA06B07C36D57
        json_body.put("out_biz_type", ""); //外部订单类型 String(64) 服务商、平台商 在支付宝侧的商 户id  可空
        json_body.put("merchant_order_status", ""); //状态 String(64) 服务商、平台商 在支付宝侧的商 户id  可空

        JSONArray item_order_list = new JSONArray();
        JSONObject item_order = new JSONObject();
        item_order.put("item_name", "挂号单");//商品名称
        item_order.put("quantity", "1");//商品数量
        item_order.put("sku_id", "1"); //商品 skuId
        item_order.put("unit_price", orderSmsRequest.getAmount());//商品单价
        item_order_list.add(item_order);
        json_body.put("item_order_list", item_order_list);


        JSONObject ext_info = new JSONObject();
        ext_info.put("hospital", orderSmsRequest.getHospital());//医院名称
        ext_info.put("hospital_register_id", orderSmsRequest.getHospital_register_id());//医院登记号
        ext_info.put("department", orderSmsRequest.getDepartment());//就诊科室
        ext_info.put("dept_num", orderSmsRequest.getDept_num());//诊室编号
        ext_info.put("dept_loc", orderSmsRequest.getDept_loc());//科室位置
        ext_info.put("navigation", orderSmsRequest.getNavigation());//导航地址
        ext_info.put("doctor", orderSmsRequest.getDoctor());//医生名称
        ext_info.put("doctor_rank", orderSmsRequest.getDoctor_rank());//医生职级
        ext_info.put("doctor_id", orderSmsRequest.getDoctor_id());//医生 id
        ext_info.put("doctor_avatar", orderSmsRequest.getDoctor_avatar());//医生头像 url
        ext_info.put("patient", orderSmsRequest.getPatient());//就诊人  必须与 buy_id 对 应的姓名一致
        ext_info.put("scheduled_time", orderSmsRequest.getScheduled_time());//预约时间
        ext_info.put("take_num_url", orderSmsRequest.getTake_num_url());//取号入口
        ext_info.put("take_num_password", orderSmsRequest.getTake_num_password());//取号密码
        ext_info.put("call_num_url", orderSmsRequest.getCall_num_url());//叫号进度入口
        ext_info.put("medical_order_id", orderSmsRequest.getMedical_order_id());//就诊单id
        ext_info.put("medical_num", orderSmsRequest.getMedical_num());//就诊/检 查序号
        ext_info.put("merchant_order_link_page", orderSmsRequest.getMerchant_order_link_page());//订单链接

        json_body.put("ext_info", ext_info);

        content.setBody(JSONObject.toJSONString(json_body));
        request.setContent(content);

        System.out.println("入参 == " + JSONObject.toJSONString(request));

        AlipayCommerceAppAuthUploadResponse response = new AlipayCommerceAppAuthUploadResponse();
        try {
            response = alipayClient.execute(request, access_token);
            System.out.println("返回：" + response.getBody());
            if (!response.isSuccess()) {
                System.out.println("错误返回：" + response.getSubMsg());
                throw new SmsException("挂单号消息发送失败");
            }
            System.out.println("挂单消息发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查单
     *
     * @param accessToken
     */
    private void hospitalCheck(String accessToken, CheckSmsRequest checkSmsRequest) {
        AlipayCommerceAppAuthUploadRequest request = getSmsRequst();

        CommerceAppUploadRequestContent content = new CommerceAppUploadRequestContent(); //服务数据参数
        content.setTenantAppId(smsConfig.getTenant_app_id()); //租户应 用ID String(64) 支付宝分配 不可空
        content.setActivityId("upload_hospital_order"); //业务流程ID String(64) 不可空 此处固定为“upload_hospital_order”

        JSONObject json_body = new JSONObject(); //业务流程参数 String(6000) 业务流程请求参数说明
        json_body.put("out_biz_no", checkSmsRequest.getOut_biz_no());//医院预约单订单号 唯一不重复(同一 家 ISV 接入的所有 医院的挂号单、检 查号、医药单都不 可重复) String(128)
        json_body.put("partner_id", ""); //医院在支付宝平台 id(小程序 appId 对 应的 pid)，不传。 根据 out_biz_id 和 partner_id 做幂等 String(128)      06：可空
        json_body.put("buyer_id", checkSmsRequest.getBuyer_id());//就诊人 id，授权 人id 就诊人在支付宝平 台的 2088 开头 16 位id
        json_body.put("tiny_app_id", smsConfig.getTiny_app_id());//医院在支付宝的小 程序id
        json_body.put("order_create_time", checkSmsRequest.getOrder_create_time());//订单创建时间
        json_body.put("order_modified_time", checkSmsRequest.getOrder_modified_time());//订单修改时间
        json_body.put("amount", checkSmsRequest.getAmount());//订单金额
        json_body.put("pay_amount", checkSmsRequest.getPay_amount());//支付金额
        json_body.put("trade_no", checkSmsRequest.getTrade_no());//支付宝交易号
        json_body.put("order_type", ""); //订单类型 String(128) 可空 固定为 ZJA06B07C36D57
        json_body.put("out_biz_type", ""); //外部订单类型 String(64) 服务商、平台商 在支付宝侧的商 户id  可空
        json_body.put("merchant_order_status", ""); //状态 String(64) 服务商、平台商 在支付宝侧的商 户id  可空

        JSONArray item_order_list = new JSONArray();
        JSONObject item_order = new JSONObject();
        item_order.put("item_name", "检查单");//商品名称
        item_order.put("quantity", "1");//商品数量
        item_order.put("sku_id", "1"); //商品 skuId
        item_order.put("unit_price", checkSmsRequest.getAmount());//商品单价
        item_order_list.add(item_order);
        json_body.put("item_order_list", item_order_list);

        JSONObject ext_info = new JSONObject();
        ext_info.put("hospital", checkSmsRequest.getHospital());//医院名称
        ext_info.put("hospital_register_id", checkSmsRequest.getHospital_register_id());//医院登记号
        ext_info.put("department", checkSmsRequest.getDepartment());//就诊科室
        ext_info.put("dept_num", checkSmsRequest.getDept_num());//诊室编号
        ext_info.put("dept_loc", checkSmsRequest.getDept_loc());//科室位置
        ext_info.put("navigation", checkSmsRequest.getNavigation());//导航地址
        ext_info.put("doctor", checkSmsRequest.getDoctor());//医生名称
        ext_info.put("doctor_rank", checkSmsRequest.getDoctor_rank());//医生职级
        ext_info.put("doctor_id", checkSmsRequest.getDoctor_id());//医生 id
        ext_info.put("doctor_avatar", checkSmsRequest.getDoctor_avatar());//医生头像 url
        ext_info.put("patient", checkSmsRequest.getPatient());//就诊人  必须与 buy_id 对 应的姓名一致
        ext_info.put("scheduled_time", checkSmsRequest.getScheduled_time());//预约时间
        ext_info.put("check_item", checkSmsRequest.getCheck_item());//检查项目 名称
        ext_info.put("check_time", checkSmsRequest.getCheck_time());//检查时间
        ext_info.put("check_num", checkSmsRequest.getCheck_num());//检查室编号
        ext_info.put("check_loc", checkSmsRequest.getCheck_loc());//检查室位置
        ext_info.put("check_precautions", checkSmsRequest.getCheck_precautions());//检查注意事项
        ext_info.put("report_url", checkSmsRequest.getReport_url());//查看报告
        ext_info.put("medical_order_id", checkSmsRequest.getMedical_order_id());//就诊单id
        ext_info.put("medical_num", checkSmsRequest.getMedical_num());//就诊/检 查序号
        ext_info.put("merchant_order_link_page", checkSmsRequest.getMerchant_order_link_page());//订单链接

        json_body.put("ext_info", ext_info);


        content.setBody(JSONObject.toJSONString(json_body));
        request.setContent(content);

        System.out.println("入参 == " + JSONObject.toJSONString(request));

        AlipayCommerceAppAuthUploadResponse response = new AlipayCommerceAppAuthUploadResponse();
        try {
            response = alipayClient.execute(request, accessToken);
            System.out.println("返回：" + response.getBody());
            if (!response.isSuccess()) {
                System.out.println("错误返回：" + response.getSubMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 医药单
     *
     * @param accessToken
     */
    private void hospitalDrug(String accessToken, DrugSmsRequest drugSmsRequest) {

        AlipayCommerceAppAuthUploadRequest request = getSmsRequst();

        CommerceAppUploadRequestContent content = new CommerceAppUploadRequestContent(); //服务数据参数
        content.setTenantAppId(smsConfig.getTenant_app_id()); //租户应 用ID String(64) 支付宝分配 不可空
        content.setActivityId("upload_hospital_order"); //业务流程ID String(64) 不可空 此处固定为“upload_hospital_order”

        JSONObject json_body = new JSONObject(); //业务流程参数 String(6000) 业务流程请求参数说明
        json_body.put("out_biz_no", drugSmsRequest.getOut_biz_no());//医院预约单订单号 唯一不重复(同一 家 ISV 接入的所有 医院的挂号单、检 查号、医药单都不 可重复) String(128)
        json_body.put("partner_id", ""); //医院在支付宝平台 id(小程序 appId 对 应的 pid)，不传。 根据 out_biz_id 和 partner_id 做幂等 String(128)      06：可空
        json_body.put("buyer_id", drugSmsRequest.getBuyer_id());//就诊人 id，授权 人id 就诊人在支付宝平 台的 2088 开头 16 位id
        json_body.put("tiny_app_id", smsConfig.getTiny_app_id());//医院在支付宝的小 程序id
        json_body.put("order_create_time", drugSmsRequest.getOrder_create_time());//订单创建时间
        json_body.put("order_modified_time", drugSmsRequest.getOrder_modified_time());//订单修改时间
        json_body.put("amount", drugSmsRequest.getAmount());//订单金额
        json_body.put("pay_amount", drugSmsRequest.getPay_amount());//支付金额
        json_body.put("trade_no", drugSmsRequest.getTrade_no());//支付宝交易号
        json_body.put("order_type", ""); //订单类型 String(128) 可空 固定为 ZJA06B07C36D57
        json_body.put("out_biz_type", ""); //外部订单类型 String(64) 服务商、平台商 在支付宝侧的商 户id  可空
        json_body.put("merchant_order_status", ""); //状态 String(64) 服务商、平台商 在支付宝侧的商 户id  可空

        JSONArray item_order_list = new JSONArray();
        JSONObject item_order = new JSONObject();
        item_order.put("item_name", "医药单");//商品名称
        item_order.put("quantity", drugSmsRequest.getQuantity());//商品数量
        item_order.put("sku_id", drugSmsRequest.getSku_id()); //商品 skuId
        item_order.put("unit_price", drugSmsRequest.getUnit_price());//商品单价
        item_order_list.add(item_order);
        json_body.put("item_order_list", item_order_list);

        JSONObject ext_info = new JSONObject();
        ext_info.put("hospital", drugSmsRequest.getHospital());//医院名称
        ext_info.put("hospital_register_id", drugSmsRequest.getHospital_register_id());//医院登记号
        ext_info.put("patient", drugSmsRequest.getPatient());//就诊人  必须与 buy_id 对 应的姓名一致
        ext_info.put("medical_order_id", drugSmsRequest.getMedical_order_id());//就诊单id
        ext_info.put("department", drugSmsRequest.getDepartment());//就诊科室
        ext_info.put("take_medicine_num", drugSmsRequest.getTake_medicine_num());//取药号码
        ext_info.put("take_medicine_window", drugSmsRequest.getTake_medicine_num());//取药窗口
        ext_info.put("take_medicine_loc", drugSmsRequest.getTake_medicine_loc());//取药地址
        ext_info.put("take_medicine_url", drugSmsRequest.getTake_medicine_url());//取药导航链接
        ext_info.put("merchant_order_link_page", drugSmsRequest.getMerchant_order_link_page());//订单链接

        json_body.put("ext_info", ext_info);


        content.setBody(JSONObject.toJSONString(json_body));
        request.setContent(content);

        System.out.println("入参 == " + JSONObject.toJSONString(request));

        AlipayCommerceAppAuthUploadResponse response = new AlipayCommerceAppAuthUploadResponse();
        try {
            response = alipayClient.execute(request, accessToken);
            System.out.println("返回：" + response.getBody());
            if (!response.isSuccess()) {
                System.out.println("错误返回：" + response.getSubMsg());
                throw new SmsException("医药单消息发送失败：" + response.getSubMsg());
            }
            System.out.println("医药单消息发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
