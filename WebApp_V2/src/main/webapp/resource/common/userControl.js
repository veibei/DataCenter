/**
 * ÍË³öµÇÂ¼
 */
function logOut() {
    var token = getCookie("token");
    $.ajax(
        {
            url: "/auth/logout",
            data: {
                "token": token,
            },
            type: 'post',
            dataType: 'json',
            success: function (strValue) {
                if (strValue == 'success') {
                    window.location.href = "/login.html"
                }
            }
        });
}
/**
 * ¸öÈËÒ³
 */
function userPage() {

    window.location.href = "userPageAction";


}