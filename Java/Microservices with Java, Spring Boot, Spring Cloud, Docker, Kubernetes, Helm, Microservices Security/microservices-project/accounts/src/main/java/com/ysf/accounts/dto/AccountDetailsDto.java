package com.ysf.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(name = "AccountDetails")
public class AccountDetailsDto {
    private CustomerDto customerDetails;
    private AccountDto accountDetails;
}
