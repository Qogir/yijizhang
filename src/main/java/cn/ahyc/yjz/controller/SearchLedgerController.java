/**
 * Copyright (c) 2015, AnHui Xin Hua She Group. All rights reserved.
 */
package cn.ahyc.yjz.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ahyc.yjz.model.Period;
import cn.ahyc.yjz.service.SubjectBalanceService;
import cn.ahyc.yjz.util.Constant;

/**
 * @ClassName: SearchLedgerController
 * @Description: 查询->总账
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月22日 下午4:14:42
 * 
 */
@Controller
@RequestMapping("/search/ledger")
public class SearchLedgerController extends BaseController {

    @Autowired
    private SubjectBalanceService subjectBalanceService;

    public SearchLedgerController() {
        this.pathPrefix = "module/search/ledger/";
	}

    /**
     * 查看表数据.
     * 
     * @param subjectCode
     * @param session
     * @param periodFrom
     * @param periodTo
     * @param subjectCodeFrom
     * @param subjectCodeTo
     * @param level
     * @param valueNotNull
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> subjectBalanceList(Long subjectCode, HttpSession session,
            Integer periodFrom, Integer periodTo, Long subjectCodeFrom, Long subjectCodeTo, Long level,
            Long valueNotNull) {
        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (periodFrom == null && periodTo == null) {
            periodFrom = period.getCurrentPeriod();
            periodTo = period.getCurrentPeriod();
        } else if (!(periodFrom != null && periodTo != null)) {
            periodFrom = periodFrom != null ? periodFrom : periodTo;
            periodTo = periodFrom != null ? periodFrom : periodTo;
        }
        if (subjectCodeFrom != null && subjectCodeTo == null) {
            subjectCodeTo = subjectCodeFrom;
        }
        if (subjectCodeTo != null) {
            subjectCodeTo += 1;
        }
        level = level != null && level > 0 ? level : 1;
        list = subjectBalanceService.queryLedgerList(period.getBookId(), periodFrom, periodTo, level,
                subjectCodeFrom, subjectCodeTo, valueNotNull);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", list != null && list.size() > 0 ? list.size() : 0);
        map.put("rows", list);
        return map;
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
        model.addAttribute("periodFrom", period.getCurrentPeriod());
        model.addAttribute("periodTo", period.getCurrentPeriod());
        model.addAttribute("level", 1);
        model.addAttribute("valueNotNull", "1");
        return view("main");
    }

    /**
     * 过滤请求页面
     * 
     * @param model
     * @param session
     * @param periodFrom
     * @param periodTo
     * @param subjectCodeFrom
     * @param subjectCodeTo
     * @param level
     * @param valueNotNull
     * @return
     */
    @RequestMapping("/filter")
    public String filter(Model model, HttpSession session, Integer periodFrom, Integer periodTo, Long subjectCodeFrom,
            Long subjectCodeTo, Long level, Long valueNotNull) {
        if (periodFrom == null && periodTo == null) {
            Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
            model.addAttribute("periodFrom", period.getCurrentPeriod());
            model.addAttribute("periodTo", period.getCurrentPeriod());
        } else if (periodFrom != null && periodTo != null) {
            model.addAttribute("periodFrom", periodFrom);
            model.addAttribute("periodTo", periodTo);
        } else {
            model.addAttribute("periodFrom", periodFrom != null ? periodFrom : periodTo);
            model.addAttribute("periodTo", periodFrom != null ? periodFrom : periodTo);
        }
        model.addAttribute("level", level != null && level > 0 ? level : 1);
        model.addAttribute("subjectCodeFrom", subjectCodeFrom);
        model.addAttribute("subjectCodeTo", subjectCodeTo);
        model.addAttribute("valueNotNull", valueNotNull);
        return view("main");
    }

    /**
     * 过滤页面
     * 
     * @param model
     * @param periodFrom
     * @param periodTo
     * @param subjectCodeFrom
     * @param subjectCodeTo
     * @param level
     * @param valueNotNull
     * @return
     */
    @RequestMapping("/search")
    public String search(Model model, Integer periodFrom, Integer periodTo, Long subjectCodeFrom,
            Long subjectCodeTo, Long level, Long valueNotNull) {
        model.addAttribute("periodFrom", periodFrom);
        model.addAttribute("periodTo", periodTo);
        model.addAttribute("level", level != null && level > 0 ? level : 1);
        model.addAttribute("subjectCodeFrom", subjectCodeFrom);
        model.addAttribute("subjectCodeTo", subjectCodeTo);
        model.addAttribute("valueNotNull", valueNotNull);
        return view("search");
    }
}
