//下拉框事件
function group_select(){
      var selectVal=$('#monitor_group').val();
      var company=selectVal.split('-')[0];
      var count=selectVal.split('-')[1];
      initMonitor(company,count);
}
//构造监测点HTML内容
function getDiv(company,name){
       var htmlDiv='<div class="col-xs-4">'+
       '<div class="box box-primary">'+
           ' <div class="box-header with-border">'+
           '  <i class="fa fa-area-chart"></i>'+
            '  <h3 class="box-title">'+company+'监测点'+name+'号</h3>'+
           ' </div>'+
            '<div class="box-body">'+
            '  <div id="interactive_'+name+'" style="height: 250px;"></div>'+
            '</div>'+
          '</div>'+
        '</div>'
        return htmlDiv;
}


function initMonitor(company,count){
    var company_name=company;
    var monitor_count=count;//监测点数量
    var plot_index=[];//监测点序号
    var domData='';//html代码
    var plot_arr=[];//监测图集合
    var data_all=[];//每个图的监测点
    for(var i=1;i<=monitor_count;i++){
      domData+=getDiv(company_name,i);
      plot_index.push(i);
    }
    $("#monitorData").html(domData);

    
    totalPoints = 10;//显示最近10次的监测数值
    function getRandomData(i,salt) {
      var data=data_all[i];
      if(!data)
        data=[];
      if (data.length > 0)
        data = data.slice(1);
      while (data.length < totalPoints) {
  
        var y = 300+Math.random() * 10*salt+Math.random()*50;
        if (y < 0) {
          y = 100;
        }
        data.push(y);
      }
      data_all[i]=data;
      var res = [];
      for (var i = 0; i < data.length; ++i) {
        res.push([i, data[i]]);
      }
      return res;
    }
    //构造监测图
    for(var i=0;i<monitor_count;i++){
      var interactive_plot = $.plot("#interactive_"+plot_index[i], [getRandomData(i,1)], {
      grid: {
        borderColor: "#f3f3f3",
        borderWidth: 1,
        tickColor: "#f3f3f3"
      },
      series: {
        shadowSize: 0, // Drawing is faster without shadows
        color: "#3c8dbc"
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
      xaxis: {
        show: true
      }
      });
      plot_arr.push(interactive_plot);

     
    }
      var updateInterval = 500; //Fetch data ever x milliseconds
      function update() {

        for(var i=0;i<plot_arr.length;i++){
            interactive_plot=plot_arr[i];
            interactive_plot.setData([getRandomData(i,i)]);
            interactive_plot.draw();
          }
        setTimeout(update, updateInterval);
      }
      
      update();
}

