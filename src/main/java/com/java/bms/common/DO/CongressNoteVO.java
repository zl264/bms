package com.java.bms.common.DO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CongressNoteVO {
    private int commonId;
    private int congressId;
    private String arrivalPlace;
    private LocalDateTime arrivalTime;
}
