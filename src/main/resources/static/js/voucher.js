/**
 * 记账凭证的JS脚本文件。
 */
Voucher = function () {
    var editIndex = undefined;//表格编辑index
    var footerdebit = undefined;//初始化总计借方金额
    var footercrebit = undefined;//初始化总计贷方金额
    var debit = undefined;//总计借方金额
    var crebit = undefined;//总计贷方金额
    var balanceFlag = false;//借贷平衡标识符
    var subjectData = undefined;//会计科目代码下来框数据
    var cellFocusFlag;//金额编辑器是否第一次focus标志
    var theAddWin = undefined;
    var theSaveWin = undefined;
    var theCashWin = undefined;
    var cashFalg = false;
    var cashData = undefined;

    return {
        //凭证页面初始化
        init: function (id, period, book, voucherWord, templateId, isreversal) {
            book = book.replace(',', '');
            //新增
            $('#voucherAdd,#voucherMm1Add').click(function () {
                Voucher.add();
            });
            //从模式凭证新增
            $('#voucherTempAdd,#voucherTempMm1Add').click(function () {
                Voucher.theAddWin = $('<div></div>').window({
                    title: '<i class="fa fa-info-circle"></i>模式凭证',
                    width: 677,
                    height: 440,
                    modal: true,
                    collapsible: false,
                    shadow: true,
                    href: 'voucher/template',
                    onClose: function () {
                        $(this).panel('destroy');
                        Voucher.theAddWin = undefined;
                    }
                });
            });
            //保存并新增
            $('#voucherSave,#voucherMm2Save2').click(function () {
                Voucher.save(1, id);
            });
            //保存
            $('#voucherMm2Save1').click(function () {
                Voucher.save('', id);
            });
            //保存为模式凭证
            $('#voucherTempSave,#voucherTempMm2Save').click(function () {
                var name;
                var rows = $('#voucherDg').datagrid('getRows');
                var ed = $('#voucherDg').datagrid('getEditor', {index: 0, field: 'summary'});
                var voucherWord = $('#voucherWord').combobox('getValue');
                if (ed && $(ed.target).textbox('getText')) {
                    name = $(ed.target).textbox('getText');
                } else if (!ed && rows && rows.length > 0 && rows[0].summary) {
                    name = rows[0].summary;
                } else {
                    name = voucherWord + $('#voucherNo').numberspinner('getValue');
                }
                Voucher.theSaveWin = $('<div></div>').window({
                    title: '<i class="fa fa-info-circle"></i>保存为模式凭证',
                    width: 325,
                    height: 375,
                    modal: true,
                    collapsible: false,
                    shadow: true,
                    href: 'voucher/template/save',
                    queryParams: {name: name, voucherWord: voucherWord},
                    onClose: function () {
                        $(this).panel('destroy');
                        Voucher.theSaveWin = undefined;
                    }
                });
            });
            //取消修改
            $('#voucherReject').click(function () {
                Voucher.reject();
            });
            //插入行
            $('#voucherAppend').click(function () {
                Voucher.append();
            });
            //删除行
            $('#voucherRemoveit').click(function () {
                Voucher.removeit();
            });
            //流量
            $('#cashFlowData').click(function () {
                //Voucher.cashFormData();
            });
            //明细
            $('#voucherSubjectDetail').click(function () {
                var subjectCode;
                if (editIndex >= 0) {
                    var ed = $('#voucherDg').datagrid('getEditor', {index: editIndex, field: 'subjectCode'});
                    if (Voucher.validateSubjectCode(ed)) {
                        $.messager.alert('警告', "科目代码'" + $(ed.target).combobox('getValue') + "'不存在或不是明细科目!", 'warning');
                        return;
                    }
                    if (Voucher.checkNum($(ed.target).combobox('getText'))) {
                        $(ed.target).combobox('setValue', $(ed.target).combobox('getText'));
                    } else {
                        $(ed.target).combobox('setValue', $(ed.target).combobox('getValue'));
                    }
                    subjectCode = $(ed.target).combobox('getText');
                } else {
                    subjectCode = $('#voucherDg').datagrid('getRows')[0]['subjectCode'];
                }
                if (!subjectCode) {
                    $.messager.alert('警告', "科目代码不能为空!", 'warning');
                    return;
                }
                if (subjectCode && period) {
                    App.addVoucherTab('明细账', 'search/detail/main?subjectCode=' + subjectCode.split(' ')[0] + '&currentPeriod=' + period, true);
                }
            });
            //科目余额
            $('#voucherSubjectBalance').click(function () {
                var subjectCode;
                if (editIndex >= 0) {
                    var ed = $('#voucherDg').datagrid('getEditor', {index: editIndex, field: 'subjectCode'});
                    if (Voucher.validateSubjectCode(ed)) {
                        $.messager.alert('警告', "科目代码'" + $(ed.target).combobox('getValue') + "'不存在或不是明细科目!", 'warning');
                        return;
                    }
                    if (Voucher.checkNum($(ed.target).combobox('getText'))) {
                        $(ed.target).combobox('setValue', $(ed.target).combobox('getText'));
                    } else {
                        $(ed.target).combobox('setValue', $(ed.target).combobox('getValue'));
                    }
                    subjectCode = $(ed.target).combobox('getText');
                } else {
                    subjectCode = $('#voucherDg').datagrid('getRows')[0]['subjectCode'];
                }
                if (!subjectCode) {
                    $.messager.alert('警告', "科目代码不能为空!", 'warning');
                    return;
                }
                $('<div></div>').window({
                    title: '<i class="fa fa-info-circle"></i>科目余额',
                    width: 1000,
                    height: 500,
                    modal: true,
                    collapsible: false,
                    shadow: true,
                    href: 'voucher/balance',
                    queryParams: {
                        subjectCode: subjectCode,
                        voucherId: id
                    },
                    onClose: function () {
                        $(this).panel('destroy');
                    }
                });
            });
            //凭证编制说明
            $('#voucherHelp').click(function () {
                $('<div></div>').window({
                    title: '<i class="fa fa-info-circle"></i>凭证编制说明',
                    width: 650,
                    height: 500,
                    modal: true,
                    collapsible: false,
                    shadow: true,
                    href: 'voucher/help',
                    onClose: function () {
                        $(this).panel('destroy');
                    }
                });
            });
            //凭证字编辑
            $('#voucherWordEdit').click(function () {
                CompanyCommonValue.openWindow('凭证字', '1', '#voucherWord');
            });
            //凭证字
            $('#voucherWord').combobox({
                url: 'company/common/value/voucherwordlist',
                method: 'get',
                valueField: 'showValue',
                textField: 'showValue',
                editable: false,
                required: true,
                onLoadSuccess: function () {
                    if (voucherWord) {
                        $('#voucherWord').combobox('setValue', voucherWord);
                    } else {
                        $('#voucherWord').combobox('setValue', '记');
                    }
                }
            });
            //日期
            $('#voucherTime').datebox({
                formatter: Voucher.myformatter,
                parser: Voucher.myparser,
                required: true,
                editable: false,
                validType: {
                    mydate: [book, period]
                }
            }).datebox('calendar').calendar({
                validator: function (date) {
                    var d1 = new Date(book, period - 1, 1);
                    var d2 = new Date(book, period, 1);
                    return d1 <= date && date < d2;
                }
            });

            //凭证分录表格
            $('#voucherDg').datagrid({
                heigth: 400,
                singleSelect: true,
                fitColumns: true,
                //fit:true,
                toolbar: '#voucherMenu,#voucherDgTd',
                onClickCell: Voucher.onClickCell,
                url: 'voucher/detail/list',
                queryParams: {voucherId: id, voucherTemplateId: templateId, isreversal: isreversal},
                method: 'get',
                showFooter: true,
                columns: [[
                    {
                        field: 'summary', title: '摘要', width: 60, halign: 'center',
                        editor: {
                            type: 'textbox',
                            options: {
                                multiline: true,
                                validType: 'voucherMaxLength[80]'
                            }
                        }, rowspan: 2
                    },
                    {
                        field: 'subjectCode', title: '会计科目', width: 60, halign: 'center',
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
                                    var tthis = $(this);
                                    $(this).textbox('textbox').bind('dblclick', function () {
                                        Account_Subject.open_subject_search_win(function (record) {
                                            tthis.combobox('setText', record.subject_code + ' ' + record.subject_name);
                                            tthis.combobox('setValue', record.subject_code);
                                        });
                                    });
                                }
                            }
                        }, rowspan: 2
                    },
                    {title: '借方金额', colspan: 11},
                    {title: '贷方金额', colspan: 11}
                ], [
                    {
                        field: 'dHundredMillion',
                        title: '亿',
                        width: 5,
                        align: 'center',
                        editor: {
                            type: 'textMax',
                            options: {num: 10000000000, direction: 'd', field: 'dHundredMillion'}
                        },
                        formatter: Voucher.formatDebit
                    },
                    {
                        field: 'dTenMillions',
                        title: '千',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 1000000000, direction: 'd', field: 'dTenMillions'}},
                        formatter: Voucher.formatDebit
                    },
                    {
                        field: 'dMillions',
                        title: '百',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 100000000, direction: 'd', field: 'dMillions'}},
                        formatter: Voucher.formatDebit
                    },
                    {
                        field: 'dHundredThousand',
                        title: '十',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 10000000, direction: 'd', field: 'dHundredThousand'}},
                        formatter: Voucher.formatDebit
                    },
                    {
                        field: 'dTenThousand',
                        title: '万',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 1000000, direction: 'd', field: 'dTenThousand'}},
                        formatter: Voucher.formatDebit
                    },
                    {
                        field: 'dThousand',
                        title: '千',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 100000, direction: 'd', field: 'dThousand'}},
                        formatter: Voucher.formatDebit
                    },
                    {
                        field: 'dHundred',
                        title: '百',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 10000, direction: 'd', field: 'dHundred'}},
                        formatter: Voucher.formatDebit
                    },
                    {
                        field: 'dTen',
                        title: '十',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 1000, direction: 'd', field: 'dTen'}},
                        formatter: Voucher.formatDebit
                    },
                    {
                        field: 'dYuan',
                        title: '元',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 100, direction: 'd', field: 'dYuan'}},
                        formatter: Voucher.formatDebit
                    },
                    {
                        field: 'dAngle',
                        title: '角',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 10, direction: 'd', field: 'dAngle'}},
                        formatter: Voucher.formatDebit
                    },
                    {
                        field: 'dCent',
                        title: '分',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 1, direction: 'd', field: 'dCent'}},
                        formatter: Voucher.formatDebit
                    },
                    {
                        field: 'crHundredMillion',
                        title: '亿',
                        width: 5,
                        align: 'center',
                        editor: {
                            type: 'textMax',
                            options: {num: 10000000000, direction: 'cr', field: 'crHundredMillion'}
                        },
                        formatter: Voucher.formatCredit
                    },
                    {
                        field: 'crTenMillions',
                        title: '千',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 1000000000, direction: 'cr', field: 'crTenMillions'}},
                        formatter: Voucher.formatCredit
                    },
                    {
                        field: 'crMillions',
                        title: '百',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 100000000, direction: 'cr', field: 'crMillions'}},
                        formatter: Voucher.formatCredit
                    },
                    {
                        field: 'crHundredThousand',
                        title: '十',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {
                            type: 'textMax',
                            options: {num: 10000000, direction: 'cr', field: 'crHundredThousand'}
                        },
                        formatter: Voucher.formatCredit
                    },
                    {
                        field: 'crTenThousand',
                        title: '万',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 1000000, direction: 'cr', field: 'crTenThousand'}},
                        formatter: Voucher.formatCredit
                    },
                    {
                        field: 'crThousand',
                        title: '千',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 100000, direction: 'cr', field: 'crThousand'}},
                        formatter: Voucher.formatCredit
                    },
                    {
                        field: 'crHundred',
                        title: '百',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 10000, direction: 'cr', field: 'crHundred'}},
                        formatter: Voucher.formatCredit
                    },
                    {
                        field: 'crTen',
                        title: '十',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 1000, direction: 'cr', field: 'crTen'}},
                        formatter: Voucher.formatCredit
                    },
                    {
                        field: 'crYuan',
                        title: '元',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 100, direction: 'cr', field: 'crYuan'}},
                        formatter: Voucher.formatCredit
                    },
                    {
                        field: 'crAngle',
                        title: '角',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 10, direction: 'cr', field: 'crAngle'}},
                        formatter: Voucher.formatCredit
                    },
                    {
                        field: 'crCent',
                        title: '分',
                        width: 5,
                        align: 'center',
                        editor: 'textbox',
                        editor: {type: 'textMax', options: {num: 1, direction: 'cr', field: 'crCent'}},
                        formatter: Voucher.formatCredit
                    }
                ]],
                onLoadSuccess: function (data) {
                    if (!(data && data.total >= 5)) {
                        var len = data && data.total > 0 ? 5 - data.total : 5;
                        for (i = 0; i < len; ++i) {
                            $('#voucherDg').datagrid('appendRow', {});
                        }
                    }
                    Voucher.initFooterCells();
                    editIndex = undefined;
                    $('#voucherDg').datagrid('fitColumns');
                    //$('#voucherLayout').find('.datagrid-row').css('height', '50px');
                    //$('#voucherDg').datagrid('fixRowHeight');
                }
            });
        },
        //流量
        cashFormData: function (callSave) {
            Voucher.theCashWin = $('<div></div>').window({
                title: '<i class="fa fa-info-circle"></i>凭证编制说明',
                width: 668,
                height: 266,
                modal: true,
                collapsible: false,
                shadow: true,
                href: 'voucher/cashFlowData',
                onClose: function () {
                    $(this).panel('destroy');
                    Voucher.theCashWin = undefined;
                    if (VoucherCash.saveFlag && callSave) {
                        callSave();
                    }
                }
            });
        },
        //验证科目代码合法性
        validateSubjectCode: function (ed) {
            var flag = false;
            var subjectCode = $(ed.target).combobox('getValue');
            $.ajax({
                url: "voucher/checkSubjectCode",
                type: 'get',
                data: {subjectCode: subjectCode},
                async: false,
                success: function (data) {
                    flag = data && data.result;
                    if (!flag) {
                        Voucher.subjectData = undefined;
                        $(ed.target).combobox('reload');
                    }
                }
            });
            return !flag;
        },
        //验证科目代码合法性
        validateSubjectCodeWithCode: function (subjectCode) {
            var flag = false;
            $.ajax({
                url: "voucher/checkSubjectCode",
                type: 'get',
                data: {subjectCode: subjectCode},
                async: false,
                success: function (data) {
                    flag = data && data.result;
                }
            });
            return !flag;
        },
        //合并footer中的单元格
        mergeFooterCells: function () {
            $('#voucherDg').datagrid('mergeCells', {
                index: 0,
                field: 'summary',
                colspan: 2,
                type: 'footer'
            });
        },
        //初始化总计金额
        initFooterCells: function () {
            var row = $('#voucherDg').datagrid('getFooterRows')[0];
            footerdebit = Voucher.cellSetRealNum(row['debit']);
            footercrebit = Voucher.cellSetRealNum(row['credit']);
            debit = Voucher.cellSetRealNum(row['debit']);
            crebit = Voucher.cellSetRealNum(row['credit']);
            Voucher.changeRowMoney(debit, "d");
            Voucher.changeRowMoney(crebit, "cr");
            Voucher.footerDX();
        },
        //结束编辑
        endEditing: function () {
            if (editIndex == undefined) {
                return true
            }
            if ($('#voucherDg').datagrid('validateRow', editIndex)) {
                var summary = $($('#voucherDg').datagrid('getEditor', {
                    index: editIndex,
                    field: 'summary'
                }).target).textbox('getText');
                var ed = $('#voucherDg').datagrid('getEditor', {index: editIndex, field: 'subjectCode'});
                var debitMoney = $('#voucherDg').datagrid('getRows')[editIndex]['newdebit'];
                var crditMoney = $('#voucherDg').datagrid('getRows')[editIndex]['newcrebit'];
                if (Voucher.checkNum($(ed.target).combobox('getText'))) {
                    $(ed.target).combobox('setValue', $(ed.target).combobox('getText'));
                } else {
                    $(ed.target).combobox('setValue', $(ed.target).combobox('getValue'));
                }
                if ((summary || (debitMoney && debitMoney != 0) || (crditMoney && crditMoney != 0)) && !$(ed.target).combobox('getValue')) {
                    $.messager.alert('警告', "科目代码不能为空!", 'warning', function () {
                        $('#voucherDg').datagrid('selectRow', editIndex);
                    });
                    return false;
                }
                if ($(ed.target).combobox('getValue') && Voucher.validateSubjectCode(ed)) {
                    $.messager.alert('警告', "科目代码'" + $(ed.target).combobox('getValue') + "'不存在或不是明细科目!", 'warning', function () {
                        $('#voucherDg').datagrid('selectRow', editIndex);
                    });
                    return false;
                }

                var subjectCode = $(ed.target).combobox('getText');
                $('#voucherDg').datagrid('getRows')[editIndex]['subjectTextName'] = subjectCode;
                if (!(debitMoney && debitMoney != 0) && !(crditMoney && crditMoney != 0) && $(ed.target).combobox('getValue')) {
                    $.messager.alert('警告', "凭证分录金额不能为零!", 'warning', function () {
                        $('#voucherDg').datagrid('selectRow', editIndex);
                    });
                    return false;
                }
                if (!(debitMoney && debitMoney != 0)) {
                    Voucher.changeEditorMoney(0, 'd', true);
                }
                if (!(crditMoney && crditMoney != 0)) {
                    Voucher.changeEditorMoney(0, 'cr', true);
                }
                $('#voucherDg').datagrid('endEdit', editIndex);
                editIndex = undefined;
                return true;
            } else {
                return false;
            }
        },
        //编辑表格行
        onClickCell: function (index, field) {
            if (editIndex != index) {
                if (Voucher.endEditing()) {
                    if (index > 0 && !$('#voucherDg').datagrid('getRows')[index - 1]['subjectCode']) {
                        return;
                    }
                    editIndex = index;
                    $('#voucherDg').datagrid('selectRow', index).datagrid('beginEdit', index);
                    var ed = $('#voucherDg').datagrid('getEditor', {index: index, field: field});
                    if (ed) {
                        ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                    }

                    //金额editor初始化需要
                    ed = $('#voucherDg').datagrid('getEditor', {index: editIndex, field: 'summary'});
                    $(ed.target).textbox('textbox').on('focus', function () {
                        cellFocusFlag = null;
                    });
                    ed = $('#voucherDg').datagrid('getEditor', {index: editIndex, field: 'subjectCode'});
                    $(ed.target).textbox('textbox').on('focus', function () {
                        cellFocusFlag = null;
                    });
                    cellFocusFlag = null;
                } else {
                    $('#voucherDg').datagrid('selectRow', editIndex);
                }
                Voucher.changeMoneyColor('d', editIndex);
                Voucher.changeMoneyColor('cr', editIndex);
            }
        },
        changeMoneyColor: function (direction, editIndex) {
            if ($('#voucherDg').datagrid('getRows')[editIndex][direction + 'pn'] == '-') {
                $("." + direction + "textMax").css("color", "red");
            } else {//金额为负时，变红色
                $("." + direction + "textMax").css("color", "black");
            }
        },
        //插入行
        append: function () {
            if (Voucher.endEditing()) {
                $('#voucherDg').datagrid('appendRow', {});
            }
        },
        //删除行
        removeit: function () {
            if (editIndex == undefined) {
                return
            }
            $('#voucherDg').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
            editIndex = undefined;
            Voucher.totalSum('d');
            Voucher.totalSum('cr');
        },
        //取消修改
        reject: function () {
            var tab = $TC.tabs('getSelected');  // get selected panel
            tab.panel('refresh');
        },
        //保存
        save: function (isAdd, id) {
            var $voucherSave = $('#voucherSave');
            $voucherSave.linkbutton('mydisable');
            if (Voucher.endEditing()) {
                if (!$('#voucherFm').form('validate')) {// 表单验证
                    $voucherSave.linkbutton('myenable');
                    return;
                }
                // 凭证分录数据
                var params = Voucher.getChanges();
                if (!params) {
                    $.messager.alert('警告', "无凭证分录!", 'warning', function () {
                        $voucherSave.linkbutton('myenable');
                    });
                    return;
                }
                if (params == 'subjectCodeError') {
                    $.messager.alert('警告', "科目代码不存在或不是明细科目!", 'warning', function () {
                        $voucherSave.linkbutton('myenable');
                    });
                    return;
                }
                if (params == 'moneyNoneError') {
                    $.messager.alert('警告', "借贷双方必须存在一个金额!", 'warning', function () {
                        $voucherSave.linkbutton('myenable');
                    });
                    return;
                }
                if (params == 'moneyBothError') {
                    $.messager.alert('警告', "借贷双方不能同时有金额!", 'warning', function () {
                        $voucherSave.linkbutton('myenable');
                    });
                    return;
                }
                if (!balanceFlag) {// 借贷平衡
                    $.messager.alert('警告', "借贷不平衡!", 'warning', function () {
                        $voucherSave.linkbutton('myenable');
                    });
                    return;
                }
//				if(cashFalg){
//					Voucher.cashFormData(function(){
//						params+=VoucherCash.cashData;
//						Voucher.submit(isAdd,id,params);
//					});
//				} else {
                Voucher.submit(isAdd, id, params);
//				}
            } else {
                $voucherSave.linkbutton('myenable');
            }
        },
        submit: function (isAdd, id, params) {
            var $voucherSave = $('#voucherSave');
            // 检查凭证号
            var $voucherNo = $('#voucherNo');
            var no = $voucherNo.numberspinner('getValue');
            $.ajax({
                url: "voucher/checkno",
                type: 'get',
                data: {no: no, id: id},
                success: function (data) {
                    if (data.result) {
                        $voucherSave.linkbutton('myenable');
                        $voucherNo.numberspinner('setValue', data.result);
                        $.messager.alert('警告', '凭证号' + no + '重复，凭证保存不成功。系统重新编号为' + data.result, 'warning');
                        return;
                    }
                    // 提交保存
                    $.ajax({
                        url: "voucher/save",
                        type: 'post',
                        data: $("#voucherFm").serialize() + params,
                        //dataType:"json",
                        //contentType:"application/json",
                        success: function (data) {
                            if (data.result) {
                                $voucherSave.linkbutton('myenable');
                                if (!$("#id").val()) {
                                    $.messager.alert('提示', "已生成了一张记账凭证，凭证字号为：" + data.result, 'info', function () {
                                        if (isAdd) {//新增
                                            Voucher.add();
                                        }
                                    });
                                } else {
                                    if (isAdd) {//新增
                                        Voucher.add();
                                    }
                                }
                            } else {
                                $voucherSave.linkbutton('myenable');
                                $.messager.alert('警告', "操作失败，请联系管理员!", 'warning', function () {
                                });
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            $voucherSave.linkbutton('myenable');
                        }
                    });
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $voucherSave.linkbutton('myenable');
                }
            });
        },
        //表格数据
        getChanges: function (haveCash) {
            var params = '';
            var rows = $('#voucherDg').datagrid('getRows');
            cashFlag = false;
            var isCashSubject;
            for (i = 0; i < rows.length; ++i) {
                if (rows[i].subjectCode) {
                    if (Voucher.validateSubjectCodeWithCode(rows[i].subjectCode)) {
                        return "subjectCodeError";
                    }
                    isCashSubject = Voucher.isCashSubject(rows[i].subjectCode);
                    if (!cashFlag && isCashSubject) {//存在现金流量
                        cashFlag = true;
                    }
                    if ((!rows[i].newdebit && !rows[i].newcrebit) || (rows[i].newdebit == 0 && rows[i].newcrebit == 0)) {
                        return "moneyNoneError";
                    }
                    if (rows[i].newdebit && rows[i].newcrebit && !(rows[i].newdebit == 0 || rows[i].newcrebit == 0)) {
                        return "moneyBothError";
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
                    params += '&' + JSON.stringify(rows[i]);
                }
            }
            params = params.replace(/{/g, '').replace(/}/g, '').replace(/","/g, '&').replace(/"/g, '').replace(/:/g, '=');
            params = params.replace(/,/g, '&').replace(/undefined/g, '');
            return params;
        },
        //判断是否是现金流量相关科目
        isCashSubject: function (subjectCode) {
            return Voucher.startWith(subjectCode, '1001') || Voucher.startWith(subjectCode, '1002');
        },
        //时间格式化
        myformatter: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
        },
        //时间格式化
        myparser: function (s) {
            if (!s) return new Date();
            var ss = (s.split('-'));
            var y = parseInt(ss[0], 10);
            var m = parseInt(ss[1], 10);
            var d = parseInt(ss[2], 10);
            if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
                return new Date(y, m - 1, d);
            } else {
                return new Date();
            }
        },
        //新增
        add: function () {
            var tab = $TC.tabs('getSelected');  // get selected panel
            tab.panel('refresh', 'voucher/main' + "?time=" + new Date().getTime());
        },
        getParamValue: function (pval, value) {
            if (value && Voucher.checkNum(value)) {
                return value;
            } else if (pval) {
                return 0;
            } else {
                return '';
            }
        },
        //单行ROW金额合计
        getMoney: function (direction, row) {
            var pval = '';
            pval += Voucher.getParamValue(pval, row[direction + 'HundredMillion']);
            pval += Voucher.getParamValue(pval, row[direction + 'TenMillions']);
            pval += Voucher.getParamValue(pval, row[direction + 'Millions']);
            pval += Voucher.getParamValue(pval, row[direction + 'HundredThousand']);
            pval += Voucher.getParamValue(pval, row[direction + 'TenThousand']);
            pval += Voucher.getParamValue(pval, row[direction + 'Thousand']);
            pval += Voucher.getParamValue(pval, row[direction + 'Hundred']);
            pval += Voucher.getParamValue(pval, row[direction + 'Ten']);
            pval += Voucher.getParamValue(pval, row[direction + 'Yuan']);
            if (Voucher.cellSetValue(row[direction + 'Angle']) != 0 || Voucher.cellSetValue(row[direction + 'Cent']) != 0) {
                if (!pval)pval += '0';
                pval += '.';
                pval += Voucher.cellSetValue(row[direction + 'Angle']);
                pval += Voucher.cellSetValue(row[direction + 'Cent']);
            }
            if (row[direction + 'pn'] == '-' && pval) {
                pval = "-" + pval;
            }
            if (pval) {
                //row['newdebit']='';
                //row['newcrebit']='';
                row['new' + direction + 'ebit'] = pval;
            }
            return pval;
        },
        //单行Editor金额合计
        getEditorMoney: function (direction) {
            var editors = $('#voucherDg').datagrid('getEditors', editIndex);
            var pval = '';
            var j = 2;
            if (direction == 'cr') {
                j = 13;
            }
            pval += editors[j++].target.val();
            pval += editors[j++].target.val();
            pval += editors[j++].target.val();
            pval += editors[j++].target.val();
            pval += editors[j++].target.val();
            pval += editors[j++].target.val();
            pval += editors[j++].target.val();
            pval += editors[j++].target.val();
            pval += editors[j++].target.val();
            if (pval && Voucher.startWith(pval, '0')) {
                pval = pval.substr(1, pval.length);
            }
            if (editors[j].target.val() || editors[j + 1].target.val()) {
                if (!pval)pval += '0';
                var row = $('#voucherDg').datagrid('getRows')[editIndex];
                var p = '';
                var v1 = editors[j].target.val() ? editors[j++].target.val() : '0';
                if (v1 && v1.length >= 2) {
                    p += v1.replace(row[direction + 'Angle'], '');
                } else {
                    p += v1;
                }
                var v2 = editors[j].target.val() ? editors[j++].target.val() : '0';
                if (v2 && v2.length >= 2) {
                    p += v2.replace(row[direction + 'Cent'], '').charAt(v2.length - 2);
                } else {
                    p += v2;
                }
                pval += p.substr(0, 2);
            }
            return pval;
        },
        //金额得到焦点事件
        cellFocus: function (direction) {
            if (cellFocusFlag != direction) {
                var rows = $('#voucherDg').datagrid('getRows');
                var ed = $('#voucherDg').datagrid('getEditor', {index: editIndex, field: direction + 'Yuan'});
                if (ed) {
                    ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                }
                if (!(rows[editIndex]['new' + direction + 'ebit'] && rows[editIndex]['new' + direction + 'ebit'] != 0)) {
                    $($('#voucherDg').datagrid('getEditor', {
                        index: editIndex,
                        field: (direction + 'Yuan')
                    }).target).val('0');
                    $($('#voucherDg').datagrid('getEditor', {
                        index: editIndex,
                        field: (direction + 'Angle')
                    }).target).val('0');
                    $($('#voucherDg').datagrid('getEditor', {
                        index: editIndex,
                        field: (direction + 'Cent')
                    }).target).val('0');
                    rows[editIndex][direction + 'Yuan'] = '0';
                    rows[editIndex][direction + 'Angle'] = '0';
                    rows[editIndex][direction + 'Cent'] = '0';
                }
            }
            cellFocusFlag = direction;
        },
        /*金额editor事件*/
        cellkeyUp: function (event, tthis, num, direction, field) {
            var rows = $('#voucherDg').datagrid('getRows');
            if (event.keyCode == 46) {//Delete按键
                Voucher.changeEditorMoney(0, direction);
            } else if (event.keyCode == 8) {//Backspace按键
            } else if (Voucher.endWith(tthis.value, '-') || Voucher.startWith(tthis.value, '-')) {//"-"录入
                if (rows[editIndex][direction + 'pn'] == '-') {
                    rows[editIndex][direction + 'pn'] = '';
                    $("." + direction + "textMax").css("color", "black");
                } else {
                    rows[editIndex][direction + 'pn'] = '-';
                    $("." + direction + "textMax").css("color", "red");
                }
                tthis.value = rows[editIndex][field];
            } else if (Voucher.endWith(tthis.value, '.') || Voucher.startWith(tthis.value, '.')) {//"."录入
                $($('#voucherDg').datagrid('getEditor', {index: editIndex, field: direction + 'Angle'}).target).focus();
                tthis.value = rows[editIndex][field];
                return;
            } else {
                //金额只能录入一方，或借方或贷方
                if (direction == 'd' && rows[editIndex]['newcrebit'] && rows[editIndex]['newcrebit'] != 0) {
                    var ed = $('#voucherDg').datagrid('getEditor', {index: editIndex, field: 'crYuan'});
                    $(ed.target).focus();
                    tthis.value = '';
                    $($('#voucherDg').datagrid('getEditor', {
                        index: editIndex,
                        field: (direction + 'Yuan')
                    }).target).val('');
                    $($('#voucherDg').datagrid('getEditor', {
                        index: editIndex,
                        field: (direction + 'Angle')
                    }).target).val('');
                    $($('#voucherDg').datagrid('getEditor', {
                        index: editIndex,
                        field: (direction + 'Cent')
                    }).target).val('');
                    return;
                } else if (direction == 'cr' && rows[editIndex]['newdebit'] && rows[editIndex]['newdebit'] != 0) {
                    ed = $('#voucherDg').datagrid('getEditor', {index: editIndex, field: 'dYuan'});
                    $(ed.target).focus();
                    tthis.value = '';
                    $($('#voucherDg').datagrid('getEditor', {
                        index: editIndex,
                        field: (direction + 'Yuan')
                    }).target).val('');
                    $($('#voucherDg').datagrid('getEditor', {
                        index: editIndex,
                        field: (direction + 'Angle')
                    }).target).val('');
                    $($('#voucherDg').datagrid('getEditor', {
                        index: editIndex,
                        field: (direction + 'Cent')
                    }).target).val('');
                    return;
                }
                //过滤空或非数字，金额只能录入数字
                if (!tthis.value) {
                    return;
                } else if (tthis.value && !Voucher.checkNum(tthis.value)) {
                    tthis.value = rows[editIndex][field];
                    return;
                }
            }
            //重新组合金额
            var rowmoney = Voucher.getEditorMoney(direction);
            if (Voucher.endWith(field, 'Angle') || Voucher.endWith(field, 'Cent')) {
                $($('#voucherDg').datagrid('getEditor', {index: editIndex, field: direction + 'Cent'}).target).focus();
            } else if (!rows[editIndex][field] || event.keyCode == 46) {
                var editors = $('#voucherDg').datagrid('getEditors', editIndex);
                $(editors[direction == 'd' ? 13 - rowmoney.length : 24 - rowmoney.length].target).focus();
            }
            Voucher.changeEditorMoney(rowmoney, direction);
            //合计
            Voucher.totalSum(direction);
        },
        // 合计
        totalSum: function (direction) {
            var rows = $('#voucherDg').datagrid('getRows');
            var money = 0;
            for (i = 0; i < rows.length; ++i) {
                money = Voucher.accAdd(parseInt(money), Voucher.accMul(Voucher.getMoney(direction, rows[i]), 100));
            }
            if (direction == 'd') {
                debit = money;
                Voucher.changeRowMoney(debit, direction);
                Voucher.footerDX();
            } else if (direction == 'cr') {
                crebit = money;
                Voucher.changeRowMoney(crebit, direction);
                Voucher.footerDX();
            }
        },
        //检查value为空或不是数字时置为0
        cellSetValue: function (value) {
            if (value && Voucher.checkNum(value)) {
                return value;
            } else {
                return 0;
            }
        },
        //检查value为空或不是实数时置为0
        cellSetRealNum: function (value) {
            if (value && Voucher.isRealNum(value)) {
                return value;
            } else {
                return 0;
            }
        },
        //合计中文
        footerDX: function () {
            var row = $('#voucherDg').datagrid('getFooterRows')[0];
            row['summary'] = '合计：';
            if (debit == crebit) {//借贷平衡
                balanceFlag = true;
                row['summary'] += Voucher.DX(debit / 100);
            } else {
                balanceFlag = false;
            }
            $('#voucherDg').datagrid('reloadFooter');
            Voucher.mergeFooterCells();
        },
        //修改表格Editor总计金额
        changeEditorMoney: function (value, direction, setNull) {
            if (!value || value == 0 || value == '0') {
                value = '';
            }
            var rows = $('#voucherDg').datagrid('getRows');
            var editors = $('#voucherDg').datagrid('getEditors', editIndex);
            var valueStr = value < 0 ? (value + '').substr(1) : value + '';
            var length = valueStr.length;
            var i = 0;
            if (length > 11) {
                i = length - 11;
            }
            var j = 2;
            if (direction == 'cr') {
                j = 13;
            }
            editors[j].target.val(length >= 11 ? valueStr.charAt(i++) : '');
            rows[editIndex][direction + 'HundredMillion'] = editors[j].target.val();
            j++;
            editors[j].target.val(length >= 10 ? valueStr.charAt(i++) : '');
            rows[editIndex][direction + 'TenMillions'] = editors[j].target.val();
            j++;
            editors[j].target.val(length >= 9 ? valueStr.charAt(i++) : '');
            rows[editIndex][direction + 'Millions'] = editors[j].target.val();
            j++;
            editors[j].target.val(length >= 8 ? valueStr.charAt(i++) : '');
            rows[editIndex][direction + 'HundredThousand'] = editors[j].target.val();
            j++;
            editors[j].target.val(length >= 7 ? valueStr.charAt(i++) : '');
            rows[editIndex][direction + 'TenThousand'] = editors[j].target.val();
            j++;
            editors[j].target.val(length >= 6 ? valueStr.charAt(i++) : '');
            rows[editIndex][direction + 'Thousand'] = editors[j].target.val();
            j++;
            editors[j].target.val(length >= 5 ? valueStr.charAt(i++) : '');
            rows[editIndex][direction + 'Hundred'] = editors[j].target.val();
            j++;
            editors[j].target.val(length >= 4 ? valueStr.charAt(i++) : '');
            rows[editIndex][direction + 'Ten'] = editors[j].target.val();
            j++;
            editors[j].target.val(length >= 3 ? valueStr.charAt(i++) : (setNull ? '' : '0'));
            rows[editIndex][direction + 'Yuan'] = editors[j].target.val();
            j++;
            editors[j].target.val(length >= 2 ? valueStr.charAt(i++) : (setNull ? '' : '0'));
            rows[editIndex][direction + 'Angle'] = editors[j].target.val();
            j++;
            editors[j].target.val(length >= 1 ? valueStr.charAt(i++) : (setNull ? '' : '0'));
            rows[editIndex][direction + 'Cent'] = editors[j].target.val();
        },
        //修改表格footer总计金额
        changeRowMoney: function (value, direction) {
            if (!value || value == 0 || value == '0') {
                value = '';
            }
            var changerow = $('#voucherDg').datagrid('getFooterRows')[0];
            var valueStr = value < 0 ? (value + '').substr(1) : value + '';
            var length = valueStr.length;
            var i = 0;
            if (length > 11) {
                i = length - 11;
            }
            changerow[direction + 'HundredMillion'] = length >= 11 ? valueStr.charAt(i++) : '';
            changerow[direction + 'TenMillions'] = length >= 10 ? valueStr.charAt(i++) : '';
            changerow[direction + 'Millions'] = length >= 9 ? valueStr.charAt(i++) : '';
            changerow[direction + 'HundredThousand'] = length >= 8 ? valueStr.charAt(i++) : '';
            changerow[direction + 'TenThousand'] = length >= 7 ? valueStr.charAt(i++) : '';
            changerow[direction + 'Thousand'] = length >= 6 ? valueStr.charAt(i++) : '';
            changerow[direction + 'Hundred'] = length >= 5 ? valueStr.charAt(i++) : '';
            changerow[direction + 'Ten'] = length >= 4 ? valueStr.charAt(i++) : '';
            changerow[direction + 'Yuan'] = length >= 3 ? valueStr.charAt(i++) : '';
            changerow[direction + 'Angle'] = length >= 2 ? valueStr.charAt(i++) : '';
            changerow[direction + 'Cent'] = length >= 1 ? valueStr.charAt(i++) : '';
            if (value < 0) {//金额为负时，变红色
                changerow[direction + 'HundredMillion'] = '<span style="color:red;">' + changerow[direction + 'HundredMillion'] + '</span>';
                changerow[direction + 'TenMillions'] = '<span style="color:red;">' + changerow[direction + 'TenMillions'] + '</span>';
                changerow[direction + 'Millions'] = '<span style="color:red;">' + changerow[direction + 'Millions'] + '</span>';
                changerow[direction + 'HundredThousand'] = '<span style="color:red;">' + changerow[direction + 'HundredThousand'] + '</span>';
                changerow[direction + 'TenThousand'] = '<span style="color:red;">' + changerow[direction + 'TenThousand'] + '</span>';
                changerow[direction + 'Thousand'] = '<span style="color:red;">' + changerow[direction + 'Thousand'] + '</span>';
                changerow[direction + 'Hundred'] = '<span style="color:red;">' + changerow[direction + 'Hundred'] + '</span>';
                changerow[direction + 'Ten'] = '<span style="color:red;">' + changerow[direction + 'Ten'] + '</span>';
                changerow[direction + 'Yuan'] = '<span style="color:red;">' + changerow[direction + 'Yuan'] + '</span>';
                changerow[direction + 'Angle'] = '<span style="color:red;">' + changerow[direction + 'Angle'] + '</span>';
                changerow[direction + 'Cent'] = '<span style="color:red;">' + changerow[direction + 'Cent'] + '</span>';
            }
        },
        // 阿拉伯数字转中文大写
        DX: function (n) {
            if (n == 0) {
                return '';
            }
            if (!n || !Voucher.isRealNum(n))
                return "数据非法";
            var flag = n < 0;
            var unit = "千百拾亿千百拾万千百拾元角分", str = "";
            n += "00";
            var n = flag ? n.substr(1) : n;
            var p = n.indexOf('.');
            if (p >= 0)
                n = n.substring(0, p) + n.substr(p + 1, 2);
            unit = unit.substr(unit.length - n.length);
            for (var i = 0; i < n.length; i++)
                str += '零壹贰叁肆伍陆柒捌玖'.charAt(n.charAt(i)) + unit.charAt(i);
            return (flag ? '负' : '') + (str.replace(/零(千|百|拾|角)/g, "零").replace(/(零)+/g, "零").replace(/零(万|亿|元)/g, "$1").replace(/(亿)万|壹(拾)/g, "$1$2").replace(/^元零?|零分/g, "").replace(/元$/g, "元整"));
        },
        formatDebit: function (val, row) {
            if (row.dpn == '-' && val) {
                return '<span style="color:red;">' + val + '</span>';
            } else {
                return val;
            }
        },
        formatCredit: function (val, row) {
            if (row.crpn == '-' && val) {
                return '<span style="color:red;">' + val + '</span>';
            } else {
                return val;
            }
        },
        //是否为数字
        checkNum: function (value) {
            return /^[0-9]*$/.test(value);
        },
        //是否为实数
        isRealNum: function (value) {
            return /^(\-)?\d+($|\.\d+$)/.test(value);
        },
        //结尾判断
        endWith: function (s, str) {
            if (str == null || str == "" || !s || s.length == 0 || str.length > s.length)
                return false;
            var sStr = s + '';
            if (sStr.substring(s.length - str.length) == str)
                return true;
            else
                return false;
            return true;
        },
        //开始判断
        startWith: function (s, str) {
            if (str == null || str == "" || s.length == 0 || str.length > s.length)
                return false;
            var sStr = s + '';
            if (sStr.substr(0, str.length) == str)
                return true;
            else
                return false;
            return true;
        },
        //乘法函数，用来得到精确的乘法结果
        //说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
        //调用：accMul(arg1,arg2)
        //返回值：arg1乘以arg2的精确结果
        accMul: function (arg1, arg2) {
            var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
            try {
                m += s1.split(".")[1].length
            } catch (e) {
            }
            try {
                m += s2.split(".")[1].length
            } catch (e) {
            }
            return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
        },
        //加法函数，用来得到精确的加法结果
        //说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
        //调用：accAdd(arg1,arg2)
        //返回值：arg1加上arg2的精确结果
        accAdd: function (arg1, arg2) {
            var r1, r2, m;
            try {
                r1 = arg1.toString().split(".")[1].length
            } catch (e) {
                r1 = 0
            }
            try {
                r2 = arg2.toString().split(".")[1].length
            } catch (e) {
                r2 = 0
            }
            m = Math.pow(10, Math.max(r1, r2))
            return (arg1 * m + arg2 * m) / m
        },
        //说明：javascript的减法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的减法结果。
        //调用：accSub(arg1,arg2)
        //返回值：arg1减上arg2的精确结果
        accSub: function (arg1, arg2) {
            return Voucher.accAdd(arg1, -arg2);
        },
        //说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
        //调用：accDiv(arg1,arg2)
        //返回值：arg1除以arg2的精确结果
        accDiv: function (arg1, arg2) {
            var t1 = 0, t2 = 0, r1, r2;
            try {
                t1 = arg1.toString().split(".")[1].length
            } catch (e) {
            }
            try {
                t2 = arg2.toString().split(".")[1].length
            } catch (e) {
            }
            with (Math) {
                r1 = Number(arg1.toString().replace(".", ""))
                r2 = Number(arg2.toString().replace(".", ""))
                return (r1 / r2) * pow(10, t2 - t1);
            }
        }
    };
}();

//自定义的金额editor
$.extend($.fn.datagrid.defaults.editors, {
    textMax: {
        init: function (container, options) {
            var input = $('<input type="text" class="' + options.direction + 'textMax" maxlength="5" onclick="Voucher.cellFocus(\'' + options.direction + '\')" onkeyup="Voucher.cellkeyUp(event,this,' + options.num + ',\'' + options.direction + '\',\'' + options.field + '\')" class="datagrid-editable-input" style="-webkit-ime-mode:disabled">').appendTo(container);
            return input;
        },
        destroy: function (target) {
            $(target).remove();
        },
        getValue: function (target) {
            return $(target).val();
        },
        setValue: function (target, value) {
            $(target).val(value);
        },
        resize: function (target, width) {
            $(target)._outerWidth(width);
        }
    }
});
//扩展voucher maxlength规则
$.extend($.fn.validatebox.defaults.rules, {
    voucherMaxLength: {
        validator: function (value, param) {
            return param[0] >= value.length;
        },
        message: '请输入最大{0}位字符.'
    },
    mydate: {
        validator: function (value, param) {
            var date = Voucher.myparser(value);
            var d1 = new Date(param[0], param[1] - 1, 1);
            var d2 = new Date(param[0], param[1], 1);
            return d1 <= date && date < d2;
        },
        message: '时间超出当前期{0}年{1}月范围.'
    }
});
