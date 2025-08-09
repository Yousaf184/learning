package com.ysf.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Loan")
public class LoanDto {
    private Long id;

    @Size(min = 6, max = 15, message = "Mobile number must be between 6 to 15 digits")
    private String mobileNumber;

    private String loanNumber;

    @Size(min = 3, max = 100, message = "Mobile number must be between 3 to 100 character long")
    private String loanType;

    @Min(value = 10, message = "Total loan should be greater than 10")
    @Max(value = 100_000, message = "Total loan cannot be greater than 100,000")
    private Integer totalLoan;

    @Min(value = 1, message = "Amount paid should be greater than or equal to 1")
    @Max(value = 100_000, message = "Amount paid cannot be greater than 100,000")
    private Integer amountPaid;

    @PositiveOrZero(message = "Outstanding amount should be greater than or equal to zero")
    @Max(value = 100_000, message = "Outstanding amount cannot be greater than 100,000")
    private Integer outstandingAmount;
}
