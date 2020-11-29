package com.java.bms.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CongressVO {
    private long congressId;
    private int organizerId;
    private String title;
    private String describe;
    private Date time;
    private String place;
    private long image;
}
