package cn.ahyc.yjz.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class AccountSubjectVo implements Serializable {

    private List<AccountSubjectVo> children;
    private int subjectLength;
    private String state;
    private String text;
    private Long id;
    private Integer subjectCode;

    private String subjectName;

    private BigDecimal totalDebit;

    private BigDecimal totalCredit;

    private BigDecimal initialLeft;

    private BigDecimal yearOccurAmount;

    private Integer baseFlag;

    private String companyId;

    private static final long serialVersionUID = 1L;


    public int getSubjectLength() {
        return subjectLength;
    }

    public void setSubjectLength(int subjectLength) {
        this.subjectLength = subjectLength;
    }

    public List<AccountSubjectVo> getChildren() {
        return children;
    }

    public void setChildren(List<AccountSubjectVo> children) {
        this.children = children;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(Integer subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public BigDecimal getInitialLeft() {
        return initialLeft;
    }

    public void setInitialLeft(BigDecimal initialLeft) {
        this.initialLeft = initialLeft;
    }

    public BigDecimal getYearOccurAmount() {
        return yearOccurAmount;
    }

    public void setYearOccurAmount(BigDecimal yearOccurAmount) {
        this.yearOccurAmount = yearOccurAmount;
    }

    public Integer getBaseFlag() {
        return baseFlag;
    }

    public void setBaseFlag(Integer baseFlag) {
        this.baseFlag = baseFlag;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}