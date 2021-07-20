package com.ykhd.office.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ykhd.office.domain.req.BankAccountSubmit;
import com.ykhd.office.service.IBankAccountService;
import com.ykhd.office.util.dictionary.TypeEnums.Type4BankAccount;

@RestController
@RequestMapping("/ba")
public class BankAccountController extends BaseController {

	@Autowired
	private IBankAccountService bankAccountService;
	
	/**
	 * 公众号收款账户列表
	 */
	@GetMapping("/oa")
	public Object oaList(Integer oaId) {
		Assert.notNull(oaId, "oaId" + nullValue);
		return bankAccountService.getList(Type4BankAccount.公众号, oaId);
	}
	
	/**
	 * 创建公众号收款账户
	 */
	@PostMapping("/oa")
	public Object addOA(BankAccountSubmit submit) {
		Assert.notNull(submit.getIdentity(), "identity" + nullValue);
		return bankAccountService.addBankAccount(Type4BankAccount.公众号, submit) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 修改公众号收款账户
	 */
	@PutMapping("/oa")
	public Object updateOA(Integer id, BankAccountSubmit submit) {
		Assert.notNull(id, "id" + nullValue);
		submit.setId(id);
		return bankAccountService.updateBankAccount(submit) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 删除公众号收款账户
	 */
	@DeleteMapping("/oa")
	public Object deleteOA(Integer id) {
		Assert.notNull(id, "id" + nullValue);
		return bankAccountService.deleteBankAccount(id) ? success(null) : failure(delete_failure);
	}
	
	/**
	 * 客户付款账户列表
	 */
	@GetMapping("/cust")
	public Object custList(Integer custId) {
		Assert.notNull(custId, "custId" + nullValue);
		return bankAccountService.getList(Type4BankAccount.客户, custId);
	}
	
	/**
	 * 创建客户付款账户
	 */
	@PostMapping("/cust")
	public Object addCust(BankAccountSubmit submit) {
		Assert.notNull(submit.getIdentity(), "identity" + nullValue);
		return bankAccountService.addBankAccount(Type4BankAccount.客户, submit) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 修改客户付款账户
	 */
	@PutMapping("/cust")
	public Object updateCust(Integer id, BankAccountSubmit submit) {
		Assert.notNull(id, "id" + nullValue);
		submit.setId(id);
		return bankAccountService.updateBankAccount(submit) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 删除客户付款账户
	 */
	@DeleteMapping("/cust")
	public Object deleteCust(Integer id) {
		Assert.notNull(id, "id" + nullValue);
		return bankAccountService.deleteBankAccount(id) ? success(null) : failure(delete_failure);
	}
	
}
