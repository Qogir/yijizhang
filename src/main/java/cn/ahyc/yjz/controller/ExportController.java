package cn.ahyc.yjz.controller;

import cn.ahyc.yjz.util.POIUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cn.ahyc.yjz.controller
 * Created by Joey Yan on 15-11-9.
 */
@Controller
public class ExportController extends BaseController {

    /**
     * @param request
     * @param response
     */
    @RequestMapping("/export/to/excel")
    @ResponseBody
    public void exportToExcel(String dataJsonStr, HttpServletRequest request, HttpServletResponse response) {

        try {
            POIUtil.exportToExcel(request, response, dataJsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
