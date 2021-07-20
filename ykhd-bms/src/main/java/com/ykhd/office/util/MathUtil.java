package com.ykhd.office.util;

import java.math.BigDecimal;

/**
 * 数学计算（业务方向）
 */
public class MathUtil {
	
	private static BigDecimal hundred = BigDecimal.valueOf(100);

	/**
	 * 计算税额，四舍五入精确单位分。
	 * <p>金额 乘以 (taxRate 除以 100)  除以（taxRate 除以 100  加 1）</p>
	 * <p>example: 12345.67 * 0.06 / 1.06</p>
	 * @param amount 金额
	 * @param taxRate 税率
	 */
	public static BigDecimal calculateTaxMoney(BigDecimal amount, int taxRate) {
		BigDecimal tax_rate = BigDecimal.valueOf(taxRate).divide(hundred);
		BigDecimal tax_rate_plus = tax_rate.add(BigDecimal.ONE);
		return amount.multiply(tax_rate).divide(tax_rate_plus, 2, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 计算利润
	 * @param [execute_price, cost_price, customer_tax_rate, oa_tax_rate, sellExpense, channelExpense]
	 * @return profit
	 */
	public static BigDecimal calculateProfit(BigDecimal execute_price, BigDecimal cost_price, int customer_tax_rate, int oa_tax_rate,
		BigDecimal sellExpense, BigDecimal channelExpense) {
		BigDecimal customer_tax = calculateTaxMoney(execute_price, customer_tax_rate);
		BigDecimal oa_tax = calculateTaxMoney(cost_price, oa_tax_rate);
		BigDecimal profit = execute_price.subtract(cost_price).subtract(customer_tax).add(oa_tax).subtract(sellExpense == null ? BigDecimal.ZERO : sellExpense)
				.add(channelExpense == null ? BigDecimal.ZERO : channelExpense);
		return profit.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 计算【客户税额，号主税额，利润，利润率】
	 * @param [execute_price, cost_price, customer_tax_rate, oa_tax_rate, sellExpense, channelExpense]
	 * @return [customer_tax, oa_tax, profit, peofit_rate]
	 */
	public static String[] calculateProfitMore(BigDecimal execute_price, BigDecimal cost_price, int customer_tax_rate, int oa_tax_rate,
		BigDecimal sellExpense, BigDecimal channelExpense) {
		BigDecimal customer_tax = calculateTaxMoney(execute_price, customer_tax_rate);
		BigDecimal oa_tax = calculateTaxMoney(cost_price, oa_tax_rate);
		BigDecimal profit = execute_price.subtract(cost_price).subtract(customer_tax).add(oa_tax).subtract(sellExpense == null ? BigDecimal.ZERO : sellExpense)
				.add(channelExpense == null ? BigDecimal.ZERO : channelExpense);
		BigDecimal peofit_rate = execute_price.doubleValue() == 0 ? BigDecimal.ZERO : profit.multiply(hundred).divide(execute_price, 2, BigDecimal.ROUND_HALF_UP);
		return new String[]{customer_tax.toString(), oa_tax.toString(),
				profit.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), peofit_rate.toString()};
	}
	
	/**
	 * 金额计算公式
	 * 金额*税率=税额
	 * 金额+税额=价税合计
	 */
	public static BigDecimal[] calculateTaxMoney2(BigDecimal rates,BigDecimal totalmoney){
		BigDecimal rate = rates.setScale(2, BigDecimal.ROUND_CEILING);
		BigDecimal money =totalmoney.divide(rates.divide(new BigDecimal(100)).add(new BigDecimal(1)), 2, BigDecimal.ROUND_DOWN);
		BigDecimal rates_money =totalmoney.subtract(money);
		return new BigDecimal[]{rate,money,rates_money};
	}

}
