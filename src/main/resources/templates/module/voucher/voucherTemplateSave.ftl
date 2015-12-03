<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'south',border:false" style="text-align:left;height:50px;line-height:48px;overflow:hidden;">
		<a id="voucherTemplateSaveSubmit" href="javascript:void(0)" class="easyui-linkbutton" style="width: 90px;">确定</a>
		<a id="voucherTemplateSaveReject" href="javascript:void(0)" class="easyui-linkbutton" style="width: 90px;">取消</a>
	</div>
	<div data-options="region:'center',border:false">
		<form id="voucherTemplateSaveFm" method="post">
			<input type="hidden" name="voucherWord" value="${voucherWord?default('')}" />
			<input type="hidden" name="checkFlag" value="checkFlag" />
			<table cellpadding="5">
				<tr>
					<td>名称：</td>
					<td><input class="easyui-textbox" type="text" id="voucherTemplateSaveName" name="name" value="${name?default('')}" data-options="required:true,validType:'voucherMaxLength[20]'"></input></td>
				</tr>
				<tr>	
					<td>类别：</td>
					<td><select id="voucherTemplateSaveTypeName" name="typeName" style="width:100px;"></select>
						<a id="voucherTemplateSaveTypeNameEdit" href="javascript:void(0)" class="button button-glow button-square button-tiny"><i class="fa fa-book"></i></a>
					</td>
				</tr>
			</table>
			<div style="margin:20px 0;"></div>
			<div style="padding:5px;">
				<div class="easyui-panel" style="position:relative;width:300px;height:160px;overflow:hidden;">
					<input id="SSN1" type="checkbox" name="wordFlag" value="1" checked="checked" />
					<label for="SSN1">凭证字</label><br/>
					<input id="SSN2" type="checkbox" name="numFlag" value="1" checked="checked" />
					<label for="SSN2">附件数</label><br/>
					<input id="SSN3" type="checkbox" name="summaryFlag" value="1" checked="checked" />
					<label for="SSN3">摘要</label><br/>
					<input id="SSN4" type="checkbox" name="subjectFlag" value="1" checked="checked" />
					<label for="SSN4">科目代码</label><br/>
					<input id="SSN5" type="checkbox" name="dFlag" value="1" />
					<label for="SSN5">借方</label><br/>
					<input id="SSN6" type="checkbox" name="crFlag" value="1" />
					<label for="SSN6">贷方</label>
				</div>
			</div>
		</form>
	</div>
</div>

<script type="text/javascript">
    $(function(){
        VoucherTemplate.saveInit();
    });
</script>