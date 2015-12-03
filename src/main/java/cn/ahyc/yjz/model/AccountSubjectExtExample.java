package cn.ahyc.yjz.model;

/**
 * Created by Joey Yan on 15-10-8.
 */
public class AccountSubjectExtExample extends AccountSubjectExample {

    public AccountSubjectExample.Criteria andSubjectCodeLike(String value) {

        AccountSubjectExample.Criteria criteria = super.getOredCriteria().get(0);
        criteria.addCriterion("subject_code like", value, "subjectCode");
        return criteria;
    }

}
