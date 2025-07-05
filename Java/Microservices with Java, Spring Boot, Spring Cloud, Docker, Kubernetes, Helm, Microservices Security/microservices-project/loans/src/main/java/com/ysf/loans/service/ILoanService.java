package com.ysf.loans.service;

import com.ysf.loans.dto.LoanDto;

public interface ILoanService {

    LoanDto createLoan(String mobileNumber);

    LoanDto getLoanDetails(String mobileNumber);

    void updateLoan(LoanDto updatedLoanDetails, String mobileNumber);

    void deleteLoan(String mobileNumber);
}
