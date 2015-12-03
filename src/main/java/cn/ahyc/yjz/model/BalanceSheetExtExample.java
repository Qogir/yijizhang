package cn.ahyc.yjz.model;

import java.util.List;

/**
 * Created by Joey Yan on 15-10-16.
 */
public class BalanceSheetExtExample extends BalanceSheetExample {

    public BalanceSheetExtExample.Criteria andCodeFLike(String value) {
        BalanceSheetExtExample.Criteria criteria = null;
        List<BalanceSheetExtExample.Criteria> criterias = super.getOredCriteria();
        if (criterias.isEmpty()) {
            criteria = super.createCriteria();
        } else {
            criteria = super.getOredCriteria().get(0);
        }
        criteria.addCriterion("code like", value, "code");
        return criteria;
    }

}
