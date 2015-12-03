/**
 * Copyright (c) 2015, AnHui Xin Hua She Group. All rights reserved.
 */
package cn.ahyc.yjz.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletResponse;

/**
 * BaseController
 *
 * @author sanlai_lee@qq.com
 */
public abstract class BaseController {

		String pathPrefix = "module/";

		/**
		 * 根据路径前缀获取视图路径
		 *
		 * @param viewName
		 * @return
		 */
		String view(String viewName) {
				return view(this.pathPrefix, viewName);
		}

		/**
		 * 根据路径前缀获取视图路径
		 *
		 * @param pathPrefix
		 * @param viewName
		 * @return
		 */
		String view(String pathPrefix, String viewName) {
				return pathPrefix + viewName;
		}

		@InitBinder
		public void initBinder(WebDataBinder binder) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		}


		/**
		 * 解决Load denied by X-Frame-Options.
		 * @param response
		 */
		@ModelAttribute
		public void setVaryResponseHeader(HttpServletResponse response) {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");
				response.setHeader("X-Frame-Options", "SAMEORIGIN");
		}

}
