<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'">
		<table id="coollectListDg"></table>
	</div>
</div>

<div id="coollectListMenu" style="padding:2px 5px;">
	<table cellpadding="0" cellspacing="0">
		<tr>
		    <td>
			    <a id="coollectListSearch" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-filter fa-lg"></i>&#8194;过滤</a>
			    <a id="exportToExcel" href="#" class="easyui-linkbutton" plain="true"><i
                    class="fa fa-file-excel-o fa-lg"></i> 导出</a>
		    </td>
		</tr>
	</table>    
</div>

<script type="text/javascript">
	$(function () {
		VouchercollectSearch.init('${startTime?default('')}','${endTime?default('')}','${voucherWord?default('')}','${voucherStartNo?default('')}','${voucherEndNo?default('')}');
	});
</script>