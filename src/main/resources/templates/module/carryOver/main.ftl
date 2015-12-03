
<div id="carryOver_layout" class="easyui-layout"
	data-options="fit:true">
	<div data-options="region:'center'">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'west'"
				style="width: 180px; background-image: url('/public/img/jianzhang.png')"></div>
			<div data-options="region:'center'" style="padding: 25px;line-height: 220%;">
				<form id="carry_over" action="#" method="post">
					<div id="firstJsp">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<label style="font-size: 12px;">
							期末时，应将各损益类科目的余额转入“本年利润”科目，以反映企业在一个会计期间内实现的
							利润或亏损总额。 本指南将帮助你自动完成对损益类科目的结转，并生成一张自动转账凭证。
							<br/><br/>
							要生成转账凭证，请输入以下内容
							<br/>
							凭证摘要:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="text"	id="summary" name="summary" value="结转本期损益" style="width: 40%;" />
							<br/>
							凭证字:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input id="category_detail" name="voucherWord" style="width: 42%;"/>
						</label>
					</div>
					<#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />-->
				</form>
			</div>


		</div>
	</div>
	<div data-options="region:'south'"
		style="height: 50px; text-align: right;">
		<a id="completeLink" href="javascript:void(0)"    class="button button-primary button-small" >
			<i class="fa fa-check-circle"></i>完成
		</a> <a id="cancelLink" href="javascript:void(0)" class="button button-primary button-small"> 
			<i class="fa fa-power-off"></i>取消
		</a>
	</div>
</div>
<script type="text/javascript">
	$(function () {
		CarryOver.init();
	});
</script>