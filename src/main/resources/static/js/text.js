$(function(){
    var cid = $.session.get('cid');

    $.ajax({
        url: "/model/getEvidenceContents",
        type: 'POST',
        // dataType:"json",
        data: {"caseID": cid},
        beforeSend: function (data) {
            //这里判断，如果没有加载数据，会显示loading
            if (data.readyState == 0) {

            }
        },
        success: function (data) {

            // console.log(data);
            $('#evidencePara').text("上述事实，有公诉机关及被告人提供，并经法庭质证、认证的下列证据予以证明："+data);

        }, error: function (XMLHttpRequest, textStatus, errorThrown) {

        }
    });
});