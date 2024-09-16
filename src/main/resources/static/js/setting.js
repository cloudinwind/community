$(function(){
    $("#uploadForm").submit(upload);
});

function upload() {
    $.ajax({
        url: "http://up-z0.qiniup.com",
        method: "post",
        processData: false,     // 不把表单转换为字符串
        contentType: false,     // 不让jquery设置上传类型, 因此此次上传的是文件
        data: new FormData($("#uploadForm")[0]),
        success: function(data) {
            if(data && data.code == 0) {
                // 更新头像访问路径
                $.post(
                    CONTEXT_PATH + "/user/header/url",
                    {"fileName":$("input[name='key']").val()},
                    function(data) {
                        data = $.parseJSON(data);
                        if(data.code == 0) {
                            window.location.reload();
                        } else {
                            alert(data.msg);
                        }
                    }
                );
            } else {
                alert("上传失败!");
            }
        }
    });
    // return false 表示 不要再继续进行表单提交, 因为上面已经进行了提交逻辑
    return false;
}