/**
 * 初始化数据.
 */
Account_Subject_Init_Data = function () {

    return {
        _editor_: {type: 'numberbox', options: {precision: 2, prompt: '0.00'}},
        _keyword_val_: '',
        datagrid_table: null,
        search: function () {
            var keyword = $("#init_data_table_container").find("#search_subject_code").val().toString();
            this._keyword_val_ = keyword;

            $("#init_data_table_container").find("#init_data_table").datagrid("disableCellEditing");
            $("#init_data_table_container").find("#init_data_table").datagrid('reload', {
                keyword: keyword
            });
        },

        //汇总
        calu: function () {
            $.ajax({
                url: 'account/subject/initData/calculate',
                success: function (data) {
                    if (data.success) {
                        $("#init_data_table_container").find("#init_data_table").datagrid("disableCellEditing");
                        $("#init_data_table_container").find("#init_data_table").datagrid('reload', {});
                    } else {
                        $.messager.alert("错误", data.msg, "error");
                    }
                }
            })
        },


        keyword_search_bind_event: function () {
            $("#init_data_toolbar").keypress(function (e) {
                if (e.which == 13) {
                    Account_Subject_Init_Data.search();
                }
            });
        },
        moneyFormatter: function (value, row, index) {
            if (value) {
                var val = value.toString();
                var idx = val.indexOf(".");
                if (idx < 0) {
                    val += ".00";
                } else {
                    if (val.substring(idx + 1, val.length).length < 2) {
                        val += "0";
                    }
                }
                return val;
            } else {
                return value == 0 ? "" : value;
            }
        },
        directionFormatter: function (value, row, index) {
            if (row.direction == 1) {
                return "<span style='color: green;'>借</span>";
            } else {
                return "<span style='color: red;'>贷</span>";
            }
        },
        yearOccurAmountStyler: function (value, row, index) {
            if (!row.isCrease) {
                return 'background-color:#ccc;color:#fff;';
            }
        },
        subjectCodeFormatter: function (value, row, index) {
            return Account_Subject_Init_Data.highlight_keyword(value.toString());
        },
        subjectNameFormatter: function (value, row, index) {
            //
            value = Account_Subject_Init_Data.highlight_keyword(value);
            var blank = "";
            for (var i = 1; i < row.level; i++) {
                blank += "&#12288;";
            }

            return blank + value;
        },
        highlight_keyword: function (val) {
            var keyword = Account_Subject_Init_Data._keyword_val_;
            return val.replace(keyword, "<span style='color: red;'>" + keyword + "</span>");
        },

        init_data_table: function () {

            this.datagrid_table = $("#init_data_table_container").find("#init_data_table").datagrid({
                url: 'account/subject/initData/alldata',
                fitColumns: true,
                rownumbers: true,
                singleSelect: true,
                border: false,
                columns: [
                    [
                        {field: 'id', title: 'id', hidden: true},
                        {field: 'subjectCode', title: '科目代码', width: 100, formatter: this.subjectCodeFormatter},
                        {field: 'subjectName', title: '科目名称', width: 180, formatter: this.subjectNameFormatter},
                        {field: 'totalDebit', title: '累计借方', width: 100, align: 'right', editor: this._editor_, formatter: this.moneyFormatter, styler: function () {
                            return 'font-weight: 700;color:green;';
                        }},
                        {field: 'totalCredit', title: '累计贷方', width: 100, align: 'right', editor: this._editor_, formatter: this.moneyFormatter, styler: function () {
                            return 'font-weight: 700;color:blue;';
                        }},
                        {field: 'directionname', title: '方向', width: 30, align: 'center', formatter: this.directionFormatter},
                        {field: 'initialLeft', title: '期初余额', width: 100, align: 'right', editor: this._editor_, formatter: this.moneyFormatter, styler: function () {
                            return 'font-weight: 700';
                        }},
                        {field: 'yearOccurAmount', title: '本年累计损益<br/>实际发生额', width: 80, align: 'right', editor: this._editor_, formatter: this.moneyFormatter, styler: this.yearOccurAmountStyler}
                    ]
                ],
                toolbar: [
                    {
                        text: '<i class="fa fa-filter fa-lg"></i> 过滤',
                        handler: function () {
                            $("#init_data_toolbar").slideToggle(function () {
                                $(this).find(".textbox-text").select().focus();
                            });
                        }
                    },
                    '-',
                    {
                        text: '<i class="fa fa-calculator fa-lg"></i> 汇总',
                        handler: function () {
                            var thiss = $(this);
                            thiss.linkbutton('disable');

                            $.ajax({
                                url: 'account/subject/initData/calculate',
                                success: function (data) {
                                    thiss.linkbutton('enable');
                                    if (data.success) {
                                        $("#init_data_table_container").find("#init_data_table").datagrid("disableCellEditing");
                                        $("#init_data_table_container").find("#init_data_table").datagrid('reload', {});
                                    } else {
                                        $.messager.alert("错误", data.msg, "error");
                                    }
                                }
                            })
                        }
                    },
                    {
                        text: '<i class="fa fa-balance-scale fa-lg"></i> 平衡',
                        handler: function () {

                            var balanceDialog = $("<div></div>").dialog({
                                title: '<i class="fa fa-balance-scale fa-lg"></i> 查看试算平衡',
                                href: 'account/subject/initData/balance/page',
                                width: 500,
                                height: 200,
                                modal: true,
                                collapsible: false,
                                shadow: true,
                                buttons: [
                                    {
                                        text: '退出',
                                        iconCls: 'icon-cancel',
                                        handler: function () {
                                            balanceDialog.dialog("destroy");
                                        }
                                    }
                                ]
                            });

                        }
                    },
                    '-',
                    {
                        text: '<i class="fa fa-file-excel-o fa-lg"></i> 导出',
                        handler: function () {
                            App.initDataExportToExcel("初始化数据", Account_Subject_Init_Data.datagrid_table);
                        }
                    }
                ],
                rowStyler: function (index, row) {
                    if (row.isParent) {
                        return 'background-color:#FEFFB4;';
                    }
                },
                loadFilter: function (data) {

                    var accountSubjects = data.accountSubjects;
                    var creaseCode = data.creaseCode; //损益类科目第一个代码.
                    var len = accountSubjects.length;
                    var isFirstPeriod = data.isFirstPeriod; //是否第一期.

                    for (var i = 0; i < len; i++) {
                        var cur_subject_code = accountSubjects[i].subjectCode.toString();

                        var _data = accountSubjects[i + 1];
                        if (_data) {
                            var next_subject_code = _data.subjectCode.toString();
                            if (next_subject_code.indexOf(cur_subject_code) > -1) {
                                accountSubjects[i].isParent = true;  //会计科目父节点
                            }
                        }
                        //是否损益校验.
                        if (cur_subject_code.substring(0, 1) == creaseCode) {
                            accountSubjects[i].isCrease = true; //是否损益类.
                        } else {
                            accountSubjects[i].isCrease = false;
                        }

                        //重新定义方向.
                        if (accountSubjects[i].direction == 1) {
                            accountSubjects[i]['directionname'] = '借';
                        } else {
                            accountSubjects[i]['directionname'] = '贷';
                        }

                    }

                    return {total: len, rows: accountSubjects, isFirstPeriod: isFirstPeriod};
                },
                onLoadSuccess: function (data) {

                    //如果第一期则不需修改.
                    if (!data.isFirstPeriod) {
                        $('td[field="totalDebit"],td[field="totalCredit"],td[field="yearOccurAmount"],td[field="initialLeft"]').css({
                            "background-color": "#f2f2f2",
                            "border-color": "#ccc"
                        });
                        return;
                    }

                    $(this).datagrid("enableCellEditing", function (cur_idx, pre_idx, field) {
                        var rows = data.rows;

                        if (pre_idx != -1) {
                            var _r = rows[pre_idx];

                            //异步保存数据.
                            setTimeout(function () {
                                $.ajax({
                                    url: 'account/subject/initData/edit',
                                    data: _r,
                                    type: 'POST',
                                    async: true,
                                    success: function (data) {
                                        if (!data.success) {
                                            $.messager.alert("错误", data.msg, "error");
                                        }
                                    }
                                })
                            }, 0);

                        }

                        //校验是否可以修改.
                        if (!$.isEmptyObject(rows)) {
                            //父级几点.
                            if (rows[cur_idx].isParent) {
                                return false;
                            }

                            //非损益类会计科目校验.
                            if (field == "yearOccurAmount") {
                                if (!(rows[cur_idx].isCrease)) {
                                    return false
                                }
                            }
                        }

                        return true;
                    });

                }
            });
        },

        //初始数据表格.
        init: function () {
            this.init_data_table();
            this.keyword_search_bind_event();
        }
    }

}()

