/**
 * 企业通用配置值的JS脚本文件。
 */
CompanyCommonValue=function(){
	
	return {
		//企业通用配置值页面初始化
		init:function(type,winTitle) {
			//表格
			$('#commonValueDg').datagrid({
				singleSelect:true,
				fitColumns: true,
				fit:true,
				url:'company/common/value/list',
				method:'get',
				queryParams:{type:type},
				onDblClickRow:CompanyCommonValue.edit,
				showFooter:false,
				columns:[[
							{field:'showValue',title:winTitle,width:60,halign:'center'}
						]]
			});
			//新增
			$('#commonValueAdd').click(function(){
				$('#commonValueShowValue').textbox('enable');
				$('#commonValueShowValue').textbox('enableValidation');
				$('#commonValueShowValue').textbox('setValue','');
				$('#commonValueId').val('');
				$('#commonValueSubmit').linkbutton('myenable');
			});
			//修改
			$('#commonValueEdit').click(function(){
				CompanyCommonValue.reject();
				var row = $('#commonValueDg').datagrid('getSelected');
				if (!row){
					$.messager.alert('警告', '请选择一个'+winTitle+'!', 'warning');
					return;
				}
				CompanyCommonValue.edit('',row);
			});
			//删除
			$('#commonValueDelete').click(function(){
				CompanyCommonValue.reject();
				var row = $('#commonValueDg').datagrid('getSelected');
				if (!row){
					$.messager.alert('警告', '请选择一个'+winTitle+'!', 'warning');
					return;
				}
				CompanyCommonValue.remove(row.id,winTitle);
			});
			//确定
			$('#commonValueSubmit').click(function(){
				CompanyCommonValue.save(winTitle,type);
			});
			//取消
			$('#commonValueReject').click(function(){
				CompanyCommonValue.reject();
			});
		},
		//取消
		reject:function(){
			$('#commonValueShowValue').textbox('disable');
			$('#commonValueShowValue').textbox('disableValidation');
			$('#commonValueId').val('');
			$('#commonValueSubmit').linkbutton('mydisable');
		},
		//修改
		edit:function(index,row){
			$('#commonValueShowValue').textbox('enable');
			$('#commonValueShowValue').textbox('enableValidation');
			$('#commonValueShowValue').textbox('setValue',row.showValue);
			$('#commonValueId').val(row.id);
			$('#commonValueSubmit').linkbutton('myenable');
		},
		// 删除
		remove:function(id,winTitle){
			$('#commonValueDelete').linkbutton('mydisable');
			$.messager.confirm('确认', '确定删除选中'+winTitle+'?', function(r){
				if (r){
					$.ajax({
		                url: "company/common/value/delete",
		                type:'get',
		                data:{id:id},
		                success: function(data){
		                	$('#commonValueDelete').linkbutton('myenable');
		                	if(data.result){
		                    	$.messager.alert('提示', "删除成功!", 'info',function(){
		                    		$('#commonValueDg').datagrid('reload');
		                    	});
		                    }else{
		                        $.messager.alert('警告', "操作失败，请联系管理员!", 'warning');
		                    }
		                },
		                error:function(){
		                	$('#commonValueDelete').linkbutton('myenable');
		            	}
		            });
				}else {
					$('#commonValueDelete').linkbutton('myenable');
				}
			});
		},
		// 提交保存
		save:function(winTitle,type){
			$('#commonValueSubmit').linkbutton('mydisable');
			if(!$('#commonValueFm').form('validate')){// 表单验证
				$('#commonValueSubmit').linkbutton('myenable');
				return;
			}
			var commonValueShowValue = $('#commonValueShowValue').textbox('getValue');
			var commonValueId = $('#commonValueId').val();
			$.ajax({
                url: "company/common/value/check",
                type:'get',
                data:{name:commonValueShowValue,id:commonValueId,type:type},
                success: function(data){
                	if(!data.result){
                		$('#commonValueSubmit').linkbutton('myenable');
                		$.messager.alert('警告', winTitle+'已存在!', 'warning');
                		return;
                	}
		        	$.ajax({
		                url: "company/common/value/save",
		                type:'post',
		                data:$('#commonValueFm').serialize(),
		                success: function(data){
		                	$('#commonValueSubmit').linkbutton('myenable');
		                    if(data.result){
		                    	$.messager.alert('提示', "保存成功!", 'info',function(){
		                    		$('#commonValueDg').datagrid('reload');
		                    		CompanyCommonValue.reject();
		                    	});
		                    }else{
		                        $.messager.alert('警告', "操作失败，请联系管理员!", 'warning');
		                    }
		                },
		                error: function(XMLHttpRequest, textStatus, errorThrown) {
		                	$('#commonValueSubmit').linkbutton('myenable');
		                }
		            });
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                	$('#commonValueSubmit').linkbutton('myenable');
                }
			});
		},
		//打开配置值窗口
		openWindow:function(winTitle,type,eventTraget){
			$('<div></div>').window({
				title:'<i class="fa fa-info-circle"></i>'+winTitle,
				width:422,
				height:477,
				modal:true,
				collapsible:false,
				shadow:true,
				href:'company/common/value/main',
				queryParams:{type:type,winTitle:winTitle},
				onClose:function(){
					$(this).panel('destroy');
					if(eventTraget){
						$(eventTraget).combobox('reload'); 
					}
				}
			});
		}
    }
}();
