<style>

    #init_data_table_container .datagrid-row-selected {
        background: #FFFFFF;
        color: #333;
    }

    #init_data_table_container .init-data-tip-text {
        position: absolute;
        z-index: 999;
        right: 18px;
        top: 30px;
        height: 24px;
        line-height: 24px;
        background: #F5F5F5;
    }

</style>

<div id="init_data_table_container">

    <div class="init-data-tip-text" style="border:1px dashed #FF0000;padding-left:5px;color: #ff0000;">
        <i class="fa fa-meh-o fa-lg"></i> 当期期末结账，自动结束初始化。
    </div>


    <div id="init_data_toolbar" class="init-data-tip-text" style="background: #F5F5F5;display: none;right: 228px;">

        <input id="search_subject_code" class="easyui-textbox" style="width: 260px;height: 28px;" data-options="
            prompt: '科目代码或科目名称',
            icons: [{
                iconCls:'icon-remove',
                handler: function(e){
                    $(e.data.target).textbox('clear');
                }
            },{
                iconCls:'icon-search',
                handler: function(e){
                    Account_Subject_Init_Data.search();
                }
            }]"
                >
    </div>

    <table id="init_data_table"></table>

</div>

<script>
    $(function () {
        Account_Subject_Init_Data.init();
    })
</script>