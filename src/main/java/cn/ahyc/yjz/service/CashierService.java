package cn.ahyc.yjz.service;


import cn.ahyc.yjz.model.AccountBook;
import cn.ahyc.yjz.model.Period;

/**
 * Created by john Hu on 15-10-09.
 */
public interface CashierService {
	
	/**
     * 结账
     * 
     * @param voucher
     * @param details
     */
	Long cashierSubmit(Period period,AccountBook accountBook); 
}
