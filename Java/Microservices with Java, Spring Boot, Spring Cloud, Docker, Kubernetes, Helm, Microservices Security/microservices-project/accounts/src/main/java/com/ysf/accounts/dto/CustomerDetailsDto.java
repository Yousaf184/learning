package com.ysf.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(name = "CustomerDetails")
public class CustomerDetailsDto {

    private CustomerDto customerDetails;
    private AccountDto accountDetails;
    private LoanDto loanDetails;
    private CardDto cardDetails;
}
