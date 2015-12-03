<div class="top-bar">
	<div class="info">
		<b><@spring.message code="app.title" /></b>
		<@security.authorize ifAnyGranted="ROLE_ADMIN,ROLE_USER">
        	-
			【
				<span style="font-size: 12px;">
					活动账套：
					<#if CURRENT_ACCOUNT_BOOK??>${CURRENT_ACCOUNT_BOOK.bookName}<input id="currentAccountBookId" type="hidden" value="${CURRENT_ACCOUNT_BOOK.id}"/></#if>
                    &#8194;<#if CURRENT_YEAR??>${CURRENT_YEAR}</#if>
					<#if CURRENT_PERIOD??>第${CURRENT_PERIOD.currentPeriod}期<input id="currentPeriodId" type="hidden" value="${CURRENT_PERIOD.id}"/></#if>
                    &#8194;&#8194;&#8194;
					<input id="accountBookList" style="border-radius: 0px;display: none;"/>
                    <a id="switchBtn" href="javascript:void(0);">切换</a>
					<a id="confirmSwitchBtn" style="display: none; margin-left: 5px;" href="javascript:void(0);">保存</a>
					<a id="cancelSwitchBtn" style="display: none; margin-left: 5px;" href="javascript:void(0);">取消</a>
					<i id="busyIcon" style="display: none;" class="fa fa-spinner fa-pulse"></i>
				</span>
			】
		</@security.authorize>
	</div>

	<div class="action">
		<ul>
			<#--<li><a href="#"><i class="fa fa-fax"></i>&#8194;客服</a></li>-->
			<#--<li><a id="helpLink" href="javascript:void(0);"><i class="fa fa-exclamation-circle"></i>&#8194;帮助</a></li>-->
			<li><a id="aboutLink" href="javascript:void(0);"><i class="fa fa-exclamation-circle"></i>&#8194;关于</a></li>
			<div id="aboutWin" class="easyui-window"
	            title="<i class='fa fa-exclamation-circle'></i>&#8194;关于"
	            style="width:400px;height:150px"
	            data-options="href:'/about',modal:true,collapsible:false,minimizable:false,maximizable:false,resizable:false,closed:true">

			</div>
			<@security.authorize ifAnyGranted="ROLE_ADMIN,ROLE_USER">
				<input type="hidden" id="redirectFlag" value="true"/>
                <li><a id="logoutLink" href="javascript:void(0);"><i class="fa fa-user"></i>&#8194;<@security.authentication property="name"/></a></li>
				<div id="passwordWin" class="easyui-window"
					 title="<i class='fa fa-lock'></i>&#8194;修改密码"
                     style="width:400px;height:220px"
                     data-options="modal:true,collapsible:false,maximizable:false,resizable:false,closed:true,
                     	onLoad:function(){
                            $('#old_passwd').next('span').find('input').focus();
                        },
                        onClose:function(){
                            $('#passwordForm_msg').hide();
                            $('#passwordForm').form('reset');
                        }">
					<form id="passwordForm" method="post">
						<div style="padding-top: 20px; text-align: center;">
							<div id="passwordForm_msg" style="margin-bottom:10px;width: 100%;display: none;">

							</div>
							<div style="margin-bottom:10px;width: 100%;">
								<input id="old_passwd" name="old_passwd" class="easyui-textbox" type="password" style="width:80%;height:30px;padding:12px"
									   data-options="required:true,missingMessage:'请输入旧密码',validType:'minLength[5]',prompt:'旧密码'">
							</div>
							<div style="margin-bottom:10px;width: 100%;">
								<input id="new_passwd" name="new_passwd" class="easyui-textbox" type="password" style="width:80%;height:30px;padding:12px"
									   data-options="required:true,missingMessage:'请输入新密码',validType:'minLength[5]',prompt:'新密码'">
							</div>
							<div style="width: 100%;">
								<a id="savePasswdBtn" href="javascript:TopBar.savePassWd();" class="button button-raised button-rounded" style="width:80%;"><i class="fa fa-floppy-o"></i> 保&#8194;&#8194;存</a>
							</div>
						</div>
					</form>
				</div>
			</@security.authorize>
		</ul>
	</div>
</div>