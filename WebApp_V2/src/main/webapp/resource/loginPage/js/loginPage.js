/**
 *
 */
function press(event) {
    if (event.keyCode == 13) {

        login();
    }

}
function login() {

    var username = $("#username").val();
    var password = $("#password").val();
    if (!username) {
        $('#noticeDiv').removeClass().addClass("alert alert-danger");
        $('#noticeDiv').html("用户名不能为空！");
        return;
    }
    if (!password) {
        $('#noticeDiv').removeClass().addClass("alert alert-danger");
        $('#noticeDiv').html("密码不能为空！");
        return;
    }
    $.ajax(
        {
            url: "/auth/login",
            data: {
                "username": username,
                "password": password
            },
            type: 'post',
            dataType: 'json',
            success: function (strValue) {
                if (strValue != 'error') {
                    $('#noticeDiv').removeClass().addClass("alert alert-success");
                    $('#noticeDiv').html("登录成功！");
                    saveCookie("token",strValue,1);
                    window.location.href = "/index.html"
                } else {
                    $('#noticeDiv').removeClass().addClass("alert alert-danger");
                    $('#noticeDiv').html("用户名密码错误！");

                }
            }
        });

}



