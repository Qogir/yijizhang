/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ahyc.yjz.controller;

import cn.ahyc.yjz.dto.BuildInfo;
import cn.ahyc.yjz.model.AccountBook;
import cn.ahyc.yjz.model.LoginHistory;
import cn.ahyc.yjz.service.AccountBookService;
import cn.ahyc.yjz.service.LoginHistoryService;
import cn.ahyc.yjz.service.PeriodService;
import cn.ahyc.yjz.service.UserService;
import cn.ahyc.yjz.util.Constant;
import cn.ahyc.yjz.util.POIUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * AppController
 *
 * @author sanlai_lee@qq.com
 */
@Controller
public class AppController extends BaseController {


    @Autowired
    LoginHistoryService loginHistoryService;

    @Autowired
    AccountBookService accountBookService;

    @Autowired
    PeriodService periodService;

    @Autowired
    UserService userService;

    @Autowired
    BuildInfo buildInfo;

    public AppController() {
        this.pathPrefix = "/";
    }

    /**
     * 跳转到Dashboard视图.
     *
     * @param model
     * @return
     */
    @RequestMapping("/")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public String dashboard(Map<String, Object> model) {
        return view("dashboard");
    }

    /**
     * 跳转到登录页面.
     *
     * @param model
     * @return
     */
    @RequestMapping("/login")
    @PermitAll
    public String login(Map<String, Object> model, HttpServletRequest request) {

        if (request.getSession().getAttribute(Constant.SPRING_SECURITY_CONTEXT) != null) {
            return "redirect:/";
        }

        Object o = request.getAttribute(Constant.SPRING_SECURITY_LAST_EXCEPTION);
        o = o == null ? request.getSession().getAttribute(Constant.SPRING_SECURITY_LAST_EXCEPTION) : o;
        if (o != null) {
            if (o instanceof BadCredentialsException || o instanceof UsernameNotFoundException) {
                model.put("failureMsg", "用户名或者密码错误，请重试！");
            } else if (o instanceof CredentialsExpiredException) {
                model.put("failureMsg", "登录会话过期,请重新登录！");
            }
        } else {
            model.remove("failureMsg");
        }
        return view("login");
    }

    /**
     * 关于页面.
     *
     * @return
     */
    @RequestMapping("/about")
    @PermitAll
    public String about(Map<String, Object> model) {
        model.put("buildInfo", buildInfo);
        return view("common/about");
    }

    /**
     * 修改保存密码.
     *
     * @return
     */
    @RequestMapping(value = "/password/save", method = RequestMethod.POST)
    @ResponseBody
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public Map<String, Object> savePassWd(@RequestParam String oldPasswd,
                                          @RequestParam String newPasswd) {
        Map<String, Object> map = new HashMap();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userService.isPassWdMatch(user.getUsername(), oldPasswd)) {
            map.put("success", false);
            map.put("msg", "原始密码不正确!");
        } else {
            if (!userService.modifyPasswd(user.getUsername(), newPasswd)) {
                map.put("success", false);
                map.put("msg", "修改密码失败，请稍候重试!");
            } else {
                map.put("success", true);
                map.put("msg", "密码修改成功！");
            }
        }
        return map;
    }

    /**
     * 注销.
     *
     * @param model
     * @return
     */
    @RequestMapping("/logout")
    public String logout(Map<String, Object> model) {
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }

    /**
     * 切换账套.
     *
     * @param id
     * @return
     */
    @RequestMapping("/switch/to/book/{id}")
    @ResponseBody
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public boolean switchAccoutBook(@PathVariable("id") Long id, HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginHistory loginHistory = loginHistoryService.selectLastSuccessLoginHistory(user.getUsername());
        loginHistory.setAccountBookId(id);
        int result = loginHistoryService.updateLoginHistory(loginHistory);
        if (result <= 0) {
            return false;
        } else {
            AccountBook accountBook = accountBookService.selectAccountBookById(id);
            request.getSession().setAttribute(Constant.CURRENT_ACCOUNT_BOOK, accountBook);
            request.getSession().setAttribute(Constant.CURRENT_YEAR, accountBook.getInitYear() + "年");
            request.getSession().setAttribute(Constant.CURRENT_PERIOD, periodService.selectCurrentPeriod(id));
            return true;
        }
    }
}
