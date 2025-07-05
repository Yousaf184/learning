package com.ysf.loans.service.impl;

import com.ysf.loans.dto.LoanDto;
import com.ysf.loans.entity.Loan;
import com.ysf.loans.exception.NotFoundException;
import com.ysf.loans.mapper.LoanMapper;
import com.ysf.loans.repository.LoanRepository;
import com.ysf.loans.service.ILoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements ILoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    private final Supplier<NotFoundException> LOAN_NOT_FOUND_EXCEPTION_SUPPLIER = () -> {
        String errorMsg = "No loan associated with the given mobile number";
        return new NotFoundException(errorMsg);
    };

    @Override
    public LoanDto createLoan(String mobileNumber) {
        if (mobileNumber.isBlank()) {
            throw new IllegalArgumentException("Mobile number cannot be empty");
        }

        Optional<Loan> loanOptional = this.loanRepository.findByMobileNumber(mobileNumber);

        if (loanOptional.isPresent()) {
            String errorMsg = "Loan associated with the given mobile number already exists";
            throw new IllegalArgumentException(errorMsg);
        }

        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        final int NEW_LOAN_LIMIT = 25000;
        Loan newLoan = Loan.builder()
                .mobileNumber(mobileNumber)
                .loanNumber(String.valueOf(randomLoanNumber))
                .loanType("HOME_LOAN")
                .totalLoan(NEW_LOAN_LIMIT)
                .amountPaid(0)
                .outstandingAmount(NEW_LOAN_LIMIT)
                .build();

        Loan createdLoan = this.loanRepository.save(newLoan);
        return this.loanMapper.toLoanDto(createdLoan);
    }

    @Override
    public LoanDto getLoanDetails(String mobileNumber) {
        Optional<Loan> loanOptional = this.loanRepository.findByMobileNumber(mobileNumber);

        Loan loan = loanOptional.orElseThrow(LOAN_NOT_FOUND_EXCEPTION_SUPPLIER);
        return this.loanMapper.toLoanDto(loan);
    }

    @Override
    public void updateLoan(LoanDto updatedLoanDetails, String mobileNumber) {
        Optional<Loan> loanOptional = this.loanRepository.findByMobileNumber(mobileNumber);

        Loan loanToUpdate = loanOptional.orElseThrow(LOAN_NOT_FOUND_EXCEPTION_SUPPLIER);

        if (StringUtils.hasText(updatedLoanDetails.getMobileNumber())) {
            loanToUpdate.setMobileNumber(updatedLoanDetails.getMobileNumber());
        }
        if (StringUtils.hasText(updatedLoanDetails.getLoanType())) {
            loanToUpdate.setLoanType(updatedLoanDetails.getLoanType());
        }
        if (updatedLoanDetails.getTotalLoan() != null) {
            loanToUpdate.setTotalLoan(updatedLoanDetails.getTotalLoan());
        }
        if (updatedLoanDetails.getAmountPaid() != null) {
            loanToUpdate.setAmountPaid(updatedLoanDetails.getAmountPaid());
        }
        if (updatedLoanDetails.getOutstandingAmount() != null) {
            loanToUpdate.setOutstandingAmount(updatedLoanDetails.getOutstandingAmount());
        }

        this.loanRepository.save(loanToUpdate);
    }

    @Override
    public void deleteLoan(String mobileNumber) {
        Optional<Loan> loanOptional = this.loanRepository.findByMobileNumber(mobileNumber);

        loanOptional.ifPresent(loanToDelete -> this.loanRepository.deleteById(loanToDelete.getId()));
    }
}
