/**
 * 总账的JS脚本文件。
 */
Ledger=function(){
	return {
        //查询-总账过滤页面初始化
        searchInit:function(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull){
        	$('#ledgerSubmit').click(function(){
        		Ledger.submit();
        	});
        	$('#ledgerReject').click(function(){
        		Ledger.theWin.window('close');
        	});
        },
        theWin: null,
        //查询-总账页面初始化
        init:function(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull){
        	Ledger.dgInit(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull);
        	$('#ledgerSearch').click(function(){
        		Ledger.theWin=$('<div></div>').window({
					title : '<i class="fa fa-info-circle"></i>科目余额表',
					width : 333,
					height : 289,
					modal : true,
					collapsible : false,
					shadow : true,
					href : 'search/ledger/search',
					queryParams:{periodFrom:periodFrom,periodTo:periodTo,level:level,subjectCodeFrom:subjectCodeFrom,subjectCodeTo:subjectCodeTo,valueNotNull:valueNotNull},
					onClose:function(){
						$(this).panel('destroy');
						Ledger.theWin=null;
					}
				});
        	});
        	$('#ledgerRefresh').bind('click', function(){
        		Ledger.refresh(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull);
        	});
        	$("#ledger").find("#exportToExcel").click(function () {
                App.exportToExcel("总账", $('#ledgerDg'));
            });
        },
        //过滤页面-确定
        submit:function(){
        	if(!$('#ledgerFm').form('validate')){// 表单验证
				return;
			}
    		var periodFrom=$('#ledgerperiodFrom').numberspinner('getValue');
    		var periodTo=$('#ledgerperiodTo').numberspinner('getValue');
    		var level=$('#ledgerlevel').numberspinner('getValue');
    		var subjectCodeFrom=$("#ledgersubjectCodeFrom").textbox('getValue');
    		var subjectCodeTo=$("#ledgersubjectCodeTo").textbox('getValue');
    		var valueNotNull=$('#ledgervalueNotNull').prop('checked')?1:'';
    		Ledger.theWin.window('close');
    		Ledger.refresh(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull);
        },
        //刷新科目余额表
        refresh:function(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull){
        	var tab = $TC.tabs('getSelected');
        	$('#tabContainer').tabs('update', {
         		tab: tab,
         		options: {
         			href: 'search/ledger/filter',
         			queryParams:{periodFrom:periodFrom,periodTo:periodTo,level:level,subjectCodeFrom:subjectCodeFrom,subjectCodeTo:subjectCodeTo,valueNotNull:valueNotNull}
         		}
         	});
        	tab.panel('refresh');
        },
        //查询-科目余额表 表格初始化
        dgInit:function(periodFrom,periodTo,level,subjectCodeFrom,subjectCodeTo,valueNotNull){
        	if(periodFrom&&periodTo&&periodFrom==periodTo){
        		$('#ledgerDg').datagrid({
        			singleSelect:true,
        			fitColumns: true,
        			fit:true,
        			toolbar: '#ledgerMenu',
        			onDblClickRow:Ledger.onDblClickRow,
        			url:'search/ledger/list',
        			queryParams:{periodFrom:periodFrom,periodTo:periodTo,level:level,subjectCodeFrom:subjectCodeFrom,subjectCodeTo:subjectCodeTo,valueNotNull:valueNotNull},
        			method:'get',
        			columns:[[
        			          {field:'subject_code_text',title:'科目代码',width:130,align:'left',halign:'center'},
        			          {field:'subject_name',title:'科目名称',width:230,align:'left',halign:'center'},
        			          {field:'wordNo',title:'凭证字号',width:100,align:'center',halign:'center'},
        			          {field:'summary',title:'摘要',width:100,align:'center',halign:'center'},
        			          {field:'debit',title:'借方',width:130,align:'right',halign:'center'},
        			          {field:'credit',title:'贷方',width:130,align:'right',halign:'center'},
        			          {field:'direction',width:30,align:'center',halign:'center'},
        			          {field:'balance',title:'余额',width:110,align:'right',halign:'center'}
    			          ]]
        		});
        	} else {
        		$('#ledgerDg').datagrid({
        			singleSelect:true,
        			fitColumns: true,
        			fit:true,
        			toolbar: '#ledgerMenu',
        			onDblClickRow:Ledger.onDblClickRow,
        			url:'search/ledger/list',
        			queryParams:{periodFrom:periodFrom,periodTo:periodTo,level:level,subjectCodeFrom:subjectCodeFrom,subjectCodeTo:subjectCodeTo,valueNotNull:valueNotNull},
        			method:'get',
        			columns:[[
        			          {field:'subject_code_text',title:'科目代码',width:130,align:'left',halign:'center'},
        			          {field:'subject_name',title:'科目名称',width:200,align:'left',halign:'center'},
        			          {field:'init_year',title:'年',width:40,align:'center',halign:'center'},
        			          {field:'current_period',title:'月',width:30,align:'center',halign:'center'},
        			          {field:'daynum',title:'日',width:30,align:'center',halign:'center'},
        			          {field:'wordNo',title:'凭证字号',width:100,align:'center',halign:'center'},
        			          {field:'summary',title:'摘要',width:100,align:'center',halign:'center'},
        			          {field:'debit',title:'借方',width:100,align:'right',halign:'center'},
        			          {field:'credit',title:'贷方',width:100,align:'right',halign:'center'},
        			          {field:'direction',width:30,align:'center',halign:'center'},
        			          {field:'balance',title:'余额',width:100,align:'right',halign:'center'}
    			          ]]
        		});
        	}
        },
        //打开明细账
        onDblClickRow:function(index,row){
        	if(row.current_period<row.init_period){
        		$.messager.alert('警告', "明细账不能反应启用期间之前的数据!", 'warning');
        		return;
        	}
        	if(row.subject_code&&row.current_period){
        		App.addVoucherTab('明细账','search/detail/main?subjectCode='+row.subject_code+'&currentPeriod='+row.current_period,true);
        	}
        }
	};
}();
