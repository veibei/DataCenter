/**
 * Created by songxu on 2016/11/2.
 */
function saveCookie(key, value, exp) {
    var date = new Date();
    date.setDate(date.getDate() + exp);
    document.cookie = key + "=" + value + ";expires=" + date.toGMTString();
}
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i].trim();
        if (c.indexOf(name) == 0) {
            var val = c.substring(name.length, c.length);
            return val.split('^')[0];
        }
    }
    return "";
}