/**
 * 记账凭证-现金流量的JS脚本文件。
 */
VoucherCash=function(){
	var saveFlag = false;
	
	return {
		//初始化
		init:function() {
			VoucherCash.saveFlag=false;
			var rows = $('#voucherDg').datagrid('getRows');
        	var result = [];
        	var voucherCashSubject;
        	var voucherCashMoney;
        	for(i=0;i<rows.length;++i){
				if(rows[i].subjectCode){
					result.push({
						relativeSubjectCode: rows[i].subjectTextName,
						cashCode: null,
	                    money: rows[i].newdebit||(rows[i].newdebit&&rows[i].newdebit==0)?rows[i].newcrebit:rows[i].newdebit
	                });
				}
        	}
        	$('#voucherCashDg').datagrid({
				singleSelect:true,
				fitColumns: true,
				fit:true,
        		toolbar: '#voucherCashMenu1',
        		columns:[[
        		          {field:'relativeSubjectCode',title:'对方科目分录',width:10,halign:'center',align:'left'},
        		          {field:'cashCode',title:'主表科目',width:10,halign:'center',align:'left',
							editor:{
								type:'combobox',
								options:{
								    valueField:'subjectCode',
								    textField:'subjectTextName',
								    method:'get',
								    url:'/voucher/subjectlist',
								    onBeforeLoad:function(){
								    	if(cashCode){
								    		$(this).combobox('loadData',cashCode); 
								    		return false;
								    	}
								    },
								    onLoadSuccess:function(){
								    	if(!cashCode){
								    		cashCode=$(this).combobox('getData');
								    	}
								    }
						        }}},
        		          {field:'money',title:'本位币',width:10,halign:'center',align:'right'}
        		      ]]
        	});
        	if(result){
        		console.log(result);
        		$('#voucherCashDg').datagrid('loadData', result);
        	}
			$('#voucherCashSubmit').click(function() {
				var cashRows = $('#voucherCashDg').datagrid('getRows');
				if(cashRows){
					cashData=JSON.stringify(cashRows);
				}
				VoucherCash.saveFlag=true;
				Voucher.theCashWin.window('close');
			});
			//取消
			$('#voucherCashReject').click(function() {
				Voucher.theCashWin.window('close');
			});
        }
	}
}();