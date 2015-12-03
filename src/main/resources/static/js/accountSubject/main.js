/**
 * Created by Joey Yan on 15-9-26.
 */


Account_Subject = function () {

    return {
        account_subject_current_table: null,
        account_subject_selected_row: null,
        specific_subject: {
            category_id: null,
            account_subject_id: -1
        },
        editWin: null,
        openEditWin: function (opt, title, icon) {
            var category_id = Account_Subject.specific_subject.category_id;
            var subject_id = Account_Subject.specific_subject.account_subject_id;
            var href = 'account/subject/opt/' + opt + '/category/' + category_id + "?subjectId=" + subject_id;

            this.editWin = $("<div></div>").window({
                title: title,
                iconCls: icon,
                width: 500,
                height: 320,
                modal: true,
                collapsible: false,
                shadow: true,
                href: href,
                onClose: function () {
                    $(this).panel('destroy');
                    Account_Subject.editWin = null;
                }
            });
        },
        checkNullAndBaseData: function () {
            var subject_id = Account_Subject.specific_subject.account_subject_id;
            if (subject_id == -1) {
                $.messager.alert('警告', '请选择一条记录。', 'warning');
                return false;
            }

            var base_flag = Account_Subject.account_subject_selected_row.base_flag;
            if (base_flag == 0) {
                $.messager.alert('警告', '基础数据不允许操作。', 'warning');
                return false;
            }

            return true;
        },
        remove: function () {
            if (!Account_Subject.checkNullAndBaseData()) {
                return false;
            }

            //判断是否已经挂件记账凭证
            var haveVoucher = Account_Subject.account_subject_selected_row.haveVoucher;
            var haveInitData = Account_Subject.account_subject_selected_row.haveInitData;
            if (haveVoucher > 0 || haveInitData > 0) {
                $.messager.alert('警告', '您选择的会计科目已经有业务关联，不允许删除。', 'warning');
                return false;
            }

            if (Account_Subject.account_subject_selected_row.children) {
                $.messager.alert('警告', '你所选择记录有子级，不允许删除。', 'warning');
                return false;
            }

            $.messager.confirm('确认', '你确定删除选中记录吗?', function (r) {
                if (r) {
                    var subject_id = Account_Subject.specific_subject.account_subject_id;
                    $.post("account/subject/delete/" + subject_id, function (data) {
                        if (data.success) {
                            $.messager.alert("提示", "删除成功。", "info", function () {
                                Account_Subject.specific_subject.account_subject_id = -1;
                                Account_Subject.account_subject_current_table.treegrid("reload");
                            });
                        } else {
                            $.messager.alert("错误", data.msg, "error");
                        }
                    });
                }
            });

        },
        edit: function () {
            if (!Account_Subject.checkNullAndBaseData()) {
                return false;
            }
            //修改是否关联凭证验证.
            var haveVoucher = Account_Subject.account_subject_selected_row.haveVoucher;
            if (haveVoucher > 0) {
                $.messager.alert('警告', '此科目关联凭证，若想修改科目代码，请先删除凭证。', 'warning', function () {
                    Account_Subject.openEditWin("edit", "修改会计科目", "icon-edit");
                });
            } else {
                Account_Subject.openEditWin("edit", "修改会计科目", "icon-edit");
            }
        },
        button_bind_event: function () {

            var container = $("#accountSubject_layout");
            //关闭当前window.
            container.find(".close").click(function () {
                $("#default_win").window("close");
            });
            //新增会计科目.
            container.find(".add").click(function () {
                if (Account_Subject.account_subject_selected_row) {
                    if (Account_Subject.account_subject_selected_row.isLeaf) {
                        var msg = '如果您在该科目下添加明细科目，它将升级为非明细科目，这样该科目本年内发生的所有业务都将转移到新增加的明细科目中，是否继续？';

                        $.messager.confirm('确认', msg, function (r) {
                            if (r) {
                                Account_Subject.openEditWin("add", "新增会计科目", "icon-add");
                            }
                        });
                        return;
                    }
                }
                //
                Account_Subject.openEditWin("add", "新增会计科目", "icon-add");
            });
            //修改会计科目.
            container.find(".edit").click(function () {
                Account_Subject.edit();
            });
            //删除会计科目.
            container.find(".remove").click(function () {
                return Account_Subject.remove();
            });
            //科目说明。
            container.find(".tip").click(function () {
                $("<div></div>").window({
                    title: "科目说明",
                    iconCls: "icon-tip",
                    width: 560,
                    height: 400,
                    modal: true,
                    href: "account/subject/tip"
                });
            });
        },
        /**
         * 会计大科目tab页初始化。
         */
        init_tab: function () {

            var $accountSubject_tabs = $("#accountSubject_tabs");

            $accountSubject_tabs.tabs({
                border: false,
                cache: true,
                onSelect: function (title) {

                    var id = $accountSubject_tabs.tabs('getSelected').panel('options').id;
                    var category_id = id.replace("category_", "").replace(",", "");

                    Account_Subject.specific_subject.category_id = category_id;
                    Account_Subject.specific_subject.account_subject_id = -1;

                    //判断是否已经加载数据.
                    if ($("#" + id).html()) {
                        return;
                    }

                    /**
                     * 以下内容加载树形数据.
                     */
                    var table_id = category_id + "_table";
                    $accountSubject_tabs.tabs('getSelected').html('<table id="' + table_id + '" style="width:100%;"></table>');
                    Account_Subject.account_subject_current_table = $("#" + table_id).treegrid({
                        url: 'account/subject/category/' + category_id + '/subjects',
                        idField: 'subject_code',
                        treeField: 'subject_code',
                        fitColumns: true,
                        border: false,
                        columns: [
                            [
                                {title: '编码', field: 'subject_code', width: 200},
                                {title: '名称', field: 'subject_name', width: 300},
                                {title: '分类', field: 'category_datail_subject_name', width: 200},
                                {title: '方向', field: 'direction', width: 100, formatter: function (value) {
                                    return value == 1 ? '<span style="color: green;">借</span>' : '<span style="color: red;">贷</span>';
                                }}
                            ]
                        ],
                        onClickRow: function (row) {
                            Account_Subject.specific_subject.account_subject_id = row.id_back;
                            Account_Subject.account_subject_selected_row = row;
                        },
                        onDblClickRow: function (row) {
                            Account_Subject.edit();
                        },
                        loadFilter: function (data) {
                            var d = [];

                            for (var i = 0; i < data.length; i++) {
                                if (data[i].id == -1) {
                                    Account_Subject.account_subject_selected_row = data[i];
                                } else {
                                    d.push(data[i]);
                                }
                            }
                            return d;
                        },
                        onLoadSuccess: function () {

                            //解决加载不同步问题。
                            $('#accountSubject_layout #button_container').show();
                        }
                    });

                }
            });

        },

        /**
         * 专门给其他功能提供的会计科目查询页面.
         */
        init_search_subjects: function (callback) {

            $("#account_subject_search_tabs").tabs({
                border: false,
                cache: true,
                onSelect: function (title) {
                    Account_Subject.subject_search_tab_onselect(callback);
                }
            });

            setTimeout(function () {
                Account_Subject.subject_search_tab_onselect(callback);
            }, 0);
        },

        subject_search_tab_onselect: function (callback) {

            var $accountSubject_tabs = $("#account_subject_search_tabs");
            var id = $accountSubject_tabs.tabs('getSelected').panel('options').id;
            var category_id = id.replace("account_subject_search_category_", "").replace(",", "");

            //判断是否已经加载数据.
            if ($("#" + id).html()) {
                return;
            }

            /**
             * 以下内容加载树形数据.
             */
            var table_id = category_id + "_search_table";
            $accountSubject_tabs.tabs('getSelected').html("<table id='" + table_id + "' style='width: 100%;'></table>");
            $("#" + table_id).treegrid({
                url: 'account/subject/category/' + category_id + '/subjects',
                idField: 'subject_code',
                treeField: 'subject_code',
                fitColumns: true,
                border: false,
                columns: [
                    [
                        {title: '编码', field: 'subject_code', width: 200},
                        {title: '名称', field: 'subject_name', width: 300},
                        {title: '分类', field: 'category_datail_subject_name', width: 200},
                        {title: '方向', field: 'direction', width: 100, formatter: function (value) {
                            return value == 1 ? '<span style="color: green;">借</span>' : '<span style="color: red;">贷</span>';
                        }}
                    ]
                ],
                onDblClickRow: function (row) {
                    callback(row);
                    setTimeout(function () {
                        $('#default_win').window("close");
                    }, 0);
                },
                loadFilter: function (data) {
                    var d = [];

                    for (var i = 0; i < data.length; i++) {
                        if (data[i].id != -1) {
                            d.push(data[i]);
                        }
                    }
                    return d;
                }
            });
        },

        /**
         * 调用可查看会计科目，双击返回会计科目编码.
         *
         * @param callback
         */
        open_subject_search_win: function (callback) {

            App.openWin("会计科目", 600, 400, "account/subject/search",
                function () {
                    Account_Subject.init_search_subjects(callback);
                }

            );

        },

        init: function () {

            this.init_tab();
            this.button_bind_event();
        }

    }

}();