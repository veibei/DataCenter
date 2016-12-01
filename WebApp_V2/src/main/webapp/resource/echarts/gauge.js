/**
 * 仪表盘
 */
function loadOptionGaugeUp()
{
	var option =
	{
		tooltip :
		{
			formatter : "{b} : {c}kb/s"
		},
		toolbox :
		{
			show : false,

		},
		series :
		[
		{
			name : '上行速率',
			type : 'gauge',
			detail :
			{
				formatter : '{value}kb/s'
			},
			
			data :
			[
			{
				value : 50,
				name : '上行速度'
			} ]
		} ]
	};
	return option;

}

function loadOptionGaugeDown()
{
	var option =
	{
		tooltip :
		{
			formatter : "{b} : {c}kb/s"
		},
		toolbox :
		{
			show : false,

		},
		series :
		[
		{
			name : '下行速率',
			type : 'gauge',
			detail :
			{
				formatter : '{value}kb/s'
			},
			data :
			[
			{
				value : 50,
				name : '下行速度'
			} ]
		} ]
	};
	return option;

}