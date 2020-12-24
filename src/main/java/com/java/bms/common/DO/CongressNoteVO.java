package com.java.bms.common.DO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会议记录的VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CongressNoteVO {
    private int commonId;
    private int congressId;
    private String arrivalPlace;
    private LocalDateTime arrivalTime;
}
