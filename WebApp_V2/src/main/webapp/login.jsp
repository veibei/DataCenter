<%@page import="org.apache.struts2.components.Else"%>
<%@page import="com.songxu.bean.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta charset="utf-8">
<title>DTU数据中心管理平台</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="DTU数据中心">
<meta name="author" content="songxu9185@163.com">


<!-- The styles -->
<link href="resource/loginPage/css/style.css" rel="stylesheet">
<link href="resource/common/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="resource/common/js/jquery.min.js"></script>
<link rel="shortcut icon" href="img/favicon.ico">

<!-- user js -->
<script type="text/javascript" src="resource/loginPage/js/loginPage.js"></script>
</head>

<body onkeydown="press(event);">

	<div class="ch-container" style="width: 100%">

			<div class="row">
				<div class="col-md-12 center login-header">
					<h2>欢迎使用DTU数据中心管理平台</h2>
				</div>
				<!--/span-->
			</div>
			<!--/row-->

			<div class="row">
				<div class="well col-md-5 center login-box">
					<div id="noticeDiv" class="alert alert-info">
						<%
							if (dataType == null || dataType.equals("0"))
							{
								out.print("请输入用户名和密码");
							}
							else if (dataType.equals("1"))
							{
								out.print("登录已过期，请输入用户名和密码");
							}
							else if (dataType.equals("3"))
							{
								out.print("登录已退出");
							}
							else
							{
								out.print("请输入用户名和密码");
							}
						
						%>

					</div>
					<form class="form-horizontal">
						<fieldset>
							<div class="input-group input-group-lg">
								<span class="input-group-addon"><i
									class="glyphicon glyphicon-user red"></i></span> <input id="username"
									type="text" class="form-control" autofocus="autofocus"
									placeholder="用户名">
							</div>
							<div class="clearfix"></div>
							<br>

							<div class="input-group input-group-lg">
								<span class="input-group-addon"><i
									class="glyphicon glyphicon-lock red"></i></span> <input id="password"
									type="password" class="form-control" placeholder="密码">
							</div>
							<div class="clearfix"></div>

							
							<div class="clearfix"></div>

							<p class="center col-md-5">
								<button type="button" class="btn btn-primary" onclick="login();">登录</button>
							</p>
							
						</fieldset>
					</form>

				</div>
				<!--/span-->

			</div>
			<!--/row-->
		</div>
		<!--/fluid-row-->







</body>
</html>
