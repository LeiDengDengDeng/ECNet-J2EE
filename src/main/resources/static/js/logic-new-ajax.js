/**
 * Created by deng on 2018/4/4.
 */
$(function () {
    loadLogicNodes();
});

/**
 * 获取数据库中画布节点数据
 */
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

/**
 * 将画布节点数据保存至数据库中
 * @param data
 */
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

/**
 * 查询根据频率统计推荐的法条数据
 * @param detail
 * @returns {Array}
 */
function queryFrequencyLaws(detail) {
    var laws = [];
    $.ajax({
        type: "GET",
        url: "http://127.0.0.1:8088/frequency",
        async: false,
        data: {content: detail, limit: 20},
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                laws.push(data[i].law);
            }
        }, error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
            alert(textStatus);
        }
    });
    return laws;
}