<#import "lib/spring.ftl" as spring/>
<#assign security=JspTaglibs["/WEB-INF/tld/security.tld"] />

<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title><@spring.message code="app.title" /></title>

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
        <#--<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>-->
        <script type="text/javascript" src="/public/js/jquery.min.js"></script>
        <script src="/public/easyui/jquery.easyui.min.js"></script>
        <script src="/js/topbar.js"></script>
        <!-- EasyUI -->
        <link rel="stylesheet" type="text/css" href="/public/easyui/themes/bootstrap/easyui.min.css">
        <!-- buttons -->
        <link rel="stylesheet" type="text/css" href="/public/buttons/buttons.min.css">
        <!-- font-awesome -->
        <link rel="stylesheet" href="/public/font-awesome/css/font-awesome.min.css">
        <link rel="stylesheet" type="text/css" href="/css/base.css">

    </head>
    <body class="easyui-layout">˝
        <div data-options="region:'north',border:false" style="height:30px;overflow: hidden;">
            <#include "./common/topbar.ftl"/>
        </div>
        <div data-options="region:'south',border:false" style="height:30px;overflow: hidden;">
            <#include "common/copyright.ftl" />
        </div>
        <input type="hidden" id="statusCode" value="${status}"/>
        <div id="errorCenter" data-options="region:'center',border:false">
            <div style="margin-top: 38%;margin-left: 40%;">
                <a href="/" class="button button-rounded button-tiny"><i class="fa fa-home"></i> 首页</a>
                <a href="javascript:history.go(-1);" class="button button-rounded button-tiny"><i class="fa fa-history"></i> 返回</a>
            </div>

        </div>
    <script>
        $(function(){
            var $e = $('#errorCenter');
            var status = $('#statusCode').val();
            if(status==401){
                $e.css({'background':'url("/images/401.jpg") no-repeat center','background-size':'100% 100%'});
            }else if(status==403){
                $e.css({'background':'url("/images/403.jpg") no-repeat center','background-size':'100% 100%'});
            }else if(status==404){
                $e.css({'background':'url("/images/404.jpg") no-repeat center','background-size':'100% 100%'});
            }else {
                $e.css({'background':'url("/images/500.jpg") no-repeat center','background-size':'100% 100%'});
            }
        });
    </script>
    </body>
</html>