package com.java.bms.manager.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 普通用户的VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private String username;
    private String name;
    private int age;
    private String sex;
    private String idCardNo;
    private String identity;
    private int commonId;
    private String tel;
}
