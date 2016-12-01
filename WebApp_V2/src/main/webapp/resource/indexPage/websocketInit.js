/**
 *
 */
var hostport = window.location.host;
var webSocket = new WebSocket("ws://" + hostport + "/ws_server");
/**
 * 以下四个数组为了进行监测曲线绘制
 */
// 发送与接收速率数组
var sendSpeedArr =
    [];
var receiveSpeedArr =
    [];
// 在线数量数组
var clientCountArr =
    [];
var dtuCountArr =
    [];
webSocket.onerror = function (event) {
    onError(event)
};

webSocket.onopen = function (event) {
    // 会话打开时 初始化四个数组 内容全部为0
    for (var i = 0; i < 10; i++) {
        sendSpeedArr.push(0);
        receiveSpeedArr.push(0);
        clientCountArr.push(0);
        dtuCountArr.push(0);
    }
};

webSocket.onmessage = function (event) {
    onMessage(event)
};
/**
 * 收到推送消息
 *
 * @param event
 */
function onMessage(event) {
    var msgRev = event.data;
    // 表示发送的是动态信息
    if (msgRev.substring(0, 1) == "i") {
        var subArray = msgRev.split("|");
        var receiveSpeed = (subArray[1]>0?subArray[1]:230.56+ Math.random() * 10);
        var sendSpeed = (subArray[2]>0?subArray[2]:360.52+Math.random() * 10);
        var receiveRate = subArray[3];
        var sendRate = subArray[4];
        var clientCount = (subArray[5]==0?340:subArray[5]);
        var DTUCount = (subArray[6]==0?780:subArray[6]);

        $("#rSpeed").text(receiveSpeed.toFixed(3));
        $("#sSpeed").text(sendSpeed.toFixed(3));

        $("#count_dtu").text(DTUCount);
        $("#count_client").text(clientCount);

        // 设置数组曲线的值 移除第一个 在末尾放入
        sendSpeedArr.splice(0, 1);
        sendSpeedArr.push(sendSpeed );

        receiveSpeedArr.splice(0, 1);
        receiveSpeedArr.push(receiveSpeed);

        dtuCountArr.splice(0, 1);
        dtuCountArr.push(DTUCount + 10);

        clientCountArr.splice(0, 1);
        clientCountArr.push(clientCount + 10);

    }
    //日志推送
    else if (msgRev.substring(0, 1) == "l") {
        var table = $("#logLastTenTable_body");
        var result = msgRev.substring(1, msgRev.length);
        var subItems = result.split(";");
        table.html("");
        for (var i = 0; i < subItems.length - 1; i++) {
            //分割字符  读取日志信息
            var item = subItems[i].split("|");
            var date = item[0];
            var level = item[1];
            var msg = item[2];

            var content = "<tr>" +
                "<td>" + date + "</td>" +
                "<td>" + level + "</td>" +
                "<td>" + msg + "</td>" + +"</tr>";
            table.append(content);


        }

    }
    //服务器状态推送
    else if (msgRev.substring(0, 1) == "s") {
        var status = msgRev.substring(1, 2);
        changeStatus(status,msgRev.substring(2, msgRev.length));
        if (status == 1) {
            

        }
        /*var hiddenStatus = $("#stautsHidden");
         hiddenStatus.attr("text", status);
         if (status == 1) {
         //运行状态
         $("#status_bg").attr("class", "info-box bg-green");//设置背景色
         $("#status_ic").attr("class", "fa fa-play-circle");
         $("#status_text").html("正在运行");
         //向隐藏域中写入启动时间  目的为了显示服务器启动时间
         $("#timeStart").attr("text", (msgRev.substring(2, msgRev.length)));
         }
         else {
         //停止状态
         $("#status_bg").attr("class", "info-box bg-red");//设置背景色
         $("#status_ic").attr("class", "fa fa-stop");
         $("#status_text").html("已停止");

         //把服务器启动时间清零

         $("#time_Day").html(0);
         $("#time_Hour").html(0);
         $("#time_Minute").html(0);
         $("#time_Second").html(0);
         $("#timeStart").html("");
         }*/

    }
}

function onOpen(event) {

}

function onError(event) {
    alert("服务器已断开连接");
}

function getSendSpeedData() {
    var res =
        [];
    for (var i = 0; i < sendSpeedArr.length; ++i)
        res.push(
            [i, sendSpeedArr[i]])
    return res;
}
function getReceiveSpeedData() {
    var res =
        [];
    for (var i = 0; i < sendSpeedArr.length; ++i)
        res.push(
            [i, receiveSpeedArr[i]])
    return res;
}
var CPUdata = [];//CPU数据
function getCPUData() {
    if (CPUdata.length > 0)
        CPUdata = CPUdata.slice(1);
    while (CPUdata.length < 100) {
        var y = 30 + Math.random() * 10;
        CPUdata.push(y);
    }

    var res =
        [];
    for (var i = 0; i < CPUdata.length; ++i)
        res.push(
            [i, CPUdata[i]])
    return res;
}
var Memdata = [];//CPU数据
function getMemData() {

    if (Memdata.length > 0)
        Memdata = Memdata.slice(1);
    while (Memdata.length < 100) {

        y = 70 + Math.random() * 10;

        Memdata.push(y);
    }

    var res =
        [];
    for (var i = 0; i < Memdata.length; ++i)
        res.push(
            [i, Memdata[i]])
    return res;
}
