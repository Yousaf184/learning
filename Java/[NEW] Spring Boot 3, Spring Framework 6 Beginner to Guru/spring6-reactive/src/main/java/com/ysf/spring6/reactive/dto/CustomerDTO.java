package com.ysf.spring6.reactive.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Integer id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
