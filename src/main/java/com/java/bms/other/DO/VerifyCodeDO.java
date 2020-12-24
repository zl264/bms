package com.java.bms.other.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码的DO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCodeDO {
    private String code;
    private byte[] imgBytes;
    private long expireTime;
}
