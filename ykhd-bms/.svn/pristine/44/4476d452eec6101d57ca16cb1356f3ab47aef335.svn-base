package com.ykhd.office.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ykhd.office.domain.entity.OfficeAccount;
import com.ykhd.office.domain.req.OfficeAccountCondition;
import com.ykhd.office.domain.req.OfficeAccountSubmit;
import com.ykhd.office.service.IContentChangeService;
import com.ykhd.office.service.IOACollectionService;
import com.ykhd.office.service.IOAFollowUpService;
import com.ykhd.office.service.IOfficeAccountService;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Change;

@RestController
@RequestMapping("/oa")
public class OfficeAccountController extends BaseController {

	@Autowired
	private IOfficeAccountService officeAccountService;
	@Autowired
	private IOAFollowUpService oAFollowUpService;
	@Autowired
	private IOACollectionService oACollectionService;
	@Autowired
	private IContentChangeService contentChangeService;
	
	/**
	 * 公众号列表
	 */
	@GetMapping
	public Object list(OfficeAccountCondition condition) {
		return officeAccountService.getListByPage(condition);
	}
	
	/**
	 * 公众号详情
	 */
	@GetMapping("/{id}")
	public Object detail(@PathVariable Integer id) {
		return officeAccountService.getDetail(id);
	}
	
	/**
	 * 新增公众号
	 */
	@PostMapping
	public Object add(@Valid OfficeAccountSubmit submit) {
		return officeAccountService.addOfficeAccount(submit) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 修改公众号
	 */
	@PutMapping("/info/{id}")
	public Object update(@Valid OfficeAccountSubmit submit, @PathVariable Integer id) {
		submit.setId(id);
		return officeAccountService.updateOfficeAccount(submit) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 提交为启用
	 */
	@PutMapping("/submitUsing/{id}")
	public Object submitUsing(@PathVariable String id) {
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return officeAccountService.submitUsing(ids);
		}
		return officeAccountService.submitUsing(Integer.valueOf(id));
	}
	
	/**
	 * 待开发转化为启用
	 */
	@PutMapping("/changeUsing/{id}")
	public Object changeUsing(@Valid OfficeAccountSubmit submit, @PathVariable Integer id) {
		submit.setId(id);
		return officeAccountService.changeUsing(submit) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 申请作废（媒介）
	 */
	@PutMapping("/appCancel/{id}")
	public Object appCancel(@PathVariable String id) {
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return officeAccountService.appCancel(ids);
		}
		return officeAccountService.appCancel(Integer.valueOf(id));
	}
	
	/**
	 * 同意或驳回作废申请
	 */
	@PutMapping("/dealCancel/{id}")
	public Object dealCancel(@PathVariable String id, Integer result) {
		Assert.notNull(result, "result" + nullValue);
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return officeAccountService.dealCancel(result, ids);
		}
		return officeAccountService.dealCancel(result, Integer.valueOf(id));
	}
	
	/**
	 * 直接作废
	 */
	@PutMapping("/cancel/{id}")
	public Object cancel(@PathVariable String id) {
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return officeAccountService.cancel(ids);
		}
		return officeAccountService.cancel(Integer.valueOf(id));
	}
	
	/**
	 * 新增跟进记录
	 */
	@PostMapping("/followUp")
	public Object addFollowUp(Integer oaId, String content) {
		Assert.notNull(oaId, "id" + nullValue);
		Assert.hasText(content, "content" + nullValue);
		return oAFollowUpService.addOAFollowUp(oaId, content) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 查询跟进记录
	 */
	@GetMapping("/followUp")
	public Object followUpList(Integer oaId) {
		Assert.notNull(oaId, "id" + nullValue);
		return oAFollowUpService.getList(oaId);
	}
	
	/**
	 * 修改刷号信息
	 */
	@PutMapping("/brush/{id}")
	public Object editBrush(@PathVariable Integer id, String isBrush) {
		Assert.hasText(isBrush, "isBrush" + nullValue);
		OfficeAccount officeAccount = officeAccountService.getOne(new QueryWrapper<OfficeAccount>().select("id", "is_brush").eq("id", id));
		Assert.notNull(officeAccount, "未知公众号");
		if (isBrush.equals(officeAccount.getIsBrush()))
			return success(null);
		boolean boo = officeAccountService.update(new UpdateWrapper<OfficeAccount>().eq("id", id).set("is_brush", isBrush).set("last_update_time", new Date()));
		if (boo)
			contentChangeService.addContentChange(Type4Change.公众号, id, "【是否刷号】：" + officeAccount.getIsBrush() + "  变为：" + isBrush);
		return boo ? success(null) : failure(update_failure);
	}
	
	/**
	 * 修改合作模式
	 */
	@PutMapping("/coopMode/{id}")
	public Object coopMode(@PathVariable Integer id, String mode) {
		Assert.hasText(mode, "mode" + nullValue);
		OfficeAccount officeAccount = officeAccountService.getOne(new QueryWrapper<OfficeAccount>().select("id", "cooperate_mode").eq("id", id));
		Assert.notNull(officeAccount, "未知公众号");
		if (mode.equals(officeAccount.getCooperateMode()))
			return success(null);
		boolean boo = officeAccountService.update(new UpdateWrapper<OfficeAccount>().eq("id", id).set("cooperate_mode", mode).set("last_update_time", new Date()));
		if (boo)
			contentChangeService.addContentChange(Type4Change.公众号, id, "【合作模式】：" + officeAccount.getCooperateMode() + "  变为：" + mode);
		return boo ? success(null) : failure(update_failure);
	}
	
	/**
	 * 修改适合客户类型
	 */
	@PutMapping("/custType/{id}")
	public Object custType(@PathVariable Integer id, String type) {
		Assert.hasText(type, "type" + nullValue);
		OfficeAccount officeAccount = officeAccountService.getOne(new QueryWrapper<OfficeAccount>().select("id", "customer_type").eq("id", id));
		Assert.notNull(officeAccount, "未知公众号");
		if (type.equals(officeAccount.getCustomerType()))
			return success(null);
		boolean boo = officeAccountService.update(new UpdateWrapper<OfficeAccount>().eq("id", id).set("customer_type", type).set("last_update_time", new Date()));
		if (boo)
			contentChangeService.addContentChange(Type4Change.公众号, id, "【适合客户类型】：" + officeAccount.getCustomerType() + "  变为：" + type);
		return boo ? success(null) : failure(update_failure);
	}
	
	/**
	 * 收藏或取消公众号
	 */
	@PutMapping("/collection/{id}")
	public Object collection(@PathVariable Integer id) {
		return oACollectionService.collection(id) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 更新评分评论
	 */
	@PutMapping("/score/{id}")
	public Object score(@PathVariable Integer id, String score, String comment) {
		Assert.hasText(score, "score" + nullValue);
		return officeAccountService.score(id, score, comment) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 查看公众号内容变更记录
	 */
	@GetMapping("/contentChange")
	public Object change(Integer oaId) {
		Assert.notNull(oaId, "oaId" + nullValue);
		return contentChangeService.getContentChangeList(Type4Change.公众号, oaId);
	}
	
	/**
	 * 申请成为优势号（媒介）
	 */
	@PutMapping("/advantageApp/{id}")
	public Object advantageApp(@PathVariable Integer id) {
		return officeAccountService.advanageApp(id) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 申请取消优势号（媒介）
	 */
	@PutMapping("/advantageCancel/{id}")
	public Object advantageCancel(@PathVariable Integer id) {
		return officeAccountService.advanageCancel(id) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 审批优势号申请（业务总监）
	 */
	@PutMapping("/advantageReview/{id}")
	public Object advantageReview(@PathVariable String id, Integer result) {
		Assert.notNull(result, "result" + nullValue);
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return officeAccountService.advantageReview(result, ids);
		}
		return officeAccountService.advantageReview(result, Integer.valueOf(id));
	}
	
	/**
	 * 审批优势号取消（业务总监）
	 */
	@PutMapping("/advantageReview2/{id}")
	public Object advantageReview2(@PathVariable String id, Integer result) {
		Assert.notNull(result, "result" + nullValue);
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return officeAccountService.advantageReview2(result, ids);
		}
		return officeAccountService.advantageReview2(result, Integer.valueOf(id));
	}
	
	/**
	 * 查询公司主体对应的联系方式
	 */
	@GetMapping("/phone")
	public Object queryPhone(String company) {
		OfficeAccount one = officeAccountService.getOne(new QueryWrapper<OfficeAccount>().select("phone").eq("company", company)
				.orderByDesc("id").last("limit 1")); // 坑B getOne查询多个结果会抛异常， 需要配合last 1
		return success(one == null ? "" : one.getPhone());
	}
	
	/**
	 * 升级协议号
	 */
	@PutMapping("/agreement/{id}")
	public Object agreement(@PathVariable Integer id, BigDecimal price) {
		return officeAccountService.up2Agreement(id, price) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 修改采购价
	 */
	@PutMapping("/collectPrice/{id}")
	public Object collectPrice(@PathVariable Integer id, BigDecimal price) {
		Assert.notNull(price, "price" + nullValue);
		OfficeAccount officeAccount = officeAccountService.getOne(new QueryWrapper<OfficeAccount>().select("id", "collect_price").eq("id", id));
		Assert.notNull(officeAccount, "未知公众号");
		if (price.equals(officeAccount.getCollectPrice()))
			return success(null);
		boolean boo = officeAccountService.update(new UpdateWrapper<OfficeAccount>().eq("id", id).set("collect_price", price).set("last_update_time", new Date()));
		if (boo)
			contentChangeService.addContentChange(Type4Change.公众号, id, "【采购价】：" + officeAccount.getCollectPrice() + "  变为：" + price);
		return boo ? success(null) : failure(update_failure);
	}
}
