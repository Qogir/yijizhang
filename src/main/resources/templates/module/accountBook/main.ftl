
<div id="accountBook_layout" class="easyui-layout"
	data-options="fit:true">
	<div data-options="region:'center'">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'west'"
				style="width: 180px; background-image: url('/public/img/jianzhang.png')">
				<div style="width:100%;height:300px;"></div>

				</div>
			<div data-options="region:'center'" style="display: none; padding: 15px;display: none;">
				<form id="create_book" action="#" method="post">
					<div id="firstJsp" >
						<br /> <label size="3"><b>请在建账向导的指引下，建立一个最合适你公司实际情况的账套。</b></label>
						<br /> <br /> <label style="color: #ff0000;">*</label><label size="2">请输入账套名称:</label>
						<input type="text"	id="book_name" name="bookName" class="easyui-textbox" data-options="required:true" style="width: 80%;" /> <br /> <br />
						<br /> <label style="color: #ff0000;">*</label><label size="2">请输入公司名称:</label>
						<input type="text"	id="company_name" name="companyName" class="easyui-textbox" data-options="required:true"style="width: 80%;" />
					</div>
					<div id="sencondJsp" style="display: none;">
						<fieldset>
							<legend>
								<font size="2" color="blue">请选择科目体系</font>
							</legend>
							<#list categories as category> <br />
								<input name="dictValueId" value="${category.id}" type="radio" checked="checked"/>${category.showValue?default('')}
							</#list>
						</fieldset>
						<br />
						<fieldset>
							<legend>
								<font size="2" color="blue">记账本位币</font>
							</legend>
							<br /> 代码：<font size="0.1"><input type="text"
								id="money_code" name="moneyCode" value="RMB" style="width: 20%;" /></font>
							名称：<input type="text" id="money_name" name="moneyName"
								value="人民币" style="width: 20%;" />
						</fieldset>
						<br />
						<fieldset>
							<legend>
								<font size="2" color="blue">定义会计期间的界定方式</font>
							</legend>
							<br /> 账套会计启用期间：<input id="init_year" name="initYear"
								class="easyui-numberspinner"
								data-options="onChange: function(value){$('#vv').text(value);
			   		  	$('#start_time').text(value+'年'+$('#init_period').val()+'月1号');}"
								style="width: 70px;" /> 年<input id="init_period"
								name="initPeriod" class="easyui-numberspinner"
								data-options="onChange: function(value){$('#vv').text(value);
			   		  	$('#start_time').text($('#init_year').val()+'年'+value+'月1号');}"
								style="width: 50px;" />期 <br /> <br />
							账套启用日期：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label
								id="start_time" style="white-space: nowrap;"></label>
						</fieldset>
					</div>
					<div id="thirdJsp" style="display: none; padding: 15px;">
						<br /> <br /> <font size="2">请定义会计科目长度：</font> <br /> <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>一级科目代码长度：</label><input
							id="level1" name="level1" class="easyui-numberspinner" value="4"
							data-options="onChange: function(value){$('#vv').text(value);}"
							style="width: 45px;" disabled="disabled" /> <br /> <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>二级科目代码长度：</label><input
							id="level2" name="level2" class="easyui-numberspinner" value="2"
							data-options="min:2,max:4,onChange: function(value){$('#vv').text(value);}"
							style="width: 45px;" /> <br /> <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>三级科目代码长度：</label><input
							id="level3" name="level3" class="easyui-numberspinner" value="2"
							data-options="min:2,max:4,onChange: function(value){$('#vv').text(value);}"
							style="width: 45px;" /> <br /> <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>四级科目代码长度：</label><input
							id="level4" name="level4" class="easyui-numberspinner" value="2"
							data-options="min:2,max:4,onChange: function(value){$('#vv').text(value);}"
							style="width: 45px;" /> <br /> <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>五级科目代码长度：</label><input
							id="level5" name="level5" class="easyui-numberspinner" value="2"
							data-options="min:2,max:4,onChange: function(value){$('#vv').text(value);}"
							style="width: 45px;" />
					</div>
				</form>
			</div>


		</div>
		<input type="hidden" name="status" value="firstJsp" />
	</div>
	<div data-options="region:'south'"
		style="height: 50px; text-align: right;">
		<a id="bookInfo" href="javascript:void(0)" 
			class="button button-primary button-small" style="display: none;">
			<i class="fa fa-info-circle"></i>制度说明
		</a> <a id="beforeLink" href="javascript:void(0)" 
			class="button button-primary button-small" style="display: none;">
			<i class="fa fa-arrow-circle-left"></i>上一步
		</a> <a id="nextLink" href="javascript:void(0)"   
			class="button button-primary button-small"> 
			<i	class="fa fa-arrow-circle-right"></i>下一步
		</a> <a id="completeLink" href="javascript:void(0)" 
			class="button button-primary button-small" style="display: none;">
			<i class="fa fa-check-circle"></i>完成
		</a> <a id="cancelLink" href="javascript:void(0)" 
			class="button button-primary button-small"> <i
			class="fa fa-power-off"></i>取消
		</a>
	</div>
</div>
<div id="account_book_info_win"></div>
<script type="text/javascript">
	$(function () {
		Book.init();
	});
</script>