//flot chart
if ($("#flotchart").length) {
    var d1 = [];
    for (var i = 0; i < Math.PI * 2; i += 0.25)
        d1.push([i, Math.sin(i)]);

    var d2 = [];
    for (var i = 0; i < Math.PI * 2; i += 0.25)
        d2.push([i, Math.cos(i)]);

    var d3 = [];
    for (var i = 0; i < Math.PI * 2; i += 0.1)
        d3.push([i, Math.tan(i)]);

    $.plot($("#flotchart"), [
        {label: "sin(x)", data: d1},
        {label: "cos(x)", data: d2},
        {label: "tan(x)", data: d3}
    ], {
        series: {
            lines: {show: true},
            points: {show: true}
        },
        xaxis: {
            ticks: [0, [Math.PI / 2, "\u03c0/2"], [Math.PI, "\u03c0"], [Math.PI * 3 / 2, "3\u03c0/2"], [Math.PI * 2, "2\u03c0"]]
        },
        yaxis: {
            ticks: 10,
            min: -2,
            max: 2
        },
        grid: {
            backgroundColor: {colors: ["#fff", "#eee"]}
        }
    });
}

// we use an inline data source in the example, usually data would
// be fetched from a server
var data = [], totalPoints = 300;

function getRandomData() {
    if (data.length > 0)
        data = data.slice(1);

    // do a random walk
    while (data.length < totalPoints) {
        var prev = data.length > 0 ? data[data.length - 1] : 50;
        var y = prev + Math.random() * 10 - 5;
        if (y < 0)
            y = 0;
        if (y > 100)
            y = 100;
        data.push(y);
    }

    // zip the generated y values with the x values
    var res = [];
    for (var i = 0; i < data.length; ++i)
        res.push([i, data[i]])
    return res;
}

// setup control widget
var updateInterval = 500;
$("#updateInterval").val(updateInterval).change(function () {
    var v = $(this).val();
    if (v && !isNaN(+v)) {
        updateInterval = +v;
        if (updateInterval < 1)
            updateInterval = 1;
        if (updateInterval > 2000)
            updateInterval = 2000;
        $(this).val("" + updateInterval);
    }
});

//速率
if ($("#realtimechart").length) {
    var options = {

        series: {lines: {show: 1}, lines: {show: 1}},
        yaxis: {min: 0, max: 30},
        xaxis: {min: 0, max: 9, show: false},
        colors: ["red", "green"]
    };
    var plot = $.plot($("#realtimechart"), [getSendSpeedData(), getReceiveSpeedData()], options);

    function update() {
        plot.setData([getSendSpeedData(), getReceiveSpeedData()]);
        plot.draw();
        setTimeout(update, updateInterval);
    }

    update();
}

if ($("#realtimechartMem").length) {
    var optionsMem = {
        grid: {
            borderColor: "#f3f3f3",
            borderWidth: 1,
            tickColor: "#f3f3f3"
        },
        lines: {
            fill: true,//转换为区域填充
            color: "#3c8dbc"
        },
        series: {shadowSize: 1},
        yaxis: {min: 0, max: 100},
        xaxis: {min: 0, max: 9, show: false},
        colors: ["#3C67A5"]
    };
    var plotMem = $.plot($("#realtimechartMem"), [getMemData()], optionsMem);

    function updateMem() {
        plotMem.setData([getMemData()]);
        plotMem.draw();
        setTimeout(updateMem, updateInterval);
    }
    updateMem();//定时更新
}

if ($("#realtimechartCPU").length) {
    var optionsCPU = {
        grid: {
            borderColor: "#f3f3f3",
            borderWidth: 1,
            tickColor: "#f3f3f3"
        },
        series: {shadowSize: 1},
        lines: {
            fill: true,
            color: "#3c8dbc"
        },
        yaxis: {min: 0, max: 100},
        xaxis: {min: 0, max: 9, show: false},
        colors: ["#539F2E"]
    };
    var plotCPU = $.plot($("#realtimechartCPU"), [getCPUData()], optionsCPU);

    function updateCpuData() {
        plotCPU.setData([getCPUData()]);
        plotCPU.draw();
        setTimeout(updateCpuData, updateInterval);
    }

    updateCpuData();//定时更新
}
