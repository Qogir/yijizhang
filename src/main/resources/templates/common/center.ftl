<div id="tabContainer">
    <div title='<i class="fa fa-home"></i> <@spring.message code="welcome.text"/>'
         class="welcome-panel easyui-layout"
         style="width: 100%; height: 100%;">
        <div style="width: 75%;overflow-y: hidden;" data-options="region:'center',border:false,
            onResize:function(w,h){
                Welcome.repaint();
            }" >
            <#include "../module/welcome/_welcome.ftl"/>
        </div>

        <div id="east" style="width:25%;" data-options="region:'east',border:false,split:true" >
            <#include "../module/welcome/east.ftl" />
        </div>
    </div>
</div>