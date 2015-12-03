<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'south'" style="text-align:left;height:50px;line-height:48px;overflow:hidden;">
		<a id="balanceListSubmit" href="javascript:void(0)" class="easyui-linkbutton" style="width: 90px;">确定</a>
		<a id="balanceListReject" href="javascript:void(0)" class="easyui-linkbutton" style="width: 90px;">取消</a>
	</div>
	<div data-options="region:'center',border:false">
		<form id="balanceListFm" method="post">
			<table cellpadding="5">
				<tr>
					<td>会计期间：</td>
					<td><input class="easyui-numberspinner" id="periodFrom" name="periodFrom" style="width:80px;" value="${periodFrom?default('1')}" data-options="min:1,max:12"></input></td>
					<td>至：</td>
					<td><input class="easyui-numberspinner" id="periodTo" name="periodTo" style="width:80px;" value="${periodTo?default('1')}" data-options="min:1,max:12"></input></td>
				</tr>
				<tr>	
					<td>科目级别：</td>
					<td colspan="3"><input class="easyui-numberspinner" id="level" name="level" style="width:80px;" value="${level?default('1')}" data-options="required:true,min:1"></input></td>
				</tr>
				<tr>	
					<td>会计科目：</td>
					<td colspan="3"><input class="easyui-numberbox" id="subjectCodeFrom" name="subjectCodeFrom" style="width:80px;" <#if subjectCodeFrom??>value="${subjectCodeFrom?c}"</#if> data-options="min:0,precision:0,validType:'voucherMaxLength[30]'"></input></td>
				</tr>
				<tr>	
					<td>至：</td>
					<td colspan="3"><input class="easyui-numberbox" id="subjectCodeTo" name="subjectCodeTo" style="width:80px;" <#if subjectCodeTo??>value="${subjectCodeTo?c}"</#if> data-options="min:0,precision:0,validType:'voucherMaxLength[30]'"></input></td>
				</tr>
				<tr>	
					<td colspan="4">
					<#if valueNotNull??>
					<input type="checkbox" id="valueNotNull" name="valueNotNull" value="1" checked="checked" />
					</#if>
					<#if !valueNotNull??>
					<input type="checkbox" id="valueNotNull" name="valueNotNull" value="1" />
					</#if>
					<label for="valueNotNull">包括余额和本期发生额均为零的科目</label>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>

<script type="text/javascript">
	$(function () {
		SubjectBalance.searchInit('${periodFrom?default('')}','${periodTo?default('')}','${level?default('')}',<#if subjectCodeFrom??>'${subjectCodeFrom?c}'<#else>''</#if>,<#if subjectCodeTo??>'${subjectCodeTo?c}'<#else>''</#if>,'${valueNotNull?default('')}');
	});
</script>