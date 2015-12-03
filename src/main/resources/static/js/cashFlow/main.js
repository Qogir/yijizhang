Cash_Flow = function () {
    return {

        container: null,
        datatable: null,
        currentPeriod: null,

        export: function () {
            this.container.find("#exportToExcel").click(function () {
                App.exportToExcel("现金流量表", Cash_Flow.datatable);
            });
        },

        //期间可选.
        init_period: function () {

            this.container.find("#period").combobox({
                url: 'search/voucher/periods',
                valueField: 'currentPeriod',
                textField: 'currentPeriod',
                editable: false,
                panelHeight: 150,
                onLoadSuccess: function () {
                    $(this).combobox("setValue", Cash_Flow.container.find("#currentPeriod_hidden").val());
                },
                onSelect: function (record) {
                    Cash_Flow.datatable.datagrid("reload", {currentPeriod: record['currentPeriod']});
                }
            });

        },

        init_table: function () {

            this.datatable = this.container.find("#cashflow_table").datagrid({
                url: '/cash/flow/cashflows',
                fit: true,
                fitColumns: true,
                border: false,
                singleSelect: true,
                rownumbers: true,
                showHeader: false,
                columns: [
                    [
                        {field: 'cAVal', title: '项目', width: 400},
                        {field: 'cCVal', title: '金额', width: 400, align: 'center'}
                    ]
                ]
            });
        },

        init: function () {
            this.container = $("#cashflow_container");

            this.init_period();
            this.init_table();
            this.export();
        }
    }
}();
