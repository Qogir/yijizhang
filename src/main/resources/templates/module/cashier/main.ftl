
<div id="cashier_layout" class="easyui-layout"	data-options="fit:true">
	<input type="hidden" id="overFlag" value="${overFlag}" />
	<input type="hidden" id="currentPeriod" value="${currentPeriod}" />
	<div data-options="region:'center'" style="height: 40%;padding: 35px;">
			<div  class="button button-action button-box button-jumbo" style="float:left;"><i class="fa fa-area-chart"></i></div>
			<div style="float:left;padding:5px 0 0 20px;overflow:hide;">
				<strong style="color: #3366FF ;font-size:16px;">
				结转损益
				</strong> <div style="padding-top:18px;font-size:13px; ">期末时，应将各损益类科目的余额转入“本年利润”科目，以反映企业在一个会计期间内实现的利润或亏损总额。</div>
			</div>
			<div style="float:right;padding-top:101px;">
			<a id="carryOver" href="javascript:void(0)" 	class="button button-primary button-small" >
			<i class="fa fa-info-circle"></i>结转损益
			</a>
			</div>
	</div>
	<div data-options="region:'south'"	style="height: 60%;padding: 35px;">
		<div  class="button button-action button-box button-jumbo" style="float:left;">
				<i class="fa fa-calendar"></i></div>
		<div style="float:left;padding:5px 0 0 20px;overflow:hide;">
			<strong style="color: #3366FF ;font-size:16px;">
				期末结账		
			</strong>
			<div style="padding-top:18px;font-size:13px; ">为了总结某一会计期间（年度或月度）的经营活动情况，必须定期进行结账，结账就是把一定时期内发生的经济业
					<br/>
					务在全部登记入账的基础上，将各种账簿记录结出“本期发生额”和期末余额，从而根据账簿记录编制会计报表。
					<br/><br/>
					<strong style="font-size:14px;">
					确定当前选择的结账方式：
					</strong>
					<br/>
					<label style="font-size:13px;">
					期末结账<input type="checkbox" checked="checked" disabled="disabled"/>
					</label>
			</div>
		</div>		
	
		<div style="float:right;padding-top:181px;">
			<a id="cashier" href="javascript:void(0)" 
			class="button button-primary button-small"> <i
			class="fa fa-info-circle"></i>&nbsp;&nbsp;&nbsp;结账&nbsp;&nbsp;&nbsp;    
		</a>
		</div>
	</div>
</div>
<div id="newDialog" style="padding:5px 0 0 20px;"></div>
<script type="text/javascript">
	$(function () {
		Cashier.init();
	});
</script>