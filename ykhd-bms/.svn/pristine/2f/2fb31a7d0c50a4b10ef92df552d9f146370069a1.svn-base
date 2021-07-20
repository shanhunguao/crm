package com.ykhd.office.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.entity.BankAccount;
import com.ykhd.office.domain.req.BankAccountSubmit;
import com.ykhd.office.domain.resp.BankAccountDto;
import com.ykhd.office.util.dictionary.TypeEnums.Type4BankAccount;

public interface IBankAccountService extends IService<BankAccount> {

	/**
	 * 新增账户
	 */
	boolean addBankAccount(Type4BankAccount type, BankAccountSubmit submit);
	boolean addBankAccount(Type4BankAccount type, Integer identity, String bankName, String accountHolder, String accountNumber);
	
	/**
	 * 修改账户
	 */
	boolean updateBankAccount(BankAccountSubmit submit);
	
	/**
	 * 删除账户
	 */
	boolean deleteBankAccount(Integer id);
	
	/**
	 * 账户列表
	 */
	List<BankAccountDto> getList(Type4BankAccount type, Integer identity);
}
