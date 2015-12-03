<div class="jtk-surface welcome-tb" id="canvas">
    <div class="jtk-node" id="flowchartWindow1">
        <button id="btn_kjkm" class="button button-action button-box button-jumbo">
            <div><i class="fa fa-list-alt"></i></div>
            <div><small style="font-size:12px;"><@spring.message code="welcome.kjkm"/></small></div>
        </button>
    </div>
    <div class="jtk-node" id="flowchartWindow2">
        <button id="btn_jiz" onClick="App.addVoucherTab('记账','voucher/main',true);" class="button button-action button-box button-jumbo">
            <div><i class="fa fa-pencil-square-o"></i></div>
            <div><small style="font-size:12px;"><@spring.message code="welcome.jiz"/></small></div>
        </button>
    </div>
    <div class="jtk-node" id="flowchartWindow3">
        <button id="btn_jzsy" class="button button-action button-box button-jumbo">
            <div><i class="fa fa-area-chart"></i></div>
            <div><small style="font-size:12px;"><@spring.message code="welcome.jzsy"/></small></div>
        </button>
    </div>
    <div class="jtk-node" id="flowchartWindow4">
        <button id="btn_cssj" onClick="javascript:App.addTab('初始化数据','account/subject/initData/main',true);" class="button button-action button-box button-jumbo">
            <div><i class="fa fa-laptop"></i></div>
            <div><small style="font-size:12px;"><@spring.message code="welcome.cssj"/></small></div>
        </button>
    </div>
    <div class="jtk-node" id="flowchartWindow5">
        <button id="btn_jiez" onClick="App.addTab('结账','account/cashier/main',true);" class="button button-action button-box button-jumbo">
            <div><i class="fa fa-calendar"></i></div>
            <div><small style="font-size:12px;"><@spring.message code="welcome.jiez"/></small></div>
        </button>
    </div>
</div>

<div class="chartBtnDiv">
    <button onclick="App.addTab('资产负债表','balance/sheet/main',true);" class="button button-glow button-rounded button-primary"><i class="fa fa-table"></i>&#8194;<@spring.message code="welcome.zcfz"/></button>
    <button onclick="App.addTab('利润表','profit/main',true);" class="button button-glow button-rounded button-primary"><i class="fa fa-table"></i>&#8194;<@spring.message code="welcome.lirb"/></button>
    <button onclick="App.addTab('现金流量表','cash/flow/main',true);" class="button button-glow button-rounded button-primary"><i class="fa fa-table"></i>&#8194;<@spring.message code="welcome.xjll"/></button>
</div>
