<div id="accountSubject_edit" style="padding: 15px 0 0 15px;">

    <form method="post">

        <input id="category_id" type="hidden" value="${categoryId}">
        <input id="opt" type="hidden" value="${opt}">
        <input id="account_subject_direction" type="hidden" value="${accountSubject.direction?default(1)}">

        <input type="hidden" name="id" value="${subjectId?c}">
        <input type="hidden" name="level" value="${level}">
        <input type="hidden" name="bookId" value="${accountSubject.bookId?default(null)}">

        <table cellpadding="5" border="0">
            <tr>
                <td style="width: 80px;">父级科目:</td>
                <td>
                    <input id="parent_subject" name="parent_subject_code" style="width: 100%;"
                           value="${parentSubjectCode?c}"/>
                </td>
            </tr>
            <tr>
                <td>科目代码:</td>
                <td>
                    <input id="subject_code" name="subjectCode"
                           style="width: 200px;border: 1px solid #cccccc;vertical-align: top;outline-style: none;-moz-border-radius: 5px 5px 5px 5px;-webkit-border-radius: 5px 5px 5px 5px;"/>
                    <label>&nbsp;助记码:</label>
                    <input name="tipInfo" class="easyui-textbox" value="${accountSubject.tipInfo?default('')}" style="width: 80px;float:
                    right;"/>
                </td>
            </tr>
            <tr>
                <td>科目名称:</td>
                <td>
                    <input name="subjectName" class="easyui-textbox" type="text" data-options="required:true"
                           value="${accountSubject.subjectName?default('')}"
                           style="width: 98%;"/>
                </td>
            </tr>
            <tr>
                <td>科目类别:</td>
                <td>
                    <input id="category_detail" name="parent_subject_code_back"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <fieldset>
                        <legend>余额方向</legend>

                        <label><input name="direction" value="1" type="radio"> 借方</label>&nbsp;&nbsp;
                        <label><input name="direction" value="2" type="radio"> 贷方</label>
                    </fieldset>

                </td>
            </tr>
            <tr></tr>
            <tr>
                <td colspan="2" align="right">
                    <a id="subject_form_submit_button" class="easyui-linkbutton" data-options="iconCls:'icon-save'"
                       href="javascript:void(0)">确定</a>
                    <a id="subject_form_close_win_button" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
                       href="javascript:void(0)">取消</a>
                </td>
            </tr>
        </table>
    </form>

</div>

<script>

    $(function () {
        Account_Subject_Edit.init();
    });

</script>