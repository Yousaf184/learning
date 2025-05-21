package com.ysf.accounts.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountDetailsDto {
    private CustomerDto customerDetails;
    private AccountDto accountDetails;
}
