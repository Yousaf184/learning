package com.ysf.accounts.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysf.accounts.client.CardsServiceFeignClient;
import com.ysf.accounts.client.LoansServiceFeignClient;
import com.ysf.accounts.dto.*;
import com.ysf.accounts.service.IAccountService;
import com.ysf.accounts.service.ICustomerDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDetailsServiceImpl implements ICustomerDetailsService {

    private final IAccountService accountService;

    private final CardsServiceFeignClient cardsServiceFeignClient;
    private final LoansServiceFeignClient loansServiceFeignClient;

    @Override
    public CustomerDetailsDto getCompleteCustomerDetails(String mobileNumber) throws Exception {
        AccountDetailsDto accountDetailsDto = this.accountService.getAccountDetails(mobileNumber);

        ResponseEntity<ResponseDto> cardResponse = this.cardsServiceFeignClient.getCard(mobileNumber);
        ResponseEntity<ResponseDto> loanResponse = this.loansServiceFeignClient.getLoan(mobileNumber);

        if (cardResponse != null && cardResponse.getStatusCode().isError()) {
            throw new Exception("Error while fetching card details");
        }

        if (loanResponse != null && loanResponse.getStatusCode().isError()) {
            throw new Exception("Error while fetching loan details");
        }

        ObjectMapper jsonMapper = new ObjectMapper();

        CardDto cardDetails = cardResponse != null && cardResponse.getBody() != null
                ? jsonMapper.convertValue(cardResponse.getBody().getData(), CardDto.class)
                : null;
        LoanDto loanDetails = loanResponse != null && loanResponse.getBody() != null
                ? jsonMapper.convertValue(loanResponse.getBody().getData(), LoanDto.class)
                : null;

        return CustomerDetailsDto.builder()
                .customerDetails(accountDetailsDto.getCustomerDetails())
                .accountDetails(accountDetailsDto.getAccountDetails())
                .cardDetails(cardDetails)
                .loanDetails(loanDetails)
                .build();
    }
}
