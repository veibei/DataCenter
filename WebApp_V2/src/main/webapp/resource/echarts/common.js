/**
 * 公共加载init方法
 */
function initCharts()
{
	// 路径配置
	require.config(
	{
		paths :
		{
			echarts : 'js/echarts'
		}
	});

	// 使用
	require(
	[ 'echarts', 'echarts/chart/map' // 使用柱状图就加载bar模块，按需加载
	], function(ec)
	{
		// 基于准备好的dom，初始化echarts图表
		var myChart = ec.init(document.getElementById('main'));
		var option = loadOption();
		// 为echarts对象加载数据
		myChart.setOption(option);
	});

}
function initGaugeUp()
{
	// 路径配置
	require.config(
	{
		paths :
		{
			echarts : 'js/echarts'
		}
	});
	
	// 使用
	require(
	[ 'echarts', 'echarts/chart/gauge' // 使用柱状图就加载bar模块，按需加载
	], function(ec)
	{
		// 基于准备好的dom，初始化echarts图表
		var myChart = ec.init(document.getElementById('mainGaugeUp'));
		window.optionG = loadOptionGaugeUp();
		var timeTicket;
		clearInterval(timeTicket);
		timeTicket = setInterval(function (optionG){
			window.optionG.series[0].data[0].value = (Math.random()*100).toFixed(2) - 0;
		    myChart.setOption(window.optionG, true);
		},2000);
		
	});

}
function initGaugeDown()
{
	// 路径配置
	require.config(
	{
		paths :
		{
			echarts : 'js/echarts'
		}
	});
	
	// 使用
	require(
	[ 'echarts', 'echarts/chart/gauge', // 使用柱状图就加载bar模块，按需加载
	  'echarts/theme/macarons' 
	], function(ec,theme)
	{
		// 基于准备好的dom，初始化echarts图表
		var myChart = ec.init(document.getElementById('mainGaugeDown'),theme);
		window.optionDown = loadOptionGaugeDown();
		var timeTicket;
		clearInterval(timeTicket);
		timeTicket = setInterval(function (optionDown){
			window.optionDown.series[0].data[0].value = (Math.random()*100).toFixed(2) - 0;
		    myChart.setOption(window.optionDown, true);
		},2000);
		
	});

}