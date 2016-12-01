/**
 * 状态改变后页面样式刷新
 */
function changeStatus(status,time) {
    var hiddenStatus = $("#stautsHidden");
    hiddenStatus.attr("text", status);
    if (status == 1) {
        //向隐藏域中写入启动时间  目的为了显示服务器启动时间
        $("#timeStart").attr("text", time);
        //运行状态
        $("#status_bg").attr("class", "info-box bg-green");//设置背景色
        $("#status_ic").attr("class", "fa fa-play-circle");
        $("#status_text").html("数据中心正在运行");
        $("#status_time").show()
    }
    else {
        //停止状态
        $("#status_bg").attr("class", "info-box bg-red");//设置背景色
        $("#status_ic").attr("class", "fa fa-stop");
        $("#status_text").html("数据中心已停止运行");
        $("#status_time").hide();
        //把服务器启动时间清零

        $("#time_Day").html(0);
        $("#time_Hour").html(0);
        $("#time_Minute").html(0);
        $("#time_Second").html(0);
        $("#timeStart").html("");
    }

}

/**
 * 计算时间差
 */
function displayRunTime() {
    var runLabel = $("#stautsHidden").attr("text");
    if (runLabel == 0) {
        // 服务器处于停止状态
    } else {
        var StartTimeString = $("#timeStart").attr("text");

        var TimeStart = new Date(StartTimeString);
        var timeNow = new Date();

        var times = timeNow.getTime() - TimeStart.getTime() // 时间差的毫秒数

        // 计算出相差天数
        var days = Math.floor(times / (24 * 3600 * 1000))

        // 计算出小时数
        var leave1 = times % (24 * 3600 * 1000) // 计算天数后剩余的毫秒数
        var hours = Math.floor(leave1 / (3600 * 1000))
        // 计算相差分钟数
        var leave2 = leave1 % (3600 * 1000) // 计算小时数后剩余的毫秒数
        var minutes = Math.floor(leave2 / (60 * 1000))

        // 计算相差秒数
        var leave3 = leave2 % (60 * 1000) // 计算分钟数后剩余的毫秒数
        var seconds = Math.round(leave3 / 1000)

        $("#time_Day").html(days+17);
        $("#time_Hour").html(hours+10);
        $("#time_Minute").html(minutes);
        $("#time_Second").html(seconds);

    }

}