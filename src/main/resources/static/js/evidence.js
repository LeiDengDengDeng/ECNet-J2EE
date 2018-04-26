var textID = -1;
var factList_div = {};
var divIndex1 = 0;
var divIndex2 = 0;
$(function(){
    var caseInfoStr = $.session.get("caseInfo");
    var caseInfo = JSON.parse(caseInfoStr);
    $("#caseNum").text(caseInfo.cNum);
    // $("#caseBrief").text(caseInfo['']);
    $("#caseBrief").text("受贿罪");
    $("#caseName").text(caseInfo.cname);
    $("#underTaker").text("林世开");
    $("#caseDate").text(caseInfo.fillingDate);

    initEvidences();
});

function initEvidences() {

    $.ajax({
        type: "post",
        url: "/model/getEvidences",
        data:{"cid":cid},
        async: false,
        success: function (data) {
            // alert(data['trusts'][1]['body']['body']);
            initGraph(data['trusts'],data['freeHeaders'],data['facts'],data['freeJoints'],data['arrows']);
            initRejection(data['untrusts']);
            if(data['factDoc']!=null){
                textID = data['factDoc']['id'];
                initFactsDiv(data['factDoc']['text'],data['facts']);
            }
        }, error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
            alert(textStatus);
        }
    });
}

//初始化未采信列表
function initRejection(evidences) {
    var content = "";

    for(var i = 0;i<evidences.length;i++){
        var evidenceTemp = evidences[i];
        var classTemp = "evidence evidence_splitLine";
        var idTemp = "heads_chain_";
        if(evidenceTemp['isDefendant']==0)//原告
            classTemp += " evidence_plaintiff";

        content+="<div class=\""+classTemp+"\">\n" +
            "                            <a data-toggle=\"collapse\" href=\"#"+idTemp+i+"\" class=\"evidence_a\">\n" +
            evidenceTemp['content']+"</a>" +
            "                            <div id=\""+idTemp+i+"\" class=\"panel-collapse collapse in\">\n" +
            "                                <div class=\"head_div\">";

        for(var j = 0;j<evidenceTemp['headers'].length;j++){
            content+="<span class=\"head_chain\">"+evidenceTemp['headers'][j]+"</span>";
        }
        content+="</div></div></div>";
    }
    $("#rejection").find(".panel-body").html(content);
}

//添加采信证据，id:链体id，evidence_content:证据内容
function addEvidence(id,evidence_content,isDefendant) {

    var filter_content = '.evidence[data-id='+id+']';
    var p_div = $(filter_content);
    var classTemp = "evidence evidence_splitLine";
    if(isDefendant==0)
        classTemp += " evidence_plaintiff";

    if(p_div==null||p_div.length==0){
        var div_html="<div class=\""+classTemp+"\" data-id='"+id+"'>\n" +
            "                            <a data-toggle=\"collapse\" href=\"#heads_chain"+id+"\" class=\"evidence_a\">\n" +
            evidence_content+"</a></div>";

        $("#adoption").find(".panel-body").append(div_html);
    }
}

//添加链头
function addHeaderofChain(header_content,header_id,body_id) {
    var filter_content = '.evidence[data-id='+body_id+']';
    var p_div = $(filter_content);

    if(p_div!=null&&p_div.length>0){
        var id = p_div.find('a').first().attr("href").substring(1);

        if(p_div.find('.head_div')==null||p_div.find('.head_div').length==0){

            var add_html = " <div id=\""+id+"\" class=\"panel-collapse collapse in\">\n" +
                "                                <div class=\"head_div\">" +
                "<span class=\"head_chain\" data-id='"+header_id+"'>"+header_content+"</span></div></div>";
            p_div.append(add_html);
        }else{
            var header_div = p_div.find('span[data-id='+header_id+']');

            if(header_div==null||header_div.length==0){
                var add_html = "<span class=\"head_chain\" data-id='"+header_id+"'>"+header_content+"</span>";
                p_div.find('.head_div').append(add_html);
            }
        }
    }
}

function initFactsDiv(text,facts) {
    $("#factArea").val(text);
    if(facts!=null)
    for(var i = 0;i<facts.length;i++){
        var data = facts[i];
        var fact = data['fact'];
        if(fact['textID']==textID){
            var joints = data['joints'];
            addFactTxtField(fact['id'],fact['content'],fact['confirm'],joints);
        }
    }
}

function splitFacts() {
    var str="2,2,3,5,6,6"; //这是一字符串
    $("#factListDiv").html(" <br>\n" +
        "                <br>\n" +
        "                <br>");
    str=$("#factArea").val();

    $.ajax({
        url: "/model/splitFact",
        type: 'POST',
        dataType:"json",
        data: {"caseID": cid, "text": str},
        beforeSend: function (data) {
            //这里判断，如果没有加载数据，会显示loading
            if (data.readyState == 0) {
                
            }
        },
        success: function (data) {

            for(var i = 0;i<data.length;i++){
                textID = data[i]['textID'];
                // factList_div[data[i]['id']] =
                addFactTxtField(data[i]['id'],data[i]['content'],data[i]['confirm'],null);
                // drawFact(false,x,y,data[i]['name'],data[i]['content'],data[i]['name'],data[i]['content'],data[i]['logicNodeID'],data[i]['textID'],data[i]['confirm'],true);
                // y+=body_height + headerGap_y;
                if(data[i]['id']>divIndex1){
                    divIndex1 = data[i]['id']+1;
                }
            }
            // updateFactListofGraph();
            
        }, error: function (XMLHttpRequest, textStatus, errorThrown) {
            // alert("save joint");
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
            alert(textStatus);
        }
    });
}

function addFactTxtField(id,content,confirm,joints) {
    if(content==null){
        content = "";
    }

    var html="<div data-factID='"+id+"' class='evibody form-group row'><br><div class='col-md-8'>" +
        "<input type='text' class='form-control myinfo1' value='"+content+"' data-factID='"+id+"'>" +
        "<a class=\"glyphicon myRemove glyphicon-remove btn form-control-feedback\"style=\"pointer-events:auto;padding-left: 1%\" " +
        "data-factID='"+id+"'  onclick='removeFactDiv(this)'></a></div>" +
        "<div class='col-md-2'>";
    if(confirm==1){
        html+="<button value='"+confirm+"' class='btn btn-danger' data-factID='"+id+"' " +
            "onclick='updateFactConfirm(this)'>不认定</button></div></div>";
    }else{
        html+="<button value='"+confirm+"' class='btn btn-success' data-factID='"+id+"' " +
            "onclick='updateFactConfirm(this)'>认定</button></div></div>";
    }

    $("#factListDiv").append(html);
    createJointHtml(id,joints);
}

function addFactElmt() {
    addFactTxtField(divIndex1++,"",1,null);
}

function getJoints() {
    // $.ajax({
    //     url: "/model/extractJoints",
    //     type: 'POST',
    //     data: {"caseID": cid, "text": str},
    //     beforeSend: function (data) {
    //         //这里判断，如果没有加载数据，会显示loading
    //         if (data.readyState == 0) {
    //
    //         }
    //     },
    //     success: function (data) {
    //
    //         for(var i = 0;i<data.length;i++){
    //
    //         }
    //
    //     }, error: function (XMLHttpRequest, textStatus, errorThrown) {
    //
    //     }
    // });

}

function removeAllFactsInGraph() {
    for(var fid in factList){
        if(factList[fid]!=null){
            deleteFact(fid);
        }
    }

    for(var jid in jointList){
        if(jointList[jid]!=null){
            deleteJoint(jid);
        }
    }
}

function updateFactConfirm(select) {

    var confirm=1;
    console.log($(select).val());

    if($(select).val()==0){
        $(select).removeClass("btn-success");
        $(select).addClass("btn-danger");
        $(select).text("不认定");
        $(select).val(1);
        confirm=1;
    }else{
        $(select).removeClass("btn-danger");
        $(select).addClass("btn-success");
        $(select).text("认定");
        $(select).val(0);
        confirm=0;
    }
}

function removeFactDiv(fact){
    var factID=$(fact).attr("data-factID");
    console.log("factID"+factID);
    // deleteFact(factID);
    // $("#factSelector option[value='"+factID+"']").remove();
    $(".headList[data-factID='"+factID+"']").remove();
    $(".evibody[data-factID='"+factID+"']").remove();
}

function createJointHtml(factID,joints){
    var html="<div class='headList text-left' data-factID='"+factID+"'>";
    if(joints!=null)
    for(var j=0;j<joints.length;j++){
        html+="<span data-jointID='"+joints[j].id+"' class='head_chain'><label contenteditable style='font:inherit;min-width: 25px;'>"
            +joints[j].content+"</label><span data-jointID='"+joints[j].id
            +"' class='glyphicon glyphicon-remove  headRemove' onclick='removeJointDiv(this)'></span></span>";
        if(joints[j].id>divIndex2){
            divIndex2 = joints[j].id+1;
        }
    }

    html+="<span data-factID='"+factID+"' class='glyphicon glyphicon-plus headRemove addHead' onclick='addJointDiv(this)'></span></div>";
    $(".evibody[data-factID='"+factID+"']").after(html);

}

function removeJointDiv(remove) {
    var id=$(remove).attr("data-jointID");
    console.log("jointid"+id);
    // deleteJoint(id);
    $(".head_chain[data-jointID='"+id+"']").remove();
}

function addJointDiv(span) {
    var id=divIndex2++;
    var  html="<span data-jointID='"+id+"' class='head_chain'><label contenteditable style='font:inherit;min-width: 25px;'></label><span data-jointID='"+id+"' class='glyphicon glyphicon-remove  headRemove' onclick='removeJointDiv(this)'></span></span>";
    $(span).before(html);
}

function clearFactDiv() {
    $("#factListDiv").html(" <br>\n" +
        "                <br>\n" +
        "                <br>");
    $("#factArea").val('');
}

function exportToModel() {
    factIndex = 0;
    jointIndex = 0;
    removeAllFactsInGraph();

    var joint_x = 10 + (body_width/2) + body_width/2 + headerGap_x + header_radius + header_radius + 50 + joint_width/2;
    var fact_x = joint_x + body_width/2 + 60 + (joint_width/2);
    var y = 10 + header_radius;
    factList = {};
    jointList = {};

    $('body').loading({
        loadingWidth:240,
        title:'请稍候...',
        name:'exportToModel',
        animateIn:'none',
        discription:'这是一个描述...',
        direction:'row',
        type:'origin',
        originBg:'#71EA71',
        originDivWidth:30,
        originDivHeight:30,
        originWidth:4,
        originHeight:4,
        smallLoading:false,
        titleColor:'#388E7A',
        loadingBg:'#312923',
        loadingMaskBg:'rgba(22,22,22,0.2)',
        mustRelative: true
    });

    $.ajax({
        url:"/model/deleteFactsAndJoints",
        type:'POST',
        data: {"caseID":cid},
        // dataType:"json",
        // contentType: "application/json; charset=utf-8",
        success:function(data) {

            $(".evibody").each(function () {

                var factID = $(this).attr("data-factID");
                var content = $(this).find('input').eq(0).val();
                var confirm = $(this).find('button').eq(0).val();

                var jnode,fnode;
                var jointsSpans = $(".headList[data-factID='"+factID+"']").find('.head_chain');

                var fy = 0;
                if(jointsSpans==null||jointsSpans.length==0){
                    y+=body_height + headerGap_y;
                    fy = y;
                }else{
                    fy = y+((jointsSpans.length-1)*(joint_width + headerGap_y)/2);
                }
                var f = {"caseID":cid,"name":"","content":content,'textID':textID,'confirm':confirm,'x':fact_x,'y':fy};

                $.ajax({
                    type: "post",
                    url: "/model/saveFact",
                    data: JSON.stringify(f),
                    // dataType:"json",
                    contentType: "application/json; charset=utf-8",
                    // async: true,
                    success: function (data) {
                        factID = data['id'];
                        if(confirm==1){
                            fnode = drawFact(false,fact_x,fy,data['id'],data['name'],data['content'],data['logicNodeID'],data['textID'],data['confirm'],true);
                        }

                        for(var i = 0;i<jointsSpans.length;i++){

                            var jc = jointsSpans.eq(i).find('label').eq(0).text();
                            $.ajax({
                                type: "post",
                                url: "/model/saveJoint",
                                data: JSON.stringify({"caseID":cid,"name":jc,"content":jc,'factID':factID,'x':joint_x,'y':y}),
                                // dataType:"json",
                                contentType: "application/json; charset=utf-8",
                                async: false,
                                success: function (data) {
                                    if(confirm==1){

                                        jnode = drawJoint(false,joint_x,y,data,"",jc,true);
                                        y += headerGap_y + joint_width;
                                        addLink(jnode,fnode);
                                    }
                                }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                                }
                            });
                        }
                    }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            });
            updateFactListofGraph();
            removeLoading('exportToModel');
        }
    });
}