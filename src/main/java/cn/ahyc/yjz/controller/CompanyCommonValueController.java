/**
 * Copyright (c) 2015, AnHui Xin Hua She Group. All rights reserved.
 */
package cn.ahyc.yjz.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ahyc.yjz.model.CompanyCommonValue;
import cn.ahyc.yjz.service.CompanyCommonValueService;

/**
 * @ClassName: CompanyCommonValueController
 * @Description: 企业通用配置值:凭证字、模式凭证类别
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月17日 上午10:14:03
 * 
 */
@Controller
@RequestMapping("/company/common/value")
public class CompanyCommonValueController extends BaseController {

    @Autowired
    private CompanyCommonValueService companyCommonValueService;

    public CompanyCommonValueController() {
        this.pathPrefix = "module/companycommonvalue/";
	}

    /**
     * 配置页面
     * 
     * @param model
     * @param type
     * @param winTitle
     * @return
     */
    @RequestMapping("/main")
    public String main(Model model, Long type, String winTitle) {
        model.addAttribute("type", type);
        model.addAttribute("winTitle", winTitle);
        return view("main");
    }
    
    /**
     * 企业通用配置值列表
     * 
     * @param type
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(Long type) {
        List<CompanyCommonValue> list = new ArrayList<CompanyCommonValue>();
        if (type != null) {
            list = companyCommonValueService.queryListByType(type);// 1L：凭证字
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", list != null && list.size() > 0 ? list.size() : 0);
        map.put("rows", list);
        return map;
    }

    /**
     * 凭证字列表
     * 
     * @return
     */
    @RequestMapping("/voucherwordlist")
    @ResponseBody
    public List<CompanyCommonValue> voucherWordList() {
        List<CompanyCommonValue> list = companyCommonValueService.queryListByType(1L);// 1L：凭证字
        return list;
    }

    /**
     * 模式凭证列表
     * 
     * @return
     */
    @RequestMapping("/templatetypelist")
    @ResponseBody
    public List<CompanyCommonValue> voucherTemplateTypeList() {
        List<CompanyCommonValue> list = companyCommonValueService.queryListByType(2L);// 2L：模式凭证类别
        return list;
    }

    /**
     * 检查是否已存在
     * 
     * @param session
     * @param name
     * @param id
     * @return
     */
    @RequestMapping("/check")
    @ResponseBody
    public Map<String, Object> check(HttpSession session, String name, Long id, Long type) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (StringUtils.isNoneBlank(name) && companyCommonValueService.check(name, id, type)) {
                map.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", e.getMessage());
        }
        return map;
    }

    /**
     * 保存
     * 
     * @param entity
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(CompanyCommonValue entity) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            entity.setValue(entity.getShowValue());
            /** 保存 **/
            companyCommonValueService.save(entity);
            map.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "系统异常！");
        }
        return map;
    }

    /**
     * 删除
     * 
     * @param entity
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Map<String, Object> delete(Long id) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            companyCommonValueService.delete(id);
            map.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "系统异常！");
        }
        return map;
    }
}
