package com.java.bms.common.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonUserVO {
    private String username;
    private String name;
    private int age;
    private String sex;
    private long idCardNo;
    private String identity;
    private int commonId;

}
