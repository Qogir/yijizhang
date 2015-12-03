package cn.ahyc.yjz.controller;

import cn.ahyc.yjz.model.AccountBook;
import cn.ahyc.yjz.model.BalanceSheet;
import cn.ahyc.yjz.model.Period;
import cn.ahyc.yjz.service.BalanceSheetService;
import cn.ahyc.yjz.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产负债控制器.
 * Created by Joey Yan on 15-10-15.
 */
@Controller
@RequestMapping("/balance/sheet")
public class BalanceSheetController extends BaseController {

    Logger logger = LoggerFactory.getLogger(BalanceSheetController.class);

    @Autowired
    private BalanceSheetService balanceSheetService;


    /**
     * 修改保存公式.
     *
     * @param balanceSheet
     * @return
     */
    @RequestMapping("/save/exp")
    @ResponseBody
    public Map saveExp(BalanceSheet balanceSheet) {

        logger.debug("修改公式 /save/exp");

        Map result = new HashMap<>();
        result.put("success", true);

        try {
            this.balanceSheetService.saveExp(balanceSheet);
        } catch (Exception e) {
            logger.error("修改公式失败 /save/exp");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 资产负债表入口.
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/main")
    public String main(HttpSession session, Model model) {
        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);

        List<BalanceSheet> balanceSheets = balanceSheetService.balanceSheetsByParentCode(-9999l);
        model.addAttribute("balanceSheets", balanceSheets);
        model.addAttribute("period", period);

        return view("balanceSheet/main");
    }


    /**
     * 获取所有code下数据.
     *
     * @param session
     * @param code
     * @param periodId
     * @return
     */
    @RequestMapping("/balancesheets")
    @ResponseBody
    public List<Map> balanceSheets(HttpSession session, Long code, Long periodId) {

        logger.debug("获取去资产负债数据 /balancesheets");
        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        AccountBook accountBook = (AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK);

        if (periodId != null) {
            period.setId(periodId);
        }

        return balanceSheetService.balanceSheets(period,accountBook, code);
    }


}
