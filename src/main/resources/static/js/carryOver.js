/**
 * 结转损益的JS脚本文件。
 */
CarryOver=function(){

	return {
		init:function(){
			$('#category_detail').combobox({
			    url:'/account/carryOver/category/detail',
			    valueField: 'showValue',
			    textField: 'showValue',
			    required:true,
			    method:'get',
			    onLoadSuccess:function(){
			    	var data = $('#category_detail').combobox('getData'); 
			    	if (data.length > 0) {
			    		$('#category_detail').combobox('select', data[0].value);
	                }
			    }
			});
			$('#completeLink').click(function(){CarryOver.complete();});
			$('#cancelLink').click(function(){CarryOver.cancel();});
		},
		cancel:function(){
			$("#default_win").window("close");
		},
		complete:function(){
			$('#completeLink').addClass("disabled");
		   $.ajax({
                url: "account/carryOver/complete",
                data:$("#carry_over").serialize(),
                success: function(data){
                	$('#completeLink').removeClass("disabled");
                    if(data.result){
                        $.messager.alert("提示信息", data.result,"info",function(){
                        		 $("#default_win").window('close');
                        });
                    }else{
                        $.messager.alert("提示信息", "操作失败，请联系管理员!");
                    }
                }
            });
		}
	};
}();