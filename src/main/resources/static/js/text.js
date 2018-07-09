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
    $.ajax({
        url: "/model/getFactContents",
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
            $('#factPara').text("公诉机关指控："+data);

        }, error: function (XMLHttpRequest, textStatus, errorThrown) {

        }
    });
    $.ajax({
        url: "/logic/getResultContents",
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
            $('#resultPara').text("本院认为："+data+" \n综上所述，本案事实清楚，证据确实充分。本院依照《中华人民共和国刑法》第一百三十三条、第六十七条第一、三款、第七十二条、第七十三条第二、三款、第七十六条之规定，判决如下：\n" +
                "判决结果:"+"\n如不服本判决，可在接到判决书的第二日起十日内，通过本院或者直接向湖南省怀化市中级人民法院提出上诉。书面上诉的，应当提交上诉状正本一份，副本六份。");

        }, error: function (XMLHttpRequest, textStatus, errorThrown) {

        }
    });
});