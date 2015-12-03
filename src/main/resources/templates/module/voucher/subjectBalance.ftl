<div class="easyui-layout" data-options="fit:true">
	
	<div data-options="region:'north'" style="height:50px">
		<table class="chart-btn-tb" style="width:100%;margin:0;text-align: left;">
			<tr>
				<td>会计科目：</td>
				<td>会计期间：${currentPeriod?default('')}期</td>
				<td>币种：${moneyCode?default('')}</td>
			</tr>
			<tr>
				<td colspan="3">${subjectName?default('')}</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center'">
		<table id="voucherBalanceDg"></table>
	</div>
</div>

<script type="text/javascript">
	$(function () {
		SubjectBalance.voucherInit('${subjectCode?default('')}');
	});
</script>