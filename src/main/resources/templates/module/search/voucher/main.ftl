<div id="search_voucher_container" class="easyui-panel" data-options="fit:true,border:false">


    <input id="currentPeriod_hidden" type="hidden" value="${period.id?default()}">

    <div id="tb" style="height: 28px;line-height: 28px;padding: 0 5px;">

        <div style="float:left;">
            <a id="new" href="javascript:App.addTab('记账','voucher/main',true);" class="easyui-linkbutton"
               plain="true"><i class="fa fa-clone fa-lg"></i> 新增</a>
            <a id="edit" href="#" class="easyui-linkbutton" plain="true"><i class="fa fa-pencil-square-o fa-lg"></i> 修改</a>
            <a id="delete" href="#" class="easyui-linkbutton" plain="true"><i class="fa fa-times fa-lg"></i> 删除</a>
        </div>

        <div class="datagrid-btn-separator"></div>

        <div style="float:left;">
            <a id="reversal" href="#" class="easyui-linkbutton" plain="true"><i class="fa fa-retweet fa-lg"></i> 冲销</a>
            <a id="set" href="#" class="easyui-linkbutton" plain="true"><i class="fa fa-wrench fa-lg"></i> 整理</a>
        </div>


        <div class="datagrid-btn-separator"></div>

        <a id="search" href="#" class="easyui-linkbutton" plain="true"><i class="fa fa-filter fa-lg"></i> 查询</a>

        <div id="search_div" style="display: none;">
            <input id="keyword_input" class="easyui-textbox" style="width: 220px;height: 26px;" data-options="
                prompt: '关键字',
                icons: [{
                    iconCls:'icon-remove',
                    handler: function(e){
                        $(e.data.target).textbox('clear');
                    }
                },{
                    iconCls:'icon-search',
                    handler: function(e){
                        Search_Voucher.search();
                    }
                }]"
                    >
        </div>

        <#--<div class="datagrid-btn-separator"></div>-->

        <a id="exportToExcel" href="#" class="easyui-linkbutton" plain="true"><i class="fa fa-file-excel-o fa-lg"></i> 导出</a>

        <div style="float: right;color: #ff0000;font-weight: 700;">
            <i class="fa fa-hand-o-right fa-lg"></i> 当前页面会计期间：
            <input id="period" name="period" style="width: 60px;">
        </div>
    </div>

    <table id="data_table"></table>

</div>

<script>

    $(function () {
        Search_Voucher.init();
    })

</script>