<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false">
		<table id="voucherCashDg"></table>
	</div>
</div>

<div id="voucherCashMenu1" style="padding:2px 5px;">
	<table cellpadding="0" cellspacing="0" style="width:600px;">
		<tr>
		    <td width="20%">现金流量科目：</td>
		    <td id="voucherCashSubject" width="60%">
		    </td>
		    <td width="20%"><a id="voucherCashSubmit" href="javascript:void(0)" class="easyui-linkbutton" style="width: 90px;">确定</a></td>
		</tr>
		<tr>
		    <td width="20%">币别：</td>
		    <td width="60%">
			    ${moneyCode?default('')}-${moneyName?default('')}
		    </td>
		    <td width="20%"><a id="voucherCashReject" href="javascript:void(0)" class="easyui-linkbutton" style="width: 90px;">取消</a></td>
		</tr>
		<tr>
		    <td width="20%">本位币金额：</td>
		    <td id="voucherCashMoney" width="60%">
		    </td>
		    <td width="20%"></td>
		</tr>
	</table>
</div>

<script type="text/javascript">
    $(function(){
        VoucherCash.init();
    });
</script>