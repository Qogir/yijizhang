<div id="voucherLayout" class="easyui-layout" data-options="fit:true">
	<div id="voucherCenter" data-options="region:'center'">
		<form id="voucherFm" method="post">
			<input type="hidden" id="id" name="id" value="${voucher.id?default('')}"/>
			<input type="hidden" id="periodId" name="periodId" value="${voucher.periodId?default('')}"/>
			<table id="voucherDg" style="display: none;"></table>
		</form>
	</div>
</div>

<div id="voucherMenu" style="padding:2px 5px;display: none;">
    <table cellpadding="0" cellspacing="0">
        <tr>
            <td>
                <a id="voucherAdd" href="javascript:void(0)" class="easyui-splitbutton" data-options="menu:'#mm1',plain:true"><i class="fa fa-edit fa-lg"></i>&#8194;新增</a>
			<#if !(currentPeriod??&&currentPeriod!=sessionPeriod)>
                <a id="voucherSave" href="javascript:void(0)" class="easyui-splitbutton" data-options="menu:'#mm2',plain:true"><i class="fa fa-save fa-lg"></i>&#8194;保存</a>
			</#if>
                <a id="voucherReject" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-reply fa-lg"></i>&#8194;取消修改</a>
            </td>
            <td>
                <div class="datagrid-btn-separator"></div>
            </td>
            <td>
                <a id="voucherTempSave" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-save fa-lg"></i>&#8194;保存为模式凭证</a>
                <a id="voucherTempAdd" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-edit fa-lg"></i>&#8194;从模式凭证新增</a>
            </td>
            <td>
                <div class="datagrid-btn-separator"></div>
            </td>
            <td>
                <a id="voucherAppend" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-indent fa-lg"></i>&#8194;插入行</a>
                <a id="voucherRemoveit" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-outdent fa-lg"></i>&#8194;删除行</a>
            </td>
            <td>
                <div class="datagrid-btn-separator"></div>
            </td>
            <td>
                <a id="voucherSubjectDetail" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-table fa-lg"></i>&#8194;明细</a>
			<#if !(currentPeriod??&&currentPeriod!=sessionPeriod)>
                <a id="voucherSubjectBalance" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-table fa-lg"></i>&#8194;科目余额</a>
			</#if>
                <a id="voucherexportToExcel" href="#" class="easyui-linkbutton" plain="true"><i class="fa fa-file-excel-o fa-lg"></i> 导出</a>
                <a id="voucherHelp" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-question-circle fa-lg"></i>&#8194;凭证制作说明</a>
            </td>
        </tr>
    </table>
</div>

<div id="mm1" style="width:100px;display: none;">
    <div id="voucherMm1Add">新增</div>
    <div id="voucherTempMm1Add">从模式凭证新增</div>
</div>
<div id="mm2" style="width:100px;display: none;">
    <div id="voucherMm2Save1">保存</div>
    <div id="voucherMm2Save2">保存并新增</div>
    <div id="voucherTempMm2Save">保存为模式凭证</div>
</div>

<div id="voucherDgTd" style="padding:10px;">
    <a id="voucherWordEdit" href="javascript:void(0)" class="button button-glow button-square button-tiny"><i class="fa fa-book fa-lg"></i></a>
    <select id="voucherWord" name="voucherWord" style="width:100px;"></select>字
    <input class="easyui-numberspinner" id="voucherNo" name="voucherNo" style="width:80px;" value="${voucher.voucherNo?default(voucherNo)}" data-options="required:true,min:1">号
    日期：<input type="text" id="voucherTime" name="voucherTime" value="${(voucher.voucherTime?string("yyyy-MM-dd"))?default(voucherTime)}" />
    第${currentPeriod?default(sessionPeriod)}期
    附单据<input class="easyui-numberspinner" name="billNum" style="width:80px;" value="${voucher.billNum?default('0')}" data-options="required:true,min:0">张
</div>


<script type="text/javascript">
	$(function () {
		Voucher.init('${voucherId?default('')}','${sessionPeriod}','${sessionBook}','${voucher.voucherWord?default('')}','${templateId?default('')}','${isreversal?default('')}');
	});
</script>