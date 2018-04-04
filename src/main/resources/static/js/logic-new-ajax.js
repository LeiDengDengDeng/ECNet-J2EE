/**
 * Created by deng on 2018/4/4.
 */
$(function () {
    loadLogicNodes();
});

function loadLogicNodes() {
    $.ajax({
        type: "GET",
        url: "/logic/getAll",
        data: {caseID: cid},
        async: false,
        success: function (data) {
            loadData(data);
        }, error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
            alert(textStatus);
        }
    });
}

function saveLogicNodes(data) {
    $.ajax({
        type: "POST",
        url: "/logic/saveAll?caseID=" + cid,
        data: data,
        contentType: "application/json",
        success: function (data) {
        }, error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
            alert(textStatus);
        }
    });
}