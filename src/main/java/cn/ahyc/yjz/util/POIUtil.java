package cn.ahyc.yjz.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Joey Yan on 15-11-5.
 */
public class POIUtil {


    /**
     * 导出excel通用。
     *
     * @param request
     * @param response
     * @param dataJsonStr
     * @throws Exception
     */
    public static void exportToExcel(HttpServletRequest request, HttpServletResponse response, String dataJsonStr) throws Exception {

        JSONObject jsonObject = new JSONObject();
        String[] titles = new String[]{};
        String[] fields = new String[]{};
        String filename = "test";
        JSONArray data = null;
        JSONArray footer = null;

        try {
            jsonObject = JSONArray.parseObject(dataJsonStr);
            titles = jsonObject.getObject("titles", String[].class);
            fields = jsonObject.getObject("fields", String[].class);
            filename = jsonObject.getObject("filename", String.class);
            JSONObject dataObj = JSONArray.parseObject(jsonObject.getObject("data", String.class));
            data = dataObj.getObject("rows", JSONArray.class);
            footer = dataObj.getObject("footer", JSONArray.class);
        } catch (Exception e) {
            throw new Exception("解析成json出现异常。");
        }

        //POI 填充数值
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet(filename);
        int rownum = 0;
//        sheet.setDefaultColumnWidth(100 * 250);

        if (titles.length != 0) {
            Row headerRow = sheet.createRow(rownum++);
            Cell cell = null;
            for (int i = 0; i < titles.length; i++) {
                String tit = titles[i];
                cell = headerRow.createCell(i);
                cell.setCellStyle(getCellStyle(wb));
                cell.setCellValue(tit);
                sheet.autoSizeColumn(i, true); //
            }
        }

        for (int j = 0; j < data.size(); j++) {
            JSONObject jo = data.getJSONObject(j);
            Row row = sheet.createRow(rownum++);

            for (int i = 0; i < fields.length; i++) {
                String val = jo.get(fields[i]) == null ? "" : jo.get(fields[i]).toString();
                row.createCell(i).setCellValue(val);
                sheet.autoSizeColumn(i, true); //
            }
        }

        //footer
        if (footer != null) {
            for (int j = 0; j < footer.size(); j++) {
                JSONObject jo = footer.getJSONObject(j);
                Row row = sheet.createRow(rownum++);

                for (int i = 0; i < fields.length; i++) {
                    String val = jo.get(fields[i]) == null ? "" : jo.get(fields[i]).toString();
                    row.createCell(i).setCellValue(val);
                    sheet.autoSizeColumn(i, true); //
                }
            }
        }

        //文件名重写 20151109-测试
        filename = filename.concat(new SimpleDateFormat("YYYYMMddHHmm").format(new Date()));
        filename = new String(filename.getBytes(), "iso8859-1");
        filename = filename.concat(".xls");

        response.addHeader("Content-Disposition", "attachment;filename=".concat(filename));
        response.setContentType("application/x-msdownload");

        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 定义单元格样式.
     *
     * @param wb
     * @return
     */
    private static CellStyle getCellStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();

        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);// 设置背景色
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE); //下边框

        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中

        Font font = (HSSFFont) wb.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 14);//设置字体大小
        cellStyle.setFont(font);

        return cellStyle;
    }

}
