/**
 * 控制类的事件
 */

function startOnclick()
{
	var a = $("#stautsHidden").attr("text");
	if (a == 1)
	{
		alert("服务器已经处于运行状态。。。");

	} else
	{
//		window.location.href = "startServerAction";
		startServer();
	}

}

function stopOnclick()
{
	var a = $("#stautsHidden").attr("text");
	if (a == 0)
	{
		alert("服务器已经处于停止状态。。。");

	} else
	{
//		window.location.href = "stopServerAction";
		stopServer();
	}

}
/**
 * 计算时间差
 */
function displayRunTime()
{
	var runLabel = $("#stautsHidden").attr("text");
	if (runLabel == 0)
	{
		// 服务器处于停止状态
	} else
	{
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

		$("#time_Day").html(days);
		$("#time_Hour").html(hours);
		$("#time_Minute").html(minutes);
		$("#time_Second").html(seconds);

	}

}