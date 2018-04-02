
$(function(){
    var caseInfoStr = $.session.get("caseInfo");
    var caseInfo = JSON.parse(caseInfoStr);
    $("#caseNum").text(caseInfo.cNum);
    // $("#caseBrief").text(caseInfo['']);
    $("#caseName").text(caseInfo.cname);
    $("#underTaker").text(caseInfo.undertaker);
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
            initGraph(data['trusts'],data['freeHeaders'],data['joints'],data['arrows'],data['facts']);
            initRejection(data['untrusts']);

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

// //初始化采信未采信列表
// function initList(parentID,evidences) {
//
//     var content = "";
//     var headerId = 0;
//
//     for(var i = 0;i<evidences.length;i++){
//         var evidenceTemp = evidences[i];
//         var classTemp = "evidence evidence_splitLine";
//         var idTemp = "heads_chain";
//         var dataattr_body = "";
//         var dataattr_header = "";
//         // if(i<evidences.length-1)
//         //     classTemp+=" evidence_splitLine";
//         if(evidenceTemp['原告']==1)
//             classTemp += " evidence_plaintiff";
//         if(parentID=='#rejection') {
//             idTemp += "_";
//         }else{
//             dataattr_body = "data-id='"+i+"'";
//         }
//
//         content+="<div class=\""+classTemp+"\" "+dataattr_body+">\n" +
//             "                            <a data-toggle=\"collapse\" href=\"#"+idTemp+i+"\" class=\"evidence_a\">\n" +
//             evidenceTemp['证据']+"</a>" +
//             "                            <div id=\""+idTemp+i+"\" class=\"panel-collapse collapse in\">\n" +
//             "                                <div class=\"head_div\">";
//
//         for(var j = 0;j<evidenceTemp['链头'].length;j++){
//             if(parentID=='#adoption') {
//                 dataattr_header = "data-id='"+(headerId++)+"'";
//             }
//             content+="<span class=\"head_chain\" "+dataattr_header+">"+evidenceTemp['链头'][j]+"</span>";
//         }
//         content+="</div></div></div>";
//     }
//     $(parentID).find(".panel-body").html(content);
// }

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