package com.ysf.spring6.rest.mvc.dto;

import com.ysf.spring6.rest.mvc.entity.BeerOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CustomerDTO {
    private UUID id;
    private Integer version;

    @NotBlank
    @NotNull
    @Size(max = 255)
    private String name;

    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

    private List<BeerOrder> beerOrders;
}