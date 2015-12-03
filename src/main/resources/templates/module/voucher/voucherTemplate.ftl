<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'east'" style="width:120px;overflow: hidden;">
        <div style="width:120px;text-align: center;">
            <div style="margin:30px 0;"></div>
            <a id="templateClose" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" style="width: 80%;">关闭</a>
			
			<div style="margin:20px 0;"></div>
            <a id="templateAdd" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="width: 80%">新增</a>
            <div style="margin:5px 0;"></div>
            <a id="templateEdit" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" style="width: 80%">修改</a>
            <div style="margin:5px 0;"></div>
            <a id="templateRemove" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" style="width: 80%">删除</a>
            
            <div style="margin:30px 0;"></div>
            <a id="templateTypeEdit" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" style="width: 80%">编辑类别</a>
            <#--<div style="margin:5px 0;"></div>
            <a id="templateShow" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-tip'" style="width: 80%">显示模式</a>
        	-->
        </div>
    </div>
	<div data-options="region:'center',border:false">
		<table id="voucherTemplateDg"></table>
	</div>
	
</div>

<script type="text/javascript">
    $(function () {
        VoucherTemplate.init();
    });
</script>