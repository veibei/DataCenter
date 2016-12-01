/**
 * 
 * @param title 标题
 * @param divId divid
 * @param map   K-V map
 * @param maxValue 最大值
 * @param type  类型
 */
function initCharts(title,divId,map,maxValue,type)
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
	[ 'echarts', 'echarts/chart/radar' // 使用柱状图就加载bar模块，按需加载
	], function(ec)
	{
		// 基于准备好的dom，初始化echarts图表
		var myChart = ec.init(document.getElementById(divId));
		var serie;
		if(type=="down")
		{
			serie=warpDownData(map);
		}
		else
		{
			serie=warpUpData(map);
		}
		var option = loadOption(title,map,maxValue,serie);
		// 为echarts对象加载数据
		myChart.setOption(option);
	});

}

function loadOption(texttitle,map,maxValue,serie)
{
	var option = {
		    color : (function (){
		        var zrColor = require('zrender/tool/color');
		        return zrColor.getStepColors('yellow', 'red', 5);
		    })(),
		    title : {
		        text: texttitle,
		        x:'left',
		        y:'bottom'
		    },
		    tooltip : {
		        trigger: 'item',
		        backgroundColor : 'rgba(0,0,250,0.2)'
		    },
		    legend: {
		       // orient : 'vertical',
		        //x : 'center',
		        data: getlegend(map)
		    },
		    toolbox: {
		        show : true,
		        orient : 'vertical',
		        y:'center',
		        feature : {
		            dataView : {show: true, readOnly: true},
		            saveAsImage : {show: true}
		        }
		    },
		   polar : [
		       {
		           indicator : [
		               { text: '30秒', max: maxValue},
		               { text: '1分钟', max: maxValue},
		               { text: '3分钟', max: maxValue},
		               { text: '5分钟', max: maxValue},
		               { text: '10分钟', max: maxValue}
		            ],
		            center : ['50%', 240],
		            radius : 150
		        }
		    ],
		    calculable : false,
		    series : serie
		};

		return option;                    
	
	

}