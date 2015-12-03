/**
 * Copyright (c) 2015, AnHui Xin Hua She Group. All rights reserved.
 */
package cn.ahyc.yjz.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ahyc.yjz.model.Period;
import cn.ahyc.yjz.service.SubjectBalanceService;
import cn.ahyc.yjz.util.Constant;

/**
 * @ClassName: SearchTrialBalanceController
 * @Description: 试算平衡表
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月21日 下午2:38:54
 * 
 */
@Controller
@RequestMapping("/search/trialbalance")
public class SearchTrialBalanceController extends BaseController {

    @Autowired
    private SubjectBalanceService subjectBalanceService;

    public SearchTrialBalanceController() {
        this.pathPrefix = "module/search/trialbalance/";
	}

    /**
     * 试算平衡表格数据
     * 
     * @param session
     * @param currentPeriod
     * @param level
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> subjectBalanceList(HttpSession session, Integer currentPeriod,
            Long level) {
        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        currentPeriod = currentPeriod != null ? currentPeriod : period.getCurrentPeriod();
        level = level != null && level > 0 ? level : 1;
        list = subjectBalanceService.querySubjectBalanceList(period.getBookId(), currentPeriod, currentPeriod, level,
                null, null, null);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", list != null && list.size() > 0 ? list.size() : 0);
        map.put("rows", list);
        if (list != null && list.size() > 0) {
            Map<String, Object> total = list.get(list.size() - 1);
            if (!(bigDecimalCompareTo(total.get("initial_debit_balance"), total.get("initial_credit_balance"))
                    && bigDecimalCompareTo(total.get("period_debit_occur"), total.get("period_credit_occur"))
                    && bigDecimalCompareTo(total.get("terminal_debit_balance"),
                            total.get("terminal_credit_balance")))) {
                map.put("notBalance", true);
            }
        }
        return map;
    }

    public static boolean bigDecimalCompareTo(Object value, Object value2) {
        if (value == null && value2 == null) {
            return true;
        } else if (value == null || value2 == null) {
            return false;
        }
        return getBigDecimal(value).compareTo(getBigDecimal(value2)) == 0;
    }

    public static BigDecimal getBigDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            value = StringUtils.replace(String.valueOf(value), ",", "");
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = new BigDecimal(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass()
                        + " into a BigDecimal.");
            }
        }
        return ret;
    }

    /**
     * 第一次默认初始化页面
     * 
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/main")
    public String main(Model model, HttpSession session) {
        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        model.addAttribute("currentPeriod", period.getCurrentPeriod());
        model.addAttribute("level", 1);
        return view("main");
    }

    /**
     * 过滤请求页面
     * 
     * @param model
     * @param session
     * @param currentPeriod
     * @param level
     * @return
     */
    @RequestMapping("/filter")
    public String filter(Model model, HttpSession session, Integer currentPeriod, Long level) {
        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        model.addAttribute("currentPeriod", currentPeriod != null ? currentPeriod : period.getCurrentPeriod());
        model.addAttribute("level", level != null && level > 0 ? level : 1);
        return view("main");
    }

    /**
     * 过滤页面
     * 
     * @param model
     * @param currentPeriod
     * @param level
     * @return
     */
    @RequestMapping("/search")
    public String search(Model model, Integer currentPeriod, Long level) {
        model.addAttribute("currentPeriod", currentPeriod);
        model.addAttribute("level", level != null && level > 0 ? level : 1);
        return view("search");
    }
}
