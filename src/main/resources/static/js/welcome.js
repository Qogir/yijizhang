/**
 * 欢迎页面的JS脚本文件。
 */

var jsPlumbInstance=null;

Welcome=function(){

    return {

        //初始化
        init:function(){
            Welcome.bindEvent();
            Welcome.createJsPlumb();
        },

        //绑定事件
        bindEvent:function(){
        	//会计科目
            var $btn_kjkm = $("#btn_kjkm");
            $btn_kjkm.click(function () {
                $("#default_win").window({
                    title: '<i class="fa fa-list-alt"></i>会计科目',
                    width: 700,
                    height: 500,
                    modal: true,
                    collapsible: false,
                    shadow: true,
                    href: 'account/subject/main',
                    onLoad: function () {
                    }
                });
            });
            //结转损益
            var $btn_jzsy = $("#btn_jzsy");
            $btn_jzsy.click(function () {
                $("#default_win").window({
                    title: '<i class="fa fa-list-alt"></i>结转本期损益',
                    width: 600,
                    height: 400,
                    modal: true,
                    collapsible: false,
                    shadow: true,
                    href: 'account/carryOver/main',
                    onLoad: function () {
                    }
                });
            });
        },

        //重画
        repaint:function(){
            if(jsPlumbInstance!=null){
                jsPlumbInstance.repaintEverything();
            }
        },

        //初始化欢迎页面的图标连线
        createJsPlumb: function(){
            jsPlumb.ready(function () {
                jsPlumbInstance = jsPlumb.getInstance({
                    ConnectionOverlays: [
                        [ "Arrow", { location: 1 } ],
                        [ "Label", {
                            location: 0.1,
                            id: "label",
                            cssClass: "aLabel"
                        }]
                    ],
                    Container: "canvas"
                });

                var basicType = {
                    connector: "StateMachine",
                    paintStyle: { strokeStyle: "red", lineWidth: 4 },
                    hoverPaintStyle: { strokeStyle: "blue" },
                    overlays: [
                        "Arrow"
                    ]
                };
                jsPlumbInstance.registerConnectionType("basic", basicType);


                // this is the paint style for the connecting lines..
                var connectorPaintStyle = {
                        lineWidth: 4,
                        strokeStyle: "#61B7CF",
                        joinstyle: "round",
                        outlineColor: "white",
                        outlineWidth: 2
                    },
                // .. and this is the hover style.
                    connectorHoverStyle = {
                        lineWidth: 4,
                        strokeStyle: "#216477",
                        outlineWidth: 2,
                        outlineColor: "white"
                    },
                    endpointHoverStyle = {
                        fillStyle: "#216477",
                        strokeStyle: "#216477"
                    },
                // the definition of source endpoints (the small blue ones)
                    sourceEndpoint = {
                        endpoint: "Dot",
                        paintStyle: { strokeStyle: "#7AB02C", fillStyle: "transparent", radius: 3, lineWidth: 3 },
                        isSource: true,
                        connector: [ "Flowchart", { stub: [40, 60], gap: 10, cornerRadius: 0, alwaysRespectStubs: true } ],
                        connectorStyle: connectorPaintStyle,
                        hoverPaintStyle: endpointHoverStyle,
                        connectorHoverStyle: connectorHoverStyle
                    },
                // the definition of target endpoints (will appear when the user drags a connection)
                    targetEndpoint = {
                        endpoint: "Dot",
                        paintStyle: { fillStyle: "#7AB02C", radius: 4 },
                        hoverPaintStyle: endpointHoverStyle,
                        maxConnections: -1,
                        dropOptions: { hoverClass: "hover", activeClass: "active" },
                        isTarget: true
                    };

                var _addEndpoints = function (toId, sourceAnchors, targetAnchors) {
                    for (var i = 0; i < sourceAnchors.length; i++) {
                        var sourceUUID = toId + sourceAnchors[i];
                        jsPlumbInstance.addEndpoint("flowchart" + toId, sourceEndpoint, {
                            anchor: sourceAnchors[i], uuid: sourceUUID
                        });
                    }
                    for (var j = 0; j < targetAnchors.length; j++) {
                        var targetUUID = toId + targetAnchors[j];
                        jsPlumbInstance.addEndpoint("flowchart" + toId, targetEndpoint, { anchor: targetAnchors[j], uuid: targetUUID });
                    }
                };

                // suspend drawing and initialise.
                jsPlumbInstance.batch(function () {

                    _addEndpoints("Window5", [], ["TopCenter"]);
                    _addEndpoints("Window4", ["RightMiddle"], []);
                    _addEndpoints("Window3", ["BottomCenter"], ["LeftMiddle"]);
                    _addEndpoints("Window2", ["RightMiddle"], ["LeftMiddle"]);
                    _addEndpoints("Window1", ["RightMiddle"], []);

                    // connect a few up
                    con1 = jsPlumbInstance.connect({uuids: ["Window1RightMiddle", "Window2LeftMiddle"]});
                    con2 = jsPlumbInstance.connect({uuids: ["Window4RightMiddle", "Window2LeftMiddle"], editable: false});
                    con3 = jsPlumbInstance.connect({uuids: ["Window2RightMiddle", "Window3LeftMiddle"], editable: false});
                    con4 = jsPlumbInstance.connect({uuids: ["Window3BottomCenter", "Window5TopCenter"], editable: false});
                });
            });
        }
    };
}();