/**
 * 利润表的JS脚本文件。
 */
Profit=function(){
	var selectIndex = undefined;

	return {
		//初始化
		init:function(searchtPeriod) {
			$('#profitSave').click(function() {
				if(Profit.endEdit()){
					Profit.save();
				}
			});
			$('#profitAppend').click(function() {
				if(Profit.endEdit()){
					Profit.insertRow();
				}
			});
			$('#profitRemoveit').click(function() {
				if(Profit.endEdit()){
					Profit.deleteRow();
				}
			});
			$('#profitPrint').click(function() {
				if(Profit.endEdit()){
					window.print();
				}
			});
			$('#profitCount').click(function() {
				if(Profit.endEdit()){
					Profit.count();
				}
			});
			$('#searchtPeriod').numberspinner({
				min:1,
				max:12,
				value:searchtPeriod,
				onChange:function(){
					if(Profit.endEdit()){
						if(Profit.isChanged()){
							$.messager.confirm('Confirm','报表“利润表”已经修改，是否保存修改后的报表？',function(r){
								if (r){
									Profit.save(1);
								}else {
									$('#profitDg').datagrid('reload');
								}
							});
						} else {
							$('#profitDg').datagrid('reload');
						}
					}
				}
			});
			Profit.datainit(1);
		},
		save:function(reload){
			$('#profitSave').linkbutton('mydisable');
			var rows=$('#profitDg').datagrid('getRows');
			var searchtPeriod=$('#searchtPeriod').numberspinner('getValue');
			 //$('#profitDg').datagrid('reload');
			$.ajax({
                url: 'profit/save?searchtPeriod='+searchtPeriod,
                data: JSON.stringify(rows),
                type: 'POST',
                dataType:"json",      
                contentType:"application/json",
                async: true,
                success: function (data) {
                    if (data&&data.success) {
                    	$.messager.alert('提示', "保存成功！", 'info',function(){
                    		if(reload){//新增
                    			$('#profitDg').datagrid('reload');
	                        }
                    		$('#profitDg').datagrid('acceptChanges');
                    		$('#profitSave').linkbutton('myenable');
                    	});
                    } else {
                    	$.messager.alert('警告', "操作失败，请联系管理员!", 'warning');
                    	$('#profitSave').linkbutton('myenable');
                    }
                }
            });
		},
		//判断是否选中某一行
		selectedRow:function(){
			if(selectIndex){
				return true;
			}else {
				return false;
			}
		},
		//插入行
		insertRow:function(){
			if(Profit.selectedRow()){
				$('#profitDg').datagrid('insertRow',{
					index: selectIndex,
					row: {}
				});
				$('#profitDg').datagrid('gotoCell', {
	                index: selectIndex,
	                field: 'cA'
	            });
				Profit.updateRows(1);
			}
		},
		//删除行
		deleteRow:function(){
			if(Profit.selectedRow()){
				var rows = $('#profitDg').datagrid('getRows');
				if(rows[selectIndex].fix){
					return;
				}
				$('#profitDg').datagrid('deleteRow',selectIndex);
				$('#profitDg').datagrid('gotoCell', {
	                index: selectIndex,
	                field: 'cA'
	            });
				Profit.updateRows(-1);
			}
		},
		//表格初始化
		datainit:function(flag){
			//表格
			var dg = $('#profitDg').datagrid({
				singleSelect:true,
				fitColumns: true,
				fit:true,
				rownumbers:true,
				clickToEdit: false,
		        dblclickToEdit: true,
				toolbar: '#profitMenu',
				url:'profit/list',
				columns:[[
							{field:'cA',title:'A',width:260,align:'left',halign:'center',
								formatter: function(value,row,index){
									if(row.cA&&row.fix){//原有行
										return row.cA;
									}else if(row.cA){//新增行
										row.cAVal = row.cA.replace(/\s/g,'&nbsp;');
										return '<div style="text-align: left;white-space:pre;">'+row.cA+'</div>';
									} else {//无值
										return '';
									}
								},
								editor:'textbox'
							},
							{field:'cB',title:'B',width:240,align:'right',halign:'center',
								formatter: function(value,row,index){
									if(index==0){//第一行居中
										return '<div style="text-align: center;">'+row.cB+'</div>';
									} else {
										return row.cBVal==0||!row.cBVal?'':Profit.formatCurrency(row.cBVal);
						            }
								},
								editor:'textbox'
							},
							{field:'cC',title:'C',width:240,align:'right',halign:'center',
								formatter: function(value,row,index){
									if(index==0){//第一行居中
										return '<div style="text-align: center;">'+row.cC+'</div>';
									} else {
										return row.cCVal==0||!row.cCVal?'':Profit.formatCurrency(row.cCVal);
						            }
								},
								editor:'textbox'
							}
						]],
				onBeforeLoad:function(param){
//					var rows=$('#profitDg').datagrid('getRows');
//					if(rows){
//						for(i=0;i<rows.length;++i){
//							param=Profit.extend(param,JSON.parse(JSON.stringify(rows[i]).replace(/(\[.+\])/g,'$1')));
//						}
//					}
					param.searchtPeriod=$('#searchtPeriod').numberspinner('getValue');
				},
				onLoadSuccess:function(data) {
					selectIndex = undefined;
					if(flag){
						$('#profitDg').datagrid('enableCellEditing', function (cur_idx, pre_idx, field) {
	                        //可修改判断
							var rows=$('#profitDg').datagrid('getRows');
							if(rows[cur_idx].fix){
								return false;
							}else{
								return true;
							}
						});
						flag = undefined;
					}
				},
				onEndEdit:function(index,row,changes){
					if(!changes){
						return;
					}
					var cell =$('#profitDg').datagrid('cell');
					if(!cell){
						return;
					}
					var rows=$('#profitDg').datagrid('getRows');
					var expStr=changes.cB!=undefined?changes.cB:changes.cC;
					if(expStr==undefined){
						return;
					}
					rows[cell.index][cell.field]=expStr;
			        //计算公式
			        Profit.count();
				},
				onClickCell:function(index,field,value){
					Profit.endEdit();
					$('#profitDg').datagrid('gotoCell', {
		                index: index,
		                field: field
		            });
					selectIndex=index;
					$('#cellContent').textbox('setText',value);
				}
			});


            //导出
            $('#profitExportToExcel').click(function() {
                if(Profit.endEdit()){
                    App.profitExportToExcel("利润表", dg);
                }
            });
		},
		isChanged:function(){
			var rows = $('#profitDg').datagrid('getChanges');
			return rows&&rows.length>0;
		},
		endEdit:function(){
			var cell = $('#profitDg').datagrid('cell');
			if(cell){
				$('#profitDg').datagrid('endEdit', cell.index);
			}
			return true;
		},
		//重新计算
		count:function(){
			var rows=$('#profitDg').datagrid('getRows');
			var searchtPeriod=$('#searchtPeriod').numberspinner('getValue');
			 //$('#profitDg').datagrid('reload');
			$.ajax({
                url: 'profit/count?searchtPeriod='+searchtPeriod,
                data: JSON.stringify(rows),
                type: 'POST',
                dataType:"json",      
                contentType:"application/json",
                async: true,
                success: function (data) {
                    if (data&&data.success) {
                    	$('#profitDg').datagrid('loadData', data);
                    } else {
                    	$.messager.alert('警告', "操作失败，请联系管理员!", 'warning');
                    }
                }
            });
		},
		//合并对象，key相同时，value拼接，逗号分隔
		extend:function(src, override){
			for(var i in override){
				if(!override[i]||override[i]=='null'){
					override[i] = '';
				}
				if(i in src){
					src[i] = src[i]+','+override[i];
				} else {
					src[i] = override[i];
				}
			} 
			return src;
		},
		//表格修改数据
		getChanges:function(){
			var params='';
			var rows = $('#profitDg').datagrid('getRows');
			for(i=0;i<rows.length;++i){
				if(rows[i].subjectCode){
					params+='&'+JSON.stringify(rows[i]);
				}
			}
			params=params.replace(/{/g,'').replace(/}/g,'').replace(/","/g,'&').replace(/"/g,'').replace(/:/g,'=');
			params=params.replace(/,/g,'&').replace(/undefined/g,'');
			return params;
		},
		//新增行或删除行时，修改相关单元格的公式
		updateRows:function(changeNum){
			var expIndex = parseInt(selectIndex)+1;
			var rows = $('#profitDg').datagrid('getRows');
			var exp;
        	for(i=selectIndex;i<rows.length;i++){
        		if(changeNum>0){
        			for(j=rows.length;j>=expIndex;j--){
        				exp = new RegExp('(B|C)'+j+'(?=[^0-9])?','g');
        				if(rows[i].cB){
        					rows[i].cB=rows[i].cB.replace(exp, '$1'+(j+changeNum));
        				}
        				if(rows[i].cC){
        					rows[i].cC=rows[i].cC.replace(exp, '$1'+(j+changeNum));
        				}
        				console.log(j+":"+rows[i].cB+':'+rows[i].cC);
        			}
        		} else {
        			for(j=expIndex;j<=rows.length;j++){
        				exp = new RegExp('(B|C)'+j+'(?=[^0-9])?','g');
        				if(rows[i].cB){
        					rows[i].cB=rows[i].cB.replace(exp, '$1'+(j+changeNum));
        				}
        				if(rows[i].cC){
        					rows[i].cC=rows[i].cC.replace(exp, '$1'+(j+changeNum));
        				}
        				console.log(j+":"+rows[i].cB+':'+rows[i].cC);
        			}
        		}
        	}
		},
		//单项公式修改时相关公式结果修改
		sum:function(cExp,cVal,exp,oldValue,newValue){
			var dir = cExp.substr(cExp.indexOf(exp),1);
			switch(dir){
				case '+':cVal=Voucher.accSub(Voucher.accAdd(cVal,newValue),oldValue);break;
				case '-':cVal=Voucher.accAdd(Voucher.accSub(cVal,newValue),oldValue);break;
				case '*':cVal=Voucher.accDiv(Voucher.accMul(cVal,newValue),oldValue);break;
				case '/':cVal=Voucher.accMul(Voucher.accDiv(cVal,newValue),oldValue);break;
				default:cVal=Voucher.accSub(Voucher.accAdd(cVal,newValue),oldValue);break;
			}
			return cVal;
		},
		/**
		 * 将数值四舍五入(保留2位小数)后格式化成金额形式
		 *
		 * @param num 数值(Number或者String)
		 * @return 金额格式的字符串,如'1,234,567.45'
		 * @type String
		 */
		formatCurrency:function (num) {
		    num = num.toString().replace(/\$|\,/g,'');
		    if(isNaN(num))
		    num = "0";
		    sign = (num == (num = Math.abs(num)));
		    num = Math.floor(num*100+0.50000000001);
		    cents = num%100;
		    num = Math.floor(num/100).toString();
		    if(cents<10)
		    cents = "0" + cents;
		    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
		    num = num.substring(0,num.length-(4*i+3))+','+
		    num.substring(num.length-(4*i+3));
		    return (((sign)?'':'-') + num + '.' + cents);
		}
    }
}();
