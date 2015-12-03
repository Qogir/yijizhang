/**
 * 首页新建账套的JS脚本文件。
 */
East=function(){

	var wsBalance = null;
	var wsVoucher = null;

	return {
		//初始化
		init : function(){
			East.bindEvent();
			East.connect('balance');
			East.connect('voucher');
		},

		//绑定事件
		bindEvent : function(){
			var $btn_xjzt = $("#btn_xjzt");
		    $btn_xjzt.click(function () {
				App.openWin('<i class="fa fa-list-alt"></i>新建账套', 550,400,'account/book/main',function(){
					$('#book_name').next('span').find('input').focus();
				});
		    });
		},

		/**
		 * 新建websocket连接
		 */
		connect : function(type){

			var h1 = ($('#latestBalanceDIV').height()/8-2) + 'px';
			var h2 = ($('#latestVoucherDIV').height()/9-2) + 'px';

			var target = '/latest/info/list';
			if(type==='balance'){
				wsBalance = new SockJS(target);
				wsBalance.onopen = function () {
					//如果websocket 连接已经打开，通知后台。后台开始推送数据。
					var msg = East.createMsg(1);
					East.sendMessage(wsBalance,msg);
					setInterval(function(){East.sendMessage(wsBalance,msg)},30000);
				};
				wsBalance.onmessage = function (event) {
					var data  = event.data;
					var m = $.parseJSON(data);
					//最新余额列表
					var array = m['latestBalance']||[];
					var html='';
					for(var i in array){
						var c = array[i]['subject_code'];
						var n = array[i]['subject_name'];
						var b = array[i]['balance'];
						html += '<tr>'+
							'<td style="height: '+h1+';"><a title="点击查看明细" href="javascript:void(0);" onclick="East.balanceDetail('+c+')">' + n + '</a></td>'+
							'<td style="text-align: right;height: '+h1+';">' + b + '</td>'+
							'</tr>';
						$('#latestBalanceTB').html(html);
					}

					//移除fa-spin样式
					$('#refresh1').removeClass('fa-spin');
				};
				wsBalance.onclose = function () {
					//console.log('Info: WebSocket connection closed.');
				};
			}else if(type==='voucher'){
				wsVoucher = new SockJS(target);
				wsVoucher.onopen = function () {
					//如果websocket 连接已经打开，通知后台。后台开始推送数据。
					var msg = East.createMsg(2);
					East.sendMessage(wsVoucher,msg);
					setInterval(function(){East.sendMessage(wsVoucher,msg)},30000);
				};
				wsVoucher.onmessage = function (event) {
					var data  = event.data;
					var m = $.parseJSON(data);
					//最新凭证列表
					var array = m['latestVoucher']||[];
					var html='<tr>'+
						'<td style="color:#009966">日期</td>'+
						'<td style="color:#009966">凭证字号</td>'+
						'<td style="color:#009966">摘要</td>'+
						'<td style="text-align: right;color:#009966;">合计数</td>'+
						'</tr>';
					for(var i in array){
						var a = array[i]['voucher_id'];
						var b = array[i]['voucher_time'];
						var c = array[i]['voucher_word'];
						var d = array[i]['summary'];
						var e = array[i]['count_sum'];
						html += '<tr>'+
							'<td style="height: '+h2+';">' + b + '</td>'+
							'<td><a title="点击查看明细" href="javascript:void(0);" onclick="East.voucherDetail('+a+')">' + c + '</a></td>'+
							'<td>' + d + '</td>'+
							'<td style="text-align: right;">' + e + '</td>'+
							'</tr>';
						$('#latestVoucherTB').html(html);
					}

					//移除fa-spin样式
					$('#refresh2').removeClass('fa-spin');
				};
				wsVoucher.onclose = function () {
					//console.log('Info: WebSocket connection closed.');
				};
			}
		},

		/**
		 * 断开websocket连接
		 */
		disconnect : function(websocket) {
			if (websocket != null) {
				websocket.close();
				websocket = null;
			}
		},

		/**
		 * 发送消息.
		 */
		sendMessage : function(websocket,message) {
			if (websocket != null) {
				//console.log('Sent: ' + message);
				websocket.send(message);
			} else {
				//alert('WebSocket connection not established, please connect.');
			}
		},

		/**
		 * 组装消息.
		 * @param type
		 */
		createMsg:function(type){
			var msg = "{ready:true,type:"+type+",currentPeriod:"+$('#currentPeriodId').val()+",bookId:"+$('#currentAccountBookId').val()+"}";
			return msg;
		},

		/**
		 * 刷新最新余额列表.
		 */
		refresh:function(type){
			if(type==='balance'){
				var msg = East.createMsg(1);
				$('#refresh1').addClass('fa-spin');
				East.sendMessage(wsBalance,msg);
			}else if(type==='voucher'){
				var msg = East.createMsg(2);
				$('#refresh2').addClass('fa-spin');
				East.sendMessage(wsVoucher,msg);
			}
		},

		/**
		 * 查看明细账.
		 * @param subjectCode
		 */
		balanceDetail:function(subjectCode){
			App.addVoucherTab('明细账','search/detail/main?subjectCode='+subjectCode,true);
		},

		/**
		 * 查看凭证明细.
		 * @param voucherId
		 */
		voucherDetail:function(voucherId){
			App.addVoucherTab('记账', 'voucher/main?voucherId='+voucherId, true);
		}

	};
}();