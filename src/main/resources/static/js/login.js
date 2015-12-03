/**
 * login.js
 * @author:sanlai_lee@qq.com
 * @date: 15/9/25
 */

Login = function(){

    return {

        //初始化
        init:function(){
            //让用户名输入框获得焦点
            $('#username').next('span').find('input').focus();
            this.bindEvent();
        },

        //绑定事件
        bindEvent:function(){
            $('#loginSubmit').click(function(){
                Login.submit();
            });

            $('#loginForm').keypress(function(e){
                if(e.which==13) {
                    Login.submit();
                }
            });
        },

        /**
         * 登录表单提交.
         */
        submit:function(){
            if ($('#username').val() == '' || $('#password').val() == '') {
                return;
            }
            $('#loginText').html('正在登录 ...&#8194;<i class="fa fa-spinner fa-pulse"></i>');
            $('#loginForm').submit();
        }

    }
}();
$(function(){
    //执行初始化函数
    Login.init();
});