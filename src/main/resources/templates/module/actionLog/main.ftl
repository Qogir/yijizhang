<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center',fit:true">
        <table style="width: 100%;" class="easyui-datagrid"
               data-options="
                url:'action/log/list',
                rownumbers:true,
                border:true,
                singleSelect:true,
                fitColumns:true,
                fit:true,
                pagination:true,
                pageSize:20">
            <thead>
            <tr>
                <th data-options="field:'action'" width="20%">操作</th>
                <th data-options="field:'states',align:'center'" width="5%">状态</th>
                <th data-options="field:'description'" width="25%">描述</th>
                <th data-options="field:'operator',align:'center'" width="10%">操作者</th>
                <th data-options="field:'address'" width="20%">IP</th>
                <th data-options="field:'actionTime'" width="19%">操作时间</th>
            </tr>
            </thead>
        </table>
    </div>
</div>