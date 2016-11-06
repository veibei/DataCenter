/**
 * Created by songxu on 2016/5/8.
 */
'use strict'
angular.module("dtucenter").controller('mainController', ['$scope', '$rootScope', '$http', 'indexService', function ($scope, $rootScope, $http, indexService) {
    //更新url 为左侧导航栏添加标识
    $rootScope.url = "main";
    $rootScope.userToken = getCookie("token");
    /**
     * 服务器控制
     */
    $scope.operate = function () {
        var run = ($("#status_bg").attr("class") == "info-box bg-green");
        if (run) {
            stopServer();
        } else {
            startServer();
        }
    }
    var startServer = function () {
        indexService.startServer({
            token: $rootScope.userToken,
            succCallback: function (data) {
                if (data == 'true') {
                    alert("数据中心服务启动中...");
                }
                else if (data == 'false') {
                    alert("数据中心服务启动失败...");
                }
                else if (data == 'close') {
                    alert("数据中心与远程主机连接失败");
                }
            }
        });
    }
    var stopServer = function () {
        indexService.stopServer({
            token: $rootScope.userToken,
            succCallback: function (data) {
                if (data == 'true') {
                    alert("数据中心服务关闭中...");
                } else if (data == 'false') {
                    alert("数据中心服务失败...");
                }
                else if (data == 'close') {
                    alert("数据中心与远程主机连接失败");
                }
            }
        });
    }
    var init = function () {
        indexService.loadInfo({
            token: $rootScope.userToken,
            succCallback: function (data) {
                $rootScope.user = data.user;
                $scope.server = data.remoteServerBean;
                if ($scope.server.status == 0) {
                    changeStatus($scope.server.status, null);
                } else {
                    var unixTime=$scope.server.startTimeDate;
                    var time=new Date(unixTime);
                    changeStatus($scope.server.status,time);
                }

            }
        });
    };

    init();


}]);
