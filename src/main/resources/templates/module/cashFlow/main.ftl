<style>
    #cashflow_container .datagrid-row-selected {
        background: #FFFFFF;
        color: #333333;
    }
</style>
<div id="cashflow_container" class="easyui-panel" data-options="fit:true,border:false">

    <input id="currentPeriod_hidden" type="hidden" value="${period.currentPeriod?default('')}">

    <div style="padding:2px 5px;background: #F5F5F5;">
        <i class="fa fa-hand-o-right fa-lg"></i> 当前页面会计期间：
        <input id="period" name="period" style="width: 60px;">

        <a id="exportToExcel" href="#" class="easyui-linkbutton" plain="true"><i class="fa fa-file-excel-o fa-lg"></i>
            导出</a>
    </div>

    <table id="cashflow_table"></table>

</div>

<script>

    $(function () {
        Cash_Flow.init();
    })

</script>