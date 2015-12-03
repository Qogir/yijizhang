<div class="top-menu">
    <button onClick="App.selectTab(0);"
            class="button button-glow button-primary button-box button-giant button-longshadow-left">
        <i class="fa fa-home"></i><br/>
        <small style="font-size:12px;">首页</small>
    </button>
    <button onClick="App.addVoucherTab('记账','voucher/main',true);"
            class="button button-glow button-primary button-box button-giant button-longshadow-left">
        <i class="fa fa-pencil-square-o"></i><br/>
        <small style="font-size:12px;">记账</small>
    </button>
    <button id="cz_bubble" class="button button-glow button-primary button-box button-giant button-longshadow-right">
        <i class="fa fa-search-plus"></i><br/>
        <small style="font-size:12px;">查账 <i class="fa fa-caret-down"></i></small>
    </button>

    <div id="cz_menu" style="display: none;">
        <ul class="fc_menu_ul">
            <li><a href="javascript:App.addTab('凭证查询','search/voucher/page/main',true)">凭证查询</a></li>
            <li><a href="javascript:App.addTab('总账','search/ledger/main',true)">总账</a></li>
            <li><a href="javascript:App.addTab('明细账','search/detail/main',true)">明细账</a></li>
            <#-- <li><a href="#">多栏账</a></li> -->
			<li><a href="javascript:App.addTab('科目余额表','search/subjectbalance/main',true)">科目余额表</a></li>
            <li><a href="javascript:App.addTab('凭证汇总表','search/vouchercollect/main',true)">凭证汇总表</a></li>
            <li><a href="javascript:App.addTab('试算平衡表','search/trialbalance/main',true)">试算平衡表</a></li>
        </ul>
    </div>

    <button onClick="App.addTab('结账','account/cashier/main',true);"
            class="button button-glow button-primary button-box button-giant button-longshadow-left">
        <i class="fa fa-calendar"></i><br/>
        <small style="font-size:12px;">结账</small>
    </button>

    <button id="bb_bubble" class="button button-glow button-primary button-box button-giant button-longshadow-right">
        <i class="fa fa-line-chart"></i><br/>
        <small style="font-size:12px;">报表 <i class="fa fa-caret-down"></i></small>
    </button>
    <div id="bb_menu" style="display: none;">
        <ul class="fc_menu_ul">
            <li><a href="javascript:App.addTab('资产负债表','balance/sheet/main',true);">资产负债表</a></li>
            <li><a href="javascript:App.addTab('利润表','profit/main',true);">利润表</a></li>
            <li><a href="#">现金流量表</a></li>
        </ul>
    </div>

	<button onClick="App.addTab('日志','action/log/main',true);" class="button button-glow button-primary button-box button-giant button-longshadow-right">
		<i class="fa fa-file-text-o"></i><br/><small style="font-size:12px;">日志</small>
	</button>

    <div style="display: none;">
        <form name="exportToExcelForm" action='export/to/excel' method='post'>
            <input type='hidden' id='dataJsonStr' name='dataJsonStr'>
            <input type='submit' name='submit' style='display:none'>
        </form>
    </div>
</div>
<script>
    $(function () {
        $('#cz_bubble').tooltip({
            position: 'bottom',
            content: $('#cz_menu').html(),
            showEvent: 'mouseenter',
            onShow: function(){
                var t = $(this);
                t.tooltip('tip').css({
                    'padding':'10px 15px 10px 15px',
                    'font-size':'12px'
                }).unbind().bind('mouseenter', function(){
                    t.tooltip('show');
                }).bind('mouseleave', function(){
                    t.tooltip('hide');
                });
            }
        });

        $('#bb_bubble').tooltip({
            position: 'bottom',
            content: $('#bb_menu').html(),
            showEvent: 'mouseenter',
            onShow: function(){
                var t = $(this);
                t.tooltip('tip').css({
                    'padding':'10px 15px 10px 15px',
                    'font-size':'12px'
                }).unbind().bind('mouseenter', function(){
                    t.tooltip('show');
                }).bind('mouseleave', function(){
                    t.tooltip('hide');
                });
            }
        });

    });
</script>
