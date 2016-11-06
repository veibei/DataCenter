/**
 * 
 */

function requestData()
{
	$.ajax(
	{
		url : 'rataAnalysisAction',// 跳转到 action
		type : 'post',
		cache : false,
		contentType : "application/x-www-form-urlencoded; charset=gbk",
		dataType : 'json',
		success : function(data)
		{
			if (data.RAData == 'success')
			{
				var clientMap = data.clientMap;
				var DTUMap = data.DTUMap;
				var maxValue=data.maxValue;//最大值
				if(maxValue==0)
				{
					$('#msgbox').attr("data-noty-options",'{"text":"暂无数据","layout":"top","type":"error"}');
					$('#msgbox').click();
				}
				initCharts("客户端上行流量比例图","clientUp",clientMap,maxValue,"up");
				initCharts("客户端下行流量比例图","clientDown",clientMap,maxValue,"down");
				initCharts("DTU上行流量比例图","DTUUp",DTUMap,maxValue,"up");
				initCharts("DTU下行流量比例图","DTUDown",DTUMap,maxValue,"down");
			} else
			{
				alert("后台数据获取出错！");
			}

		},
		error : function()
		{
			alert("后台数据获取出错！");
		}

	});

}

/**
 * 获取图表图例
 * 
 * @param map
 * @returns {Array}
 */
function getlegend(map)
{
	var list =
	[];
	for ( var sub in map)
	{
		list.push(sub);
	}
	return [];

}

/**
 * 获取上行流量比例
 * 
 * @param map
 */
function warpUpData(map)
{
	var series =
	[];
	for ( var subName in map)
	{
		subData=map[subName];
		var dataDisplay=subData.split("|");
		if(!dataDisplay)
		{
			//可能得到undifine
			continue;
		}
		
		
		series.push(
		{
			name : subName+'上行流量数据',
			type : 'radar',
			symbol : 'none',
			itemStyle :
			{
				normal :
				{
					lineStyle :
					{
						width : 1
					}
				},
				emphasis :
				{
					areaStyle :
					{
						color : 'rgba(0,250,0,0.3)'
					}
				}
			},
			data :
			[
			{
				value :
				[ 
				  dataDisplay[0], 
				  dataDisplay[2], 
				  dataDisplay[4], 
				  dataDisplay[6],
				  dataDisplay[8]
				],
				name :subName
			} ]
		})
	}
	return series;

}
/**
 * 获取下行流量比例
 * 
 * @param map
 */
function warpDownData(map)
{
	var series =
		[];
		for ( var subName in map)
		{
			subData=map[subName];
			var dataDisplay=subData.split("|");
			if(!dataDisplay)
			{
				//可能得到undifine
				continue;
			}
			
			
			series.push(
			{
				name : subName+'上行流量数据',
				type : 'radar',
				symbol : 'none',
				itemStyle :
				{
					normal :
					{
						lineStyle :
						{
							width : 1
						}
					},
					emphasis :
					{
						areaStyle :
						{
							color : 'rgba(0,250,0,0.3)'
						}
					}
				},
				data :
				[
				{
					value :
					[ 
					  dataDisplay[1], 
					  dataDisplay[3], 
					  dataDisplay[5], 
					  dataDisplay[7],
					  dataDisplay[9]
					],
					name :subName
				} ]
			})
		}
		return series;
}