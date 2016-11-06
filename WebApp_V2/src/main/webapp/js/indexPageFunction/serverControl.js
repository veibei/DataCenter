/**
 * 页面控制服务器启动与关闭
 */

function startServer()
{
	$.ajax(

	{

		url : "startServerAction",
		dataType : "text",
		type : 'post',
		async : false,

		success : function(strValue)

		{
			if (strValue == 'true')

			{
				alert("数据中心服务启动中...");
			}
			else if(strValue=='false')
			{
				alert("数据中心服务启动失败...");
			}
			else if(strValue=='close')
			{
				alert("数据中心与远程主机连接失败");
			}
		}
	});

}
function stopServer()
{
	$.ajax(

	{

		url : "stopServerAction",
		dataType : "text",
		type : 'post',
		async : false,

		success : function(strValue)

		{
			if (strValue == 'true')

			{
				alert("数据中心服务关闭中...");
			} else if(strValue=='false')
			{
				alert("数据中心服务失败...");
			}
			else if(strValue=='close')
			{
				alert("数据中心与远程主机连接失败");
			}
		}
	});

}