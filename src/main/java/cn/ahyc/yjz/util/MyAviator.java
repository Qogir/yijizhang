/**
 * @Title: MyAviator.java
 * @Package cn.ahyc.yjz.util
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年11月2日 上午9:51:57
 * @version V1.0
 */
package cn.ahyc.yjz.util;

import cn.ahyc.yjz.dto.ReportRow;
import cn.ahyc.yjz.mapper.extend.SubjectBalanceExtendMapper;
import cn.ahyc.yjz.thread.ExpressionThread;
import cn.ahyc.yjz.thread.NewExpressionThread;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chengjiarui 1256064203@qq.com
 * @ClassName: MyAviator
 * @Description:
 * @date 2015年11月2日 上午9:51:57
 */
public class MyAviator {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MyAviator.class);
    /**
     * 账上取数
     */
    private static final String BOOK_DATA_FLAG = "a";
    public static final Pattern CELL_PATTERN = Pattern.compile("^([A-Z]+)([0-9]+)");
    public static final Pattern SUM_PATTERN = Pattern.compile("^SUM\\(([A-Z]+)([0-9]+):([A-Z]+)([0-9]+)\\)");
    public static final Pattern IF_PATTERN = Pattern.compile("^IF\\(\"(.*?)(>0)\",(.*?),(.*?)\\)");


    /**
     * 编译表达式
     *
     * @param list
     * @param expression
     * @return
     */
    public static List<Expression> compile(List<Expression> list, String expression) {
        if (StringUtils.isBlank(expression)) {
            expression = "0";
        }
        LOGGER.info("expression compile：{}", expression);
        Expression compiledExp = AviatorEvaluator.compile(expression, true);// 编译表达式缓存
        list.add(compiledExp);
        return list;
    }

    /**
     * 设置变量
     *
     * @param envMap
     * @param expStr
     * @return
     */
    public static String getEnvAndExpression(Map<String, Object> envMap, String expStr) {
        if (StringUtils.isBlank(expStr)) {
            return null;
        }
        String result = expStr.replace("=", "");
        String[] exps = getExps(result);
        String param;
        Matcher matcher;
        for (String exp : exps) {
            param = "";
            matcher = CELL_PATTERN.matcher(exp);
            if (matcher.find()) { // 单元格取数 B2
                param = "cell(list," + (Integer.valueOf(matcher.group(2)) - 1) + ",c" + matcher.group(1) + "Val)";
                result = result.replace(exp, param);
            } else if (exp.indexOf("<") >= 0) { // 账上取数
                param = BOOK_DATA_FLAG + envMap.size();
                envMap.put(param, exp);
                result = result.replace(exp, param);
            }
        }
        return result;
    }

    /**
     * 设置变量
     *
     * @param envMap
     * @param expStr
     * @param colList
     * @return
     */
    public static String getEnvAndExpression(Map<String, Object> envMap, String expStr, List<ReportRow> colList,
                                             Map<String, Object> expMap)
            throws RuntimeException {
        if (StringUtils.isBlank(expStr)) {
            return "0";
        }
        String result = expStr.replace("=", "");
        String[] exps = getExps(result);
        String param;
        Matcher matcher;
        Matcher sumMatcher;
        Matcher ifMatcher;
        for (String exp : exps) {
            matcher = CELL_PATTERN.matcher(exp);
            sumMatcher = SUM_PATTERN.matcher(exp);
            ifMatcher = IF_PATTERN.matcher(exp);
            if (matcher.find()) { // 单元格取数：B2
                if ("B".equals(matcher.group(1))) {
                    param = colList.get(Integer.valueOf(matcher.group(2)) - 1).getcB();
                } else if ("C".equals(matcher.group(1))) {
                    param = colList.get(Integer.valueOf(matcher.group(2)) - 1).getcC();
                } else {
                    LOGGER.error("cell type {} is wrong!", matcher.group(1));
                    throw new RuntimeException("cell type" + matcher.group(1) + " is wrong!");
                }
                result = result.replaceFirst(exp, "(" + getEnvAndExpression(envMap, param, colList, expMap) + ")");
            } else if (exp.contains("ACCT")) { // 账上取数：ACCT("4001","Y","",0,0,0,"")
                if (expMap.containsKey(exp)) {
                    param = String.valueOf(expMap.get(exp));
                } else {
                    param = BOOK_DATA_FLAG + envMap.size();
                    envMap.put(param, exp);
                    expMap.put(exp, param);
                }
                result = result.replace(exp, param);
            } else if (sumMatcher.find()) { // SUM函数：SUM(E23:E26)
                param = getSumExp(sumMatcher.group(1), sumMatcher.group(2), sumMatcher.group(4));
                result = result.replace(exp, "(" + getEnvAndExpression(envMap, param, colList, expMap) + ")");
            } else if (ifMatcher.find()) {  // IF函数：IF("D13>0",D13,0)
                param = "((" + getEnvAndExpression(envMap, ifMatcher.group(1), colList, expMap) + ifMatcher.group(2) +
                        ")?";
                param += getEnvAndExpression(envMap, ifMatcher.group(3), colList, expMap);
                param += ":" + getEnvAndExpression(envMap, ifMatcher.group(4), colList, expMap) + ")";
                result = result.replace(exp, param);
            }
        }
        return result;
    }

    /**
     * 解析SUM
     *
     * @param cellName
     * @param start
     * @param end
     * @return
     */
    private static String getSumExp(String cellName, String start, String end) {
        StringBuffer sb = new StringBuffer();
        for (int i = Integer.valueOf(start); i < Integer.valueOf(end); i++) {
            sb.append(cellName).append(i).append("+");
        }
        sb.append(cellName).append(end);
        return sb.toString();
    }

    /**
     * 获取公式原子
     *
     * @param result
     * @return
     */
    private static String[] getExps(String result) {
        String str = result.replaceAll("\\+|\\*|\\/", "&");// 替换加号、乘号、除号
        str = str.replaceAll("\\-([A-Z]+)", "&$1");// 替换减号，排除@-1
        return str.split("&");
    }

    /**
     * 获取变量值<br/>
     * 账上取数调用多线程<br/>
     * 单元格合计调用自定义函数cell，单元格计算只支持从上到下计算
     *
     * @param map
     * @param currentPeriod
     * @param bookId
     * @param subjectBalanceExtendMapper
     * @return
     */
    public static Map<String, Object> getEnvValue(Map<String, Object> map, Integer currentPeriod, Long bookId,
                                                  SubjectBalanceExtendMapper subjectBalanceExtendMapper) {
        ExecutorService executor = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(map.size());// 计数
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        Entry<String, Object> entry;
        while (it.hasNext()) {
            entry = it.next();
            if (StringUtils.startsWith(entry.getKey(), BOOK_DATA_FLAG)) { // 账上取数
                executor.execute(new ExpressionThread(map, entry.getKey(), String.valueOf(entry.getValue()), latch,
                        currentPeriod, bookId, subjectBalanceExtendMapper));
            } else {
                latch.countDown();
            }
        }
        executor.shutdown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取变量值<br/>
     * 账上取数调用多线程<br/>
     * 单元格合计调用自定义函数cell，单元格计算只支持从上到下计算
     *
     * @param map
     * @param currentPeriod
     * @param bookId
     * @param subjectBalanceExtendMapper
     * @return
     */
    public static Map<String, Object> getEnvValueNew(Map<String, Object> map, Integer currentPeriod, Long bookId,
                                                     SubjectBalanceExtendMapper subjectBalanceExtendMapper) {
        ExecutorService executor = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(map.size());// 计数
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        Entry<String, Object> entry;
        while (it.hasNext()) {
            entry = it.next();
            if (StringUtils.startsWith(entry.getKey(), BOOK_DATA_FLAG)) { // 账上取数
                executor.execute(new NewExpressionThread(map, entry.getKey(), String.valueOf(entry.getValue()), latch,
                        currentPeriod, bookId, subjectBalanceExtendMapper));
            } else {
                latch.countDown();
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        return map;
    }

    /**
     * 执行表达式
     *
     * @param expression
     * @param envMap
     * @return
     */
    public static Object execute(Expression expression, Map<String, Object> envMap) {
        return expression.execute(envMap);
    }

//    public static void main(String[] args) {
//        Matcher matcher = CELL_PATTERN.matcher("C36");
//        if(matcher.find()) {
//            System.out.println(matcher.group(2));
//        }
//    }
}
