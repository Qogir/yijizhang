<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'east'" style="width:120px;overflow: hidden;">
        <div style="width:120px;text-align: center;">
            <div style="margin:20px 0;"></div>
            <a id="commonValueAdd" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="width: 80%">新增</a>
            <div style="margin:10px 0;"></div>
            <a id="commonValueEdit" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" style="width: 80%">修改</a>
            <div style="margin:10px 0;"></div>
            <a id="commonValueDelete" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" style="width: 80%">删除</a>
        </div>
    </div>
	<div data-options="region:'south'" style="text-align:right;height:50px;line-height:48px;overflow:hidden;">
		<a id="commonValueSubmit" href="javascript:void(0)" class="easyui-linkbutton" style="width: 90px;">确定</a>
		<a id="commonValueReject" href="javascript:void(0)" class="easyui-linkbutton" style="width: 90px;">取消</a>
	</div>
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'south',border:false" style="height:48px;overflow: hidden;">
				<form id="commonValueFm" method="post">
					<input type="hidden" id="commonValueId" name="id" />
					<input type="hidden" name="typeId" value="${type?default('')}" />
				 	<table cellpadding="5">
						<tr>
							<td>类别名称：</td>
							<td><input id="commonValueShowValue" class="easyui-textbox" type="text" name="showValue" data-options="disabled:true,required:true,validType:'voucherMaxLength[20]'"></input></td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'center',border:false">
				<table id="commonValueDg"></table>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
    $(function () {
        CompanyCommonValue.init('${type?default('')}','${winTitle?default('')}');
    });
</script>