/**
 * 试算平衡的JS脚本文件。
 */
TrialBalance=function(){
	return {
		theWin: null,
        datatable:null,
		//查询-试算平衡表页面初始化
		init:function(currentPeriod,level){
			TrialBalance.dgInit(currentPeriod,level);
			$('#trialbalanceListSearch').click(function(){
				TrialBalance.theWin=$('<div></div>').window({
					title : '<i class="fa fa-info-circle"></i>试算平衡表',
					width : 268,
					height : 210,
					modal : true,
					collapsible : false,
					shadow : true,
					href : 'search/trialbalance/search',
					queryParams:{currentPeriod:currentPeriod,level:level},
					onClose:function(){
						$(this).panel('destroy');
						TrialBalance.theWin=null;
					}
				});
			});
			$('#trialbalanceListRefresh').bind('click', function(){
				TrialBalance.refresh(currentPeriod,level);
			});

            //导出
            $("#trialbalanceListMenu").find("#exportToExcel").click(function(){
                App.exportToExcel("试算平衡表", TrialBalance.datatable);
            });
		},
        //查询-试算平衡表过滤页面初始化
        searchInit:function(currentPeriod,level){
        	$('#trialbalanceListSubmit').click(function(){
        		TrialBalance.submit();
        	});
        	$('#trialbalanceListReject').click(function(){
        		TrialBalance.theWin.window('close');
        	});
        },
        //过滤页面-确定
        submit:function(){
        	if(!$('#trialbalanceListFm').form('validate')){// 表单验证
				return;
			}
    		var currentPeriod=$('#trialcurrentPeriod').numberspinner('getValue');
    		var level=$('#triallevel').numberspinner('getValue');
    		TrialBalance.theWin.window('close');
    		TrialBalance.refresh(currentPeriod,level);
        },
        //刷新
        refresh:function(currentPeriod,level){
        	var tab = $TC.tabs('getSelected');
        	$('#tabContainer').tabs('update', {
         		tab: tab,
         		options: {
         			href: 'search/trialbalance/filter',
         			queryParams:{currentPeriod:currentPeriod,level:level}
         		}
         	});
        	tab.panel('refresh');
        },
        //表格初始化
        dgInit:function(currentPeriod,level){
            TrialBalance.datatable = $('#trialbalanceListDg').datagrid({
    			singleSelect:true,
    			fitColumns: true,
    			fit:true,
    			toolbar: '#trialbalanceListMenu',
    			url:'search/trialbalance/list',
    			queryParams:{currentPeriod:currentPeriod,level:level},
    			method:'get',
    			columns:[[
    			          {field:'subject_code',title:'科目代码',width:60,align:'left',halign:'center',rowspan:2},
    			          {field:'subject_name',title:'科目名称',width:60,align:'left',halign:'center',rowspan:2},
    			          {title:'期初余额',colspan:2},
    			          {title:'本期发生额',colspan:2},
    			          {title:'期末余额',colspan:2}
  				      ],[
    			          {field:'initial_debit_balance',title:'借方',width:60,align:'right',halign:'center'},
    			          {field:'initial_credit_balance',title:'贷方',width:60,align:'right',halign:'center'},
    			          {field:'period_debit_occur',title:'借方',width:60,align:'right',halign:'center'},
    			          {field:'period_credit_occur',title:'贷方',width:60,align:'right',halign:'center'},
    			          {field:'terminal_debit_balance',title:'借方',width:60,align:'right',halign:'center'},
    			          {field:'terminal_credit_balance',title:'贷方',width:60,align:'right',halign:'center'}
			          ]],
	            rowStyler: function(index,row){
		      		if (row.subject_name=='合计'){
		      			return 'background-color:#6293BB;color:#fff;';
		      		}
		      	},
		      	onLoadSuccess:function(data){
		      		if(data&&!data.notBalance){
		      			$('#balanceResult').html('试算结果：<span style="font-weight: 700;color: green;">平衡</span>');
		      		}else if(data){
		      			$('#balanceResult').html('试算结果：<span style="font-weight: 700;color: #ff0000;">不平衡</span>');
		      		}
		      	}
    		});
        }
	};
}();
