package cn.ahyc.yjz.service;

import javax.servlet.http.HttpSession;

/**
 * Created by john Hu on 15-9-28.
 */
public interface  CarryOverService {

	 /**
     * 结转损益
     * 
     * @param voucher
     * @param details
     */
	String CarryoverSubmit(String summary,String voucherWord,HttpSession session);
}
