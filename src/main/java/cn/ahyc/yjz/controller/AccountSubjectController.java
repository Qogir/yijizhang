package cn.ahyc.yjz.controller;

import cn.ahyc.yjz.model.AccountSubject;
import cn.ahyc.yjz.model.Period;
import cn.ahyc.yjz.service.AccountSubjectService;
import cn.ahyc.yjz.util.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会计科目.
 * Created by Joey Yan on 15-9-22.
 */
@Controller
@RequestMapping("/account/subject")
public class AccountSubjectController extends BaseController {

    private final Long category_subject_code = -9999l;

    @Resource
    private AccountSubjectService accountSubjectService;


    /**
     * 试算平衡页面.
     *
     * @param session
     * @return
     */
    @RequestMapping("/initData/balance/page")
    public String balancePage(Model model, HttpSession session) {

        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        Long bookId = period.getBookId();
        Map map = accountSubjectService.balance(bookId, category_subject_code);
        model.addAttribute("balance", map);
        model.addAttribute("currentPeriod", period.getCurrentPeriod());

        return view("accountSubject/initData/balance");
    }

    /**
     * 汇总.
     *
     * @return
     */
    @RequestMapping("/initData/calculate")
    @ResponseBody
    public Map<String, Object> calculate(HttpSession session) {

        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        Long bookId = period.getBookId();

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);

        try {
            accountSubjectService.calculate(bookId, category_subject_code);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", e.getMessage());
        }

        return result;
    }

    /**
     * 初始化数据修改.
     *
     * @param session
     * @param accountSubject
     * @return
     */
    @RequestMapping("/initData/edit")
    @ResponseBody
    public Map<String, Object> initDataEdit(HttpSession session, AccountSubject accountSubject) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);

        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);

        try {
            accountSubjectService.initDataEdit(accountSubject, period.getId());
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", e.getMessage());
        }

        return result;
    }

    /**
     * 获取树形结构数据.
     *
     * @param session
     * @param keyword
     * @return
     */
    @RequestMapping("/initData/alldata")
    @ResponseBody
    public Map allSubjectTreeData(HttpSession session, String keyword) {

        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);

        return accountSubjectService.allSubjectTreeData(period, keyword);
    }


    /**
     * 初始化数据页面入口.
     *
     * @return
     */
    @RequestMapping("/initData/main")
    public String initDataPage() {
        return view("accountSubject/initData/main");
    }

    /**
     * 跳转到科目说明页面.
     *
     * @return
     */
    @RequestMapping("/tip")
    public String tip() {
        return view("accountSubject/tip");
    }


    /**
     * 删除会计科目.
     *
     * @param subjectId
     * @return
     */
    @RequestMapping("/delete/{subjectId}")
    @ResponseBody
    public Map<String, Object> delete(@PathVariable("subjectId") Long subjectId) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);

        try {
            accountSubjectService.delete(subjectId);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }


    /**
     * 会计科目新增、修改统一入口.
     *
     * @param accountSubject
     * @param parentSubjectCodeBack
     * @param parentSubjectCode
     * @param session
     * @return
     */
    @RequestMapping(value = ("/edit"))
    @ResponseBody
    public Map<String, Object> edit(
            AccountSubject accountSubject
            , @RequestParam("parent_subject_code_back") Long parentSubjectCodeBack
            , @RequestParam("parent_subject_code") Long parentSubjectCode
            , HttpSession session
    ) {

        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);

        try {
            accountSubjectService.editAccountSubject(accountSubject, parentSubjectCodeBack, parentSubjectCode, period);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }


    /**
     * 会计科目管理页面入口.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = ("/main"))
    public String main(Model model, HttpSession session) {

        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        Long bookId = period.getBookId();

        List<AccountSubject> templates = accountSubjectService.getCategoriesByCode(category_subject_code, bookId);
        model.addAttribute("categories", templates);

        return view("accountSubject/main");
    }


    /**
     * 提供给其他功能的查询页面
     *
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = ("/search"))
    public String search(Model model, HttpSession session) {

        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        Long bookId = period.getBookId();

        List<AccountSubject> templates = accountSubjectService.getCategoriesByCode(category_subject_code, bookId);
        model.addAttribute("categories", templates);

        return view("accountSubject/search");
    }

    /**
     * 新增 修改页面入口.
     *
     * @param categoryId
     * @param subjectId
     * @param model
     * @return
     */
    @RequestMapping("/opt/{opt}/category/{categoryId}")
    public String editPage(
            @PathVariable("opt") String opt
            , @PathVariable("categoryId") Long categoryId
            , Long subjectId
            , Model model
            , HttpSession session) {

        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        Long bookId = period.getBookId();

        AccountSubject subject = new AccountSubject();
        subject.setBookId(bookId);
        int level = 1;

        model.addAttribute("opt", opt);
        model.addAttribute("categoryId", categoryId.toString());
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("accountSubject", subject);
        model.addAttribute("parentSubjectCode", -1);

        if (subjectId != -1) {
            subject = accountSubjectService.getSubjectById(subjectId);
            Long subjectCode = subject.getSubjectCode();
            model.addAttribute("subjectId", subject.getId());

            if ("edit".equals(opt)) {
                level = subject.getLevel();
                model.addAttribute("accountSubject", subject);
                model.addAttribute("parentSubjectCode", level == 1 ? -1 : subject.getParentSubjectCode());
            } else {
                level = subject.getLevel() + 1;
                model.addAttribute("subjectId", -1);
                model.addAttribute("parentSubjectCode", level == 1 ? -1 : subjectCode);
            }

        }

        model.addAttribute("level", level);

        return view("accountSubject/edit");
    }


    /**
     * 会计小分类.
     *
     * @return
     */
    @RequestMapping("/category/detail")
    @ResponseBody
    public List<AccountSubject> getCategoryDetailByCategoryId(@RequestParam("category_id") Long categoryId, HttpSession session) {

        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        Long bookId = period.getBookId();

        List<AccountSubject> categoryDetails = accountSubjectService.getCategoriesByCategoryId(categoryId, bookId);
        return categoryDetails;
    }


    /**
     * 会计分类列表.
     *
     * @param categoryId
     * @return
     */
    @RequestMapping("/category/{id}/subjects")
    @ResponseBody
    public List<Map> getSubject(@PathVariable("id") Long categoryId, HttpSession session) {

        Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        Long bookId = period.getBookId();

        return accountSubjectService.getSubjectsByCategoryId(categoryId, bookId);
    }

}
