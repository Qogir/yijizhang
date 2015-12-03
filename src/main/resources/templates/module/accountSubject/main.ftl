<div id="accountSubject_layout" class="easyui-layout" data-options="fit:true">
    <div data-options="region:'east',title:'操作'" style="width:120px;overflow: hidden;">
        <div id="button_container" style="display: none;width:120px;text-align: center;">
            <br/>
            <a href="javascript:void(0)" class="easyui-linkbutton close" data-options="iconCls:'icon-cancel'"
               style="width: 80%;">关闭</a>

            <br/>
            <br/>
            <br/>
            <br/>
            <a href="javascript:void(0)" class="easyui-linkbutton add" data-options="iconCls:'icon-add'"
               style="width: 80%">新增</a>

            <div style="height: 10px;"></div>
            <a href="javascript:void(0)" class="easyui-linkbutton edit" data-options="iconCls:'icon-edit'"
               style="width: 80%">修改</a>

            <div style="height: 10px;"></div>
            <a href="javascript:void(0)" class="easyui-linkbutton remove" data-options="iconCls:'icon-remove'"
               style="width: 80%">删除</a>

            <div style="height: 10px;"></div>
            <a href="javascript:void(0)" class="easyui-linkbutton tip" data-options="iconCls:'icon-tip'"
               style="width: 80%">科目说明</a>
        </div>
    </div>
    <div data-options="region:'center',border:false">
        <div id="accountSubject_tabs" class="easyui-tabs" data-options="fit:true,border:false">
        <#list categories as category>
            <div id="category_${category.id?c}" title="${category.subjectName?default('')}"
                 style="overflow-x: hidden;"></div>
        </#list>
        </div>
    </div>
</div>

<script>
    $(function () {
        Account_Subject.init();
    });
</script>