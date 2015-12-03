package cn.ahyc.yjz.thread;

import cn.ahyc.yjz.mapper.extend.SubjectBalanceExtendMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 账上取数-现金流量表
 */
public class NewExpressionThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewExpressionThread.class);

    /**
     * 公式
     */
    private String cell;

    /**
     * 计数器，判断线程全部计算结束
     */
    private CountDownLatch latch;

    /**
     * 当前期间值
     */
    private Integer currentPeriod;

    /**
     * 账套id
     */
    private Long bookId;

    /**
     * sql mapper
     */
    private SubjectBalanceExtendMapper subjectBalanceExtendMapper;

    /**
     * 变量map
     */
    Map<String, Object> map;

    /**
     * 变量名
     */
    String key;

    public NewExpressionThread() {
    }

    public NewExpressionThread(Map<String, Object> map, String key, String cell, CountDownLatch latch,
                               Integer currentPeriod, Long bookId,
                               SubjectBalanceExtendMapper subjectBalanceExtendMapper) {
        this.map = map;
        this.key = key;
        this.cell = cell;
        this.latch = latch;
        this.currentPeriod = currentPeriod;
        this.bookId = bookId;
        this.subjectBalanceExtendMapper = subjectBalanceExtendMapper;
    }


    /**
     * 解析公式，返回查询map
     */
    private Map<String,Object> searchMap() {

        String subjectCodeStart;
        String subjectCodeEnd;
        String searchFeild = null;
        String year = null;
        Integer periodStart = currentPeriod;
        Integer periodEnd = currentPeriod;

        Pattern pattern = Pattern.compile("ACCT\\(\"(.*?)\",\"(.*?)\",\"(.*?)\",(.*?),(.*?),(.*?),\"(.*?)\"\\)");
        Matcher matcher = pattern.matcher(cell);
        // 会计科目
        String subjectCode = null;
        if (matcher.find()) {
            subjectCode = matcher.group(1);
        }
        if (StringUtils.contains(subjectCode, ":")) {
            subjectCodeStart = subjectCode.split(":")[0];
            subjectCodeEnd = subjectCode.split(":")[1];
        } else {
            subjectCodeStart = subjectCode;
            subjectCodeEnd = subjectCode;
        }
        // 取数类型
        if (matcher.find()) {
            searchFeild = matcher.group(2);
        }
        // 会计年度、会计期间
        if (matcher.find()) {
            year = matcher.group(4);
            periodStart = Integer.valueOf(matcher.group(5));
            periodStart = periodStart < 0 ? currentPeriod + periodStart : periodStart;
            periodEnd = Integer.valueOf(matcher.group(6));
            periodEnd = periodEnd < 0 ? currentPeriod + periodEnd : periodEnd;
        }

        Map<String,Object> searchMap = new HashMap<String, Object>();
        searchMap.put("subjectCodeStart", subjectCodeStart);
        searchMap.put("subjectCodeEnd", Integer.valueOf(subjectCodeEnd) + 1);
        searchMap.put("searchFeild", searchFeild);
        searchMap.put("year", "0".equals(year) ? null : year);
        searchMap.put("periodStart", periodStart);
        searchMap.put("periodEnd", periodEnd);
        searchMap.put("bookId", bookId);
        LOGGER.debug("call searchMap {}", searchMap);
        return searchMap;
    }

    /**
     * 数据库查询
     *
     * @param map
     * @return
     */
    private Long getValueBySql(Map<String, Object> map) {
        Long value = subjectBalanceExtendMapper.getExpressCellValueWithBook(map);
        LOGGER.debug("call value {}", value);
        return value == null ? 0 : value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        LOGGER.debug("while call {}", cell);
        try {
            Map<String,Object> searchMap = searchMap();
            map.put(key, getValueBySql(searchMap));
        } catch (Exception e) {
            e.printStackTrace();
            map.put(key, 0);
        } finally {
            latch.countDown();
        }
    }

//    public static void main(String[] args) {
//
//        NewExpressionThread thread = new NewExpressionThread();
//        thread.cell = "ACCT(\"6001\",\"SY\",\"\",0,0,0,\"\")";
//        thread.currentPeriod = 10;
//        thread.bookId = 59L;
//        thread.searchMap();
//    }
}
