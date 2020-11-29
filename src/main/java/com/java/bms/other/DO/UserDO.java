package com.java.bms.other.DO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 不管什么类型的用户注册后都可以只有用户名和密码，其他信息可以之后填写
 * 所以设置这个UserVO类用来登录和注册
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDO {
    private String username;
    private String password;
    private Integer id;


}
