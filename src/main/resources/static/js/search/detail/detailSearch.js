Search_Detail = function () {
    return {
        init_period_select: function () {
            $('#startPeriod').val($("#current_hidden").val());
            $('#endPeriod').val($("#current_hidden").val());
        },
        init_SubjectCode: function () {

            $('#subjectCode').searchbox({
                searcher: function (value, name) {
                    Account_Subject.open_subject_search_win(function (record) {
                        $('span[name="subjectName_value"]').text("(" + record.subject_code + ")" + record.subject_name);
                        $('#subjectCode').searchbox('setValue', record.subject_code);
                        Search_Detail.searchDetail();
                    })
                }
            });

            //
            var searchVal = $("#subjectCode_hidden").val();
            if (searchVal) {
                $('#subjectCode').searchbox("setValue", searchVal);
                $('span[name="subjectName_value"]').text("(" + searchVal + ")" + $('#subjectName_detail_hidden').val());
            }

        },
        select_SubjectCode: function () {
            Account_Subject.open_subject_search_win(function (record) {
                $('span[name="subjectName_value"]').text("(" + record.subject_code + ")" + record.subject_name);
                $('#subjectCode').searchbox('setValue', record.subject_code);
                Search_Detail.searchDetail();
            });
        },
        datatable: null,
        //查询按钮
        searchDetail: function () {
            if ($('#subjectCode').val() == null || $('#subjectCode').val() == "") {
                $.messager.alert("提示信息", "请选择科目代码!");
                return;
            }
            var aa = $('#endPeriod').val();
            if (parseInt($('#startPeriod').val()) > parseInt($('#endPeriod').val())) {
                $.messager.alert("提示信息", "会计期间起始期间必须小于结束期间!");
                return;
            }
            Search_Detail.datatable = $("#search_detail_container").find("#detail_data_table").datagrid({
                url: "search/detail/submitNow",
                queryParams: {
                    startPeriod: $('#startPeriod').val(),
                    endPeriod: $('#endPeriod').val(),
                    subjectCode: $('#subjectCode').val()
                },
                border: false,
                fit: true,
                singleSelect: true,
                fitColumns: true,
                rownumbers: true,
                columns: [
                    [
                        {field: 'voucherId', title: 'voucherId', hidden: true},
                        {field: 'voucherTime', title: '日期'},
                        {field: 'voucherWord', title: '凭证字号'},
                        {field: 'summary', title: '摘要', width: 100},
                        {field: 'subjectName', title: '对方科目', width: 100},
                        {field: 'debit', title: '借方金额', align: 'right', formatter: this.moneyFormatter, width: 100, styler: function () {
                            return "font-weight:700;color:green;";
                        }},
                        {field: 'credit', title: '贷方金额', align: 'right', formatter: this.moneyFormatter, width: 100, styler: function () {
                            return "font-weight:700;color:blue;";
                        }},
                        {field: 'direction', title: '方向', width: 100},
                        {field: 'balance', title: '余额', width: 100}
                    ]
                ],
                onDblClickCell: function (index, field, value) {
                    var selected_row = $("#search_detail_container").find("#detail_data_table").datagrid('getSelected');
                    if (!selected_row.voucherId) {
                        return;
                    }
                    App.addVoucherTab('记账', 'voucher/main?voucherId=' + selected_row.voucherId, true);
                },
                rowStyler: function (index, row) {
                    if (row.summary == "本日合计") {
                        return 'background-color:#6293BB;color:#fff;'; // return inline style
                    }
                }
            });
        },
        init: function () {
            this.init_period_select();
            this.init_SubjectCode();
            $('#subjectCode').textbox('textbox').bind('dblclick', function (e) {
                Search_Detail.select_SubjectCode();
            });
            $('#searchDetail').click(function () {
                Search_Detail.searchDetail();
            });
            if ($('#subjectCode_hidden').val() != "") {
                Search_Detail.searchDetail();
            }

            $("#search_detail_container").find("#exportToExcel").click(function () {
                App.exportToExcel("明细账", Search_Detail.datatable);
            });
        }
    }
}();