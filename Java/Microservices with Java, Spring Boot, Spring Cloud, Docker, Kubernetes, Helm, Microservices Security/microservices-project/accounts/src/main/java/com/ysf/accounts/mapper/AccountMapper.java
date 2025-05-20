package com.ysf.accounts.mapper;

import com.ysf.accounts.dto.AccountDto;
import com.ysf.accounts.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountMapper {

    @Mapping(target = "customerId", ignore = true)
    Account toAccountEntity(AccountDto accountDto);

    AccountDto toAccountDto(Account account);
}
