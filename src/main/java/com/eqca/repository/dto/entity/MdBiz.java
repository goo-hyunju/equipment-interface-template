package com.eqca.repository.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MdBiz {
    private String bizCode;
    private String bizType;
    private String bizName;
    private String bizDesc;
    private int stockLevel;
    private String stockDesc;
    private String procType;
    private LocalDateTime createDate;
}
