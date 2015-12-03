<#import "lib/spring.ftl" as spring/>
<#assign security=JspTaglibs["/WEB-INF/tld/security.tld"] />

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><@spring.message code="app.title" /></title>

    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js" async="true"></script>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js" async="true"></script>

    <script src="/public/js/jquery.min.js"></script>
    <script src="/public/easyui/jquery.easyui.min.js"></script>

    <!-- mergeJSLoginTo:/app/app.login.pack -->
    <script src="/js/topbar.js"></script>
    <script src="/js/login.js"></script>
    <!-- mergeJSLoginTo -->

    <link rel="stylesheet" type="text/css" href="/public/easyui/themes/icon.min.css">
    <link rel="stylesheet" type="text/css" href="/public/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/public/easyui/themes/bootstrap/easyui.min.css">

    <!-- mergeLoginCSSTo:/app/app.login.pack -->
    <link rel="stylesheet" type="text/css" href="/public/buttons/buttons.css">
    <link rel="stylesheet" type="text/css" href="/css/base.css">
    <!-- mergeLoginCSSTo -->
</head>
<body class="easyui-layout">
    <div data-options="region:'north',border:false" style="height:30px;overflow: hidden;">
        <#include "./common/topbar.ftl"/>
    </div>
    <div data-options="region:'center',border:false">
        <form name="loginForm" id='loginForm' action="/login" method="POST">
            <div style="width:400px;margin:10% auto;padding:30px 70px 20px 70px">
                <div style="margin-bottom:10px;color: #CC6600;"><#if failureMsg??><i class="fa fa-exclamation-circle"></i> ${failureMsg}</#if></div>
                <div style="margin-bottom:10px">
                    <input id="username" name="username" class="easyui-textbox" style="width:100%;height:40px;padding:12px"
                           data-options="required:true,missingMessage:'请输入登录账号',prompt:'账号',iconCls:'icon-man',iconWidth:38">
                </div>
                <div style="margin-bottom:20px">
                    <input id="password" name="password" class="easyui-textbox" type="password"
                           style="width:100%;height:40px;padding:12px"
                           data-options="required:true,missingMessage:'请输入登录密码',prompt:'密码',iconCls:'icon-lock',iconWidth:38">
                </div>
                <div>
                    <a id="loginSubmit" href="javascript:void(0);" class="easyui-linkbutton"
                       data-options="iconCls:'icon-ok'" style="padding:5px 0px;width:100%;">
                        <span id="loginText" style="font-size:14px;">登&#8194;&#8194;录</span>
                    </a>
                </div>
            </div>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="height:30px;overflow: hidden;">
        <#include "./common/copyright.ftl" />
    </div>
    <script>
        $(function () {

            //sessionTimeout之后Tab里面的页面跳转到登录页面，形成了嵌套
            // 自己都很瞧不上的垃圾的解决方案。
            if ($('#redirectFlag').val() == 'true') {
                $.messager.alert('提示信息', '您的登录信息已过期，请重新登录！', 'info', function () {
                    document.location.reload();
                });
            }
        });
    </script>
</body>
</html>