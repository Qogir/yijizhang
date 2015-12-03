/**
 * 凭证汇总表的JS脚本文件。
 */
VouchercollectSearch=function(){
	return {
		datatable: null,
		//页面初始化
        init:function(startTime,endTime,voucherWord,voucherStartNo,voucherEndNo){
        	VouchercollectSearch.dgInit(startTime,endTime,voucherWord,voucherStartNo,voucherEndNo);
        	$('#coollectListSearch').click(function(){
				$("#default_win").window({
					title : '<i class="fa fa-info-circle"></i>凭证汇总',
					width : 470,
					height : 240,
					modal : true,
					collapsible : false,
					shadow : true,
					href : 'search/vouchercollect/search',
					queryParams:{startTime:startTime,endTime:endTime,voucherWord:voucherWord,voucherStartNo:voucherStartNo,voucherEndNo:voucherEndNo},
				});
        	});
    	  $("#coollectListMenu").find("#exportToExcel").click(function () {
              App.exportToExcel("凭证汇总表", VouchercollectSearch.datatable);
          });
        },
        searchInit:function(){
        	//凭证字
			$('#search_voucherWord').combobox({
			    url:'company/common/value/voucherwordlist',
			    method:'get',
			    valueField:'showValue',
			    textField:'showValue',
			    editable:false,
			    required:true,
			    onChange:function(){
			    	$('#voucherStartNo').numberbox('enable');
			    	$('#voucherEndNo').numberbox('enable');
			    	$('#voucherStartNo').numberbox('setValue',1);
			    	$('#voucherEndNo').numberbox('setValue',1);
			    }
			
			});
        	//日期
			$('#startTime').datebox({
				formatter:Voucher.myformatter,
				parser:Voucher.myparser,
				required:true,
				editable:false
			});
			$('#endTime').datebox({
				formatter:Voucher.myformatter,
				parser:Voucher.myparser,
				required:true,
				editable:false
			});
        	$('#coollectListSubmit').click(function(){
        		VouchercollectSearch.submit();
        	});
        	$('#coollectListReject').click(function(){
        		$("#default_win").window('close');
        	});
        },
        submit:function(){
    		var startTime=$('#startTime').datebox('getValue');
    		var endTime=$('#endTime').datebox('getValue');
    		var voucherWord=$('#search_voucherWord').combo('getValue');
    		var voucherStartNo=$("#voucherStartNo").numberspinner('getValue');
    		var voucherEndNo=$("#voucherEndNo").numberspinner('getValue');
         	var tab = $TC.tabs('getSelected');
         	$('#tabContainer').tabs('update', {
         		tab: tab,
         		options: {
         			href: 'search/vouchercollect/filter',
         			queryParams:{startTime:startTime,endTime:endTime,voucherWord:voucherWord,voucherStartNo:voucherStartNo,voucherEndNo:voucherEndNo},
         			onLoad:function(){
         				$("#default_win").window('close');
         			}
         		}
         	});
         	tab.panel('refresh');
        },
        dgInit:function(startTime,endTime,voucherWord,voucherStartNo,voucherEndNo){
        	VouchercollectSearch.datatable = $('#coollectListDg').datagrid({
    			heigth:400,
    			singleSelect:true,
    			fitColumns: true,
    			fit:true,
    			toolbar: '#coollectListMenu',
    			url:'search/vouchercollect/list',
    			queryParams:{startTime:startTime,endTime:endTime,voucherWord:voucherWord,voucherStartNo:voucherStartNo,voucherEndNo:voucherEndNo},
    			method:'get',
    			columns:[[
    			          {field:'subject_code',title:'科目代码',width:60},
    			          {field:'subject_name',title:'科目名称',width:60},
    			          {field:'debit',title:'借方金额',align:'left',width: 100, styler: function () {
	                            return "font-weight:700;color:green;";
	                      }},
	                      {field:'credit',title:'贷方金额',align:'left', width: 100, styler: function () {
	                            return "font-weight:700;color:blue;";
	                      }}
  				      ]],
  				      rowStyler: function(index,row){
  				    	  if (row.subject_name=='合计'){
  				    		  return 'background-color:#6293BB;color:#fff;';
  				    	  }
		      	}
    		});
        }
	}
}();