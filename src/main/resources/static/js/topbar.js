TopBar = function(){

    return {

        init:function(){
            this.bindEvent();
        },

        bindEvent: function () {

            //关于按钮
            $('#aboutLink').click(function(){
                $('#aboutWin').panel('open');
            });

            //绑定注销按钮事件
            $('#logoutLink').tooltip({
                content: $('<div style="text-align: center;font-size: 11px;">' +
                    '<i class="fa fa-key"></i>&#8194;<a id="passwordLink" href="javascript:void(0);" style="color: #3c3c3c;text-decoration: none; ">密码</a></br></br>' +
                    '<i class="fa fa-sign-out"></i>&#8194;<a href="/logout" style="color: #3c3c3c;text-decoration: none; ">注销</a>' +
                    '</div>'),
                onUpdate: function(content){
                    content.panel({
                        width: 80,
                        border: false
                    });
                },

                onShow: function(){
                    var t = $(this);
                    t.tooltip('tip').unbind().bind('mouseenter', function(){
                        t.tooltip('show');
                    }).bind('mouseleave', function(){
                        t.tooltip('hide');
                    });

                    //绑定事件
                    $('#passwordLink').click(function(){
                        $('#passwordWin').panel('open');
                    });
                }
            });

            //切换账套按钮单击事件
            $('#switchBtn').click(function(){
                $('#accountBookList').combobox({
                    url:'account/book/list',
                    valueField:'id',
                    textField:'bookName',
                    editable:false,
                    onSelect: function(rec){
                        if(rec.id == $('#currentAccountBookId').val()){
                            $('#confirmSwitchBtn').attr('disabled','disabled');
                        }
                    },
                    onLoadSuccess:function(){
                        $('#accountBookList').combobox('setValue', $('#currentAccountBookId').val());
                        $('#busyIcon').hide();
                        $('#confirmSwitchBtn').show();
                        $('#cancelSwitchBtn').show();
                    }
                });
                $('.top-bar .info .textbox-text').css({'padding-top':'3px'});
                $("#accountBookList + .combo").show();
                $('#switchBtn').hide();
                $('#busyIcon').show();
            });

            //确认切换账套按钮单击事件
            $('#confirmSwitchBtn').click(function(){
                var id = $('#accountBookList').combobox('getValue');
                if(id != $('#currentAccountBookId').val()){
                    $(this).hide();
                    $('#cancelSwitchBtn').hide();
                    $('#busyIcon').show();
                    $.ajax({
                        url: "switch/to/book/" + id,
                        context: document.body,
                        success:function(data){
                            if(data){
                                document.location.reload();
                            }else{
                                $('#busyIcon').hide();
                                $(this).show();
                                $('#cancelSwitchBtn').show();
                                $.messager.alert("提示信息", "切换账套失败，请稍候重试！","error");
                            }
                        }
                    });
                }
            });

            //取消切换按钮单击事件
            $('#cancelSwitchBtn').click(function(){
                $("#accountBookList + .combo").hide();
                $('#confirmSwitchBtn').hide();
                $('#cancelSwitchBtn').hide();
                $('#switchBtn').show();
            });
        },

        //保存密码
        savePassWd:function(){

            $f = $('#passwordForm');
            $o = $('#old_passwd');
            $n = $('#new_passwd');
            $m = $('#passwordForm_msg');
            if(!$f.form('validate')){
                if(!$o.textbox('validate')){
                    $o.focus();
                }else if(!$n.textbox('validate')){
                    $n.focus();
                };
                return;
            }

            //去掉a标签中的onclick事件
            $('#savePasswdBtn').removeAttr('href');
            $("#savePasswdBtn>i").addClass("fa-spinner",true);
            $.ajax({
                url: "/password/save",
                context: document.body,
                method: 'POST',
                data:{
                    'oldPasswd': $.trim($o.val()),
                    'newPasswd': $.trim($n.val())
                },
                success:function(data){
                    $('#savePasswdBtn').attr('href',"javascript:TopBar.savePassWd();");
                    $("#savePasswdBtn>i").removeClass("fa-spinner",true);
                    $m.html('<label style="width:80%;"><i class="fa fa-info-circle"></i>&#8194;'+data.msg+'</label>')
                        .css({"color":data.success?"#009966":"#CC3333"})
                        .show();
                    if($(data.success)){
                        $f.form('reset');
                    }else{
                        $o.focus();
                    }
                }
            });
        }
    };
}();

$(function(){
    TopBar.init();
});