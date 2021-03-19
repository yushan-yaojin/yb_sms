package com.mock.sms.mapper;

import com.mock.sms.vo.AuthToken;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * description: ISmsTokenMapper <br>
 * date: 2021/3/18 13:51 <br>
 * author: cn_yaojin <br>
 * version: 1.0 <br>
 */
@Mapper
public interface ISmsTokenMapper {

    @Insert("insert into sms_user_token (`alipayUid`,`appId`,`ticket`,`accessToken`)values (#{alipayUid},#{appId},#{ticket},#{accessToken})")
    void add(@Param("alipayUid") String alipayUid, @Param("appId") String appId, @Param("ticket") String ticket, @Param("accessToken") String accessToken);

    @Update("update sms_user_token set accessToken=#{accessToken},ticket=#{ticket} where alipayUid=#{alipayUid}")
    void update(@Param("alipayUid") String alipayUid, @Param("ticket") String ticket, @Param("accessToken") String accessToken);

    @Update("update sms_user_token set card_num=#{cardNum},phone=#{phone} where alipayUid=#{alipayUid}")
    void updateCard(@Param("cardNum") String cardNum, @Param("phone") String phone, @Param("alipayUid") String alipayUid);


    @Select("SELECT * FROM sms_user_token where alipayUid =#{userId} and appId=#{appId}")
    List<AuthToken> list(@Param("userId") String userId, @Param("appId") String appId);

    @Select("SELECT * FROM sms_user_token where alipayUid =#{userId} order by add_time desc")
    List<AuthToken> listByUserId(@Param("userId") String userId);


    @Select("SELECT * FROM sms_user_token where card_num=#{cardNum} order by add_time desc")
    List<AuthToken> listByCardNum(@Param("cardNum") String cardNum);

    @Select("SELECT * FROM sms_user_token where phone=#{phone} order by add_time desc")
    List<AuthToken> listByPhone(@Param("phone") String phone);

}
