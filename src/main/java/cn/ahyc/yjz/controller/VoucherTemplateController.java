package cn.ahyc.yjz.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ahyc.yjz.model.AccountBook;
import cn.ahyc.yjz.model.VoucherTemplate;
import cn.ahyc.yjz.model.VoucherTemplateDetail;
import cn.ahyc.yjz.service.VoucherTemplateService;
import cn.ahyc.yjz.util.Constant;

/**
 * @ClassName: VoucherTemplateController
 * @Description: 模式凭证
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年11月3日 下午2:41:10
 * 
 */
@Controller
@RequestMapping("/voucher/template")
public class VoucherTemplateController extends BaseController {

    @Autowired
    private VoucherTemplateService voucherTemplateService;

    public VoucherTemplateController() {
	    this.pathPrefix="module/voucher/";
	}

    /**
     * 模式凭证列表数据
     * 
     * @param session
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> voucherTemplateList(HttpSession session) {
        List<VoucherTemplate> list = voucherTemplateService.queryVoucherTemplateList();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", list != null && list.size() > 0 ? list.size() : 0);
        map.put("rows", list);
        return map;
    }

    /**
     * 模式凭证编辑页面
     * 
     * @param model
     * @param voucherTemplateId
     * @param session
     * @return
     */
    @RequestMapping("/add")
    public String voucherTemplateAdd(Model model, Long voucherTemplateId, HttpSession session) {
        VoucherTemplate voucherTemplate = new VoucherTemplate();
        if (voucherTemplateId != null) {
            voucherTemplate = voucherTemplateService.queryVoucherTemplate(voucherTemplateId);
        }
        model.addAttribute("voucherTemplate", voucherTemplate);
        return view("voucherTemplateAdd");
    }

    /**
     * 模式凭证保存页面
     * 
     * @param model
     * @param name
     * @param voucherWord
     * @return
     */
    @RequestMapping("/save")
    public String voucherTemplateSave(Model model, String name, String voucherWord) {
        model.addAttribute("name", name);
        model.addAttribute("voucherWord", voucherWord);
        return view("voucherTemplateSave");
    }

    /**
     * 模式凭证明细列表
     * 
     * @param session
     * @param voucherTemplateId
     * @return
     */
    @RequestMapping("/detail/list")
    @ResponseBody
    public Map<String, Object> voucherTemplateDetailList(HttpSession session, Long voucherTemplateId) {
        List<VoucherTemplateDetail> list = new ArrayList<VoucherTemplateDetail>();
        if (voucherTemplateId != null) {
            AccountBook accountBook = (AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK);
            list = voucherTemplateService.queryVoucherTemplateDetailList(voucherTemplateId, accountBook.getId());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", list != null && list.size() > 0 ? list.size() : 0);
        map.put("rows", list);
        return map;
    }

    /**
     * 检查模式凭证名称是否重复
     * 
     * @param session
     * @param name
     * @param id
     * @return
     */
    @RequestMapping("/checkname")
    @ResponseBody
    public Map<String, Object> checkTemplateName(HttpSession session, String name, Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (StringUtils.isNoneBlank(name) && voucherTemplateService.checkTemplateName(name, id)) {
                map.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", e.getMessage());
        }
        return map;
    }

    /**
     * 删除模式凭证
     * 
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> deleteTemplate(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (id!=null) {
                voucherTemplateService.deleteTemplate(id);
                map.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", e.getMessage());
        }
        return map;
    }

    /**
     * 保存模式凭证
     * 
     * @param model
     * @param request
     * @param voucherTemplate
     * @param wordFlag
     * @param numFlag
     * @param summaryFlag
     * @param subjectFlag
     * @param dFlag
     * @param crFlag
     * @param checkFlag
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveTemplate(Model model, HttpServletRequest request, VoucherTemplate voucherTemplate,
            String wordFlag, String numFlag, String summaryFlag, String subjectFlag, String dFlag, String crFlag,
            String checkFlag) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (!"1".equals(wordFlag) && StringUtils.isNotBlank(checkFlag)) {
                voucherTemplate.setVoucherWord(null);
            }
            if (!"1".equals(numFlag) && StringUtils.isNotBlank(checkFlag)) {
                voucherTemplate.setBillNum(null);
            }
            /** 组织凭证明细数据 **/
            List<VoucherTemplateDetail> details = new ArrayList<VoucherTemplateDetail>();
            String[] subjectCodeArr = request.getParameterValues("subjectCode");
            String[] summaryArr = request.getParameterValues("summary");
            String[] debitArr = request.getParameterValues("newdebit");
            String[] crebitArr = request.getParameterValues("newcrebit");
            VoucherTemplateDetail voucherTemplateDetail;
            boolean addFlag;
            boolean bsummaryFlag = "1".equals(summaryFlag) || StringUtils.isBlank(checkFlag);
            boolean bsubjectFlag = "1".equals(subjectFlag) || StringUtils.isBlank(checkFlag);
            boolean bdFlag = "1".equals(dFlag) || StringUtils.isBlank(checkFlag);
            boolean bcrFlag = "1".equals(crFlag) || StringUtils.isBlank(checkFlag);
            for (int i = 0; i < subjectCodeArr.length; i++) {
                if (StringUtils.isNotBlank(debitArr[i]) && StringUtils.isNotBlank(crebitArr[i])) {
                    throw new RuntimeException("同一凭证分录中借方金额、贷方金额只能存在一个");
                }
                voucherTemplateDetail = new VoucherTemplateDetail();
                addFlag = false;
                if (StringUtils.isNotBlank(summaryArr[i]) && bsummaryFlag) {
                    addFlag = true;
                    voucherTemplateDetail.setSummary(summaryArr[i]);
                }
                if (StringUtils.isNotBlank(subjectCodeArr[i]) && bsubjectFlag) {
                    addFlag = true;
                    voucherTemplateDetail.setSubjectCode(Long.valueOf(subjectCodeArr[i]));
                }
                if (StringUtils.isNotBlank(debitArr[i]) && bdFlag) {
                    addFlag = true;
                    voucherTemplateDetail.setDebit(new BigDecimal(debitArr[i]));
                }
                if (StringUtils.isNotBlank(crebitArr[i]) && bcrFlag) {
                    addFlag = true;
                    voucherTemplateDetail.setCredit(new BigDecimal(crebitArr[i]));
                }
                if (addFlag) {
                    details.add(voucherTemplateDetail);
                }
            }
            /** 保存 **/
            voucherTemplateService.saveTemplate(voucherTemplate, details);
            map.put("result", "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", e.getMessage());
        }
        return map;
    }
}
