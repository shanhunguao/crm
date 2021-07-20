package com.ykhd.office.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.domain.entity.BankAccount;
import com.ykhd.office.domain.req.BankAccountSubmit;
import com.ykhd.office.domain.resp.BankAccountDto;
import com.ykhd.office.service.IBankAccountService;
import com.ykhd.office.util.dictionary.TypeEnums.Type4BankAccount;

@Service
public class BankAccountService extends ServiceImpl<BaseMapper<BankAccount>, BankAccount> implements IBankAccountService {

	@Override
	public boolean addBankAccount(Type4BankAccount type, BankAccountSubmit submit) {
		BankAccount account = new BankAccount();
		BeanUtils.copyProperties(submit, account);
		account.setType(type.code());
		return save(account);
	}

	@Override
	public boolean addBankAccount(Type4BankAccount type, Integer identity, String bankName, String accountHolder, String accountNumber) {
		return save(new BankAccount(type.code(), identity, bankName, accountHolder, accountNumber));
	}

	@Override
	public boolean updateBankAccount(BankAccountSubmit submit) {
		BankAccount account = getById(submit.getId());
		Assert.notNull(account, "未知账户");
		BeanUtils.copyProperties(submit, account, "id", "identity");
		return updateById(account);
	}

	@Override
	public boolean deleteBankAccount(Integer id) {
		return removeById(id);
	}

	@Override
	public List<BankAccountDto> getList(Type4BankAccount type, Integer identity) {
		return list(new QueryWrapper<BankAccount>().eq("type", type.code()).eq("identity", identity)).stream().flatMap(v -> {
			BankAccountDto dto = new BankAccountDto();
			BeanUtils.copyProperties(v, dto);
			return Stream.of(dto);
		}).collect(Collectors.toList());
	}

}
