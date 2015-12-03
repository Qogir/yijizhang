package cn.ahyc.yjz.dto;

/**
 * Created by Joey Yan on 15-10-15.
 */
public enum BalanceSheetType {

    QCYE(".C", ""), //期初余额
    QCJFYE(".JC", "initial_debit_balance"), //期初借方余额
    QCDFYE(".DC", "initial_credit_balance"), //期初贷方余额
    JFFSE(".JF", "period_debit_occur"), //借方发生额
    DFFSE(".DF", "period_credit_occur"), //贷方发生额
    JFLJFSE(".JL", "year_debit_occur"), //借方累计发生额
    DFLJFSE(".DL", "year_credit_occur"), //贷方累计发生额
    QMYE("", ""), //期末余额
    QMJFYE(".JY", "terminal_debit_balance"), //期末借方余额
    QMDFYE(".DY", "terminal_credit_balance"), //期末贷方余额
    SYLKMSJFSE(".SY", ""), //损益类科目实际发生额
    SYLKMBNLJFSE(".SL", ""); //损益类科目本年累计发生额

    // 成员变量
    private String type;
    private String columnName;

    private BalanceSheetType(String type, String columnName) {
        this.type = type;
        this.columnName = columnName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
