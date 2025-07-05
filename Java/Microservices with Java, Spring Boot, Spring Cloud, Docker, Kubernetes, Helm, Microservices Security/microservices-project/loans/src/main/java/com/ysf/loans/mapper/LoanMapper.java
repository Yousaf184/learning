package com.ysf.loans.mapper;

import com.ysf.loans.dto.LoanDto;
import com.ysf.loans.entity.Loan;
import org.mapstruct.Mapper;

@Mapper
public interface LoanMapper {

//    Loan toLoanEntity(LoanDto loanDto);

    LoanDto toLoanDto(Loan loan);
}
