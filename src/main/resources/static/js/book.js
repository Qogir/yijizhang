Book=function(){
	return {
		init:function(){
			var nowdate = new Date();
			var year = nowdate.getFullYear();
			var month = nowdate.getMonth() + 1;
			//初始化年月
			$('#init_year').numberbox({
				value : year
			});
			$('#init_period').numberbox({
				value : month
			});

			$('#start_time').text(year + '年' + month + '月1号');
			$('#start_time').css('white-space', 'nowrap');
			$('#create_book').parent().show();
			$('#bookInfo').click(function(){Book.info();});
			$('#beforeLink').click(function(){Book.beforeLink();});
			$('#nextLink').click(function(){Book.nextLink();});
			$('#completeLink').click(function(){Book.completeLink();});
			$('#cancelLink').click(function(){Book.cancelLink();});
		},
		//制度说明
		info:function(){
			$("#account_book_info_win").window({
				title : '<i class="fa fa-info-circle"></i>制度说明',
				width : 650,
				height : 500,
				modal : true,
				collapsible : false,
				shadow : true,
				href : 'account/book/info'
			});
		},
		//上一步按钮操作
		beforeLink:function(){
			if ($('input[name=status]').val() == 'sencondJsp') {
				//显示对应页面与按钮
				$('#firstJsp').css('display', '');
				$('#sencondJsp').css('display', 'none');
				$('#thirdJsp').css('display', 'none');
				$('#bookInfo').css('display', 'none');
				$('#beforeLink').css('display', 'none');
				//设置标志
				$('input[name=status]').val('firstJsp');
			} else {
				//显示对应页面与按钮
				$('#firstJsp').css('display', 'none');
				$('#sencondJsp').css('display', '');
				$('#thirdJsp').css('display', 'none');
				$('#bookInfo').css('display', '');
				$('#nextLink').css('display', '');
				$('#completeLink').css('display', 'none');
				//设置标志
				$('input[name=status]').val('sencondJsp');
			}
		},
		//下一步按钮操作
		nextLink:function(){
			if ($('input[name=status]').val() == 'firstJsp') {
				//firstJsp页面要素要填写
				var isValid = $('#create_book').form('validate');
				if (!isValid){
					return;	
				}
                //检查账套名称是否已经存在
                $.ajax({
                    url: "account/book/is/exist",
                    async:false,
                    data:{
                        'name':$('#book_name').val().trim()
                    },
                    success: function(result){
                        if(result){
                            $.messager.alert("提示信息", "账套名称已经存在，请重新命名账套!");
                            return;
                        }else{
                            //显示对应页面与按钮
                            $('#firstJsp').css('display', 'none');
                            $('#sencondJsp').css('display', '');
                            $('#thirdJsp').css('display', 'none');
                            $('#bookInfo').css('display', '');
                            $('#beforeLink').css('display', '');
                            //设置标志
                            $('input[name=status]').val('sencondJsp');
                        }
                    }
                });
			} else {
				//secondJsp页面需选择科目体系
				if ($('input:radio[name="dictValueId"]:checked')
						.val() == undefined) {
					$.messager.alert("提示信息", "请选择科目体系!");
					return;
				}
				if($('#init_year').val()==undefined||$('#init_year').val()==""){
					$.messager.alert("提示信息", "请选择启用年数!");
					return;
				}
				if($('#init_period').val()==undefined||$('#init_period').val()==""){
					$.messager.alert("提示信息", "请选择启用期数!");
					return;
				}
				if(parseInt($('#init_year').val())<2014||parseInt($('#init_year').val())>3000){
					$.messager.alert("提示信息", "启用期间年数不合法!");
					return;
				}
				if(parseInt($('#init_period').val())<1||parseInt($('#init_period').val())>12){
					$.messager.alert("提示信息", "启用期间期数不合法!");
					return;
				}
				//显示对应页面与按钮
				$('#firstJsp').css('display', 'none');
				$('#sencondJsp').css('display', 'none');
				$('#thirdJsp').css('display', '');
				$('#bookInfo').css('display', 'none');
				$('#nextLink').css('display', 'none');
				$('#completeLink').css('display', '');
				//设置标志
				$('input[name=status]').val('thirdJsp');
			}
		},
		//完成按钮
		completeLink:function(){
			$('#completeLink').addClass("disabled");
			 $.ajax({
	                url: "account/book/complete",
	                data:$("#create_book").serialize(),
	                success: function(data){
	        			$('#completeLink').removeClass("disabled");
	                    if(data.resultId){
	                        $.ajax({ 
	                        	url: "switch/to/book/" + data.resultId,
	            				context: document.body,
	            				success:function(data){
	            					if(data){
	            					    $.messager.alert("提示信息", "新建账套成功并自动切换为当前账套!","info",function(){
	                               		 	$("#default_win").window('close');
	                               		 	document.location.reload();
	                               		 });
	            					}else{
	            						$('#busyIcon').hide();
	            						$(this).show();
	            						$('#cancelSwitchBtn').show();
	            						$.messager.alert("提示信息", "切换账套失败，请稍候重试！","error");
	            					}
	            				}
	                        });
	                    }else{
	                        $.messager.alert("提示信息", "操作失败，请联系管理员!");
	                    }
	                }
	            });
		},
		//取消按钮操作
		cancelLink:function(){
			$("#default_win").window("close");
		}
	};
}();