<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'">
		<table id="trialbalanceListDg"></table>
	</div>
</div>

<div id="trialbalanceListMenu" style="padding:2px 5px;">
	<table cellpadding="0" cellspacing="0">
		<tr>
		    <td>
			    <a id="trialbalanceListSearch" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-filter fa-lg"></i>&#8194;过滤</a>
		    </td>
		    <td>
			    <a id="trialbalanceListRefresh" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-refresh fa-lg"></i>&#8194;刷新</a>
		    </td>
		    <td>
                <a id="exportToExcel" href="#" class="easyui-linkbutton" plain="true"><i class="fa fa-file-excel-o fa-lg"></i> 导出</a>
		    </td>
		    <td><div class="datagrid-btn-separator"></div></td>
		    <td><div id="balanceResult" style="padding:0px 100px"></div></td>
		</tr>
	</table>    
</div>

<script type="text/javascript">
	$(function () {
		TrialBalance.init('${currentPeriod?default('')}','${level?default('')}');
	});
</script>