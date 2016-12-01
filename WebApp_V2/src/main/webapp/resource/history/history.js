//下拉框事件
function group_select(){
      var selectVal=$('#monitor_group').val();
      var company=selectVal.split('-')[0];
      var count=selectVal.split('-')[1];

      var selectPos=$("#monitorpos");
      selectPos.html("");
      for(var i=1;i<=count;i++){
        selectPos.append('<option>'+company+'监测点'+i+'号</option>');
      }
}

//初始化历史数据表格
function init_history(salt,picker){
    var interactive_plot = $.plot("#interactive", [getRandomData(salt,picker)], {
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
        min: 0,
        max: 800,
        show: true
      },
      xaxis: {
        mode: 'time', 
        timeformat: "%m-%d %H:%M",
        show: true
      }
    });
    interactive_plot.setData([getRandomData(salt,picker)]);
    interactive_plot.draw();
}
  //生成日期表
  function generateTime(picker){
    if(!picker){return []}
    var start=new Date(picker.startDate);
    var end=new Date(picker.endDate);
    var span=end.getTime()-start.getTime();
    var hours=span/(3600*1000);
    var result_date=[]; 
    for(var i=0;i<hours;i++){
       var sub_date=new Date(start.setHours(start.getHours()+1));//每小时生成一个采样点
       result_date.push(sub_date);
    }
    return result_date;
  }

  function getRandomData(salt,picker) {
      var data = []; 
      if(!picker){
        data.push([0,0]);
        return data;
      }

      var datearr=generateTime(picker);
      while (data.length < datearr.length) {

       
        var y = 300+Math.random() * 10*salt+Math.random()*50;
        data.push(y);
      }

      var res = [];
      for (var i = 0; i < data.length; ++i) {
        res.push([datearr[i], data[i]]);
      }

      return res;
}    
