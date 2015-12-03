/**
 * Copyright (c) 2015, AnHui Xin Hua She Group. All rights reserved.
 */
package cn.ahyc.yjz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ahyc.yjz.dto.ReportRow;
import cn.ahyc.yjz.model.Period;
import cn.ahyc.yjz.service.ProfitService;
import cn.ahyc.yjz.util.Constant;

/**
 * @ClassName: ProfitController
 * @Description: 利润表
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月23日 下午3:39:25
 * 
 */
@Controller
@RequestMapping("/profit")
public class ProfitController extends BaseController {

    @Autowired
    private ProfitService profitService;

    public ProfitController() {
        this.pathPrefix = "module/profit/";
	}

    /**
     * 页面
     * 
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/main")
    public String main(Model model, HttpSession session) {
        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        model.addAttribute("searchtPeriod", period.getCurrentPeriod());
        return view("main");
    }
    
    /**
     * 数据列表
     * 
     * @param model
     * @param session
     * @param searchtPeriod
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> list(Model model, HttpSession session, Integer searchtPeriod) {
        
        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        Long bookId = period.getBookId();
        Integer currentPeriod;
        if (searchtPeriod != null) {
            currentPeriod= searchtPeriod;
        } else {
            currentPeriod = period.getCurrentPeriod();
        }
        List<Map<String, Object>> list = profitService.getList(currentPeriod, bookId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", list != null && list.size() > 0 ? list.size() : 0);
        map.put("rows", list);
        return map;
    }

    /**
     * 重新计算，公式取自页面，数据列表
     * 
     * @param model
     * @param session
     * @param searchtPeriod
     * @param expList
     * @return
     */
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> count(Model model, HttpSession session, Integer searchtPeriod,
            @RequestBody(required = false) List<ReportRow> expList) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
            Long bookId = period.getBookId();
            Integer currentPeriod;
            if (searchtPeriod != null) {
                currentPeriod = searchtPeriod;
            } else {
                currentPeriod = period.getCurrentPeriod();
            }
            List<Map<String, Object>> list;
            if (expList != null) {
                list = profitService.getListWithExpList(expList, currentPeriod, bookId);
            } else {
                list = profitService.getList(currentPeriod, bookId);
            }
            map.put("total", list != null && list.size() > 0 ? list.size() : 0);
            map.put("rows", list);
            map.put("success", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", e.getMessage());
        }
        return map;
    }

    /**
     * 保存
     * 
     * @param model
     * @param session
     * @param searchtPeriod
     * @param expList
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(Model model, HttpSession session, Integer searchtPeriod,
            @RequestBody(required = false) List<ReportRow> expList) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
            Long bookId = period.getBookId();
            Integer currentPeriod;
            if (searchtPeriod != null) {
                currentPeriod = searchtPeriod;
            } else {
                currentPeriod = period.getCurrentPeriod();
            }
            profitService.save(expList, currentPeriod, bookId);
            map.put("success", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", e.getMessage());
        }
        return map;
    }
}
