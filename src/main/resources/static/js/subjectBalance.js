/**
 * 科目余额的JS脚本文件。
 */
SubjectBalance=function(){
	return {

        datatable:null,

        init_export_button:function(){
            $("#balanceListMenu #exportToExcel").click(function(){
                App.exportToExcel("科目余额", SubjectBalance.datatable);
            });
        },

        //记账科目余额页面初始化
		voucherInit:function(id) {
			$('#voucherBalanceDg').datagrid({
				heigth:400,
				singleSelect:true,
				fitColumns: true,
				fit:true,
				url:'search/subjectbalance/list',
				queryParams:{subjectCode:id},
				method:'get',
				columns:[[
			          	{field:'subject_name',title:'科目名称',width:60,align:'left',halign:'center'},
			          	{field:'initial_debit_balance',title:'期初借方金额',width:60,align:'right',halign:'center'},
			          	{field:'initial_credit_balance',title:'期初贷方金额',width:60,align:'right',halign:'center'},
			          	{field:'period_debit_occur',title:'本期借方发生额',width:60,align:'right',halign:'center'},
			          	{field:'period_credit_occur',title:'本期贷方发生额',width:60,align:'right',halign:'center'},
			          	{field:'year_debit_occur',title:'本年累计借方发生额',width:60,align:'right',halign:'center'},
			          	{field:'year_credit_occur',title:'本年累计贷方发生额',width:60,align:'right',halign:'center'},
			          	{field:'terminal_debit_balance',title:'期末借方金额',width:60,align:'right',halign:'center'},
			          	{field:'terminal_credit_balance',title:'期末贷方金额',width:60,align:'right',halign:'center'}
				]]
			});
        },
        //查询-科目余额表过滤页面初始化
        searchInit:function(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull){
        	$('#balanceListSubmit').click(function(){
        		SubjectBalance.submit();
        	});
        	$('#balanceListReject').click(function(){
        		SubjectBalance.theWin.window('close');
        	});
        },
        theWin:null,
        //查询-科目余额表页面初始化
        init:function(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull){
        	SubjectBalance.dgInit(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull);
        	$('#balanceListSearch').click(function(){
        		SubjectBalance.theWin=$('<div></div>').window({
					title : '<i class="fa fa-info-circle"></i>科目余额表',
					width : 333,
					height : 289,
					modal : true,
					collapsible : false,
					shadow : true,
					href : 'search/subjectbalance/search',
					queryParams:{periodFrom:periodFrom,periodTo:periodTo,level:level,subjectCodeFrom:subjectCodeFrom,subjectCodeTo:subjectCodeTo,valueNotNull:valueNotNull},
					onClose:function(){
						$(this).panel('destroy');
						SubjectBalance.theWin=null;
					}
				});
        	});
        	$('#balanceListRefresh').bind('click', function(){
        		SubjectBalance.refresh(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull);
        	});

            //导出
            SubjectBalance.init_export_button();

        },
        //过滤页面-确定
        submit:function(){
        	if(!$('#balanceListFm').form('validate')){// 表单验证
				return;
			}
    		var periodFrom=$('#periodFrom').numberspinner('getValue');
    		var periodTo=$('#periodTo').numberspinner('getValue');
    		var level=$('#level').numberspinner('getValue');
    		var subjectCodeFrom=$("#subjectCodeFrom").textbox('getValue');
    		var subjectCodeTo=$("#subjectCodeTo").textbox('getValue');
    		var valueNotNull=$('#valueNotNull').prop('checked')?1:'';
    		SubjectBalance.theWin.window('close');
    		SubjectBalance.refresh(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull);
        },
        //刷新科目余额表
        refresh:function(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull){
        	var tab = $TC.tabs('getSelected');
        	$('#tabContainer').tabs('update', {
         		tab: tab,
         		options: {
         			href: 'search/subjectbalance/filter',
         			queryParams:{periodFrom:periodFrom,periodTo:periodTo,level:level,subjectCodeFrom:subjectCodeFrom,subjectCodeTo:subjectCodeTo,valueNotNull:valueNotNull}
         		}
         	});
        	tab.panel('refresh');
        },
        //查询-科目余额表 表格初始化
        dgInit:function(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull){
        	if(periodFrom&&periodTo&&periodFrom==periodTo){
                SubjectBalance.datatable = $('#balanceListDg').datagrid({
        			singleSelect:true,
        			fitColumns: true,
        			fit:true,
        			toolbar: '#balanceListMenu',
        			onDblClickRow:SubjectBalance.onDblClickRow,
        			url:'search/subjectbalance/list',
        			queryParams:{periodFrom:periodFrom,periodTo:periodTo,level:level,subjectCodeFrom:subjectCodeFrom,subjectCodeTo:subjectCodeTo,valueNotNull:valueNotNull},
        			method:'get',
        			columns:[[
        			          {field:'subject_code',title:'科目代码',width:60,align:'left',halign:'center',rowspan:2},
        			          {field:'subject_name',title:'科目名称',width:60,align:'left',halign:'center',rowspan:2},
        			          {title:'期初余额',colspan:2},
        			          {title:'本期发生额',colspan:2},
        			          {title:'本年累计发生额',colspan:2},
        			          {title:'期末余额',colspan:2}
      				      ],[
        			          {field:'initial_debit_balance',title:'借方',width:60,align:'right',halign:'center'},
        			          {field:'initial_credit_balance',title:'贷方',width:60,align:'right',halign:'center'},
        			          {field:'period_debit_occur',title:'借方',width:60,align:'right',halign:'center'},
        			          {field:'period_credit_occur',title:'贷方',width:60,align:'right',halign:'center'},
        			          {field:'year_debit_occur',title:'借方',width:60,align:'right',halign:'center'},
        			          {field:'year_credit_occur',title:'贷方',width:60,align:'right',halign:'center'},
        			          {field:'terminal_debit_balance',title:'借方',width:60,align:'right',halign:'center'},
        			          {field:'terminal_credit_balance',title:'贷方',width:60,align:'right',halign:'center'}
    			          ]],
		            rowStyler: function(index,row){
			      		if (row.subject_name=='合计'){
			      			return 'background-color:#6293BB;color:#fff;';
			      		}
			      	}
        		});
        	} else {
                SubjectBalance.datatable = $('#balanceListDg').datagrid({
        			singleSelect:true,
        			fitColumns: true,
        			fit:true,
        			toolbar: '#balanceListMenu',
        			onDblClickRow:SubjectBalance.onDblClickRow,
        			url:'search/subjectbalance/list',
        			queryParams:{periodFrom:periodFrom,periodTo:periodTo,level:level,subjectCodeFrom:subjectCodeFrom,subjectCodeTo:subjectCodeTo,valueNotNull:valueNotNull},
        			method:'get',
        			columns:[[
        			          {field:'current_period',title:'期间',width:20,align:'left',halign:'center',rowspan:2},
        			          {field:'subject_code',title:'科目代码',width:60,align:'left',halign:'center',rowspan:2},
        			          {field:'subject_name',title:'科目名称',width:60,align:'left',halign:'center',rowspan:2},
        			          {title:'期初余额',colspan:2},
        			          {title:'本期发生额',colspan:2},
        			          {title:'本年累计发生额',colspan:2},
        			          {title:'期末余额',colspan:2}
      				      ],[
        			          {field:'initial_debit_balance',title:'借方',width:60,align:'right',halign:'center'},
        			          {field:'initial_credit_balance',title:'贷方',width:60,align:'right',halign:'center'},
        			          {field:'period_debit_occur',title:'借方',width:60,align:'right',halign:'center'},
        			          {field:'period_credit_occur',title:'贷方',width:60,align:'right',halign:'center'},
        			          {field:'year_debit_occur',title:'借方',width:60,align:'right',halign:'center'},
        			          {field:'year_credit_occur',title:'贷方',width:60,align:'right',halign:'center'},
        			          {field:'terminal_debit_balance',title:'借方',width:60,align:'right',halign:'center'},
        			          {field:'terminal_credit_balance',title:'贷方',width:60,align:'right',halign:'center'}
    			          ]],
		            rowStyler: function(index,row){
			      		if (row.subject_name=='合计'){
			      			return 'background-color:#6293BB;color:#fff;';
			      		}
			      	}
        		});
        	}
        },
        //打开明细账
        onDblClickRow:function(index,row){
        	if(row.subject_code&&row.current_period){
        		App.addVoucherTab('明细账','search/detail/main?subjectCode='+row.subject_code+'&currentPeriod='+row.current_period,true);
        	}
        }
	};
}();
