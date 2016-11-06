<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.songxu.bean.User"%>
<%
	//免登录或登录过期
	User user =(User) session.getAttribute("userinfo");
	if(null==user||user.getLogtimeTimestamp()<0L)
	{
		response.sendRedirect("login.jsp?datatype=1");
	}


%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>客户端连接情况</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description"
	content="DTU数据中心">
<meta name="author" content="songxu9185@163.com">

<!-- The styles -->
<link id="bs-css" href="css/bootstrap-cerulean.min.css" rel="stylesheet">

<link href="css/charisma-app.css" rel="stylesheet">
<link href='bower_components/fullcalendar/dist/fullcalendar.css'
	rel='stylesheet'>
<link href='bower_components/fullcalendar/dist/fullcalendar.print.css'
	rel='stylesheet' media='print'>
<link href='bower_components/chosen/chosen.min.css' rel='stylesheet'>
<link href='bower_components/colorbox/example3/colorbox.css'
	rel='stylesheet'>
<link href='bower_components/responsive-tables/responsive-tables.css'
	rel='stylesheet'>
<link
	href='bower_components/bootstrap-tour/build/css/bootstrap-tour.min.css'
	rel='stylesheet'>
<link href='css/jquery.noty.css' rel='stylesheet'>
<link href='css/noty_theme_default.css' rel='stylesheet'>
<link href='css/elfinder.min.css' rel='stylesheet'>
<link href='css/elfinder.theme.css' rel='stylesheet'>
<link href='css/jquery.iphone.toggle.css' rel='stylesheet'>
<link href='css/uploadify.css' rel='stylesheet'>
<link href='css/animate.min.css' rel='stylesheet'>

<!-- jQuery -->
<script src="bower_components/jquery/jquery.min.js"></script>

<!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

<!-- The fav icon -->
<link rel="shortcut icon" href="img/favicon.ico">
<!-- userJs -->
<script type="text/javascript" src="js/clientPageFunction/clientPage.js"></script>
<script src="js/indexPageFunction/userControl.js"></script>

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
						</ul>
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




				<!--日志内容-->
				<div class="row">

					<div class="box col-md-12">
						<div class="box-inner">
							<div class="box-header well" data-original-title="">
								<h2>
									<i class="glyphicon glyphicon-list"></i>客户端连接情况
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a> <a href="#"
										class="btn btn-close btn-round btn-default"><i
										class="glyphicon glyphicon-remove"></i></a>
								</div>
							</div>

							<!--设置内容-->
							<div class="box-content buttons" style="min-height: 550px ;height:auto">
								<div class="row">
									<div class="col-md-6">
										<div id="DataTables_Table_0_length" class="dataTables_length">
											<label> 每页&nbsp<select id="perPageCountSelect" name="DataTables_Table_0_length"
												size="1" aria-controls="DataTables_Table_0"  onchange="changePerPage();">
											
												
													<option value="10"  <s:if test="#session.perPageCountClient==10">selected="selected"</s:if> >10</option>
													<option value="25" <s:if test="#session.perPageCountClient==25">selected="selected"</s:if>>25</option>
													<option value="50" <s:if test="#session.perPageCountClient==50">selected="selected"</s:if>>50</option>
													<option value="100" <s:if test="#session.perPageCountClient==100">selected="selected"</s:if>>100</option>
											</select> 条记录
											</label>
										</div>
									</div>
									<div class="col-md-6">
										<div class="dataTables_filter" id="DataTables_Table_0_filter">
											<label>Search: <input type="text"
												aria-controls="DataTables_Table_0"></label>
										</div>
									</div>
								</div>

								<table
									class="table table-bordered table-striped table-condensed">
									<thead>
										<tr>
											<th>IMEI</th>
											<th>IP</th>
											<th>上行流量</th>
											<th>下行流量</th>
											<th>状态</th>
										</tr>
									</thead>
									<tbody>

										 <s:iterator value="#session.pageClient" id="Client" status="st">
											<tr>
												<td class="center"><s:property value="#Client.IMEI" /></td>
												<td class="center"><s:property value="#Client.IP" /></td>
												<td class="center"><s:property value="#Client.down" /></td>
												<td class="center"><s:property value="#Client.up" /></td>
												<td class="center"><span
													class="label-success label label-default"><s:property
															value="#Client.status" /></span></td>
											</tr>



										</s:iterator> 



									</tbody>
								</table>

								<s:property value="#session.rowsCountClient" />
								条记录&nbsp 共
								<s:property value="#session.pageCountClient" />
								页
								<ul class="pagination pagination-centered">
									<!-- 显示前一页页按钮 -->
									<s:if test="#session.currentPageClient>1">
										<li><a href="clientSubPage?pageIndexClient=<s:property	value="#session.currentPageClient-1" />">前一页</a></li>
									</s:if>
									<s:iterator value="#session.listBtnClient" id="value" status="st">
										<s:if test="#value==#session.currentPageClient">
											<li class="active"> <a href="clientSubPage?pageIndexClient=<s:property	value="#value" />"><s:property	value="#value" /></a></li>	
										</s:if>
											
										<s:else>
											<li><a href="clientSubPage?pageIndexClient=<s:property	value="#value" />"><s:property value="#value" /></a></li>
										</s:else>


									</s:iterator>


									<!-- 显示后一页按钮 -->
									<s:if test="#session.currentPageClient<#session.pageCountClient">
										<li><a href="clientSubPage?pageIndexClient=<s:property	value="#session.currentPageClient+1" />">后一页</a></li>
									</s:if>


								</ul>
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
				&copy; <a href="loginAction">DTU数据中心</a> 2014 - 2015
			</p>

			<p class="col-md-3 col-sm-3 col-xs-12 powered-by">
				Powered by: <a href="http://blog.csdn.net/u010723709">宋旭</a>
			</p>
		</footer>

	</div>
	<!--/.fluid-container-->

	<!-- external javascript -->
	<!-- chart libraries start -->
	<script src="bower_components/flot/excanvas.min.js"></script>
	<script src="bower_components/flot/jquery.flot.js"></script>
	<script src="bower_components/flot/jquery.flot.pie.js"></script>
	<script src="bower_components/flot/jquery.flot.stack.js"></script>
	<script src="bower_components/flot/jquery.flot.resize.js"></script>
	<!-- chart libraries end -->
	<script src="js/init-chart.js"></script>



	<script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

	<!-- library for cookie management -->
	<script src="js/jquery.cookie.js"></script>
	<!-- calender plugin -->
	<script src='bower_components/moment/min/moment.min.js'></script>
	<script src='bower_components/fullcalendar/dist/fullcalendar.min.js'></script>
	<!-- data table plugin -->
	<script src='js/jquery.dataTables.min.js'></script>

	<!-- select or dropdown enhancer -->
	<script src="bower_components/chosen/chosen.jquery.min.js"></script>
	<!-- plugin for gallery image view -->
	<script src="bower_components/colorbox/jquery.colorbox-min.js"></script>
	<!-- notification plugin -->
	<script src="js/jquery.noty.js"></script>
	<!-- library for making tables responsive -->
	<script src="bower_components/responsive-tables/responsive-tables.js"></script>
	<!-- tour plugin -->
	<script
		src="bower_components/bootstrap-tour/build/js/bootstrap-tour.min.js"></script>
	<!-- star rating plugin -->
	<script src="js/jquery.raty.min.js"></script>
	<!-- for iOS style toggle switch -->
	<script src="js/jquery.iphone.toggle.js"></script>
	<!-- autogrowing textarea plugin -->
	<script src="js/jquery.autogrow-textarea.js"></script>
	<!-- multiple file upload plugin -->
	<script src="js/jquery.uploadify-3.1.min.js"></script>
	<!-- history.js for cross-browser state change on ajax -->
	<script src="js/jquery.history.js"></script>
	<!-- application script for Charisma demo -->
	<script src="js/charisma.js"></script>


</body>
</html>
