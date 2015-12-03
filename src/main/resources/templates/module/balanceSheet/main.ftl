<style>
    #balance_sheet_container .datagrid-row-selected {
        background: #FFFFFF;
        color: #333333;
    }
</style>
<div id="balance_sheet_container" class="easyui-panel" data-options="fit:true,border:false">

    <input id="currentPeriod_hidden" type="hidden" value="${period.id?default('')}">

    <div style="padding:2px 5px;background: #F5F5F5;">
        <i class="fa fa-hand-o-right fa-lg"></i> 当前页面会计期间：
        <input id="period" name="period" style="width: 60px;">

        <a id="exportToExcel" href="#" class="easyui-linkbutton" plain="true"><i class="fa fa-file-excel-o fa-lg"></i> 导出</a>

        <div style="float: right;"><a id="tip" href="#"><i class="fa fa-info-circle"></i> 公式说明 </a></div>
    </div>

    <div id="tip_content" style="display: none;">
        <p>
            公式格式： <span style="font-weight: 700;">e.g. <1001:1002>.C@1</span> <br/>
            注释：<br>
        <ol>
            <li>'<1001:1002>'内部是科目代码，可单独出现或区间方式出现。</li>
            <li>'.C'表示期初余额，可参考如下对照表。</li>
            <li>'@1'表示相应账套起始期间，默认不加表示当前期间。</li>
        </ol>
        对照表：
        </p>
        <ol>
            <li>[.C] 期初余额</li>
            <li>[.JC] 期初借方余额</li>
            <li>[.DC] 期初贷方余额</li>
            <li>[.JF] 借方发生额</li>
            <li>[.DF] 贷方发生额</li>
            <li>[.JL] 借方累计发生额</li>
            <li>[.DL] 贷方累计发生额</li>
            <li>[] 期末余额</li>
            <li>[.JY] 期末借方余额</li>
            <li>[.DY] 期末贷方余额</li>
            <li>[.SY] 损益类科目实际发生额</li>
            <li>[.SL] 损益类科目本年累计发生额</li>
        </ol>
    </div>



<#list balanceSheets as balanceSheet>
    <div style="float: left;width: 50%;">
        <table id="data_table_${balanceSheet.id?default()}" code="${balanceSheet.code?default()}"></table>
    </div>
</#list>

</div>

<script>

    $(function () {
        Balance_Sheet.init();
    })

</script>