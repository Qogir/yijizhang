/**
 * Copyright (c) 2015, AnHui Xin Hua She Group. All rights reserved.
 */
package cn.ahyc.yjz.controller;

import cn.ahyc.yjz.model.*;
import cn.ahyc.yjz.service.PeriodService;
import cn.ahyc.yjz.service.VoucherService;
import cn.ahyc.yjz.service.VoucherTemplateService;
import cn.ahyc.yjz.service.impl.CarryOverServiceImpl;
import cn.ahyc.yjz.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 记账凭证
 *
 * @author sanlai_lee@qq.com
 */
@Controller
@RequestMapping("/voucher")
public class VoucherController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherController.class);

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private VoucherTemplateService voucherTemplateService;

    public VoucherController() {
        this.pathPrefix = "module/voucher/";
    }

    /**
     * 凭证页面
     *
     * @param model
     * @param voucherId
     * @param voucherTemplateId
     * @param session
     * @return
     */
    @RequestMapping("/main")
    public String voucher(Model model, Long voucherId, Long voucherTemplateId, Long isreversal, HttpSession session) {
        if (voucherTemplateId != null) {// 从模式凭证新增
            VoucherTemplate template = voucherTemplateService.queryVoucherTemplate(voucherTemplateId);
            model.addAttribute("templateId", template.getId());
            template.setId(null);
            model.addAttribute("voucher", template);
        } else if (voucherId != null) {// 修改、查看、冲销
            Voucher voucher = voucherService.queryVoucher(voucherId);
            model.addAttribute("voucherId", voucher.getId());
            if (isreversal != null) {// 冲销
                model.addAttribute("isreversal", isreversal);
                voucher.setId(null);
                voucher.setVoucherNo(null);
                voucher.setVoucherTime(null);
                voucher.setPeriodId(null);
            } else {
                Period voucherPeriod = periodService.queryPeriod(voucher.getPeriodId());
                model.addAttribute("currentPeriod", voucherPeriod.getCurrentPeriod());
            }
            model.addAttribute("voucher", voucher);
        } else {// 新增
            model.addAttribute("voucher", new Voucher());
        }
        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        AccountBook accountBook = (AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK);
        model.addAttribute("sessionBook", accountBook.getInitYear());
        model.addAttribute("sessionPeriod", period.getCurrentPeriod());
        model.addAttribute("voucherNo", voucherService.queryNextVoucherNo(period.getId()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sessiontime = accountBook.getInitYear() + "-" + String.format("%02d", period.getCurrentPeriod());
        String systemtime = dateFormat.format(new Date());
        String lastTime = dateFormat.format(CarryOverServiceImpl.getLastDayOfMonth(accountBook.getInitYear(), period.getCurrentPeriod()));
        model.addAttribute("voucherTime", systemtime.startsWith(sessiontime) ? systemtime :
                (systemtime.compareTo(sessiontime) < 0 ? sessiontime + "-01" : lastTime));
        return view("voucher");
    }

    /**
     * 凭证明细列表
     *
     * @param voucherId
     * @param voucherTemplateId
     * @param session
     * @return
     */
    @RequestMapping("/detail/list")
    @ResponseBody
    public Map<String, Object> voucherDetailList(Long voucherId, Long voucherTemplateId, Long isreversal,
                                                 HttpSession session) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> footerlist = new ArrayList<Map<String, Object>>();
        if (voucherTemplateId != null) {// 从模式凭证新增
            long bookId = ((AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK)).getId();
            list = voucherTemplateService.queryDetailList(voucherTemplateId, bookId);
            footerlist.add(voucherTemplateService.queryDetailTotal(voucherTemplateId));
        } else if (voucherId != null) {// 修改、查看
            long bookId = ((AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK)).getId();
            list = voucherService.queryVoucherDetailList(voucherId, bookId, isreversal);
            footerlist.add(voucherService.queryDetailTotal(voucherId, isreversal));
        } else {// 新增
            footerlist.add(new HashMap<String, Object>());
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", list != null && list.size() > 0 ? list.size() : 0);
        map.put("rows", list);
        map.put("footer", footerlist);
        return map;
    }

    /**
     * 凭证号检查
     *
     * @param session
     * @param no
     * @param id
     * @return
     */
    @RequestMapping(value = "/checkno")
    @ResponseBody
    public Map<String, Object> checkNo(HttpSession session, Integer no, Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
            int maxNo = voucherService.checkNo(no, period.getId(), id);
            if (maxNo > 0) {
                map.put("result", maxNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 凭证保存
     *
     * @param session
     * @param model
     * @param request
     * @param voucher
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(HttpSession session, Model model, HttpServletRequest request, Voucher voucher) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<VoucherDetail> details = validateVoucher(session, request, voucher);
            /** 保存 **/
            map.put("result", voucherService.save(voucher, details));
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 验证凭证以及凭证分录，返回组织好的凭证分录
     *
     * @param session
     * @param request
     * @param voucher
     * @return
     */
    private List<VoucherDetail> validateVoucher(HttpSession session, HttpServletRequest request, Voucher voucher) {
        AccountBook accountBook = (AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK);
        // 当然账套ID所对应的当前期.
        Period period = periodService.selectCurrentPeriod(accountBook.getId());
        if (voucher.getPeriodId() == null) {
            voucher.setPeriodId(period.getId());
        }
        if (!period.getId().equals(voucher.getPeriodId())) {
            throw new RuntimeException("当前会计期间id：" + period.getId() + "不等于保存凭证会计期间id：" + voucher.getPeriodId());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sessiontime = accountBook.getInitYear() + "-" + String.format("%02d", period.getCurrentPeriod());
        if (voucher.getVoucherTime() == null) {
            throw new RuntimeException("凭证日期不能为空");
        }
        String voucherTime = dateFormat.format(voucher.getVoucherTime());
        if (!StringUtils.startsWith(voucherTime, sessiontime)) {
            throw new RuntimeException("凭证日期" + voucherTime + "不在当前会计期间日期" + sessiontime + "范围内");
        }
        List<VoucherDetail> details = validateVoucherDetail(request);
        return details;
    }

    /**
     * 验证凭证分录，返回组织好的凭证分录
     *
     * @param request
     * @return
     */
    private List<VoucherDetail> validateVoucherDetail(HttpServletRequest request) {
        /** 组织凭证明细数据 **/
        List<VoucherDetail> details = new ArrayList<VoucherDetail>();
        String[] subjectCodeArr = request.getParameterValues("subjectCode");
        if (subjectCodeArr != null) {
            if (subjectCodeArr.length <= 0) {
                throw new RuntimeException("必须存在凭证分录");
            }
            String[] summaryArr = request.getParameterValues("summary");
            String[] debitArr = request.getParameterValues("newdebit");
            String[] crebitArr = request.getParameterValues("newcrebit");
            VoucherDetail voucherDetail;
            BigDecimal debit;
            BigDecimal credit;
            BigDecimal debitSum = new BigDecimal(0);
            BigDecimal crebitSum = new BigDecimal(0);
            for (int i = 0; i < subjectCodeArr.length; i++) {
                LOGGER.info("借方：{}|贷方：{}", debitArr[i], crebitArr[i]);
                if (StringUtils.isBlank(debitArr[i]) && StringUtils.isBlank(crebitArr[i])) {
                    throw new RuntimeException("同一凭证分录中借方金额、贷方金额必须存在一个");
                }
                if (StringUtils.isNotBlank(debitArr[i]) && StringUtils.isNotBlank(crebitArr[i])) {
                    throw new RuntimeException("同一凭证分录中借方金额、贷方金额只能存在一个");
                }
                voucherDetail = new VoucherDetail();
                voucherDetail.setSummary(summaryArr[i]);
                voucherDetail.setSubjectCode(Long.valueOf(subjectCodeArr[i]));
                if (StringUtils.isNotBlank(debitArr[i])) {
                    debit = new BigDecimal(debitArr[i]);
                    debitSum = debitSum.add(debit);
                    voucherDetail.setDebit(debit);
                }
                if (StringUtils.isNotBlank(crebitArr[i])) {
                    credit = new BigDecimal(crebitArr[i]);
                    crebitSum = crebitSum.add(credit);
                    voucherDetail.setCredit(credit);
                }
                details.add(voucherDetail);
            }
            if (debitSum.compareTo(crebitSum) != 0) {
                throw new RuntimeException("记账凭证借贷不平衡，借方：" + debitSum + "|贷方：" + crebitSum);
            }
        }
        return details;
    }

    /**
     * 会计科目叶子节点列表
     *
     * @param session
     * @return
     */
    @RequestMapping("/subjectlist")
    @ResponseBody
    public List<AccountSubject> accountSubjectList(HttpSession session) {
        long bookId = ((AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK)).getId();
        List<AccountSubject> list = voucherService.queryAccountSubjectList(bookId);
        return list;
    }

    /**
     * 凭证制作说明页面
     *
     * @return
     */
    @RequestMapping("/help")
    public String help() {
        return view("help");
    }

    /**
     * 凭证-科目余额页面
     *
     * @param model
     * @param subjectCode
     * @param voucherId
     * @param session
     * @return
     */
    @RequestMapping("/balance")
    public String subjectBalance(Model model, String subjectCode, Long voucherId, HttpSession session) {
        Period voucherPeriod = null;
        if (voucherId != null) {
            Voucher voucher = voucherService.queryVoucher(voucherId);
            voucherPeriod = periodService.queryPeriod(voucher.getPeriodId());
        }
        AccountBook accountBook = (AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK);
        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        model.addAttribute("currentPeriod",
                voucherPeriod != null ? voucherPeriod.getCurrentPeriod() : period.getCurrentPeriod());
        model.addAttribute("moneyCode", accountBook.getMoneyCode());
        model.addAttribute("subjectName", subjectCode);
        model.addAttribute("subjectCode", StringUtils.split(subjectCode, " ")[0]);
        return view("subjectBalance");
    }

    /**
     * 模式凭证列表页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/template")
    public String voucherTemplate(Model model) {
        return view("voucherTemplate");
    }

    @RequestMapping("/cashFlowData")
    public String cashFlowData(Model model, HttpSession session, HttpServletRequest request) {
        AccountBook accountBook = (AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK);
        model.addAttribute("moneyCode", accountBook.getMoneyCode());
        model.addAttribute("moneyName", accountBook.getMoneyName());
        return view("cashFlowData");
    }

    /**
     * 检查是否为明细科目代码
     *
     * @param session
     * @param request
     * @param subjectCode
     * @return
     */
    @RequestMapping(value = "/checkSubjectCode", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> checkSubjectCode(HttpSession session, HttpServletRequest request, String subjectCode) {

        AccountBook accountBook = (AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("result", voucherService.checkSubjectCode(subjectCode, accountBook.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", e.getMessage());
        }
        return map;
    }
}
