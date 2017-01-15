//下拉框事件
function group_select() {
    if (window.alarm) {//如果已经展示了一次报警，则不需要再次展示了
        window.alarm = false;
    }
    var selectVal = $('#monitor_group').val();
    var company = selectVal.split('-')[0];
    var count = selectVal.split('-')[1];
    initMonitor(company, count);

}
function getheader() {

}
//构造监测点HTML内容
function getDiv(company, name) {
    var htmlDiv = '<div class="col-md-6">' +
        '<div class="box box-primary direct-chat direct-chat-primary direct-chat-contacts-close">' +
        ' <div class="box-header with-border">' +
        '  <i class="fa fa-area-chart"></i>' +
        '  <h3 class="box-title">' + company + '监测点' + name + '号</h3>' +
         ' <div class="box-tools pull-right"><button type="button" class="btn btn-box-tool" data-toggle="tooltip" title="" data-widget="chat-pane-toggle" data-original-title="监测点信息"> <i class="fa fa-comments"></i></button></div>' +
        ' </div>' +
        '<div class="box-body">' +
        '  <div class="direct-chat-messages"  id="interactive_' + name + '" style="height: 400px;"></div>' +
        '  <div class="direct-chat-contacts"> '+
        '<ul class="nav nav-stacked"><li>'+

        '<a href="#">监测点位置 <span class="badge bg-blue">大连海事大学凌水码头</span></a>'+
        '</li> <li><a href="#">当前报警阈值 <span class="badge bg-blue">725</span></a></li>'+
        '</li> <li><a href="#">当前荧光值 <span class="badge bg-red">746</span></a></li>'+
        '</li> <li><a href="#">最后通信时间<span class="badge bg-blue">2016-1-15 10:23:04</span></a></li>'+
        '<li><a href="#">负责人 <span class="badge bg-blue">张三</span></a></li>'+
        '<li><a href="#">联系电话 <span class="badge bg-blue">13900001111</span></a></li>'+
        '<li><a href="#">最近五天报警 <span class=" badge bg-red">3次</span></a></li> </ul>'+
        '</div>'+

        '</div>' +
        '</div>' +
        '</div>'
    return htmlDiv;
}


function initMonitor(company, count) {
    var company_name = company;
    var monitor_count = count;//监测点数量
    var plot_index = [];//监测点序号
    var domData = '';//html代码
    var plot_arr = [];//监测图集合
    var data_all = [];//每个图的监测点
    for (var i = 1; i <= monitor_count; i++) {
        domData += getDiv(company_name, i);
        plot_index.push(i);
    }
    $("#monitorData").html(domData);

    totalPoints = 20;//显示最近10次的监测数值
    function getRandomData(i, salt) {
        var data = data_all[i];
        if (!data)
            data = [];
        if (data.length > 0)
            data = data.slice(1);
        while (data.length < totalPoints) {
            if (window.alarm && i == 0) {
                var y = 700 + Math.random() * 10 * salt + Math.random() * 50;
            } else {
                var y = 300 + Math.random() * 10 * salt + Math.random() * 50;

            }
            if (y < 0) {
                y = 100;
            }
            data.push(y);
        }
        data_all[i] = data;
        var res = [];
        for (var i = 0; i < data.length; ++i) {
            res.push([i, data[i]]);
        }
        return res;
    }

    /**
     * 生成随机阈值
     * @param i
     * @returns {Array}
     */
    function getlimit(salt) {
        var res = [];
        var limit=650+ 30 * salt ;
        for (var i = 0; i < totalPoints; ++i) {
            res.push([i, limit]);
        }
        return res;
    }
    //构造监测图
    for (var i = 0; i < monitor_count; i++) {
        var interactive_plot = $.plot("#interactive_" + plot_index[i], [
            {
                data: getRandomData(i, 1),
                label:'溢油监测荧光值',
                lines: { show: true,fill:true,color: "#3c8dbc"}
            },{
                data: getlimit(i),
                label:'报警阈值',
                lines: { show: true,color:'#ff0000'}
            }
        ], {
            grid: {
                borderColor: "#f3f3f3",
                borderWidth: 1,
                tickColor: "#f3f3f3"
            },
            series: {
                shadowSize: 0, // Drawing is faster without shadows
            },
           /* lines: {
                fill: true, //Converts the line chart to area chart
                color: "#3c8dbc"
            },*/
            yaxis: {
                min: 00,
                max: 800,
                show: true
            },
            xaxis:{show:false}

        });
        plot_arr.push(interactive_plot);


    }
    var updateInterval = 700; //Fetch data ever x milliseconds
    var alert_plot = $.plot("#interactive_1", [{
        data: getRandomData(0, 1),
        label:'溢油监测荧光值',
        lines: { show: true,fill:true,color: "#3c8dbc"}
    },{
        data: getlimit(i),
        label:'报警阈值',
        lines: { show: true,color:'#ff0000'}
    }], {
        grid: {
            borderColor: "#f3f3f3",
            borderWidth: 1,
            tickColor: "#f3f3f3"
        },
        series: {
            shadowSize: 0, // Drawing is faster without shadows
        },
        lines: {
            fill: true, //Converts the line chart to area chart
            color: "#3c8dbc"
        },
        yaxis: {
            min: 00,
            max: 800,
            show: true
        },
        xaxis:{show:false}
    });

    function update() {

        for (var i = 0; i < plot_arr.length; i++) {
            if (window.alarm && i == 0) {
                interactive_plot = alert_plot;
                interactive_plot.setData([
                        {
                            data: getRandomData(i, 1),
                            label:'溢油监测荧光值',
                            lines: { show: true,fill:true},
                            color:'red'
                        },{
                            data: getlimit(i),
                            label:'报警阈值',
                            lines: { show: true,} ,
                            color:'red',
                        }
                    ]
                );
            } else {
                interactive_plot = plot_arr[i];
                interactive_plot.setData([
                        {
                            data: getRandomData(i, 1),
                            label:'溢油监测荧光值',
                            lines: { show: true,fill:true},
                        },{
                            data: getlimit(i),
                            label:'报警阈值',
                            lines: { show: true,},


                        }
                    ]
                );
            }
            //interactive_plot = plot_arr[i];

            interactive_plot.draw();
        }
        setTimeout(update, updateInterval);
    }

    update();
}

