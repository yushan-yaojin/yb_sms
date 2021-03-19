    CREATE TABLE `sms_user_token`  (
            `alipayUid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '支付宝buser_id',
            `appId` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'app_id',
            `ticket` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'ticket',
            `accessToken` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
        INDEX `sms_user_token`(`alipayUid`, `appId`, `ticket`, `accessToken`) USING BTREE
    )