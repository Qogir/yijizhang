<style>
    #account_subject_comment h2 {
        color: #FFFFFF;
    }

    .north {
        text-align: center;
        background-color: #7dc4fa;
        height: 60px;
        overflow: hidden;
    }

</style>

<div id="account_subject_comment" class="easyui-layout" style="width: 100%;" data-options="fit:true,border:false">
    <div class="north" data-options="region:'north'">
        <h2>科目说明</h2>
    </div>
    <div data-options="region:'center'" style="padding: 15px;">
        <P>
            科目说明<br/>

            科目级次：最大支持10级科目，编码规则为4-2-2-2-2-2-2-2-2-2。<br/>

            科目编码规则：一级科目编码为4位，不允许重复；明细科目编码为上级科目编码+ 本级科目编码<br/>

            举例：如新增1002银行存款明细科目招商银行华强北支行，科目编码录入100201，科目名称录入招商银行华强北支行。

        </P>
    </div>
</div>
