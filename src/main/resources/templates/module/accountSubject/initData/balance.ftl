<table style="width: 100%;padding: 5px;">

    <tr>
        <th>试算项</th>
        <th>借方</th>
        <th>贷方</th>
        <th>差额</th>
    </tr>

    <tr>
        <td>期初余额</td>
        <td>${balance.initLeftBalance.sum_total_debit?c}</td>
        <td>${balance.initLeftBalance.sum_total_credit?c}</td>
        <td>${balance.initLeftBalance.dValue?c}</td>
    </tr>

    <tr>
        <td>本年累计</td>
        <td>${balance.totalBalance.sum_total_debit?c}</td>
        <td>${balance.totalBalance.sum_total_credit?c}</td>
        <td>${balance.totalBalance.dValue?c}</td>
    </tr>

    <tr>
        <td colspan="4">
            <br/>
            <i class="fa fa-bullhorn fa-2x"></i>
            当前初始化期间第 <span style="font-size: 20px;">${currentPeriod}</span>
            期：试算结果

        <#if balance.isBalance>
            <span style="font-weight: 700;color: green;">平衡</span>
        <#else>
            <span style="font-weight: 700;color: #ff0000;">不平衡</span>
        </#if>
        </td>
    </tr>

</table>

