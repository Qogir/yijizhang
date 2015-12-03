/**
 * 打印的JS脚本文件。
 */
Print=function(){
	return {
		//打印
		myPrintArea:function(printLayout){
			printLayout.find('.datagrid-body').css({
	 			   'height' : 'auto', //高度自动
	 			   'overflow' : 'visible' //在打印之前把这个Object的overflow改成全部显示
	 			});
			printLayout.find('.datagrid-view').css({
				   'height' : 'auto', //高度自动
				   'overflow' : 'visible' //在打印之前把这个Object的overflow改成全部显示
				}).printArea();
		}
    }
}();
