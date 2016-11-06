"use strict";

angular.module("dtucenter").factory('indexService', ['httpService', function ( httpService) {
    return {
        loadInfo:function (context) {
            return httpService.get("/main/info?token="+context.token, context);
        },
        startServer:function (context) {
            return httpService.post("/main/start?token="+context.token, context);
        },
        stopServer:function (context) {
            return httpService.post("/main/stop?token="+context.token, context);
        }
    }
}]);
