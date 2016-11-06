<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.songxu.bean.RemoteServerBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.songxu.bean.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	//免登录或登录过期
	User user =(User) session.getAttribute("userinfo");
	if(null==user||user.getLogtimeTimestamp()<0L)
	{
		response.sendRedirect("login.jsp?datatype=1");
	}


%>



<html >
<head>

<meta charset="utf-8">
<title>DTU数据中心管理页面</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description"
	content="DTU数据中心">
<meta name="author" content="songxu9185@163.com">

<link href="resource/common/css/bootstrap.min.css" rel="stylesheet">
<link href="resource/common/css/AdminLTE.min.css" rel="stylesheet">
<link href="resource/common/css/skin-blue.min.css" rel="stylesheet">

<!--
<link id="bs-css" href="css/bootstrap-cerulean.min.css" rel="stylesheet">
<link href="resource/loginPage/css/style.css" rel="stylesheet">
<link href="resource/common/css/bootstrap.min.css" rel="stylesheet">
The styles -->
<!-- jQuery -->
<script src="resource/common/js/jquery.min.js"></script>


<!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

<!-- The fav icon -->
<link rel="shortcut icon" href="img/favicon.ico">
<!-- myFucntion -->
<script src="js/indexPageFunction/serverEvent.js"></script>
<script src="js/indexPageFunction/userControl.js"></script>
<script src="js/indexPageFunction/serverControl.js"></script>
</head>

<body>
	<!-- topbar starts -->
	<div class="navbar navbar-default" role="navigation">

		<div class="navbar-inner">
			<button type="button" class="navbar-toggle pull-left animated flip">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="loginAction"> <!--img alt="Charisma Logo" src="img/logo20.png" class="hidden-xs"/> -->
				<span>DTUCENTER</span></a>

			<!-- user dropdown starts -->
			<div class="btn-group pull-right">
				<button class="btn btn-default dropdown-toggle"
					data-toggle="dropdown">
					<i class="glyphicon glyphicon-user"></i><span
						class="hidden-sm hidden-xs">
						<!-- 显示用户名 -->
						<s:property value="#session.userinfo.username"/>
						</span> <span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a onclick="userPage();" style="cursor: pointer;">用户页面</a></li>
					<li class="divider"></li>
					<li><a onclick="logOut();" style="cursor: pointer;">退出</a></li>
				</ul>
			</div>
			<!-- user dropdown ends -->

			<!-- theme selector starts -->
			<div class="btn-group pull-right theme-container animated tada">
				<button class="btn btn-default dropdown-toggle"
					data-toggle="dropdown">
					<i class="glyphicon glyphicon-tint"></i><span
						class="hidden-sm hidden-xs"> 更换主题</span> <span
						class="caret"></span>
				</button>
				<ul class="dropdown-menu" id="themes">
					<li><a data-value="classic" href="#"><i class="whitespace"></i>
							Classic</a></li>
					<li><a data-value="cerulean" href="#"><i
							class="whitespace"></i> Cerulean</a></li>
					<li><a data-value="cyborg" href="#"><i class="whitespace"></i>
							Cyborg</a></li>
					<li><a data-value="simplex" href="#"><i class="whitespace"></i>
							Simplex</a></li>
					<li><a data-value="darkly" href="#"><i class="whitespace"></i>
							Darkly</a></li>
					<li><a data-value="lumen" href="#"><i class="whitespace"></i>
							Lumen</a></li>
					<li><a data-value="slate" href="#"><i class="whitespace"></i>
							Slate</a></li>
					<li><a data-value="spacelab" href="#"><i
							class="whitespace"></i> Spacelab</a></li>
					<li><a data-value="united" href="#"><i class="whitespace"></i>
							United</a></li>
				</ul>
			</div>
			<!-- theme selector ends -->



		</div>
	</div>
	<!-- topbar ends -->
	<div class="ch-container">
		<div class="row">

			<!-- left menu starts -->
			<div class="col-sm-2 col-lg-2">
				<div class="sidebar-nav">
					<div class="nav-canvas">
						<div class="nav-sm nav nav-stacked"></div>
						<ul class="nav nav-pills nav-stacked main-menu">
							<li class="nav-header">导航菜单</li>
							<li><a class="ajax-link" href="loginAction"><i
									class="glyphicon glyphicon-home"></i><span>基本信息</span></a></li>
							<li><a class="ajax-link" href="clientAction"><i
									class="glyphicon glyphicon-eye-open"></i><span>Client客户端</span></a>
							</li>
							<li><a class="ajax-link" href="dtuAction"><i
									class="glyphicon glyphicon-list-alt"></i><span>DTU客户端</span></a></li>
							<li><a class="ajax-link" href="rateAction"><i
									class=" glyphicon glyphicon-random"></i><span>流量监控</span></a></li>	
							<li><a class="ajax-link" href="rateAnalysisAction"><i
									class=" glyphicon glyphicon-random"></i><span>流量透视</span></a></li>	
							<li><a class="ajax-link" href="logAction"><i
									class="glyphicon glyphicon-font"></i><span>日志</span></a></li>
					</div>
				</div>
			</div>
			<!--/span-->
			<!-- left menu ends -->

			<noscript>
				<div class="alert alert-block col-md-12">
					<h4 class="alert-heading">Warning!</h4>

					<p>
						You need to have <a href="http://en.wikipedia.org/wiki/JavaScript"
							target="_blank">JavaScript</a> enabled to use this site.
					</p>
				</div>
			</noscript>

			<div id="content" class="col-lg-10 col-sm-10">
				<!-- content starts -->
				<div></div>

				<!--第一行-->
				<div class="row">
					<!--服务器基本信息-->
					<div class="box box-primary direct-chat direct-chat-primary col-md-8 ">
                       <div class="box-header with-border">
                          <h3 class="box-title">基本信息</h3>

                             <div class="box-tools pull-right">
                                    <span data-toggle="tooltip" title="" class="badge bg-light-blue" data-original-title="3 New Messages">3</span>
                                    <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                                    </button>
                                    <button type="button" class="btn btn-box-tool" data-toggle="tooltip" title="" data-widget="chat-pane-toggle" data-original-title="Contacts">
                                      <i class="fa fa-comments"></i></button>
                                    <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                                  </div>
                                </div>
                                <!-- /.box-header -->
                                <div class="box-body">

                                </div>
                                <!-- /.box-body -->

                              </div>










					<div class="box col-md-8">
						<div class="box-inner">
							<div class="box-header well">
								<h2>
									<i class="glyphicon glyphicon-info-sign"></i> 基本信息
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a> <a href="#"
										class="btn btn-close btn-round btn-default"><i
										class="glyphicon glyphicon-remove"></i></a>
								</div>
							</div>
							<!--基本信息 内容设置-->

							<div class="box-content row">
								<div class="col-lg-7 col-md-12">
									<h1>DTU数据中心</h1>
									<%-- <%RemoteServerBean bean=(RemoteServerBean)session.getAttribute("remoteServerBean"); %> --%>
									<strong>主机名：</strong> <label>${applicationScope.remoteServerBean.hostIP}</label><br>
									<strong>端口号：</strong> <label>${applicationScope.remoteServerBean.port}</label><br>
									<strong>内核版本：</strong> <label>DTUCENTER 3.0</label><br> <strong>服务器版本：</strong>
									<label>CENTOS 6.5</label><br> <strong>最大连接数：</strong> <label>${applicationScope.remoteServerBean.maxConnections}</label><br>
									<strong>DTU最大连接数：</strong><label>${applicationScope.remoteServerBean.maxDtuConnections}</label><br>
									<strong>Client最大连接数：</strong> <label>${applicationScope.remoteServerBean.maxClientConnections}</label><br>
									<strong>接收队列深度：</strong> <label>${applicationScope.remoteServerBean.receiveQueueLength}</label><br>
									<strong>发送队列深度：</strong> <label>${applicationScope.remoteServerBean.sendQueueLength}</label><br>


								</div>


							</div>
						</div>
					</div>
					<!--服务器状态-->
					<div class="box col-md-4">
						<div class="box-inner">
							<div class="box-header well" data-original-title="">
								<h2>
									<i class="glyphicon glyphicon-list"></i> 状态
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a> <a href="#"
										class="btn btn-close btn-round btn-default"><i
										class="glyphicon glyphicon-remove"></i></a>
								</div>
							</div>
							
							<!--设置内容-->
							<div class="box-content buttons">
								<%
									int status = ((RemoteServerBean) application
											.getAttribute("remoteServerBean")).getStatus();

									out.print("<input id='stautsHidden' type='hidden' text='" + status
											+ "'>");
									
								%>
								<div>
									<%
										if (status == 0)
										{
											out.print("<image id='status_Img' src='img/status/stop.png' />");
											//在此处插入隐藏域 用于记录服务器启动时间 
											out.print("<input id='timeStart' type='hidden' text=''/>");
										}
										else
										{
											out.print("<image id='status_Img' src='img/status/run.png' />");
											Date start=((RemoteServerBean) application
													.getAttribute("remoteServerBean")).getStartTimeDate();
											
											//转换到合适的格式
											SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
													"yyyy/MM/dd HH:mm:ss");
											String timeString = simpleDateFormat.format(start);
											
											out.print("<input id='timeStart' type='hidden' text='"+timeString+"'/>");
											
											
										}
									%>
								</div>
								<div>
									<ul>
										<li><strong>服务器状态</strong> <%
 	if (status == 0) {
 		//服务器停止状态
 		out.print("<span id='status_Text' class='label-default label label-danger'>停止</span></li>");

 	} else {
 		out.print("<span id='status_Text' class='label-success label label-default'>正在运行</span></li>");
 	}
 %>
										<li><strong>服务器运行时间</strong><label id="time_Day">0</label>天
											<label id="time_Hour">0</label>小时 <label id="time_Minute">0</label>分钟
											<label id="time_Second">0</label>秒</li>
										<ul>
								</div>

								<button class="btn btn-primary btn-lg" onclick="startOnclick();">启动服务器</button>
								<button class="btn btn-primary btn-lg" onclick="stopOnclick();">停止服务器</button>
							</div>
						</div>
					</div>

				</div>
				<!--第二行-->
				<div class="row">
					<div class="box col-md-4">
						<div class="box-inner">
							<div class="box-header well" data-original-title="">
								<h2>
									<i class="glyphicon glyphicon-list"></i> 传输速率
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a> <a href="#"
										class="btn btn-close btn-round btn-default"><i
										class="glyphicon glyphicon-remove"></i></a>
								</div>
							</div>
							<div class="box-content buttons">

								<strong>接收速率</strong> <label id="rSpeed">0.0</label><label>KB/S</label><br>
								<strong>发送速率</strong> <label id="sSpeed">0.0</label><label>KB/S</label><br>
								<hr>
								<div id="realtimechart" style="height: 190px;"></div>


							</div>
						</div>
					</div>
					<!--/span-->
					<div class="box col-md-4">
						<div class="box-inner">
							<div class="box-header well" data-original-title="">
								<h2>
									<i class="glyphicon glyphicon-list"></i> DTU连接状态
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a> <a href="#"
										class="btn btn-close btn-round btn-default"><i
										class="glyphicon glyphicon-remove"></i></a>
								</div>
							</div>
							<div class="box-content buttons">
								<br> <strong>DTU连接数量</strong> <label id="dtuCount">
									28</label><br>

								<hr>
								<div id="realtimechartDTU" style="height: 190px;"></div>
							</div>
						</div>
					</div>

					<!--/span-->
					<div class="box col-md-4">
						<div class="box-inner">
							<div class="box-header well" data-original-title="">
								<h2>
									<i class="glyphicon glyphicon-list"></i> Client连接状态
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a> <a href="#"
										class="btn btn-close btn-round btn-default"><i
										class="glyphicon glyphicon-remove"></i></a>
								</div>
							</div>
							<div class="box-content buttons">
								<br> <strong>Client连接数量</strong> <label id="clientCount">
									28</label><br>

								<hr>
								<div id="realtimechartClient" style="height: 190px;"></div>
							</div>
						</div>
					</div>

					<!--/span-->
				</div>
				<!--/row-->

				<!--第三行-->
				<div class="row">

					<div class="box col-md-12">
						<div class="box-inner">
							<div class="box-header well" data-original-title="">
								<h2>
									<i class="glyphicon glyphicon-list"></i>日志
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a> <a href="#"
										class="btn btn-close btn-round btn-default"><i
										class="glyphicon glyphicon-remove"></i></a>
								</div>
							</div>

							<!--设置内容-->
							<div class="box-content buttons">
								<table id="logLastTenTable"
									class="table table-bordered table-striped table-condensed">
									<thead>
										<tr>
											<th>时间</th>
											<th>等级</th>
											<th>内容</th>
										</tr>
									</thead>
									<tbody id="logLastTenTable_body">

									</tbody>
								</table>
							</div>
						</div>
					</div>


				</div>




				<!-- content ends -->
			</div>
			<!--/#content.col-md-0-->
		</div>
		<!--/fluid-row-->


		<hr>

		<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">

			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">×</button>
						<h3>Settings</h3>
					</div>
					<div class="modal-body">
						<p>Here settings can be configured...</p>
					</div>
					<div class="modal-footer">
						<a href="#" class="btn btn-default" data-dismiss="modal">Close</a>
						<a href="#" class="btn btn-primary" data-dismiss="modal">Save
							changes</a>
					</div>
				</div>
			</div>
		</div>

		<footer class="row">
		<p class="col-md-9 col-sm-9 col-xs-12 copyright">
			&copy; <a href="loginAction" >DTU数据中心</a> 2014 - 2015
		</p>

		<p class="col-md-3 col-sm-3 col-xs-12 powered-by">
			Powered by: <a href="http://blog.csdn.net/u010723709"  target="_blank">宋旭</a>
		</p>
		</footer>

	</div>
	<!--/.fluid-container-->

	<!-- chart libraries start -->
	<script src="bower_components/flot/excanvas.min.js"></script>
	<script src="bower_components/flot/jquery.flot.js"></script>
	<script src="bower_components/flot/jquery.flot.pie.js"></script>
	<script src="bower_components/flot/jquery.flot.stack.js"></script>
	<script src="bower_components/flot/jquery.flot.resize.js"></script>




	<!-- webSocket逻辑 -->
	<script src="js/websocket/websocketInit.js"></script>
	<!-- chart libraries end -->
	<script src="js/init-chart.js"></script>

	<!-- 开启计算时间差线程 -->
	<script type="text/javascript">
		setInterval("displayRunTime()", 1000);
	</script>




</body>
</html>
