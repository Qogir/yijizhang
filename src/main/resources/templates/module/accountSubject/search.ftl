<div id="account_subject_search_tabs" class="easyui-tabs" data-options="fit:true,border:false">
<#list categories as category>
    <div id="account_subject_search_category_${category.id?c}" title="${category.subjectName?default('')}"
         style="overflow-x: hidden;"></div>
</#list>
</div>
