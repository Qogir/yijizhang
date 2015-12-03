<div id="ledger" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'">
		<table id="ledgerDg"></table>
	</div>
</div>

<div id="ledgerMenu" style="padding:2px 5px;">
	<table cellpadding="0" cellspacing="0">
		<tr>
		    <td>
			    <a id="ledgerSearch" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-filter fa-lg"></i>&#8194;过滤</a>
		    </td>
		    <td>
			    <a id="ledgerRefresh" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-refresh fa-lg"></i>&#8194;刷新</a>
		    </td>
		    <td>
			    <a id="exportToExcel" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-file-excel-o fa-lg"></i>&#8194;导出</a>
		    </td>
		</tr>
	</table>    
</div>

<script type="text/javascript">
	$(function () {
		Ledger.init('${periodFrom?default('')}','${periodTo?default('')}','${level?default('')}',<#if subjectCodeFrom??>'${subjectCodeFrom?c}'<#else>''</#if>,<#if subjectCodeTo??>'${subjectCodeTo?c}'<#else>''</#if>,'${valueNotNull?default('')}');
	});
</script>