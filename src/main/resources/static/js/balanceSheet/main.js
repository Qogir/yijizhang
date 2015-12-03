Balance_Sheet = function () {

    return {
        container: null,
        datagrid_tables: [],
        num: 1, //编号
        yearBegin_fmt: function (value, row, index) {
            if (row.level == 2) {
                value += '<a class="calculate" level="' + row.level + '" href="#"><i class="fa fa-calculator"></i></a>';
                value += '<div class="menu-content" style="padding:10px;text-align:left;display: none;width:250px;">计算公式：<br/><span>' + row.yearBeginExp;
                value += '</span> <i class="fa fa-pencil-square-o fa-lg" onclick="Balance_Sheet.toEdit(this,\'yearBegin\',' + row.id + ')"></i></div>';
                return  value;
            } else if (row.level == -1) {
                return value;
            }
        },
        periodEnd_fmt: function (value, row, index) {
            var field = "";
            if (row.level == 2) {
                value += '<a class="calculate" level="' + row.level + '" href="#"><i class="fa fa-calculator"></i></a>';
                value += '<div class="menu-content" style="padding:10px;text-align:left;display: none;width:250px;">计算公式：<br/><span>' + row.periodEndExp;
                value += '</span> <i class="fa fa-pencil-square-o fa-lg" onclick="Balance_Sheet.toEdit(this,\'periodEnd\',' + row.id + ')"></i></div>';
                return  value;
            } else if (row.level == -1) {
                return value;
            }
        },

        refresh: function () {
            this.init_calculate_dropdow.isinit = 0;
            this.num = 1;
            var periodId = this.container.find("#currentPeriod_hidden").val();
            var ts = Balance_Sheet.datagrid_tables;
            ts[0].datagrid("reload", {periodId: periodId});
            setTimeout(function () {
                ts[1].datagrid("reload", {periodId: periodId});
            }, 100);
        },

        toEdit: function (obj, field, id) {
            var $span = $(obj).prev();
            var $this_obj = $(obj);
            if ($this_obj.hasClass("fa-check")) {
                var exp = $span.find("input").val();

                //保存公式
                $.messager.confirm('确认', '此操作不可逆，继续修改？', function (r) {
                    if (r) {
                        var _data = {};
                        if (field == 'yearBegin') {
                            _data = {yearBeginExp: exp, id: id};
                        } else {
                            _data = {periodEndExp: exp, id: id};
                        }

                        $.ajax({
                            url: '/balance/sheet/save/exp',
                            data: _data,
                            success: function (data) {
                                if (data.success) {
                                    $span.html(exp);
                                    $this_obj.removeClass("fa-check").addClass("fa-pencil-square-o");

                                    //刷新页面.
                                    Balance_Sheet.refresh();

                                }
                            }
                        })
                    }
                });

            } else {
                var exp = $span.html();

                var re1 = new RegExp("&lt;", "g");
                var re2 = new RegExp("&gt;", "g");

                exp = exp.replace(re2, ">");
                exp = exp.replace(re1, "<");

                $span.html($('<input type="text">').val(exp));
                $this_obj.removeClass("fa-pencil-square-o").addClass("fa-check");
            }
        },
        init_calculate_dropdow: {
            isinit: 0,
            init: function () {
                if (this.isinit == 2) {
                    $("a.calculate").each(function () {
                        $(this).menubutton({
                            hasDownArrow: false,
                            hideOnUnhover: false,
                            menu: $(this).next()
                        });

                    });
                }
            }
        },
        init_data_table: function () {

            Balance_Sheet.datagrid_tables = []; //清空

            this.container.find("table").each(function (index) {

                var idx = index;

                var datagrid_table = $(this).datagrid({
                    url: 'balance/sheet/balancesheets?code=' + $(this).attr("code"),
                    border: false,
                    fitColumns: true,
                    singleSelect: true,
//                    rownumbers: true,
                    columns: [
                        [
                            {field: 'num', title: '编号', align: "center"},
                            {field: 'name', title: '名称', width: 120, formatter: function (value, row, index) {
                                var level = row.level;
                                var pre = '<i class="tree-icon tree-folder tree-folder-open"></i> ';
                                if (level == 2) {
                                    pre = '&#12288;&#12288;<i class="tree-icon tree-file"></i> ';
                                } else if (level == 1) {
                                    pre = '&#12288;' + pre;
                                } else if (level == -1) {
                                    pre = '<i class="fa fa-jpy"></i> ';
                                } else if (level == -2) {
                                    pre = '';
                                }
                                return pre + value;
                            }},
                            {field: 'yearBegin', title: '年初数', width: 100, align: 'right', formatter: Balance_Sheet.yearBegin_fmt},
                            {field: 'periodEnd', title: '期末数', width: 100, align: 'right', formatter: Balance_Sheet.periodEnd_fmt}
                        ]
                    ],
                    rowStyler: function (index, row) {
                        if (row.level == -1) {
                            return 'background-color:#6293BB;color:#fff;';
                        }
                    },
                    onLoadSuccess: function (data) {
                        Balance_Sheet.init_calculate_dropdow.isinit++;
                        Balance_Sheet.init_calculate_dropdow.init();

                        //样式计算.
                        Balance_Sheet.container.find("div.datagrid-view,div.datagrid-body").height("932px");

                        setTimeout(function () {
                            var hs = [];
                            Balance_Sheet.container.find("table.datagrid-btable").each(function () {
                                hs.push($(this).height());
                            });
                            Balance_Sheet.container.find("#wraphiddendiv").closest("td").height(function (n, c) {
                                var h = $(this).height();
                                return h + (hs[0] - hs[1]);
                            });
                        }, 500);
                    },
                    loadFilter: function (data) {
                        if (data) {
                            var new_data = [];
                            var length = data.length;
                            var sum_name = "总计";

                            var _periodEnd = 0, twoLev_periodEnd = 0;
                            var _yearBegin = 0, twoLev_yearBegin = 0;
                            for (var i = 0; i < length; i++) {
                                var record = data[i];
                                var periodEnd = record.periodEnd;
                                var yearBegin = record.yearBegin;
                                var need_num = record.needSum;

                                _periodEnd = need_num == 0 ? (_periodEnd + periodEnd) : need_num == 1 ? (_periodEnd - periodEnd) : _periodEnd;
                                _yearBegin = need_num == 0 ? (_yearBegin + yearBegin) : need_num == 1 ? (_yearBegin - yearBegin) : _yearBegin;


                                record["num"] = Balance_Sheet.num++;
                                //组装新的数据.
                                new_data.push(record);

                                //二级类别合计
                                if (record.level == 2) {

                                    if (i > 0 && data[i - 1].level == 1) {
                                        twoLev_periodEnd = 0;
                                        twoLev_yearBegin = 0;
                                        sum_name = data[i - 1].name + "合计";
                                    }
                                    twoLev_periodEnd = need_num == 0 ? (twoLev_periodEnd + periodEnd) : need_num == 1 ? (twoLev_periodEnd - periodEnd) : twoLev_periodEnd;
                                    twoLev_yearBegin = need_num == 0 ? (twoLev_yearBegin + yearBegin) : need_num == 1 ? (twoLev_yearBegin - yearBegin) : twoLev_yearBegin;

                                    if ((i < (length - 1) && data[i + 1].level == 1) || ( i == (length - 1))) {
                                        new_data.push({num: Balance_Sheet.num++, level: -1, name: sum_name, periodEnd: twoLev_periodEnd, yearBegin: twoLev_yearBegin});
                                    }
                                }

                            }

                            if (idx > 0) {
                                new_data.push({num: "", level: -2, name: '<div id="wraphiddendiv"></div>', periodEnd: "", yearBegin: ""});
                            }

                            sum_name = data[0].name + "合计";
                            var sum = {num: Balance_Sheet.num++, level: -1, name: sum_name, periodEnd: _periodEnd, yearBegin: _yearBegin};
                            new_data.push(sum);
                        }

                        return {total: new_data.length, rows: new_data};

                    }

                });

                Balance_Sheet.datagrid_tables.push(datagrid_table);

            })

            //导出
            setTimeout(function () {
                Balance_Sheet.init_export_button();
            }, 500);

        },
        //期间可选.
        init_period: function () {

            this.container.find("#period").combobox({
                url: 'search/voucher/periods',
                valueField: 'id',
                textField: 'currentPeriod',
                editable: false,
                panelHeight: 150,
                onLoadSuccess: function () {
                    $(this).combobox("setValue", Balance_Sheet.container.find("#currentPeriod_hidden").val());
                },
                onSelect: function (record) {
                    Balance_Sheet.refresh();
                }
            });

        },

        tip_init: function () {
            this.container.find('#tip').tooltip({
                position: 'left',
                content: this.container.find('#tip_content').html(),
                showEvent: 'mouseenter',
                onShow: function () {
                    var t = $(this);
                    t.tooltip('tip').css({
                        'padding': '30px 15px 10px 15px',
                        'font-size': '12px'
                    }).unbind().bind('mouseenter',function () {
                            t.tooltip('show');
                        }).bind('mouseleave', function () {
                            t.tooltip('hide');
                        });
                }
            });
        },

        init_export_button: function () {
            this.container.find("#exportToExcel").click(function () {

                var tbs = Balance_Sheet.datagrid_tables;

                var titles = ['编号', '名称', '年初数', '期末数', '编号', '名称', '年初数', '期末数'];
                var fields = ['num', 'name', 'yearBegin', 'periodEnd', 'num1', 'name1', 'yearBegin1', 'periodEnd1'];
                var datarows = $(tbs[0]).datagrid("getData")["rows"];
                var datarows1 = $(tbs[1]).datagrid("getData")["rows"];

                var ds = [];

                var len = datarows.length;
                if (datarows1.length < datarows.length) {
                    len = datarows.length;
                }
                for (var i = 0; i < len; i++) {
                    var _d = {};
                    var d = datarows[i];
                    var d1 = datarows1[i];
                    if (d) {
                        if (d['num']) {
                            _d['num'] = d['num'];
                            _d['name'] = d['name'];
                            _d['yearBegin'] = d['yearBegin'];
                            _d['periodEnd'] = d['periodEnd'];
                        }
                    }
                    if (d1) {
                        if (d1['num']) {
                            _d['num1'] = d1['num'];
                            _d['name1'] = d1['name'];
                            _d['yearBegin1'] = d1['yearBegin'];
                            _d['periodEnd1'] = d1['periodEnd'];
                        }
                    }
                    ds.push(_d);
                }

                var data = {rows: ds};
                var dataJsonStr = {filename: "资产负债表", titles: titles, fields: fields, data: JSON.stringify(data)};

                $('#dataJsonStr').val(JSON.stringify(dataJsonStr));
                document.exportToExcelForm.submit.click();
            });
        },

        init: function () {
            this.container = $("#balance_sheet_container");
            this.init_calculate_dropdow.isinit = 0;
            this.num = 1;

            this.init_period();
            this.tip_init();
            this.init_data_table();
        }
    }

}();