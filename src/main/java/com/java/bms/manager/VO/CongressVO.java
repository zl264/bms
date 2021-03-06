package com.java.bms.manager.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会议信息的VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CongressVO {
    private long congressId;
    private int organizerId;
    private String title;
    private String content;
    private String place;
    private long image;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}