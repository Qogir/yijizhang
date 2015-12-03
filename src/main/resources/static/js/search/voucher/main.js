Search_Voucher = function () {

    return {
        datatable: null,
        init_data_table: function () {

            Search_Voucher.datatable = $("#search_voucher_container").find("#data_table").datagrid({
                url: 'search/voucher/vouchers',
                border: false,
                fit: true,
                singleSelect: true,
                fitColumns: true,
                rownumbers: true,
                toolbar: '#search_voucher_container #tb',
                columns: [
                    [
                        {field: 'voucher', title: '凭证字号', hidden: true, show: true},
                        {field: 'summary', title: '摘要', width: 100, formatter: function (value) {
                            var keyword = $("#search_voucher_container").find("#keyword_input").textbox("getValue");
                            if (value) {
                                return value.replace(keyword, "<span style='color: red;'>" + keyword + "</span>");
                            } else {
                                return '无';
                            }
                        }},
                        {field: 'subject_code', title: '科目代码', formatter: this.keywordHighlight, width: 100},
                        {field: 'subject_name', title: '科目名称', formatter: this.keywordHighlight, width: 100},
                        {field: 'debit', title: '借方金额', align: 'right', formatter: this.moneyFormatter, width: 100, styler: function () {
                            return "font-weight:700;color:green;";
                        }},
                        {field: 'credit', title: '贷方金额', align: 'right', formatter: this.moneyFormatter, width: 100, styler: function () {
                            return "font-weight:700;color:blue;";
                        }}
                    ]
                ],
                onDblClickRow: function (index, row) {
                    App.addVoucherTab('记账', 'voucher/main?voucherId=' + row.id, true);
                },
                view: groupview,
                groupField: 'voucher',
                groupFormatter: function (value, rows) {
                    var keyword = $("#search_voucher_container").find("#keyword_input").textbox("getValue");
                    var voucher_arr = value.split(" ");

                    var voucherWord = voucher_arr[0].replace(keyword, "<span style='color: red;'>" + keyword + "</span>");
                    var time = voucher_arr[1].replace(keyword, "<span style='color: red;'>" + keyword + "</span>");

                    return voucherWord + " ~ " + time;
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

        keywordHighlight: function (val) {
            var keyword = $("#search_voucher_container").find("#keyword_input").textbox("getValue");
            return val.toString().replace(keyword, "<span style='color: red;'>" + keyword + "</span>");
        },

        init_period_select: function () {

            $("#search_voucher_container").find("#period").combobox({
                url: 'search/voucher/periods',
                valueField: 'id',
                textField: 'currentPeriod',
                editable: false,
                panelHeight: 150,
                onLoadSuccess: function () {
                    $(this).combobox("setValue", $("#currentPeriod_hidden").val());
                },
                onSelect: function (record) {
                    $("#search_voucher_container").find("#data_table").datagrid('reload', {
                        periodId: record.id
                    });
                }
            });

        },

        init_search: function () {
            $("#search_voucher_container").find("#search").click(function () {
                var search_div = $("#search_voucher_container").find("#search_div");
                if (search_div.css("display") == 'none') {
                    search_div.css("display", "inline");
                    search_div.find("input.textbox-text").select().focus();
                } else {
                    search_div.hide();
                }
            })

            $("#search_voucher_container").keypress(function (e) {
                if (e.which == 13) {
                    Search_Voucher.search();
                }
            });
        },

        search: function () {
            //关键字
            var keyword = $("#search_voucher_container").find("#keyword_input").textbox("getValue");
            //期间id
            var record = $("#search_voucher_container").find("#period").combobox("getValue");

            $("#search_voucher_container").find("#data_table").datagrid('reload', {
                periodId: record,
                keyword: keyword
            });
        },

        init_button_event: function () {
            //整理.
            $("#search_voucher_container").find("#set").click(function () {

                var curPeriodId = $("#currentPeriod_hidden").val();
                var nowPeriodId = $("#search_voucher_container").find("#period").combobox("getValue");
                if (curPeriodId != nowPeriodId) {
                    $.messager.alert("警告", "只允许整理当前期。", "warning");
                    return;
                }

                var msg = '&#12288;&#12288;凭证整理适用于存在凭证断号，需要系统自动进行凭证号的连续排序的情况。已结账期间的凭证不参与凭证整理。' +
                    '<br/>&#12288;&#12288;&#12288;&nbsp;<span style="color: #ff0000;"><i class="fa fa-exclamation-triangle fa-lg"></i> 整理后，凭证号不可恢复，继续吗？</span>';

                $.messager.confirm('确认', msg, function (r) {
                    if (r) {
                        $.ajax({
                            url: 'search/voucher/set',
                            async: true,
                            success: function (result) {
                                if (result.success) {
                                    var curPeriodId = $("#currentPeriod_hidden").val();
                                    $("#search_voucher_container").find("#period").combobox("setValue", curPeriodId);
                                    $("#search_voucher_container").find("#data_table").datagrid('reload', {
                                        periodId: curPeriodId
                                    });
                                }
                            }
                        });
                    }
                });

            });

            //冲销
            $("#search_voucher_container").find("#reversal").click(function () {

                var selected_row = $("#search_voucher_container").find("#data_table").datagrid('getSelected');
                if (selected_row) {
                    App.addVoucherTab('记账', 'voucher/main?voucherId=' + selected_row.id + '&isreversal=1', true);
                } else {
                    $.messager.alert("警告", "请选择一条记录。", "warning");
                }

            });

            //修改
            $("#search_voucher_container").find("#edit").click(function () {

                var selected_row = $("#search_voucher_container").find("#data_table").datagrid('getSelected');
                if (selected_row) {
                    App.addVoucherTab('记账', 'voucher/main?voucherId=' + selected_row.id, true);
                } else {
                    $.messager.alert("警告", "请选择一条记录。", "warning");
                }

            });

            //修改
            $("#search_voucher_container").find("#delete").click(function () {

                var curPeriodId = $("#currentPeriod_hidden").val();
                var nowPeriodId = $("#search_voucher_container").find("#period").combobox("getValue");
                if (curPeriodId != nowPeriodId) {
                    $.messager.alert("警告", "只允许删除当前期。", "warning");
                    return;
                }

                var selected_row = $("#search_voucher_container").find("#data_table").datagrid('getSelected');
                if (selected_row) {
                    $.messager.confirm('确认', '确定删除所选择的记录？此操作不可恢复。', function (r) {
                        if (r) {
                            $.ajax({
                                url: 'search/voucher/delete',
                                data: {
                                    voucherId: selected_row.id
                                },
                                async: true,
                                success: function (result) {
                                    if (result.success) {
                                        var curPeriodId = $("#currentPeriod_hidden").val();
                                        $("#search_voucher_container").find("#period").combobox("setValue", curPeriodId);
                                        $("#search_voucher_container").find("#data_table").datagrid('reload', {
                                            periodId: curPeriodId
                                        });
                                    }
                                }
                            });
                        }
                    });
                } else {
                    $.messager.alert("警告", "请选择一条记录。", "warning");
                }

            });
        },

        init_export_button: function () {
            $("#search_voucher_container").find("#exportToExcel").click(function () {
                App.exportToExcel("凭证查询", Search_Voucher.datatable);
            });
        },

        init: function () {

            this.init_button_event();
            this.init_search();
            this.init_period_select();
            this.init_data_table();
            this.init_export_button();

        }
    }

}();