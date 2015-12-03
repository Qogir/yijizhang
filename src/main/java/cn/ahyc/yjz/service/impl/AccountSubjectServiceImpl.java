package cn.ahyc.yjz.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.ahyc.yjz.mapper.extend.VoucherDetailExtendMapper;
import cn.ahyc.yjz.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ahyc.yjz.mapper.extend.AccountSubjectExtendMapper;
import cn.ahyc.yjz.mapper.extend.PeriodExtendMapper;
import cn.ahyc.yjz.mapper.extend.SubjectBalanceExtendMapper;
import cn.ahyc.yjz.service.AccountSubjectService;

/**
 * Created by Joey Yan on 15-9-24.
 */
@Service
public class AccountSubjectServiceImpl implements AccountSubjectService {

    @Autowired
    private AccountSubjectExtendMapper accountSubjectMapper;

    @Autowired
    private PeriodExtendMapper periodExtendMapper;

    @Autowired
    private SubjectBalanceExtendMapper subjectBalanceExtendMapper;

    @Autowired
    private VoucherDetailExtendMapper voucherDetailExtendMapper;

    private final int first_level_subject_len = 4;

    private Logger logger = LoggerFactory.getLogger(AccountSubjectServiceImpl.class);

    @Override
    public List<AccountSubject> getCategoriesByCode(Long subjectCode, Long bookId) {
        AccountSubjectExample example = new AccountSubjectExample();

        AccountSubjectExample.Criteria criteria = example.createCriteria();
        criteria.andParentSubjectCodeEqualTo(subjectCode);
        criteria.andBookIdEqualTo(bookId);

        return accountSubjectMapper.selectByExample(example);
    }

    @Override
    public List<Map> getSubjectsByCategoryId(Long categoryId, Long bookId) {

        //获取父级.
        AccountSubject parentSubject = accountSubjectMapper.selectByPrimaryKey(categoryId);

        Map map = new HashMap();
        map.put("parent_subject_code", parentSubject.getSubjectCode());
        map.put("bookId", bookId);

        List<Map> subjects = accountSubjectMapper.getSubjectsByCategoryId(map);
        subjects = this.setChildrenSubject(subjects, map, "", "");

        map.put("id", -1);
        map.put("text", "无");
        map.put("level", -1);
        map.put("next_level_length", first_level_subject_len - 1);
        map.put("subject_name", "无");
        map.put("subject_code", subjects.get(0).get("subject_code").toString().substring(0, 1));

        //添加第一级
        List<Map> new_subjects = new ArrayList<Map>();
        new_subjects.add(map);
        new_subjects.addAll(subjects);

        return new_subjects;
    }

    private List<Map> setChildrenSubject(List<Map> subjects, Map map, String categoryDatailSubjectCode, String categoryDatailSubjectName) {

        for (Map sub : subjects) {

            String cdsc = categoryDatailSubjectCode;
            String cdsn = categoryDatailSubjectName;

            if (!StringUtils.isEmpty(categoryDatailSubjectCode)) {
                sub.put("category_datail_subject_code", categoryDatailSubjectCode);
                sub.put("category_datail_subject_name", categoryDatailSubjectName);
            } else {
                cdsc = sub.get("category_datail_subject_code").toString();
                cdsn = sub.get("category_datail_subject_name").toString();
            }

            String state = sub.get("state").toString();
            if ("closed".equals(state)) {
                map.put("parent_subject_code", sub.get("subject_code").toString());
                List<Map> children = accountSubjectMapper.getChildrenSubjectsByCategoryId(map);
                sub.put("children", children);
                setChildrenSubject(children, map, cdsc, cdsn);
            } else {
                sub.put("isLeaf", true);
            }

        }

        return subjects;
    }


    @Override
    public AccountSubject getSubjectById(Long subjectId) {
        return accountSubjectMapper.selectByPrimaryKey(subjectId.longValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editAccountSubject(AccountSubject accountSubject, Long parentSubjectCodeBack, Long parentSubjectCode, Period period) throws Exception {

        if (parentSubjectCode == -1) {
            parentSubjectCode = parentSubjectCodeBack;
        }

        accountSubject.setParentSubjectCode(parentSubjectCode);
        accountSubject.setBaseFlag(1);
        Date now = new Date();
        accountSubject.setModifyTime(now);

        if (accountSubject.getId() == -1) {

            //检查新增科目代码是否有重复
            AccountSubjectExample example2 = new AccountSubjectExample();
            AccountSubjectExample.Criteria criteria2 = example2.createCriteria();
            criteria2.andSubjectCodeEqualTo(accountSubject.getSubjectCode());
            criteria2.andBookIdEqualTo(period.getBookId());
            List<AccountSubject> accountSubjects_exists = accountSubjectMapper.selectByExample(example2);
            if (!accountSubjects_exists.isEmpty()) {
                throw new Exception("科目代码重复，请检查后重新保存.");
            }


            //子级添加子级需把该子级关联凭证移到子级，初始化数据重新计算。
            AccountSubjectExample example = new AccountSubjectExample();
            AccountSubjectExample.Criteria criteria = example.createCriteria();
            criteria.andParentSubjectCodeEqualTo(parentSubjectCode);
            criteria.andBookIdEqualTo(accountSubject.getBookId());
            List<AccountSubject> accountSubjectChildren = accountSubjectMapper.selectByExample(example);

            //需要处理业务。
            if (accountSubjectChildren.isEmpty()) {
                if (parentSubjectCode > 0) { //非会计科目小分类
                    AccountSubjectExample example1 = new AccountSubjectExample();
                    AccountSubjectExample.Criteria criteria1 = example1.createCriteria();
                    criteria1.andSubjectCodeEqualTo(parentSubjectCode);
                    criteria1.andBookIdEqualTo(accountSubject.getBookId());
                    List<AccountSubject> accountSubjectParent = accountSubjectMapper.selectByExample(example1);

                    if (!accountSubjectParent.isEmpty()) {
                        AccountSubject as = accountSubjectParent.get(0);
                        //父级初始数据移给子级
                        accountSubject.setInitialLeft(as.getInitialLeft());
                        accountSubject.setTotalCredit(as.getTotalCredit());
                        accountSubject.setTotalDebit(as.getTotalDebit());
                        accountSubject.setYearOccurAmount(as.getYearOccurAmount());
                        //父级关联的凭证移到子级
                        Map param = new HashMap<>();
                        param.put("periodId", period.getId());
                        param.put("oldSubjectCode", as.getSubjectCode());
                        param.put("newSubjectCode", accountSubject.getSubjectCode());
                        voucherDetailExtendMapper.updateToNewSubjectCode(param);
                    }

                }
            }

            accountSubject.setId(null);
            accountSubjectMapper.insertSelective(accountSubject);

            if (accountSubjectChildren.isEmpty()) {

                //程家瑞让我调更新科目余额表.
                final Long _periodId = period.getId();
                new Thread() {

                    @Override
                    public void run() {
                        try {
                            subjectBalanceExtendMapper.insertOrUpdateSubjectBalance(_periodId);
                            subjectBalanceExtendMapper.collectSubjectBalance(_periodId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }.start();
            }

        } else {

            //检查修改科目代码是否有重复
            AccountSubjectExample example2 = new AccountSubjectExample();
            AccountSubjectExample.Criteria criteria2 = example2.createCriteria();
            criteria2.andSubjectCodeEqualTo(accountSubject.getSubjectCode());
            criteria2.andBookIdEqualTo(period.getBookId());
            criteria2.andIdNotEqualTo(accountSubject.getId());
            List<AccountSubject> accountSubjects_exists = accountSubjectMapper.selectByExample(example2);
            if (!accountSubjects_exists.isEmpty()) {
                throw new Exception("科目代码重复，请检查后重新保存.");
            }

            accountSubject.setCreateTime(now);
            accountSubjectMapper.updateByPrimaryKeySelective(accountSubject);
        }

    }

    @Override
    public List<AccountSubject> getCategoriesByCategoryId(Long categoryId, Long bookId) {

        AccountSubject parent_accountSubject = accountSubjectMapper.selectByPrimaryKey(categoryId);

        AccountSubjectExample example = new AccountSubjectExample();
        AccountSubjectExample.Criteria criteria = example.createCriteria();
        criteria.andParentSubjectCodeEqualTo(parent_accountSubject.getSubjectCode());
        criteria.andBookIdEqualTo(bookId);
        return accountSubjectMapper.selectByExample(example);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long subjectId) throws Exception {
        this.accountSubjectMapper.deleteByPrimaryKey(subjectId);
    }

    @Override
    public Map allSubjectTreeData(Period period, String keyword) {

        Long bookId = period.getBookId();

        //每次取数据之前做汇总。
        try {
            calculate(bookId, null);
        } catch (Exception e) {
            logger.error("汇总报错");
            e.printStackTrace();
        }

        AccountSubjectExtExample example = new AccountSubjectExtExample();
        AccountSubjectExtExample.Criteria criteria = example.createCriteria();

        criteria.andBookIdEqualTo(bookId);
        criteria.andSubjectCodeGreaterThan(0l);
        example.setOrderByClause("CAST(subject_code AS CHAR)");

        /**
         * 关键字查询.
         */
        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword.trim();
            try {
                Long kw = Long.parseLong(keyword);
                example.andSubjectCodeLike(keyword.concat("%"));
            } catch (Exception e) {
                criteria.andSubjectNameLike("%".concat(keyword).concat("%"));
            }
        }

        //损益类会计科目父级代码.
        Map map = new HashMap();
        map.put("bookId", bookId);
        Map creaseCode_map = accountSubjectMapper.getSubjectCodeByRoot(map);
        String code = creaseCode_map.get("subject_code").toString();
        if (!StringUtils.isEmpty(code)) {
            code = code.substring(0, 1);
        }

        map.put("accountSubjects", accountSubjectMapper.selectByExample(example));
        map.put("creaseCode", code);
        map.put("isFirstPeriod", false);

        //是否是该账套第一期
        PeriodExample periodExample = new PeriodExample();
        PeriodExample.Criteria periodCri = periodExample.createCriteria();
        periodCri.andBookIdEqualTo(bookId);
        periodExample.setOrderByClause("current_period ASC");

        List<Period> periods = periodExtendMapper.selectByExample(periodExample);
        if (periods.get(0).getCurrentPeriod() == period.getCurrentPeriod()) {
            map.put("isFirstPeriod", true);
        }

        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initDataEdit(AccountSubject accountSubject, Long periodId) throws Exception {

        if (accountSubject.getTotalDebit() != null && BigDecimal.ZERO.compareTo(accountSubject.getTotalDebit()) == 0)
            accountSubject.setTotalDebit(null);
        if (accountSubject.getTotalCredit() != null && BigDecimal.ZERO.compareTo(accountSubject.getTotalCredit()) == 0)
            accountSubject.setTotalCredit(null);
        if (accountSubject.getYearOccurAmount() != null && BigDecimal.ZERO.compareTo(accountSubject.getYearOccurAmount()) == 0)
            accountSubject.setYearOccurAmount(null);
        if (accountSubject.getInitialLeft() != null && BigDecimal.ZERO.compareTo(accountSubject.getInitialLeft()) == 0)
            accountSubject.setInitialLeft(null);

        accountSubjectMapper.updateByPrimaryKey(accountSubject);

        //程家瑞让我调更新科目余额表.
        final Long _periodId = periodId;
        new Thread() {

            @Override
            public void run() {
                try {
                    subjectBalanceExtendMapper.insertOrUpdateSubjectBalance(_periodId);
                    subjectBalanceExtendMapper.collectSubjectBalance(_periodId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculate(Long bookId, Long category_subject_code) throws Exception {

        List<Map> childSums = accountSubjectMapper.getLastChildSum(bookId);
        calculateChildren(childSums, bookId);

    }

    @Override
    public Map balance(Long bookId, Long category_subject_code) {

        Map result = new HashMap();
        result.put("isBalance", false);

        Map initLeftBalance = accountSubjectMapper.getInitLeftBalance(bookId);
        Map totalBalance = accountSubjectMapper.getTotalBalance(bookId);
        result.put("initLeftBalance", initLeftBalance);
        result.put("totalBalance", totalBalance);

        //期初余额借贷差值
        BigDecimal initLeftBalance_dValue = (BigDecimal) initLeftBalance.get("dValue");
        //本年累计借贷差值
        BigDecimal totalBalance_dValue = (BigDecimal) totalBalance.get("dValue");

        if (initLeftBalance_dValue.compareTo(BigDecimal.ZERO) == 0 && totalBalance_dValue.compareTo(BigDecimal.ZERO) == 0) {
            result.put("isBalance", true);
        }

        return result;
    }

    private void calculateChildren(List<Map> sums, Long bookId) {

        Set<Long> parent_code_set = new HashSet<Long>();

        for (Map map : sums) {

            AccountSubject accountSubject = new AccountSubject();
            boolean isUpdate = false;

            Long parent_subject_code = (Long) map.get("parent_subject_code");
            Long subject_code = (Long) map.get("subject_code");
            BigDecimal sum_total_debit = (BigDecimal) map.getOrDefault("sum_total_debit", BigDecimal.ZERO);
            BigDecimal sum_total_credit = (BigDecimal) map.getOrDefault("sum_total_credit", BigDecimal.ZERO);
            BigDecimal sum_initial_left = (BigDecimal) map.getOrDefault("sum_initial_left", BigDecimal.ZERO);
            BigDecimal sum_year_occur_amount = (BigDecimal) map.getOrDefault("sum_year_occur_amount", BigDecimal.ZERO);

            if (sum_total_debit.compareTo((BigDecimal) map.getOrDefault("total_debit", BigDecimal.ZERO)) != 0) {
                isUpdate = true;
                accountSubject.setTotalDebit(sum_total_debit == null ? BigDecimal.ZERO : (BigDecimal) sum_total_debit);
            }
            if (sum_total_credit.compareTo((BigDecimal) map.getOrDefault("total_credit", BigDecimal.ZERO)) != 0) {
                isUpdate = true;
                accountSubject.setTotalCredit(sum_total_credit == null ? BigDecimal.ZERO : (BigDecimal) sum_total_credit);
            }
            if (sum_initial_left.compareTo((BigDecimal) map.getOrDefault("initial_left", BigDecimal.ZERO)) != 0) {
                isUpdate = true;
                accountSubject.setInitialLeft(sum_initial_left == null ? BigDecimal.ZERO : (BigDecimal) sum_initial_left);
            }
            if (sum_year_occur_amount.compareTo((BigDecimal) map.getOrDefault("year_occur_amount", BigDecimal.ZERO)) != 0) {
                isUpdate = true;
                accountSubject.setYearOccurAmount(sum_year_occur_amount == null ? BigDecimal.ZERO : (BigDecimal) sum_year_occur_amount);
            }

            //是否需要更新数据库。
            if (isUpdate) {
                AccountSubjectExample example = new AccountSubjectExample();
                AccountSubjectExample.Criteria criteria = example.createCriteria();
                criteria.andBookIdEqualTo(bookId);
                criteria.andSubjectCodeEqualTo(subject_code);

                accountSubjectMapper.updateByExampleSelective(accountSubject, example);
            }

            /**
             * 是否是会计科目父级。如果是父级科目，则该科目父级是负数。
             */
            if (parent_subject_code > 0) {
                parent_code_set.add(parent_subject_code);
            }

        }

        //递归计算父级节点.
        List<Long> parent_code_list = new ArrayList<Long>(parent_code_set);
        if (!parent_code_list.isEmpty()) {
            List<Map> childSums = accountSubjectMapper.getParentSum(parent_code_list, bookId);
            calculateChildren(childSums, bookId);
        }
    }

}
