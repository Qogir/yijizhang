/**
 * Application通用函数集合
 */

App = function () {

    return{

        //初始化
        init: function () {
            $TC = $('#tabContainer');
            $TC.tabs({
                border: false,
                fit: true,
                tabWidth: 150
            })
        },

        /**
         * 弹出窗口.
         * @param title
         * @param width
         * @param height
         * @param href
         * @param loadFn
         */
        openWin: function (title, width, height, href, loadFn) {
            $('#default_win').window({
                title: title,
                width: width,
                shadow: false,
                closed: false,
                cache: false,
                height: height,
                collapsible: false,
                minimizable: false,
                maximizable: false,
                resizable: false,
                href: href,
                modal: true,
                onOpen: function () {
                    $(this).window('center');
                },
                onLoad: loadFn || function () {
                    ;
                }
            });
        },

        /** 增加新的tab */
        addTab: function (title, href, closable) {
            $TC = $('#tabContainer');
            if ($TC.tabs('exists', title)) {
                $TC.tabs('select', title);
            } else {
                $TC.tabs('add', {
                    id: title,
                    title: title,
                    href: href,
                    selected: true,
                    closable: closable | true,
                    tools: [
                        {
                            iconCls: 'icon-mini-refresh',
                            handler: function () {
                                $TC.tabs('select', title);
                                var tab = $TC.tabs('getSelected');  // get selected panel
                                tab.panel('refresh', href);
                            }
                        }
                    ]
                });
            }
        },

        /** 凭证增加新的tab */
        addVoucherTab: function (title, href, closable) {
            $TC = $('#tabContainer');
            if ($TC.tabs('exists', title)) {
                $TC.tabs('select', title);
                $TC.tabs('getSelected').panel('refresh', href);
            } else {
                App.addTab(title, href, closable);
            }
        },

        /**
         * 选择tab
         * @param title
         */
        selectTab: function (title) {
            $TC = $('#tabContainer');
            $TC.tabs('select', title);
        },

        /**
         * 利润表导出excel.
         * @param filename
         * @param datatable
         */
        profitExportToExcel: function (filename, datatable) {
            var data = $(datatable).datagrid("getData");
            var columns = [], titles = ["项目", "本期金额", "上期金额"], fields = ["cA", "cBVal", "cCVal"], dataJsonStr = JSON.stringify(data);

            dataJsonStr = {filename: filename, titles: titles, fields: fields, data: dataJsonStr};

            $('#dataJsonStr').val(JSON.stringify(dataJsonStr));
            document.exportToExcelForm.submit.click();
        },

        /**
         *  初始化数据.
         * @param filename
         * @param datatable
         */
        initDataExportToExcel: function (filename, datatable) {

            var data = $(datatable).datagrid("getData");
            var titles = ["科目代码", "科目名称", "累计借方", "累计贷方", "方向", "期初余额", "本年累计损益实际发生额" ];
            var fields = ["subjectCode", "subjectName", "totalDebit", "totalCredit", "directionname", "initialLeft", "yearOccurAmount"];

            dataJsonStr = {filename: filename, titles: titles, fields: fields, data: JSON.stringify(data)};
            $('#dataJsonStr').val(JSON.stringify(dataJsonStr));
            document.exportToExcelForm.submit.click();
        },


        /**
         * 导出excel公用方法， 把当前展示表datatable数据导出。
         * @param title
         * @param datatable
         */
        exportToExcel: function (filename, datatable) {

            var data = $(datatable).datagrid("getData");
            var cs = $(datatable).datagrid("options")['columns'];
            var columns = [], titles = [], fields = [], dataJsonStr = JSON.stringify(data);

            //合并column
            var first_column = cs[0];

            for (var i = 0; i < first_column.length; i++) {
                var fc = first_column[i];
                //去除hidden列，有特殊需求可在该列加show=true.
                if (fc.hidden) {
                    if (fc.show) {
                    } else {
                        continue;
                    }
                }
                //解析title,field
                if (fc["field"]) {
                    titles.push(fc["title"]);
                    fields.push(fc["field"]);
                } else {
                    if (fc["colspan"]) {
                        var cols_num = parseInt(fc["colspan"]);
                        var title = fc["title"];
                        if (cs[1]) {
                            for (var j = 0; j < cols_num; j++) {
                                var fie = cs[1][j]["field"];
                                var tit = title + "/" + cs[1][j]["title"];
                                titles.push(tit);
                                fields.push(fie);
                            }
                        }
                    }
                }
            }

            dataJsonStr = {filename: filename, titles: titles, fields: fields, data: dataJsonStr};
            $('#dataJsonStr').val(JSON.stringify(dataJsonStr));
            document.exportToExcelForm.submit.click();
        }

    };

}();


//////扩展easyui的功能
$.extend($.fn.validatebox.defaults.rules, {
    minLength: {
        validator: function (value, param) {
            return value.length >= param[0];
        },
        message: '请输入至少{0}个字符'
    }
});

$(function () {
    App.init();

    /** 所有的ajax请求都自动提交_csrf头信息，否则POST方式调用REST会报403. */
    //var token = $("meta[name='_csrf']").attr("content");
    //var header = $("meta[name='_csrf_header']").attr("content");
    //$(document).ajaxSend(function(e, xhr, options) {
    //	xhr.setRequestHeader(header, token);
    //});


    /**
     * 重写jquery ajax
     */
    (function ($) {
        //备份jquery的ajax方法
        var _ajax = $.ajax;

        //重写jquery的ajax方法
        $.ajax = function (opt) {
            //备份opt中error和success方法
            var fn = {
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                },
                success: function (data, textStatus) {
                }
            };
            if (opt.error) {
                fn.error = opt.error;
            }
            if (opt.success) {
                fn.success = opt.success;
            }

            //扩展增强处理
            var _opt = $.extend(opt, {
                //覆盖状态码处理函数，当session过期的时候，能够让ajax请求处理
                statusCode: {
                    401: function () {
                        //如果是顶层窗口
                        if (window.top != window.self) {
                            window.parent.location.href = "/";
                        } else {
                            window.location.href = "/";
                        }
                    },
                    403: function () {
                        $.messager.alert('警告消息', '您的权限不足，请联系管理申请权限！', 'warning');
                        return;
                    },
                    500: function () {
                        $.messager.alert('错误消息', '很抱歉，系统好像正在开小差，请稍候再试！', 'error');
                        return;
                    }
                },

                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    //错误方法增强处理
                    fn.error(XMLHttpRequest, textStatus, errorThrown);
                },
                success: function (data, textStatus) {
                    //成功回调方法增强处理
                    fn.success(data, textStatus);
                }
            });
            _ajax(_opt);
        };
    })(jQuery);
});
