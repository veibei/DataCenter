"use strict";

angular.module("dtucenter").factory('logService', ['httpService', function ( httpService) {
    return {
        //首次加载
        first:function (context) {
            return httpService.get("/log/first", context);
        },
        //获取分页
        changePage:function (context) {
            return httpService.get("/log/index?index="+context.pageIndex, context);
        },
        //改变每页的记录条数
        changePerPageCount:function (context) {
            return httpService.get("/log/change?perPageCount="+context.perPageCount, context);
        }
    }
}]);
