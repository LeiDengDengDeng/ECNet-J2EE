/**
 * Created by deng on 2018/3/16.
 */
var idCounter = 0;
var borderColors = ['66, 140, 109', '253, 185, 51', '243, 113, 92', '66, 106, 179'];
var borderTypes = ["证据", "事实", "法条", "结论"];

// 储存森林，forest中每个是tree，tree中每个是node节点
var forest = Array.of();
// 保存历史节点
var historyForests = Array.of();
// 存储所有link
var links = Array.of();
// 保存历史连线
var historyLinks = Array.of();
// 用来判断右键点击来源
var isNodeRightClick = false;
// 当前图是直线图or曲线图
var isCurve = false;
// 用来判断是否进行了拖拽行为
var isDragged = false;

var mouseX;
var mouseY;

//用于记录鼠标拖拽框选、复制粘贴的变量
var copyNodeArr = Array.of();//保存要被复制的节点数组
var mouseClickX;//鼠标上一次点击的位置X坐标（不论左键还是右键点击）
var mouseClickY;//鼠标上一次点击的位置Y坐标（不论左键还是右键点击）
var copyAreaLeft;//框选区域的最左边X坐标
var copyAreaTop;//框选区域的最上边Y坐标

$(document).ready(function () {
    canvas = document.getElementById('canvas');
    stage = new JTopo.Stage(canvas); // 创建一个舞台对象
    scene = new JTopo.Scene(stage); // 创建一个场景对象

    stage.mode = "select"; //将舞台对象的模式设置为“可选择”，这样支持框选

    stage.addEventListener("mousemove", function (event) {
        $("#posX").text(event.pageX - $("#canvas").offset().left);
        $("#posY").text(event.pageY - $("#canvas").offset().top);
    });

    stage.addEventListener("mousedown", function (event) {
        saveScene();
    });

    stage.addEventListener("mousedrag", function (event) {
        isDragged = true;
    });

    stage.addEventListener("mouseup", function (event) {
        mouseClickX = event.pageX - $("#canvas").offset().left;
        mouseClickY = event.pageY - $("#canvas").offset().top;

        if (!isNodeRightClick && event.button == 2) {
            $("#element-id").hide();
            $("#element-name").hide();
            $("#hr").hide();
            $("#del-element-li").hide();
            $("#mul-del-element-li").hide();
            $("#mod-element-li").hide();
            $("#hr2").hide();
            $("#advice-element-li").hide();
            $("#mul-advice-element-li").hide();

            mouseX = event.pageX;
            mouseY = event.pageY;

            $("#stageMenu").css({
                top: event.pageY,
                left: event.pageX
            }).show();
        } else if (event.button == 0) {
            $("#stageMenu").hide();

            $("#endPosX").text(mouseClickX);
            $("#endPosY").text(mouseClickY);
            $("#myAction").text("鼠标左键松开");

            //注：只考虑肉眼能看见的选中框，非常小的区域画出来会是一个小点，不予考虑
            if (Math.abs(startPosX - endPosX) > 0.05 && Math.abs(startPosY - endPosY) > 0.05) {
                //删除之前画的选中框
                if (selectedRangeNode != undefined) {
                    scene.remove(selectedRangeNode);
                }

                //在场景上添加一个近似透明、不覆盖其他节点的节点，作为选中框
                selectedRangeNode = new JTopo.Node("");
                selectedRangeNode.setBound(startPosX, startPosY, Math.abs(endPosX - startPosX), Math.abs(endPosY - startPosY));
                selectedRangeNode.borderColor = '222,222,222';
                selectedRangeNode.fillColor = '255, 255, 255';
                selectedRangeNode.borderWidth = 1;
                selectedRangeNode.borderRadius = 1;
                selectedRangeNode.alpha = 0.2;
                selectedRangeNode.zIndex = 11;  // 是否覆盖其他，这里设置为可选的最小值，不覆盖其他
                scene.add(selectedRangeNode);

                var areaLeftPos = endPosX > startPosX ? startPosX : endPosX;
                var areaRightPos = endPosX > startPosX ? endPosX : startPosX;
                var areaTopPos = endPosY > startPosY ? startPosY : endPosY;
                var areaBottomPos = endPosY > startPosY ? endPosY : startPosY;
                console.log("left:" + areaLeftPos + ",right:" + areaRightPos + ",top:" + areaTopPos + ",bottom:" + areaBottomPos);
                var nodeList = scene.getDisplayedNodes();
                var length = nodeList.length;
                for (var i = 0; i < length; i++) {
                    var tempNode = nodeList[i];
                    var thisLeftPos = tempNode.x;
                    var thisRightPos = tempNode.x + tempNode.width;
                    var thisTopPos = tempNode.y;
                    var thisBottomPos = tempNode.y + tempNode.height;
                    console.log("left:" + thisLeftPos + ",right:" + thisRightPos + ",top:" + thisTopPos + ",bottom:" + thisBottomPos);
                    if (( (thisLeftPos >= areaLeftPos) || (thisRightPos <= areaRightPos) ) &&
                        ( (thisTopPos >= areaTopPos) || (thisBottomPos <= areaBottomPos) ) &&
                        ( tempNode != selectedRangeNode)) {
                        //只要和被选中区域有交叉，这个节点就算是在选中区域内，改为被选中状态
                        tempNode.selected = true;
                        tempNode.borderColor = "(0,0,0)";
                    }
                }
            }
        }

        if (!isDragged) {
            deleteScene();
        }
        isDragged = false;
    });

    window.addEventListener("mousedown", function (event) {
        mouseClickX = event.pageX - $("#canvas").offset().left;
        mouseClickY = event.pageY - $("#canvas").offset().top;

        if (event.button == 0) {
            $("#startPosX").text(mouseClickX);
            $("#startPosY").text(mouseClickY);
            $("#myAction").text("鼠标左键按下");
        }
    });

    window.addEventListener("keydown", function (event) {
        if (event.ctrlKey && event.keyCode === 67) {
            $("#myAction").text('你按下了CTRL+C');

            //将框选区域的左边界定为场景对象的右边界，框选区域的上边界定为场景对象的下边界
            copyAreaLeft = stage.width;
            copyAreaTop = stage.height;
            //console.log(copyAreaLeft+", "+copyAreaTop);

            //循环查找当前场景中的所有显示节点，若被选中，则加入待复制的节点数组中
            var nodeList = scene.getDisplayedNodes();
            var nodeListLen = nodeList.length;
            for (var i = 0; i < nodeListLen; i++) {
                if (nodeList[i].selected) {
                    copyNodeArr.push(nodeList[i]);
                    copyAreaLeft = nodeList[i].x < copyAreaLeft ? nodeList[i].x : copyAreaLeft;//将框选区域的左边界定为所有选中节点的最小左边界值
                    copyAreaTop = nodeList[i].y < copyAreaTop ? nodeList[i].y : copyAreaTop;//将框选区域的上边界定为所有选中节点的最小上边界值
                }
            }
            console.log("现在selectedArr里面有" + copyNodeArr.length + "个节点将被复制");
        }
        if (event.ctrlKey && event.keyCode === 86) {
            saveScene();

            $("#myAction").text('你按下了CTRL+V');

            //获取当前的鼠标位置，以当前鼠标位置作为起始位置进行粘贴
            //console.log("上一次鼠标点击位置:"+mouseClickX+","+mouseClickY);

            //逐一粘贴每个节点
            console.log("现在selectedArr里面有" + copyNodeArr.length + "个节点将被粘贴");
            for (var i = 0; i < copyNodeArr.length; i++) {
                //注：这里类似于对象赋值，不能用等于号直接让新节点等于老节点
                console.log("粘贴位置：" + (copyNodeArr[i].x - copyAreaLeft + mouseClickX) + "," + (copyNodeArr[i].y - copyAreaTop + mouseClickY));
                drawNode((copyNodeArr[i].x - copyAreaLeft + mouseClickX), (copyNodeArr[i].y - copyAreaTop + mouseClickY), null, copyNodeArr[i].text, borderColors.indexOf(copyNodeArr[i].borderColor), copyNodeArr[i].detail, copyNodeArr[i].parentId);
            }
            //清空被选中的节点列表
            copyNodeArr = [];
        }
    });

    window.addEventListener("keyup", function (event) {
        if (event.ctrlKey && event.keyCode === 67) { //复制已经选中的节点或者连线
            $("#myAction").text('你松开了CTRL+C');
        }
        else if (event.ctrlKey && event.keyCode === 86) {//粘贴上次复制的节点或者连线
            $("#myAction").text('你松开了CTRL+V');
        }

    });

    bindMenuClickEvent();

    $('#print-btn').click(function () {
        stage.saveImageInfo(undefined, undefined, "文书说理逻辑图");
    });
});

function drawNode(x, y, id, topic, type, detail, parentId) {
    // 将中文字符以2个长度的英文字母替换
    var topicLength = 32 + topic.replace(/[\u0391-\uFFE5]/g, "aa").length * 8;

    var node = new JTopo.Node(topic);
    node.id = id ? id : ++idCounter;
    node.borderColor = borderColors[type];

    node.fillColor = '255, 255, 255';
    node.borderWidth = 2;
    node.textPosition = 'Middle_Center';
    node.borderRadius = 3;

    // 根据内容长度决定node宽度
    node.setSize(topicLength, 24);
    // 设置树的方向
    node.layout = {type: 'tree', direction: 'left', width: 70, height: 250};

    node.addEventListener('mouseup', function (event) {
        nodeClickEvent(node.id, event);
    });
    node.addEventListener('mouseout', function (event) {
        isNodeRightClick = false;
    });
    node.setLocation(x, y);

    scene.add(node);

    if (parentId == null || parentId == "null") {
        var tree = Array.of();
        tree.push({
            node: node,
            id: node.id,
            topic: topic,
            type: type,
            detail: detail,
            parentId: parentId
        });
        forest.push(tree);
    } else {
        // 将节点插入到具体的tree中
        var targetTreeNum = -1;
        var parentNode = null;
        for (var m = 0, len1 = forest.length; m < len1; m++) {
            var tree = forest[m];
            for (var n = 0, len2 = tree.length; n < len2; n++) {
                if (parentId == tree[n].id) {
                    tree.push({
                        node: node,
                        id: idCounter,
                        topic: topic,
                        type: type,
                        detail: detail,
                        parentId: parentId
                    });

                    targetTreeNum = m;
                    parentNode = tree[n].node;
                    break;
                }
            }

            if (targetTreeNum != -1) {
                break;
            }
        }
        drawLink(parentNode, node);
    }

    return node.id;
}

function drawLink(parentNode, node) {
    var link = isCurve ? new JTopo.CurveLink(parentNode, node, "") : new JTopo.Link(parentNode, node);
    scene.add(link);
    links.push(link);
}

function bindMenuClickEvent() {
    $('#add-element-li').click(function (event) {
        $('#stageMenu').hide();
        prepareAddModal(getId());
        $("#node-add-modal").modal('show');
    });

    $('#mod-element-li').click(function (event) {
        $('#stageMenu').hide();
        prepareEditModal(getId());
        $("#node-edit-modal").modal('show');
    });

    $('#del-element-li').click(function (event) {
        $('#stageMenu').hide();
        prepareDelModal(getId());
        $("#node-del-modal").modal('show');
    });

    $('#advice-element-li').click(function (event) {
        $('#stageMenu').hide();
        prepareLawModal(getId());
        $("#law-recommend-modal").modal('show');
    });

    $('#mul-advice-element-li').click(function (event) {
        $('#stageMenu').hide();
        prepareMulLawModal()
        $("#mul-law-recommend-modal").modal('show');
    });

    function getId() {
        if ($('#element-id').css("display") == "none") {
            return null;
        } else {
            var idStr = $('#element-id').text();
            return idStr.substring(idStr.indexOf('：') + 1);
        }
    }
}

function prepareAddModal(id) {
    $("#node-add-modal .alert").hide();

    $("#node-add-modal #node-add-topic-input").val("");
    $("#node-add-modal #node-add-detail-input").val("");
    $("#node-add-modal #node-add-type-select").val("证据");
    $("#node-add-modal #node-add-leadTo-select").empty();

    prepareSelect($("#node-add-modal #node-add-leadTo-select"), id, null);
}

function addBtnEvent() {
    saveScene();

    const $alert = $('#node-add-modal .alert');
    $alert.empty();
    $alert.hide();

    var topic = $('#node-add-topic-input').val();
    var detail = $('#node-add-detail-input').val();
    var type = borderTypes.indexOf($('#node-add-type-select').val());
    var leadTo = $('#node-add-leadTo-select').val();
    // 发起验证
    // const checkCode = validateAddModal();

    // if (checkCode === LogicValidate.OK) {
    //     const id = me.graphModel.insertNode(topic, type, detail, leadTo);
    //     me.logOperation(new AddOperation(id, me));
    drawNode(mouseX - $("#canvas").offset().left, mouseY - $("#canvas").offset().top, null, topic, type, detail, leadTo);
    $('#node-add-modal').modal('hide');
    // me.redraw();
    // return;
// }
//     var hint = LogicValidator.generateHint(checkCode);
//     if (hint) {
//         $alert.append(hint);
//         $alert.show();
//     }
}

function prepareEditModal(id) {
    $("#node-edit-modal .alert").hide();
    var node = findNodeById(id);
    $("#node-edit-modal #node-edit-type-select").removeAttr("disabled");
    $("#node-edit-modal #node-edit-leadTo-select").removeAttr("disabled");
    if (node) {
        $("#node-edit-modal #node-edit-id-input").val(node.id);
        $("#node-edit-modal #node-edit-topic-input").val(node.topic);
        $("#node-edit-modal #node-edit-detail-input").val(node.detail);
        $("#node-edit-modal #node-edit-type-select").val(borderTypes[node.type]);

        prepareSelect($("#node-edit-modal #node-edit-leadTo-select"), null, node.id);
        $("#node-edit-modal #node-edit-leadTo-select").val(node.parentId ? node.parentId : "null");
    }
}

function editBtnEvent() {
    saveScene();

    const $alert = $('#node-edit-modal .alert');
    $alert.empty();
    $alert.hide();

    const id = $('#node-edit-id-input').val();
    const topic = $('#node-edit-topic-input').val();
    const detail = $('#node-edit-detail-input').val();
    const type = borderTypes.indexOf($('#node-edit-type-select').val());
    const leadTo = $('#node-edit-leadTo-select').val();

    // 发起验证
    // const checkCode = LogicValidator.validateEditModal(me.graphModel);

    // if (checkCode === LogicValidate.OK) {
    //     me.logOperation(new RemoveAndEditOperation(me.graphModel, me));
    //     me.graphModel.modifyNode(id, topic, type, detail, leadTo);

    var node = findNodeById(id);

    // 修改forest内数据
    moveNode(id, leadTo);

    // 修改连线
    editLink(id, node.parentId, id, leadTo);

    // 修改node信息
    node.topic = topic;
    node.detail = detail;
    node.type = type;
    node.parentId = leadTo;

    // 修改图上的node文字、宽度、边框颜色
    var topicLength = 32 + topic.replace(/[\u0391-\uFFE5]/g, "aa").length * 8;
    node.node.setSize(topicLength, 24);
    node.node.text = topic;
    node.node.borderColor = borderColors[type];

    $('#node-edit-modal').modal('hide');
    $(".node-info-wrapper .node-panel").hide();
    // me.redraw();
    // return;
    // }
    // const hint = LogicValidator.generateHint(checkCode);
    // if (hint) {
    //     $alert.append(hint);
    //     $alert.show();
    // }
}

function prepareDelModal(id) {
    var node = findNodeById(id);
    var parentTopic = node && node.parentId != "null" && node.parentId != null ? findNodeById(node.parentId).topic : "";
    $("#node-del-modal .del-id-td").text(node.id);
    $("#node-del-modal .del-topic-td").text(node.topic);
    $("#node-del-modal .del-type-td").text(borderTypes[node.type]);
    $("#node-del-modal .del-detail-td").text(node.detail);
    $("#node-del-modal .del-leadTo-td").text(node.parentId + " " + parentTopic);
}

// 删除当前节点。如果当前节点非根节点，子节点向上移动；若为根节点，则删除该树
function delNodeBtnEvent() {
    saveScene();

    var id = $('#node-del-modal table .del-id-td').text();
    delNode(id);

    $('#node-del-modal').modal('hide');
}

function delNode(id) {
    var node = findNodeById(id);
    var treeNum = findTreeNumOfNode(id);
    var tree = forest[treeNum];
    if (tree.indexOf(node) == 0) {
        // 删除该棵树的所有连线和node
        for (var m = 0, len = tree.length; m < len; m++) {
            var nodeA = tree[m];
            scene.remove(nodeA.node);

            for (var n = 0; n < m; n++) {
                var nodeZ = tree[n];
                editLink(nodeA.id, nodeZ.id, null, null);
            }
        }

        forest.splice(treeNum, 1);
    } else {
        var parentNode = findNodeById(node.parentId);
        for (var i = 0; i < tree.length; i++) {
            var nodeZ = tree[i];
            if (nodeZ.id != node.id) {
                // 删除所有与目标节点之间的连线
                editLink(node.id, nodeZ.id, null, null);
                if (parentNode.id != nodeZ.id && findLinkByNodeId(parentNode.id, nodeZ.id) == null) {
                    // 子节点与目标节点的父节点连线
                    drawLink(parentNode.node, nodeZ.node);
                }
            }
        }
        scene.remove(node.node);
        tree.splice(tree.indexOf(node), 1);
    }
}

// 删除当前节点及其子节点
function delNodeAndItsChildrenBtnEvent() {
    saveScene();

    var delNodes = Array.of();
    var node = findNodeById($('#node-del-modal table .del-id-td').text());
    delNodes.push(node);

    // 递归删除子节点
    delNodeAndItsChildren(delNodes, node.id);
    // 删除当前节点
    scene.remove(node.node);
    editLink(node.id, node.parentId, null, null);

    // 删除node在树中的信息
    var tree = forest[findTreeNumOfNode(node.id)];
    for (var m = 0, len = delNodes.length; m < len; m++) {
        tree.splice(tree.indexOf(delNodes[m]), 1);
    }

    $('#node-del-modal').modal('hide');
}

function delNodeAndItsChildren(delNodes, id) {
    var node = findNodeById(id);
    var treeNum = findTreeNumOfNode(id);
    var tree = forest[treeNum];
    for (var m = 0, len = tree.length; m < len; m++) {
        var nodeA = tree[m];
        if (nodeA.parentId == id) {
            delNodes.push(nodeA);

            scene.remove(nodeA.node);
            editLink(nodeA.id, node.id, null, null);
            delNodeAndItsChildren(delNodes, nodeA.id);
        }
    }
}

function multipleDelNodesEvent() {
    saveScene();

    var selectedNodes = getSelectedNodes();
    for (var i = 0; i < selectedNodes.length; i++) {
        delNode(selectedNodes[i].id);
    }

    $('#stageMenu').hide();
}

function prepareLawModal(id) {
    var node = findNodeById(id);
    $("#node-law-id").val(id);
    $("#node-law-topic").val(node.topic);

    var lawsDiv = $("#laws");
    lawsDiv.empty();
    // TODO:获得根据事实推荐的法条
    var laws = [{
        "name": "中华人民共和国刑法_第六十七条",
        "content": "犯罪以后自动投案,如实供述自己的罪行的,是自首。对于自首的犯罪分子,可以从轻或者减轻处罚。其中,犯罪较轻的,可以免除处罚。"
    }];

    prepareLawsDiv(lawsDiv, laws);
}

function prepareMulLawModal() {
    var lawsDiv = $("#mul-laws");
    lawsDiv.empty();
    // TODO:获得根据多个事实推荐的法条
    var laws = [{
        "name": "中华人民共和国刑法_第六十七条",
        "content": "犯罪以后自动投案,如实供述自己的罪行的,是自首。对于自首的犯罪分子,可以从轻或者减轻处罚。其中,犯罪较轻的,可以免除处罚。"
    }];

    prepareLawsDiv(lawsDiv, laws);
}

function prepareLawsDiv(lawsDiv, laws) {
    for (var i = 0, len = laws.length; i < len; i++) {
        var div = document.createElement("div");
        div.setAttribute("class", "form-group");
        var checkbox = document.createElement("input");
        checkbox.setAttribute("style", "margin-right:5px;");
        checkbox.setAttribute("type", "checkbox");
        checkbox.setAttribute("id", "checkbox-" + i);
        var a = document.createElement("a");
        a.setAttribute("id", "law-" + i);
        a.setAttribute("title", laws[i].content);
        a.text = laws[i].name;

        div.append(checkbox);
        div.append(a);
        lawsDiv.append(div);
    }
}

function lawAdviceEvent() {
    var factNodeId = $("#node-law-id").val();
    var factNode = findNodeById(factNodeId);
    var parentId = factNode.parentId;
    if (parentId == null || parentId == "null") {
        parentId = drawNode(factNode.node.x + 80, factNode.node.y, null, "结论", 3, "系统自动生成的结论", null);

        // 将事实节点与结论节点连接起来
        moveNode(factNodeId, parentId);
        factNode.parentId = parentId;
        editLink(factNodeId, null, factNodeId, parentId);
    }

    var checkboxes = $("#laws input[type=checkbox]:checked");
    for (var i = 0, len = checkboxes.length; i < len; i++) {
        var id = checkboxes[i].id.substring(checkboxes[i].id.indexOf("-") + 1);

        var lawA = $("#law-" + id);
        var topic = lawA.text();
        var detail = lawA.attr("title");
        if (topic.length > 15) {
            topic = "法条";
            detail = lawA.text() + "\n" + lawA.attr("title");
        }

        // 不重复时添加节点
        if (!isLawRepeated(parentId, topic, detail)) {
            drawNode(factNode.node.x, factNode.node.y + 50, null, topic, 2, detail, parentId);
        }
    }

    $("#law-recommend-modal").modal("hide");
}

function mulLawAdviceEvent() {
    $("#mul-law-recommend-modal").modal("hide");
}

// 生成"指向"下拉框内容
function prepareSelect($select, id, self) {
    $select.empty();
    if (id) {
        // 只添加所选节点信息
        var node = findNodeById(id);
        $select.append("<option value='" + id + "'>" + id + " " + node.topic + "</option>");
    } else {
        var children = getAllChildren(self); // self的所有子节点

        // 将除self以外的所有非子节点信息增加到"指向"中
        var html = "<option value='null'>无</option>";
        for (var m = 0, len1 = forest.length; m < len1; m++) {
            var tree = forest[m];
            for (var n = 0, len2 = tree.length; n < len2; n++) {
                var id = parseInt(tree[n].id);
                if (id != self && children.indexOf(id) == -1) {
                    html += "<option value='" + id + "'>" + id + " " + tree[n].topic + "</option>";
                }
            }
        }
        $select.append(html);
    }
}

// node左键点击显示信息panel，右键点击弹出菜单
function nodeClickEvent(id, event) {
    var node = findNodeById(id);
    if (event.button == 2) {// 右键
        isNodeRightClick = true;

        mouseX = event.pageX;
        mouseY = event.pageY;

        var selectedNodes = getSelectedNodes();
        if (selectedNodes.length > 1) {
            // 多选nodes
            var isFact = true;
            for (var i = 0; i < selectedNodes.length; i++) {
                if (borderColors.indexOf(selectedNodes[i].borderColor) != 1) {
                    isFact = false;
                    break;
                }
            }

            $("#add-element-li").hide();
            $("#element-id").hide();
            $("#element-name").hide();
            $("#hr").hide();
            $("#del-element-li").hide();
            $("#mod-element-li").hide();
            $("#hr2").hide();
            $("#advice-element-li").hide();
            $("#mul-del-element-li").show();
            if (isFact) {
                // 多选事实nodes
                $("#mul-advice-element-li").show();
            }
        } else {
            // 单选node
            $("#mul-advice-element-li").hide();
            $("#mul-del-element-li").hide();
            $("#element-id").show();
            $("#element-name").show();
            $("#hr").show();
            $("#del-element-li").show();
            $("#mod-element-li").show();
            if (node.type == 1) {
                // 事实节点
                $("#hr2").show();
                $("#advice-element-li").show();
            } else {
                $("#hr2").hide();
                $("#advice-element-li").hide();
            }
            $("#element-id").html("<a>id：" + id + "</a>");
            $("#element-name").html("<a>名称：" + node.topic + "</a>");
        }

        // 当前位置弹出菜单（div）
        $("#stageMenu").css({
            top: event.pageY,
            left: event.pageX
        }).show();
    } else if (event.button == 0) {
        // 左键点击节点显示node的信息panel
        $(".node-info-wrapper .node-panel .alert").hide();
        var $infoPanel = $(".node-info-wrapper .node-panel");
        var $panelIdInput = $(".node-info-wrapper #panel-id-input");
        var $panelTopicInput = $(".node-info-wrapper #panel-topic-input");
        var $panelTypeSelect = $(".node-info-wrapper #panel-type-select");
        var $panelLeadToSelect = $(".node-info-wrapper #panel-leadTo-select");
        var $panelDetailInput = $(".node-info-wrapper #panel-detail-input");

        // 清掉各种状态
        $infoPanel.removeClass("panel-primary panel-info panel-danger panel-success panel-warning");
        $panelTypeSelect.removeAttr("disabled");
        $panelLeadToSelect.removeAttr("disabled");

        // 填入信息
        $panelIdInput.val(node.id);
        $panelTopicInput.val(node.topic);
        $panelDetailInput.val(node.detail);
        $panelTypeSelect.val(borderTypes[node.type]);
        // LogicPainter.fillLeadToSelect(graphModel, node.id, node.type, $(".node-info-wrapper #panel-leadTo-select"));
        prepareSelect($(".node-info-wrapper #panel-leadTo-select"), null, node.id);
        $(".node-info-wrapper #panel-leadTo-select").val(node.parentId ? node.parentId : "null");

        switch (node.type) {
            case 0:
                $infoPanel.addClass("panel-success");
                break;
            case 1:
                $infoPanel.addClass("panel-warning");
                break;
            case 2:
                $infoPanel.addClass("panel-danger");
                break;
            case 3:
                $infoPanel.addClass("panel-info");
                break;
        }

        $infoPanel.show();
    }
}

/**
 * 编辑node间的连线，当后两个参数存在空值时，即为删除该连线，否则为重新与新端点连线
 *
 * @param oldNodeId
 * @param oldParentNodeId
 * @param newNodeId
 * @param newParentNodeId
 */
function editLink(oldNodeId, oldParentNodeId, newNodeId, newParentNodeId) {
    var oldLink = findLinkByNodeId(oldNodeId, oldParentNodeId);
    if (oldLink != null) {
        scene.remove(oldLink);
        links.splice(links.indexOf(oldLink), 1);
    }

    if ((newNodeId != null && newNodeId != "null") && (newParentNodeId != null && newParentNodeId != "null")) {
        drawLink(findNodeById(newParentNodeId).node, findNodeById(newNodeId).node);
    }
}

// 移动节点时，需要修改forest内存储的数据
function moveNode(id, newParentId) {
    if (newParentId == null || newParentId == "null") {
        var tree = Array.of();
        var oldTree = forest[findTreeNumOfNode(id)];
        var nodeLoc = oldTree.indexOf(findNodeById(id));
        for (var i = nodeLoc, len = oldTree.length; i < len; i++) {
            tree.push(oldTree[i]);
        }
        if (nodeLoc != 0) {
            oldTree.splice(nodeLoc);
        } else {
            forest.splice(findTreeNumOfNode(id), 1);
        }
        forest.push(tree);
    } else {
        for (var m = 0, len = forest.length; m < len; m++) {
            // 判断当前节点是否是一棵树的根节点
            if (forest[m][0].id == id) {
                // 直接将原树的节点拼接到新树后
                var newTree = forest[findTreeNumOfNode(newParentId)];
                for (var n = 0, len2 = forest[m].length; n < len2; n++) {
                    newTree.push(forest[m][n]);
                }

                // 删除原树
                forest.splice(m, 1);

                return;
            }
        }

        // 不是根节点，判断是否迁移到另一棵树上
        var oldTreeNum = findTreeNumOfNode(id);
        var newTreeNum = findTreeNumOfNode(newParentId);
        if (oldTreeNum != newTreeNum) {
            var node = findNodeById(id);
            // 节点需要迁移到另一棵树上，将node移动到新树上然后从原树上删除
            forest[newTreeNum].push(node);
            console.log(node);
            console.log(forest[oldTreeNum].indexOf(node));
            forest[oldTreeNum].splice(forest[oldTreeNum].indexOf(node), 1);
        }
        // 不需要迁移到另一棵树上时不需要修改forest内的数据内容
        return;
    }
}

/**
 * 根据连线的两个端点找到link
 * @param nodeAId link的一个端点id
 * @param nodeZId link的另外一个端点id
 * @returns {*}
 */
function findLinkByNodeId(nodeAId, nodeZId) {
    for (var i = 0, len = links.length; i < len; i++) {
        var link = links[i];
        if (link.nodeA.id == nodeAId && link.nodeZ.id == nodeZId || link.nodeA.id == nodeZId && link.nodeZ.id == nodeAId) {
            return link;
        }
    }
    return null;
}

/**
 * 根据node的id获得自定义node信息
 * @param id node的id
 * @returns {*}
 */
function findNodeById(id) {
    for (var m = 0, len1 = forest.length; m < len1; m++) {
        var tree = forest[m];
        for (var n = 0, len2 = tree.length; n < len2; n++) {
            if (id == tree[n].id) {
                return tree[n];
            }
        }
    }
    return null;
}

/**
 * 找到node所在树的序号
 * @param id node的id
 * @returns {number} node所在树的序号
 */
function findTreeNumOfNode(id) {
    for (var m = 0, len1 = forest.length; m < len1; m++) {
        var tree = forest[m];
        for (var n = 0, len2 = tree.length; n < len2; n++) {
            if (tree[n].id == id) {
                return m;
            }
        }
    }
}

function getAllChildren(parentId) {
    if (parentId == null) return [];

    var children = getDirectChildren(parentId);
    for (var i = 0; i < children.length; i++) {
        children.concat(getAllChildren(children[i]));
    }
    return children;
}

function getDirectChildren(parentId) {
    var result = Array.of();

    var tree = forest[findTreeNumOfNode(parentId)];
    for (var i = 0; i < tree.length; i++) {
        if (tree[i].parentId == parentId) {
            result.push(tree[i].id);
        }
    }

    return result;
}

/**
 * 获得scene中所有选中的JTopo的node
 * @returns {*}
 */
function getSelectedNodes() {
    var selectedNodes = Array.of();
    var nodes = scene.getDisplayedNodes();
    for (var i = 0; i < nodes.length; i++) {
        if (nodes[i].selected) {
            selectedNodes.push(nodes[i]);
        }
    }
    return selectedNodes;
}

/**
 * 自动排版
 */
function compose() {
    saveScene();

    // 思路是先直接重新排版，然后根据树的minX和minY再对树的根节点的位置进行调整，再重新排版
    for (var m = 0, len1 = forest.length; m < len1; m++) {
        JTopo.layout.layoutNode(scene, forest[m][0].node, true);
    }

    var lastMaxY = 0;    // 上棵树的maxY
    for (var m = 0, len1 = forest.length; m < len1; m++) {
        var minX = forest[m][0].node.x;
        var minY = forest[m][0].node.y;
        for (var n = 0, len2 = forest[m].length; n < len2; n++) {
            minX = forest[m][n].node.x < minX ? forest[m][n].node.x : minX;
            minY = forest[m][n].node.y < minY ? forest[m][n].node.y : minY;
        }
        forest[m][0].node.setLocation(forest[m][0].node.x + (80 - minX), forest[m][0].node.y + 50 + (lastMaxY - minY));
        JTopo.layout.layoutNode(scene, forest[m][0].node, true);

        // 因为排版后y的值有变化，因此需要在排完版后再计算lastMaxY
        for (var n = 0, len2 = forest[m].length; n < len2; n++) {
            lastMaxY = forest[m][n].node.y > lastMaxY ? forest[m][n].node.y : lastMaxY;
        }
    }
}

/**
 * 显示直/曲线图
 */
function curveGraph() {
    isCurve = !isCurve;

    var text = isCurve ? "显示直线图" : "显示曲线图";
    $("#line-btn").text(text);

    for (var i = 0, len = links.length; i < len; i++) {
        var link = isCurve ? new JTopo.CurveLink(links[i].nodeA, links[i].nodeZ, "") : new JTopo.Link(links[i].nodeA, links[i].nodeZ);
        scene.remove(links[i]);
        links.splice(i, 1, link);
        scene.add(link);
    }
}

/**
 * 保存画布
 */
function saveScene() {
    var historyForest = Array.of();
    for (var m = 0, len1 = forest.length; m < len1; m++) {
        var tree = forest[m];
        var backupTree = Array.of();
        for (var n = 0, len2 = tree.length; n < len2; n++) {
            backupTree.push({
                x: tree[n].node.x,
                y: tree[n].node.y,
                id: tree[n].id,
                topic: tree[n].topic,
                type: tree[n].type,
                detail: tree[n].detail,
                parentId: tree[n].parentId
            });
        }
        historyForest.push(backupTree);
    }
    historyForests.push(historyForest);

    var historyLink = Array.of();
    for (var i = 0, len = links.length; i < len; i++) {
        historyLink.push({
            parentNodeId: links[i].nodeA.id,
            nodeId: links[i].nodeZ.id
        });
    }
    historyLinks.push(historyLink);

    $("#revoke-btn").removeClass("disabled");
}

function deleteScene() {
    historyForests.pop();
    historyLinks.pop();
}

/**
 * 撤销
 */
function repeal() {
    scene.clear();
    forest.splice(0, forest.length);
    links.splice(0, links.length);

    var lastForest = historyForests.pop();
    for (var m = 0, len1 = lastForest.length; m < len1; m++) {
        var tree = lastForest[m];
        for (var n = 0, len2 = tree.length; n < len2; n++) {
            drawNode(tree[n].x, tree[n].y, tree[n].id, tree[n].topic, tree[n].type, tree[n].detail, null);
        }
    }

    var lastLinks = historyLinks.pop();
    for (var i = 0, len = lastLinks.length; i < len; i++) {
        var nodeId = lastLinks[i].nodeId, parentNodeId = lastLinks[i].parentNodeId;

        moveNode(nodeId, parentNodeId);
        findNodeById(nodeId).parentId = parentNodeId;

        drawLink(findNodeById(parentNodeId).node, findNodeById(nodeId).node);
    }

    if (historyForests.length == 0) {
        $("#revoke-btn").addClass("disabled");
    }
}

function isLawRepeated(parentId, lawTopic, lawDetail) {
    var parentNodeChildren = getDirectChildren(parentId);
    for (var i = 0; i < parentNodeChildren.length; i++) {
        var childNode = findNodeById(parentNodeChildren[i]);
        if (childNode.type == 2 && childNode.topic == lawTopic && childNode.detail == lawDetail) {
            return true;
        }
    }
    return false;
}