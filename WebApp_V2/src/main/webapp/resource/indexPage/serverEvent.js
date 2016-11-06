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
