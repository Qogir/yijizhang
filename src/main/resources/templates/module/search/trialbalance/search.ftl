<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'south'" style="text-align:left;height:50px;line-height:48px;overflow:hidden;">
		<a id="trialbalanceListSubmit" href="javascript:void(0)" class="easyui-linkbutton" style="width: 90px;">确定</a>
		<a id="trialbalanceListReject" href="javascript:void(0)" class="easyui-linkbutton" style="width: 90px;">取消</a>
	</div>
	<div data-options="region:'center',border:false">
		<form id="trialbalanceListFm" method="post">
			<table cellpadding="5">
				<tr>
					<td>会计期间：</td>
					<td colspan="3"><input class="easyui-numberspinner" id="trialcurrentPeriod" name="currentPeriod" style="width:80px;" value="${currentPeriod?default('1')}" data-options="required:true,min:1,max:12"></input></td>
				</tr>
				<tr>	
					<td>科目级别：</td>
					<td colspan="3"><input class="easyui-numberspinner" id="triallevel" name="level" style="width:80px;" value="${level?default('1')}" data-options="required:true,min:1"></input></td>
				</tr>
			</table>
		</form>
	</div>
</div>

<script type="text/javascript">
	$(function () {
		TrialBalance.searchInit('${currentPeriod?default('')}','${level?default('')}');
	});
</script>