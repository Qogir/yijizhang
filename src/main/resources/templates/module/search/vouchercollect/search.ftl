<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'south'" style="text-align:left;height:50px;line-height:48px;overflow:hidden;">
		<a id="coollectListSubmit" href="javascript:void(0)" class="easyui-linkbutton" style="width: 90px;">确定</a>
		<a id="coollectListReject" href="javascript:void(0)" class="easyui-linkbutton" style="width: 90px;">取消</a>
	</div>
	<div data-options="region:'center',border:false">
		<form id="coollectListFm" method="post">
			<table cellpadding="5">
				<tr>
					<td>日期：</td>
					<td><input type="text" id="startTime" name="startTime" value="${startTime?default(startTime)}"></input></td>
					<td>至：</td>
					<td><input type="text" id="endTime" name="endTime" value="${endTime?default(startTime)}"></input></td>
				</tr>
				<tr>
					<td>凭证字：</td>
					<td><select id="search_voucherWord" name="search_voucherWord" style="width:144px;"></select></td>
				</tr>
				<tr>	
					<td>凭证号：</td>
					<td><input class="easyui-numberspinner" id="voucherStartNo" name="voucherStartNo"   data-options="disabled:true,min:1"></input></td>
					<td>至：</td>
					<td><input class="easyui-numberspinner" id="voucherEndNo"   name="voucherEndNo"     data-options="disabled:true,min:1"></input></td>
				</tr>
			</table>
		</form>
	</div>
</div>

<script type="text/javascript">
	$(function () {
		VouchercollectSearch.searchInit();
	});
</script>