<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false">
		<form id="voucherTemplateFm" method="post">
			<input type="hidden" id="voucherTemplateId" name="id" value="${voucherTemplate.id?default('')}"/>
			<table id="voucherTemplateDetailDg"></table>
		</form>
	</div>
</div>

<div id="voucherTemplateMenu" style="padding:2px 5px;">
	<table cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<a id="templateAddAdd" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">新增</a>
		    </td>
		    <td>
				<a id="templateSave" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a>
		    </td>
		    <td>
				<a id="templateReject" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true">取消修改</a>
		    </td>
			<td>
				<div class="datagrid-btn-separator"></div>
			</td>
		    <td>
				<a id="templateAppend" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">插入行</a>
		    </td>
		    <td>
				<a id="templateRemoveit" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除行</a>
			</td>
		</tr>
	</table>
</div>

<div id="voucherTemplateTb" style="padding:10px;">
	名称：<input id="voucherTemplateName" class="easyui-textbox" type="text" name="name" value="${voucherTemplate.name?default('')}" data-options="required:true,validType:'voucherMaxLength[20]'"></input>
	类别：<select id="typeName" name="typeName" style="width:100px;"></select>
	<a id="typeNameEdit" href="javascript:void(0)" class="button button-glow button-square button-tiny"><i class="fa fa-book"></i></a>
	凭证字：<select id="voucherTemplateWord" name="voucherWord" style="width:100px;"></select>
	附单据：<input class="easyui-numberspinner" name="billNum" style="width:80px;" value="${voucherTemplate.billNum?default('0')}" data-options="min:0">
</div>

<div id="voucherTemAdd_win"></div>

<script type="text/javascript">
    $(function(){
        VoucherTemplate.addInit('${voucherTemplate.id?default('')}','${voucherTemplate.typeName?default('')}','${voucherTemplate.voucherWord?default('')}');
    });
</script>