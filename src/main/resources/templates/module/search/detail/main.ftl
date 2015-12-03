<div id="search_detail_container" class="easyui-panel" data-options="fit:true,border:false">
    <div id="tb" class="tabs-header tabs-header-noborder" style="height: 28px;line-height: 28px;padding: 0 5px;">
        <form id="formSubmit" action="#" method="post">
            <input id="current_hidden" type="hidden" value="${currentPeriod?default()}">
            <input id="subjectCode_hidden" type="hidden" value="${subjectCode?default('')}">
            <input id="subjectName_detail_hidden" type="hidden" value="${subjectName?default('')}">

            <div class="datagrid-btn-separator"></div>
            <a id="searchDetail" href="#" class="easyui-linkbutton" plain="true"><i class="fa fa-filter fa-lg"></i>
                查询</a>

            <a id="exportToExcel" href="#" class="easyui-linkbutton" plain="true"><i
                    class="fa fa-file-excel-o fa-lg"></i> 导出</a>


            <span id="subjectName_value" name="subjectName_value" style="width: 200px;"/></span>
            <div style="float: left;font-weight: 700;">
                <i class="fa fa-hand-o-right fa-lg"></i> 会计期间：
                <input id="startPeriod" name="startPeriod" class="easyui-numberspinner"
                       data-options="min:1,max:12,onChange: function(value){$('#vv').text(value);}" style="width: 50px;"/>
                至
                <input id="endPeriod" name="endPeriod" class="easyui-numberspinner"
                       data-options="min:1,max:12,onChange: function(value){$('#vv').text(value);}" style="width: 50px;"/>
            </div>
            <div style="float: left;font-weight: 700;">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <i class="fa fa-hand-o-right fa-lg"></i>科目代码：
                <input id="subjectCode" name="subjectCode" style="width: 110px;"/>
            </div>
            <div style="float: right;font-weight: 700;">
                <i class="fa fa-hand-o-right fa-lg"></i><font color="#ff0000"> 双击结果中的凭证可弹出详细凭证信息</font>
            </div>
        </form>
    </div>

    <table id="detail_data_table"></table>

</div>

<script>

    $(function () {
        Search_Detail.init();
    })

</script>