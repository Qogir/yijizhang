/**
 * 记账模式凭证的JS脚本文件。
 */
VoucherTemplate = function () {
    var templateEditIndex = undefined;//编辑行索引
    var voucherTemWin;

    return {
        //模式凭证列表页面初始化
        init: function () {
            //关闭
            $('#templateClose').click(function () {
                Voucher.theAddWin.window('close');
            });
            //新增
            $('#templateAdd').click(function () {
                VoucherTemplate.templateAdd();
            });
            //修改
            $('#templateEdit').click(function () {
                var row = $('#voucherTemplateDg').datagrid('getSelected');
                if (!row) {
                    $.messager.alert('警告', "请选择一个模式凭证!", 'warning');
                    return;
                }
                VoucherTemplate.templateAdd(row.id, row.name);
            });
            //删除
            $('#templateRemove').click(function () {
                var row = $('#voucherTemplateDg').datagrid('getSelected');
                if (!row) {
                    $.messager.alert('警告', "请选择一个模式凭证!", 'warning');
                    return;
                }
                VoucherTemplate.templateRemove(row.id);
            });
            //类别编辑
            $('#templateTypeEdit').click(function () {
                CompanyCommonValue.openWindow('模式凭证类别', '2');
            });
            //显示模式
            $('#templateShow').click(function () {

            });
            //模式凭证列表
            $('#voucherTemplateDg').datagrid({
                singleSelect: true,
                fitColumns: true,
                fit: true,
                url: 'voucher/template/list',
                method: 'get',
                onDblClickRow: VoucherTemplate.onDblClickRow,
                columns: [[
                    {field: 'name', title: '名称', width: 60, halign: 'center'},
                    {field: 'voucherWord', title: '凭证字', width: 60, halign: 'center'},
                    {field: 'typeName', title: '类别', width: 60, halign: 'center'}
                ]]
            });
        },
        //模式凭证编辑页面初始化
        addInit: function (id, typeName, voucherWord) {
            //编辑页面表格
            $('#voucherTemplateDetailDg').datagrid({
                singleSelect: true,
                fitColumns: true,
                //fit:true,
                toolbar: '#voucherTemplateMenu,#voucherTemplateTb',
                onClickCell: VoucherTemplate.onClickCell,
                url: 'voucher/template/detail/list',
                queryParams: {voucherTemplateId: id},
                method: 'get',
                showFooter: true,
                columns: [[
                    {
                        field: 'summary',
                        title: '摘要',
                        width: 60,
                        halign: 'center',
                        editor: {type: 'textbox', options: {multiline: true, validType: 'voucherMaxLength[80]'}}
                    },
                    {
                        field: 'subjectCode',
                        title: '会计科目',
                        width: 40,
                        halign: 'center',
                        formatter: function (value, row) {
                            return row.subjectTextName;
                        },
                        editor: {
                            type: 'combobox',
                            options: {
                                valueField: 'subjectCode',
                                textField: 'subjectTextName',
                                method: 'get',
                                url: '/voucher/subjectlist',
                                onBeforeLoad: function () {
                                    if (Voucher.subjectData) {
                                        $(this).combobox('loadData', Voucher.subjectData);
                                        return false;
                                    }
                                },
                                onLoadSuccess: function () {
                                    if (!Voucher.subjectData) {
                                        Voucher.subjectData = $(this).combobox('getData');
                                    }
                                }
                            }
                        }
                    },
                    {
                        field: 'newdebit', title: '借方金额', width: 20, editor: {
                        type: 'numberbox',
                        options: {
                            precision: 2,
                            min: -999999999.99,
                            max: 999999999.99
                        }
                    }
                    },
                    {
                        field: 'newcrebit', title: '贷方金额', width: 20, editor: {
                        type: 'numberbox',
                        options: {
                            precision: 2,
                            min: -999999999.99,
                            max: 999999999.99
                        }
                    }
                    }
                ]],
                onLoadSuccess: function (data) {
                    if (!(data && data.total >= 1)) {
                        $('#voucherTemplateDetailDg').datagrid('appendRow', {});
                    }
                    templateEditIndex = undefined;
                    $('#voucherTemplateDetailDg').datagrid('fitColumns');
                }
            });
            //新增
            $('#templateAddAdd').click(function () {
                VoucherTemplate.templateAdd();
            });
            //保存
            $('#templateSave').click(function () {
                if (!VoucherTemplate.endEditing()) {
                    return;
                }
                var voucherTemplateName = $('#voucherTemplateName').textbox('getValue');
                var voucherTemplateId = $('#voucherTemplateId').val();
                VoucherTemplate.save('#templateSave', '#voucherTemplateFm', voucherTemplateName, voucherTemplateId, '#voucherTemplateDetailDg', 1);
            });
            //取消修改
            $('#templateReject').click(function () {
                VoucherTemplate.voucherTemWin.panel('refresh');
            });
            //插入行
            $('#templateAppend').click(function () {
                VoucherTemplate.append();
            });
            //删除行
            $('#templateRemoveit').click(function () {
                VoucherTemplate.removeit();
            });
            //模式类别编辑
            $('#typeNameEdit').click(function () {
                CompanyCommonValue.openWindow('模式凭证类别', '2', '#typeName');
            });
            //模式类别
            $('#typeName').combobox({
                url: 'company/common/value/templatetypelist',
                method: 'get',
                valueField: 'showValue',
                textField: 'showValue',
                editable: false,
                required: true,
                onLoadSuccess: function () {
                    $('#typeName').combobox('setValue', typeName);
                }
            });
            //凭证字
            $('#voucherTemplateWord').combobox({
                url: 'company/common/value/voucherwordlist',
                method: 'get',
                valueField: 'showValue',
                textField: 'showValue',
                editable: false,
                onLoadSuccess: function () {
                    if (voucherWord) {
                        $('#voucherTemplateWord').combobox('setValue', voucherWord);
                    } else {
                        $('#voucherTemplateWord').combobox('clear');
                    }
                }
            });
        },
        //保存为模式凭证页面初始化
        saveInit: function () {
            //模式类别编辑
            $('#voucherTemplateSaveTypeNameEdit').click(function () {
                CompanyCommonValue.openWindow('模式凭证类别', '2', '#voucherTemplateSaveTypeName');
            });
            //模式类别
            $('#voucherTemplateSaveTypeName').combobox({
                url: 'company/common/value/templatetypelist',
                method: 'get',
                valueField: 'showValue',
                textField: 'showValue',
                editable: false,
                required: true
            });
            //确定
            $('#voucherTemplateSaveSubmit').click(function () {
                var voucherTemplateName = $('#voucherTemplateSaveName').textbox('getValue');
                VoucherTemplate.save('#voucherTemplateSaveSubmit', '#voucherTemplateSaveFm', voucherTemplateName, '', '#voucherDg', 2);
            });
            //取消
            $('#voucherTemplateSaveReject').click(function () {
                Voucher.theSaveWin.window('close');
            });
        },
        //模式凭证新增
        templateAdd: function (id, name) {
            voucherTemWin = $("<div></div>").window({
                title: '<i class="fa fa-info-circle"></i>' + (name ? '模式凭证 - ' + name : '新增凭证模式'),
                width: 768,
                height: 483,
                modal: true,
                collapsible: false,
                shadow: true,
                href: 'voucher/template/add',
                queryParams: {voucherTemplateId: id},
                onClose: function () {
                    $(this).panel('destroy');
                    voucherTemWin = undefined;
                }
            });
        },
        //模式凭证删除
        templateRemove: function (id) {
            $('#templateRemove').linkbutton('mydisable');
            $.messager.confirm('确认', '确定删除选中模式凭证?', function (r) {
                if (r) {
                    $.ajax({
                        url: "voucher/template/delete",
                        type: 'get',
                        data: {id: id},
                        success: function (data) {
                            $('#templateRemove').linkbutton('myenable');
                            if (data.result) {
                                $.messager.alert('提示', "删除成功!", 'info', function () {
                                    $('#voucherTemplateDg').datagrid('reload');
                                });
                            } else {
                                $.messager.alert('警告', "操作失败，请联系管理员!", 'warning');
                            }
                        },
                        error: function () {
                            $('#templateRemove').linkbutton('myenable');
                        }
                    });
                } else {
                    $('#templateRemove').linkbutton('myenable');
                }
            });
        },
        //结束行编辑
        endEditing: function () {
            if (templateEditIndex == undefined) {
                return true
            }
            if ($('#voucherTemplateDetailDg').datagrid('validateRow', templateEditIndex)) {
                var ed = $('#voucherTemplateDetailDg').datagrid('getEditor', {
                    index: templateEditIndex,
                    field: 'subjectCode'
                });
                if ($(ed.target).combobox('getValue') && Voucher.validateSubjectCode(ed)) {
                    $.messager.alert('警告', "科目代码'" + $(ed.target).combobox('getValue') + "'不存在!", 'warning', function () {
                        $('#voucherTemplateDetailDg').datagrid('selectRow', templateEditIndex);
                    });
                    return false;
                }
                if (Voucher.checkNum($(ed.target).combobox('getText'))) {
                    $(ed.target).combobox('setValue', $(ed.target).combobox('getText'));
                } else {
                    $(ed.target).combobox('setValue', $(ed.target).combobox('getValue'));
                }
                var subjectCode = $(ed.target).combobox('getText');
                $('#voucherTemplateDetailDg').datagrid('getRows')[templateEditIndex]['subjectTextName'] = subjectCode;
                $('#voucherTemplateDetailDg').datagrid('endEdit', templateEditIndex);
                templateEditIndex = undefined;
                return true;
            } else {
                return false;
            }
        },
        //编辑行
        onClickCell: function (index, field) {
            if (templateEditIndex != index) {
                if (VoucherTemplate.endEditing()) {
                    $('#voucherTemplateDetailDg').datagrid('selectRow', index).datagrid('beginEdit', index);
                    var ed = $('#voucherTemplateDetailDg').datagrid('getEditor', {index: index, field: field});
                    if (ed) {
                        ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                    }
                    templateEditIndex = index;
                } else {
                    $('#voucherTemplateDetailDg').datagrid('selectRow', templateEditIndex);
                }
            }
        },
        //插入行
        append: function () {
            if (VoucherTemplate.endEditing()) {
                $('#voucherTemplateDetailDg').datagrid('appendRow', {});
                //templateEditIndex = $('#voucherTemplateDetailDg').datagrid('getRows').length-1;
            }
        },
        //删除行
        removeit: function () {
            if (templateEditIndex == undefined) {
                return
            }
            $('#voucherTemplateDetailDg').datagrid('cancelEdit', templateEditIndex).datagrid('deleteRow', templateEditIndex);
            templateEditIndex = undefined;
        },
        //保存模式凭证
        save: function (savebutton, fm, voucherTemplateName, voucherTemplateId, dgtarget, reloadFlag) {
            $(savebutton).linkbutton('mydisable');
            if (!$(fm).form('validate')) {// 表单验证
                $(savebutton).linkbutton('myenable');
                return;
            }
            // 凭证分录数据
            var params = VoucherTemplate.getChanges(dgtarget);
            if (params == 'moneyerror') {
                $(savebutton).linkbutton('myenable');
                $.messager.alert('警告', "借贷双方不能同时有金额!", 'warning');
                return;
            }
            $.ajax({
                url: "voucher/template/checkname",
                type: 'get',
                data: {name: voucherTemplateName, id: voucherTemplateId},
                success: function (data) {
                    if (!data.result) {
                        $(savebutton).linkbutton('myenable');
                        $.messager.alert('警告', "模式凭证名称不能重复!", 'warning');
                        return;
                    }
                    // 提交保存
                    $.ajax({
                        url: "voucher/template/save",
                        type: 'post',
                        data: $(fm).serialize() + params,
                        success: function (data) {
                            $(savebutton).linkbutton('myenable');
                            if (data.result) {
                                $.messager.alert('提示', "保存成功!", 'info', function () {
                                    if (reloadFlag == 1) {
                                        $('#voucherTemplateDg').datagrid('reload');
                                    } else if (reloadFlag == 2) {
                                        Voucher.theSaveWin.window('close');
                                    }
                                });
                            } else {
                                $.messager.alert('警告', "操作失败，请联系管理员!", 'warning');
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            $(savebutton).linkbutton('myenable');
                        }
                    });
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $(savebutton).linkbutton('myenable');
                }
            });
        },
        //表格修改数据
        getChanges: function (dgtarget) {
            var params = '';
            var rows = $(dgtarget).datagrid('getRows');
            for (i = 0; i < rows.length; ++i) {
                if (rows[i].newdebit && rows[i].newcrebit && !(rows[i].newdebit == 0 || rows[i].newcrebit == 0)) {
                    return "moneyerror";
                }
                if (!rows[i].newdebit || (rows[i].newdebit && rows[i].newdebit == 0)) {
                    rows[i].newdebit = '';
                }
                if (!rows[i].newcrebit || (rows[i].newcrebit && rows[i].newcrebit == 0)) {
                    rows[i].newcrebit = '';
                }
                if (!rows[i].summary) {
                    rows[i].summary = '';
                }
                if (!rows[i].subjectCode) {
                    rows[i].subjectCode = '';
                }
                params += '&' + JSON.stringify(rows[i]);
            }
            params = params.replace(/{/g, '').replace(/}/g, '').replace(/","/g, '&').replace(/"/g, '').replace(/:/g, '=');
            params = params.replace(/,/g, '&').replace(/undefined/g, '');
            return params;
        },
        //模式凭证新增
        onDblClickRow: function (index, row) {
            Voucher.theAddWin.window('close');
            var tab = $TC.tabs('getSelected');  // get selected panel
            tab.panel('refresh', 'voucher/main?voucherTemplateId=' + row.id + '&time=' + new Date().getTime());
        }
    };
}();

/**
 * linkbutton方法扩展
 * @param {Object} jq
 */
$.extend($.fn.linkbutton.methods, {
    /**
     * 激活选项（覆盖重写）
     * @param {Object} jq
     */
    myenable: function (jq) {
        return jq.each(function () {
            var state = $.data(this, 'linkbutton');
            if ($(this).hasClass('l-btn-disabled')) {
                var itemData = state._eventsStore;
                //恢复超链接
                if (itemData.href) {
                    $(this).attr("href", itemData.href);
                }
                //回复点击事件
                if (itemData.onclicks) {
                    for (var j = 0; j < itemData.onclicks.length; j++) {
                        $(this).bind('click', itemData.onclicks[j]);
                    }
                }
                //设置target为null，清空存储的事件处理程序
                itemData.target = null;
                itemData.onclicks = [];
                $(this).removeClass('l-btn-disabled');
            }
        });
    },
    /**
     * 禁用选项（覆盖重写）
     * @param {Object} jq
     */
    mydisable: function (jq) {
        return jq.each(function () {
            var state = $.data(this, 'linkbutton');
            if (!state._eventsStore)
                state._eventsStore = {};
            if (!$(this).hasClass('l-btn-disabled')) {
                var eventsStore = {};
                eventsStore.target = this;
                eventsStore.onclicks = [];
                //处理超链接
                var strHref = $(this).attr("href");
                if (strHref) {
                    eventsStore.href = strHref;
                    $(this).attr("href", "javascript:void(0)");
                }
                //处理直接耦合绑定到onclick属性上的事件
                var onclickStr = $(this).attr("onclick");
                if (onclickStr && onclickStr != "") {
                    eventsStore.onclicks[eventsStore.onclicks.length] = new Function(onclickStr);
                    $(this).attr("onclick", "");
                }
                //处理使用jquery绑定的事件
                var eventDatas = $(this).data("events") || $._data(this, 'events');
                if (eventDatas["click"]) {
                    var eventData = eventDatas["click"];
                    for (var i = 0; i < eventData.length; i++) {
                        if (eventData[i].namespace != "menu") {
                            eventsStore.onclicks[eventsStore.onclicks.length] = eventData[i]["handler"];
                            $(this).unbind('click', eventData[i]["handler"]);
                            i--;
                        }
                    }
                }
                state._eventsStore = eventsStore;
                $(this).addClass('l-btn-disabled');
            }
        });
    }
});
